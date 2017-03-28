package cz.jan.maly.model.game.wrappers;

/**
 * Contract for strategy to wrap type
 * Created by Jan on 28-Mar-17.
 */
interface StrategyToWrapType<T, V extends AbstractWrapper<T>> {

    /**
     * Wrap type
     *
     * @param type
     * @return
     */
    V createNewWrapper(T type);

}
