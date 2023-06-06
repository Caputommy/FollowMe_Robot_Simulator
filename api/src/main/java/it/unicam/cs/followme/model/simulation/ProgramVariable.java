package it.unicam.cs.followme.model.simulation;

import java.util.HashMap;
import java.util.Map;

/**
 * Instances of this class represent a variable that can assume multiple values depending on the
 * programmed item it is used for.
 *
 * @param <I> type of the programmed item.
 * @param <T> type of the variable.
 */
public final class ProgramVariable<I, T> {
    private final Map<I, T> valueMap;
    private final T initialValue;

    public ProgramVariable(T initialValue) {
        this.valueMap = new HashMap<>();
        this.initialValue = initialValue;
    }

    public T getValue(I item) {
        return valueMap.getOrDefault(item, initialValue);
    }

    public void setValue(I item, T newValue) {
        this.valueMap.put(item, newValue);
    }
}
