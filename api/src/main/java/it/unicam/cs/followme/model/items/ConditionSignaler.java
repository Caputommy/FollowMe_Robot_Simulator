package it.unicam.cs.followme.model.items;

import java.util.Set;

/**
 * Classes implementing this interface consist of entities that are able to signal self conditions, in
 * order to be detected from other listening entities.
 *
 * @param <L> type representing the label associated with a condition
 */
public interface ConditionSignaler<L> {

    /**
     * Returns the set of labels representing the conditions signaled by this signaler.
     *
     * @return the set of condition labels.
     */
    Set<L> getConditions ();

    /**
     * Adds a condition to those that are signaled by this signaler.
     *
     * @param label the label representing the signaled condition.
     * @return true if the set of signaled conditions of this item changed as result of the call.
     */
    boolean signal (L label);

    /**
     * Removes a condition to those that are signaled by this signaler.
     *
     * @param label the label representing the signaled condition
     * @return true if the set of signaled conditions of this item changed as result of the call.
     */
    boolean unsignal (L label);
}
