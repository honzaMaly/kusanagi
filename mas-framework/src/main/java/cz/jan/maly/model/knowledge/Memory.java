package cz.jan.maly.model.knowledge;

/**
 * Interface to be implemented by each memory fragment. It defines method clone - to get copy which can be propagated
 * in system
 * Created by Jan on 16-Feb-17.
 */
interface Memory<V extends Memory> {

    /**
     * Returns exact clone "snapshot" of memory
     *
     * @return
     */
    V cloneMemory();

}
