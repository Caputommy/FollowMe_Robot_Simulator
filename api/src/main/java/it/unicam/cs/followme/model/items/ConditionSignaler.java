package it.unicam.cs.followme.model.items;

import java.util.List;

/**
 * Classes implementing this interface consist of entities that are able to signal self conditions, in
 * order to be detected from other listening entities.
 *
 * @param <L> type representing the label associated with a condition
 */
public interface ConditionSignaler<L> {

    /**
     * Returns the list of labels representing the conditions signaled by this signaler.
     *
     * @return the list of labels
     */
    List<L> getConditions ();
}
