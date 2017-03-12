package cz.jan.maly.model.planing.command;

import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.ActCommandParameters;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.planing.Command;
import cz.jan.maly.model.planing.IntentionCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Template for command with action to execute
 * Created by Jan on 11-Mar-17.
 */
public abstract class ActCommand<T extends IntentionCommand<?, ? extends ActCommand<T>, Memory<?>>> extends Command<T, Memory<?>> {
    private final ActCommandParameters commandParameters;

    private ActCommand(T intention, ActCommandParameters commandParameters) {
        super(intention);
        this.commandParameters = commandParameters;
    }

    @Override
    public boolean act(Memory<?> memory) {
        return act(new ActCommandParametersInitialized(commandParameters, memory));
    }

    /**
     * User's implementation of act action with additional parameters to execute it
     *
     * @param parameters
     * @return
     */
    protected abstract boolean act(ActCommandParametersInitialized parameters);

    /**
     *
     */
    protected static class ActCommandParametersInitialized implements FactContainerInterface {
        private final Map<FactKey, Object> factParameterMap = new HashMap<>();
        private final Map<FactKey, Set<?>> factSetParameterMap = new HashMap<>();

        private ActCommandParametersInitialized(ActCommandParameters commandParameters, Memory memory) {
            //fill maps with actual parameters from memory
            commandParameters.getParametersTypesForFacts().forEach(factKey -> {
                Optional<?> value = memory.returnFactValueForGivenKey(factKey);
                value.ifPresent(o -> factParameterMap.put(factKey, o));
            });
            commandParameters.getParametersTypesForFactSets().forEach(factKey -> {
                Optional<Set<?>> value = memory.returnFactSetValueForGivenKey(factKey);
                value.ifPresent(o -> factSetParameterMap.put(factKey, o));
            });
        }

        public <V> Optional<V> returnFactValueForGivenKey(FactKey<V> factKey) {
            Object value = factParameterMap.get(factKey);
            if (value != null) {
                return Optional.of((V) value);
            }
            return Optional.empty();
        }

        public <V, S extends Set<V>> Optional<S> returnFactSetValueForGivenKey(FactKey<V> factKey) {
            Set values = factSetParameterMap.get(factKey);
            if (values != null) {
                return Optional.of((S) values);
            }
            return Optional.empty();
        }

    }

    /**
     * Template for command initiated by another agent's desire
     */
    public static abstract class DesiredByAnotherAgent extends ActCommand<IntentionCommand.FromAnotherAgent> {
        protected DesiredByAnotherAgent(IntentionCommand.FromAnotherAgent intention, ActCommandParameters commandParameters) {
            super(intention, commandParameters);
        }
    }

    /**
     * Template for command initiated by own desire
     */
    public static abstract class Own extends ActCommand<IntentionCommand.OwnActing> {
        protected Own(IntentionCommand.OwnActing intention, ActCommandParameters commandParameters) {
            super(intention, commandParameters);
        }
    }

}
