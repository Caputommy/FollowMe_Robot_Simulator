package it.unicam.cs.followme.model.items;

import it.unicam.cs.followme.model.environment.Position;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An nstance of this class is a particular {@link MovingItemTracker} that takes track of items that can also signal
 * conditions (i. e. implement {@link ConditionSignaler}) and, thus, can track the origin of conditions signaled
 * by those items.
 *
 * @param <P> type representing the position of the items.
 * @param <L> type representing the label of the signaled conditions.
 * @param <I> type representing the moving items.
 */
public abstract class SignalingMovingItemTracker<P extends Position<P>, L, I extends ConditionSignaler<L> & MovingItem<P>>
        implements MovingItemTracker<P, I> {

    /**
     * Factory method that constructs and returns an item tracker which initial state is described
     * by a given map of items and positions.
     *
     * @param map the map of items and positions.
     * @return a new <code>SignalingMovingItemTracker</code> object with the given initial configuration.
     * @param <P> type representing the position of the items.
     * @param <L> type representing the label of the signaled conditions.
     * @param <I> type representing the moving items.
     */
    public static <P extends Position<P>, L, I extends ConditionSignaler<L> & MovingItem<P>> SignalingMovingItemTracker<P, L, I> trackerOf(Map<I, P> map) {
        SignalingMovingItemTracker<P, L, I> tracker = new MapSignalingMovingItemTracker<>();
        tracker.addAllItems(map);
        return tracker;
    }

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
    public Set<P> getSourcePositions(P position, L condition, double maxDistance) {
        return this.getMapping()
                .entrySet()
                .stream()
                .filter(e -> e.getKey().getConditions().contains(condition))
                .filter(e -> (e.getValue().getDistanceFrom(position) <= maxDistance))
                .map(Map.Entry::getValue)
                .collect(Collectors.toCollection(HashSet::new));
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
    public Set<P> getSourcePositions(I item, L condition, double maxDistance) {
        if (!this.isPresent(item)) return new HashSet<>();
        else return getSourcePositions(this.getCurrentPosition(item).get(), condition, maxDistance);
    }
}
