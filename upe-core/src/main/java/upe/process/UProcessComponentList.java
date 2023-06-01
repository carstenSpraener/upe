package upe.process;

import java.util.List;

/**
 * This process element is for implementation of one to many relations at the process level.
 * 
 * @param <T> The type of process components inside the list.
 */
public interface UProcessComponentList<T extends UProcessComponent> extends UProcessComponent {
    /**
     * Add an UProcessComponent into the list
     * @param element
     */
    void add(T element);

    /**
     * Remove a UProcessComponent from the list.
     * @param element
     */
    void remove(T element);

    /**
     * Get the UProcessComponent at the idx position
     * @param idx
     * @return
     */
    T getAt(int idx);

    /**
     * How many UProcessComponents are in the list?
     * 
     * @return the number of UProcessComponents in the list.
     */
    int size();

    /**
     * Create a new Instance of the contained UProcessComponents and connect it with the
     * running process instance.
     * 
     * @return an newly created and connected instance of the UProcessComponents in the list.
     */
    T createNewInstance();

    /**
     * Give the position of a given UProcessComponent inside the list ore -1 if not contained.
     * 
     * @param element The UProcessComponent to search 
     * @return position in the list or -1
     */
    int indexOf(UProcessElement element);

    /**
     * Return the whole list of UProcessComponent. This list can be immutable, so use only for
     * reading. In for-loops for example.
     *
     * @return a (potential) immutable list of the contained UProcessComponents.
     */
    List<T> getComponentList();
}
