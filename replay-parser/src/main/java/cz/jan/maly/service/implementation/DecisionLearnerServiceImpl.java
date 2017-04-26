package cz.jan.maly.service.implementation;

import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.Episode;
import burlap.mdp.core.action.SimpleAction;
import burlap.mdp.singleagent.SADomain;
import cz.jan.maly.model.decision.DecisionPointDataStructure;
import cz.jan.maly.model.decision.NextActionEnumerations;
import cz.jan.maly.model.features.FeatureNormalizer;
import cz.jan.maly.model.irl.DecisionDomainGenerator;
import cz.jan.maly.model.irl.DecisionModel;
import cz.jan.maly.model.irl.DecisionState;
import cz.jan.maly.model.metadata.AgentTypeID;
import cz.jan.maly.model.metadata.DesireKeyID;
import cz.jan.maly.model.tracking.State;
import cz.jan.maly.model.tracking.Trajectory;
import cz.jan.maly.service.DecisionLearnerService;
import cz.jan.maly.service.PolicyLearningService;
import cz.jan.maly.service.StorageService;
import cz.jan.maly.utils.Configuration;
import cz.jan.maly.utils.MyLogger;
import jsat.linear.DenseVector;
import jsat.linear.Vec;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of DecisionLearnerService
 * Created by Jan on 25-Apr-17.
 */
public class DecisionLearnerServiceImpl implements DecisionLearnerService {
    private final StorageService storageService = StorageServiceImp.getInstance();
    private final StateClusteringServiceImpl stateClusteringService = new StateClusteringServiceImpl();
    private final PolicyLearningService policyLearningService = new PolicyLearningServiceImpl();

    //maximum number of representative to find
    private static final int numberOfCenters = 100;

    @Override
    public void learnDecisionMakers() {
        Map<AgentTypeID, Set<DesireKeyID>> data = storageService.getParsedAgentTypesWithDesiresTypesContainedInStorage();
        data.forEach((agentTypeID, desireKeyIDS) -> desireKeyIDS.forEach(desireKeyID -> {
            try {
                List<Trajectory> trajectories = storageService.getTrajectories(agentTypeID, desireKeyID);

                //get number of features for state
                int numberOfFeatures = trajectories.get(0).getNumberOfFeatures();

                //find representatives of states
                List<State> states = trajectories.stream()
                        .map(Trajectory::getStates)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
                List<FeatureNormalizer> normalizers = stateClusteringService.computeFeatureNormalizersBasedOnStates(states, numberOfFeatures);
                List<Vec> classes = stateClusteringService.computeStateRepresentatives(states, normalizers, numberOfCenters, numberOfFeatures);

                //create states with corresponding mean
                Map<DecisionState, Vec> statesAndTheirMeans = new HashMap<>();
                for (int i = 0; i < classes.size(); i++) {
                    statesAndTheirMeans.put(new DecisionState(i), classes.get(i));
                }

                //create transitions
                Map<DecisionState, Map<NextActionEnumerations, Map<DecisionState, Double>>> transitions = new HashMap<>();
                List<Episode> episodes = new ArrayList<>();
                trajectories.stream()
                        .map(Trajectory::getStates)
                        //interested in trajectories with some transitions
                        .filter(stateList -> stateList.size() > 2)
                        .forEach(stateList -> {

                            Episode episode = new Episode();
                            DecisionState currentState = closestStateRepresentative(new DenseVector(Configuration.normalizeFeatureVector(stateList.get(0).getFeatureVector(), normalizers)), statesAndTheirMeans);
                            NextActionEnumerations nextAction = NextActionEnumerations.returnNextAction(stateList.get(0).isCommittedWhenTransiting());

                            //add initial position in expert trajectory
                            episode.addState(currentState);

                            //there is no transition in last state
                            for (int i = 1; i < stateList.size(); i++) {
                                Map<DecisionState, Double> transitedTo = transitions.computeIfAbsent(currentState, decisionState -> new HashMap<>())
                                        .computeIfAbsent(nextAction, actionEnumerations -> new HashMap<>());
                                currentState = closestStateRepresentative(new DenseVector(Configuration.normalizeFeatureVector(stateList.get(i).getFeatureVector(), normalizers)), statesAndTheirMeans);

                                //add transition to episode. Do not add last transition if agent is committed
                                episode.transition(new SimpleAction(nextAction.name()), currentState, DecisionDomainGenerator.defaultReward);

                                nextAction = NextActionEnumerations.returnNextAction(stateList.get(i).isCommittedWhenTransiting());

                                //increment count of this type transitions
                                Double currentValue = transitedTo.get(currentState);
                                if (currentValue == null) {
                                    currentValue = 0.0;
                                }
                                transitedTo.put(currentState, currentValue + 1.0);
                            }

                            episodes.add(episode);
                        });
                Map<DecisionState, Map<NextActionEnumerations, Double>> sums = transitions.keySet().stream()
                        .collect(Collectors.toMap(Function.identity(),
                                o -> transitions.get(o).entrySet().stream()
                                        .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingDouble(value -> value.getValue().values().stream().mapToDouble(v -> v).sum())))
                        ));

                //fill DecisionModel
                DecisionModel decisionModel = new DecisionModel();
                transitions.forEach((decisionState, nextActionEnumerationsMapMap) ->
                        nextActionEnumerationsMapMap.forEach((actionEnumerations, decisionStateDoubleMap) ->
                                decisionStateDoubleMap.forEach((ds, aDouble) -> decisionModel.addTransition(decisionState, actionEnumerations, ds,
                                        aDouble / sums.get(decisionState).get(actionEnumerations))))
                );

                //learn policy
                DecisionDomainGenerator decisionDomainGenerator = new DecisionDomainGenerator(decisionModel);
                SADomain domain = decisionDomainGenerator.generateDomain();
                Policy policy = policyLearningService.learnPolicy(domain, episodes, numberOfCenters);

                //form decision point data structure and store it
                DecisionPointDataStructure decisionPoint = createDecisionPoint(normalizers, statesAndTheirMeans, policy);
                storageService.storeLearntDecision(decisionPoint, agentTypeID, desireKeyID);
                MyLogger.getLogger().info("Successfully learn decisions for " + desireKeyID.getName() + " of " + agentTypeID.getName());
            } catch (Exception e) {
                MyLogger.getLogger().warning(e.getLocalizedMessage());
            }
        }));
    }

    /**
     * Create decision point data structure
     *
     * @param normalizers
     * @param statesAndTheirMeans
     * @param policy
     * @return
     */
    private DecisionPointDataStructure createDecisionPoint(List<FeatureNormalizer> normalizers, Map<DecisionState, Vec> statesAndTheirMeans, Policy policy) {
        Set<DecisionPointDataStructure.StateWithTransition> states = statesAndTheirMeans.entrySet().stream()
                .map(entry -> new DecisionPointDataStructure.StateWithTransition(entry.getValue().arrayCopy(), NextActionEnumerations.returnNextAction(policy.action(entry.getKey()).actionName())))
                .collect(Collectors.toSet());

        //check if all actions are available
        if (states.stream().map(DecisionPointDataStructure.StateWithTransition::getNextAction).distinct().count() < NextActionEnumerations.values().length) {
            MyLogger.getLogger().warning("Not all actions are covered.");
        }
        return new DecisionPointDataStructure(states, normalizers);
    }

    /**
     * Find nearest representative
     *
     * @param featureVector
     * @return
     */
    private static DecisionState closestStateRepresentative(Vec featureVector, Map<DecisionState, Vec> statesAndTheirMeans) {
        Optional<DecisionState> closestState = statesAndTheirMeans.entrySet().stream()
                .min(Comparator.comparingDouble(o -> Configuration.DISTANCE_FUNCTION.dist(o.getValue(), featureVector)))
                .map(Map.Entry::getKey);
        if (!closestState.isPresent()) {
            MyLogger.getLogger().warning("No state is present.");
            throw new IllegalArgumentException("No state is present.");
        }
        return closestState.get();
    }
}
