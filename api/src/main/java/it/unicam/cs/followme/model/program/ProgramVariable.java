package it.unicam.cs.followme.model.program;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Instances of this class represent an initialized variable that can assume a different value for each one of the
 * programmed items it is used for.
 *
 * @param <I> type of the programmed item.
 * @param <T> type of the variable.
 */
public final class ProgramVariable<I, T> {
    private final Map<I, T> valueMap;
    private final T initialValue;

    public ProgramVariable(T initialValue) {
        this.valueMap = new ConcurrentHashMap<>();
        this.initialValue = initialValue;
    }

    /**
     * Returns the current value of this variable associated with the given programmed item.
     *
     * @param item the programmed item.
     * @return the value of this variable associated with the item.
     */
    public T getValue(I item) {
        return valueMap.getOrDefault(item, initialValue);
    }

    /**
     * Sets the current value of this variable associated with the given programmed item to the new given value.
     *
     * @param item the programmed item.
     * @param newValue the new value to set.
     */
    public void setValue(I item, T newValue) {
        valueMap.put(item, newValue);
    }
}
