package cz.jan.maly.model.servicies;

/**
 * Interface to be implemented by each working registry to define method which make snapshot of this registry
 * Created by Jan on 17-Feb-17.
 */
public interface WorkingRegister<V extends Register<?>> {

    /**
     * Create snapshot - read only - with current state of affairs
     *
     * @return
     */
    V makeSnapshot();

}
