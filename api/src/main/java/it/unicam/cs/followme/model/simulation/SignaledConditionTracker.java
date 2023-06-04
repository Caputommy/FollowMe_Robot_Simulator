package it.unicam.cs.followme.model.simulation;

import it.unicam.cs.followme.model.environment.Position;
import it.unicam.cs.followme.model.items.ConditionSignaler;
import it.unicam.cs.followme.model.items.MovingItem;

import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Instances of this class are used to manage the reception of conditions signaled by moving items that
 * are already being tracked by a moving item tracker.
 *
 * @param <P> type representing the positions.
 * @param <L> type representing the labels associated with the signaled conditions.
 * @param <I> type representing the moving items that can also signal conditions.
 */

public class SignaledConditionTracker<P extends Position<P>, L, I extends ConditionSignaler<L> & MovingItem<P>> {

    private final MovingItemTracker<P, I> itemTracker;

    public SignaledConditionTracker (MovingItemTracker<P, I> itemTracker) {
        this.itemTracker = itemTracker;
    }

    /**
     * Given a position and a distance, returns the condition labels mapped with the position of the items they are
     * signaled by such that those positions are in the range of the given distance from the given position.
     *
     * @param position the position to capture signals from.
     * @param maxDistance the distance range of the capture.
     * @return the captured labels mapped with their signaling positions.
     */
    Set<L> captureSignaledConditions (P position, double maxDistance) {
        return itemTracker.getMapping()
                .entrySet()
                .stream()
                .filter(e -> e.getValue().getDistanceFrom(position) <= maxDistance)
                .map(Map.Entry::getKey)
                .<L>flatMap(i -> i.getConditions().stream())
                .collect(Collectors.toSet());
    }

    /**
     * Returns the set positions of those items that are signaling the given condition that are not further
     * than the given distance from the given position.
     *
     * @param position the position to capture signals from.
     * @param condition the label of the condition.
     * @param maxDistance the distance range of the capture.
     * @return the nearby positions where the given condition is signaled.
     */
    Set<P> getSources ( P position, L condition, double maxDistance) {
        return itemTracker.getMapping()
                .entrySet()
                .stream()
                .filter(e -> e.getKey().getConditions().contains(condition))
                .filter(e -> (e.getValue().getDistanceFrom(position) <= maxDistance))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }
}
