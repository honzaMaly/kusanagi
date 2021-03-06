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
import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of DecisionLearnerService
 * Created by Jan on 25-Apr-17.
 */
public class DecisionLearnerServiceImpl implements DecisionLearnerService {
    private final StorageService storageService = StorageServiceImp.getInstance();
    private final StateClusteringServiceImpl stateClusteringService = new StateClusteringServiceImpl();
    private final PolicyLearningService policyLearningService = new PolicyLearningServiceImpl();

//    public void test() {
//        List<DecisionState> states = Arrays.asList(new DecisionState(0), new DecisionState(1), new DecisionState(2));
//        DecisionModel decisionModel = new DecisionModel();
//
//        //transitions
//        decisionModel.addTransition(states.get(0), NextActionEnumerations.YES, states.get(1), 0.5);
//        decisionModel.addTransition(states.get(0), NextActionEnumerations.YES, states.get(2), 0.5);
//        decisionModel.addTransition(states.get(0), NextActionEnumerations.NO, states.get(1), 1.0);
//        decisionModel.addTransition(states.get(1), NextActionEnumerations.NO, states.get(2), 0.5);
//        decisionModel.addTransition(states.get(1), NextActionEnumerations.NO, states.get(0), 0.5);
//        decisionModel.addTransition(states.get(1), NextActionEnumerations.YES, states.get(2), 1.0);
//        decisionModel.addTransition(states.get(2), NextActionEnumerations.NO, states.get(2), 0.5);
//        decisionModel.addTransition(states.get(2), NextActionEnumerations.NO, states.get(0), 0.5);
//        decisionModel.addTransition(states.get(2), NextActionEnumerations.YES, states.get(1), 1.0);
//
//        Episode episode1 = new Episode();
//        episode1.addState(states.get(0));
//        episode1.transition(new SimpleAction(NextActionEnumerations.YES.name()), states.get(0), DecisionDomainGenerator.defaultReward);
//        episode1.transition(new SimpleAction(NextActionEnumerations.YES.name()), states.get(2), DecisionDomainGenerator.defaultReward);
//        episode1.transition(new SimpleAction(NextActionEnumerations.YES.name()), states.get(1), DecisionDomainGenerator.defaultReward);
//
//        Episode episode2 = new Episode();
//        episode2.addState(states.get(1));
//        episode2.transition(new SimpleAction(NextActionEnumerations.NO.name()), states.get(2), DecisionDomainGenerator.defaultReward);
//        episode2.transition(new SimpleAction(NextActionEnumerations.NO.name()), states.get(1), DecisionDomainGenerator.defaultReward);
//
//        List<Episode> episodes = new ArrayList<>();
//        for (int i = 0; i < 1000; i++) {
//            episodes.add(episode1);
//            episodes.add(episode2);
//        }
//
//        //learn policy
//        DecisionDomainGenerator decisionDomainGenerator = new DecisionDomainGenerator(decisionModel);
//        SADomain domain = decisionDomainGenerator.generateDomain();
//        Policy policy = policyLearningService.learnPolicy(domain, episodes, states.size(), episodes.size() / 2);
//        System.out.println();
//    }

    private static class StateExtended {
        @Getter
        private final double[] featureVector;
        @Getter
        private final boolean committedWhenTransiting;

        private StateExtended(State state) {
            this.featureVector = state.getFeatureVector();
            this.committedWhenTransiting = state.isCommittedWhenTransiting();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StateExtended that = (StateExtended) o;

            if (committedWhenTransiting != that.committedWhenTransiting) return false;
            return Arrays.equals(featureVector, that.featureVector);
        }

        @Override
        public int hashCode() {
            int result = Arrays.hashCode(featureVector);
            result = 31 * result + (committedWhenTransiting ? 1 : 0);
            return result;
        }
    }

    @Override
    public void learnDecisionMakers() {
        Map<AgentTypeID, Set<DesireKeyID>> data = storageService.getParsedAgentTypesWithDesiresTypesContainedInStorage();
        data.forEach((agentTypeID, desireKeyIDS) -> desireKeyIDS.forEach(desireKeyID -> {
            try {
                MyLogger.getLogger().info("Starting computation for " + desireKeyID.getName() + " of " + agentTypeID.getName());

                List<Trajectory> trajectories = storageService.getRandomListOfTrajectories(agentTypeID, desireKeyID, -1);

                //get number of features for state
                int numberOfFeatures = trajectories.get(0).getNumberOfFeatures();

                //find representatives of states
                List<State> states = trajectories.stream()
                        .map(Trajectory::getStates)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

                List<FeatureNormalizer> normalizers = stateClusteringService.computeFeatureNormalizersBasedOnStates(states, numberOfFeatures);
                List<Vec> classes = stateClusteringService.computeStateRepresentatives(trajectories, states, normalizers);

                //create states with corresponding mean
                Map<DecisionState, Vec> statesAndTheirMeans = new HashMap<>();
                for (int i = 0; i < classes.size(); i++) {
                    statesAndTheirMeans.put(new DecisionState(i), classes.get(i));
                }

                MyLogger.getLogger().info("Creating MDP...");

                //create transitions
                Map<DecisionState, Map<NextActionEnumerations, Map<DecisionState, Double>>> transitions = new HashMap<>();
                List<Episode> episodes = new ArrayList<>();
                trajectories.stream()
                        .map(Trajectory::getStates)
                        //interested in trajectories with some transitions
                        .filter(stateList -> stateList.size() > 2)
                        .forEach(stateList -> {
                            Episode episode = new Episode();

                            //convert vectors to states (more memory intensive but faster)
                            List<DecisionState> convertedOnStates = stateList.parallelStream()
                                    .map(state -> closestStateRepresentative(new DenseVector(Configuration.normalizeFeatureVector(state.getFeatureVector(), normalizers)), statesAndTheirMeans))
                                    .collect(Collectors.toList());

                            DecisionState currentState = convertedOnStates.get(0);
                            NextActionEnumerations nextAction = NextActionEnumerations.returnNextAction(stateList.get(0).isCommittedWhenTransiting());

                            //add initial position in expert trajectory
                            episode.addState(convertedOnStates.get(0));

                            //there is no transition in last state
                            for (int i = 1; i < stateList.size(); i++) {
                                Map<DecisionState, Double> transitedTo = transitions.computeIfAbsent(currentState, decisionState -> new HashMap<>())
                                        .computeIfAbsent(nextAction, actionEnumerations -> new HashMap<>());
                                currentState = convertedOnStates.get(i);

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

                MyLogger.getLogger().info("Transitions ready...");

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
                decisionModel.addExtraTransitionToStateWhenNoActionExists();

                //learn policy
                DecisionDomainGenerator decisionDomainGenerator = new DecisionDomainGenerator(decisionModel);
                SADomain domain = decisionDomainGenerator.generateDomain();

                MyLogger.getLogger().info("Learning policy...");
                List<Episode> episodesToUse;
                if (episodes.size() > 35) {
                    int middle = ((episodes.size() - 25) / 2) + 5;
                    episodes.sort(Comparator.comparingInt(o -> o.stateSequence.size()));
                    episodesToUse = Stream.concat(Stream.concat(episodes.subList(episodes.size() - 20, episodes.size()).stream(),
                            episodes.subList(0, 10).stream()), episodes.subList(middle - 3, middle + 2).stream())
                            .collect(Collectors.toList());
                } else {
                    episodesToUse = new ArrayList<>(episodes);
                }
                episodesToUse.sort(Comparator.comparingInt(o -> o.stateSequence.size()));
                Policy policy = policyLearningService.learnPolicy(domain, episodesToUse, classes.size() + 1, episodesToUse.size());
                //form decision point data structure and store it
                DecisionPointDataStructure decisionPoint = createDecisionPoint(normalizers, statesAndTheirMeans, policy);
                storageService.storeLearntDecision(decisionPoint, agentTypeID, desireKeyID);
                MyLogger.getLogger().info("Successfully learn decisions for " + desireKeyID.getName() + " of " + agentTypeID.getName());
            } catch (Exception e) {
                e.printStackTrace();
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
