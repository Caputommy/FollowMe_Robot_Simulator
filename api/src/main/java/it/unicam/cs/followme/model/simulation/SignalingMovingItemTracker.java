package it.unicam.cs.followme.model.simulation;

import it.unicam.cs.followme.model.environment.Position;
import it.unicam.cs.followme.model.items.ConditionSignaler;
import it.unicam.cs.followme.model.items.MovingItem;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class SignalingMovingItemTracker<P extends Position<P>, L, I extends ConditionSignaler<L> & MovingItem<P>>
        implements MovingItemTracker<P, I> {

    /**
     * Given a position and a distance, returns the condition labels mapped with the position of the items they are
     * signaled by such that those positions are in the range of the given distance from the given position.
     *
     * @param position the position to capture signals from.
     * @param maxDistance the distance range of the capture.
     * @return the captured labels mapped with their signaling positions.
     */
    public Set<L> captureSignaledConditions (P position, double maxDistance) {
        return this.getMapping()
                .entrySet()
                .stream()
                .filter(e -> e.getValue().getDistanceFrom(position) <= maxDistance)
                .map(Map.Entry::getKey)
                .<L>flatMap(i -> i.getConditions().stream())
                .collect(Collectors.toSet());
    }

    /**
     * Given a position, a condition and a distance, returns the set positions of those items that are signaling the
     * given condition that are not further than the given distance from the given position.
     *
     * @param position the position to capture signals from.
     * @param condition the label of the condition.
     * @param maxDistance the distance range of the capture.
     * @return the set of nearby positions where the given condition is signaled.
     */
    public Set<P> getSources (P position, L condition, double maxDistance) {
        return this.getMapping()
                .entrySet()
                .stream()
                .filter(e -> e.getKey().getConditions().contains(condition))
                .filter(e -> (e.getValue().getDistanceFrom(position) <= maxDistance))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    /**
     * Given an item, a condition and a distance, returns the set positions of those items that are signaling the
     * given condition that are not further than the given distance from the given item.
     *
     * @param item the item that captures the conditions.
     * @param condition the label of the condition.
     * @param maxDistance the distance range of the capture.
     * @return the set of nearby positions to the given item where the given condition is signaled.
     */
    public Set<P> getSources(I item, L condition, double maxDistance) {
        if (!this.isPresent(item)) return new HashSet<>();
        else return getSources(this.getCurrentPosition(item).get(), condition, maxDistance);
    }
}
