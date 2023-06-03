package it.unicam.cs.followme.model.simulation;

import it.unicam.cs.followme.model.environment.Position;
import it.unicam.cs.followme.model.items.ConditionSignaler;
import it.unicam.cs.followme.model.items.MovingItem;

import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

public class SignaledConditionTracker<P extends Position<P>, L, I extends ConditionSignaler<L> & MovingItem<P>> {

    private final MovingItemTracker<P, I> itemTracker;

    public  SignaledConditionTracker (MovingItemTracker<P, I> itemTracker) {
        this.itemTracker = itemTracker;
    }

    /**
     * Given a position and a distance, returns the list of condition labels that are signaled by items in positions
     * that are in the range of the given distance from the given position.
     *
     * @param position the position to capture signals from.
     * @param maxDistance the distance range of the capture.
     * @return the list of labels of captured signals.
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
}
