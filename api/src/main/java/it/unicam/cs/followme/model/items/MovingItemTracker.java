package it.unicam.cs.followme.model.items;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Classes implementing this interface take track of the position of a set of moving items over time.
 *
 * @param <P> type representing the position of the items.
 * @param <I> type representing the moving items.
 */

public interface MovingItemTracker<P, I extends MovingItem<P>> {

    /**
     * Adds a moving item to this tracker in the given position.
     * If an equal item already exists in this tracker, a false value is returned and the invocation
     * of this method has no effect on the status of the tracker.
     *
     * @param item the moving item to add to this tracker.
     * @param position the position where to deploy the item in this tracker.
     * @return true if the item has been successfully added and the status of the tracker changed as a result
     * of the call.
     */
    boolean addItem(I item, P position);

    /**
     * Adds the items in the given mapping to this tracker in their respective mapped positions.
     * If all the given items already exists in this tracker, a false value is returned and the invocation
     * of this method has no effect on the status of the tracker.
     *
     * @param items the moving items mapped with their positions to add to this tracker.
     * @return true if at least one item has been successfully added and the status of the tracker changed as a result
     * of the call.
     */
    default boolean addAllItems(Map<I, P> items) {
        return items.entrySet()
                .stream()
                .map(e -> addItem(e.getKey(), e.getValue()))
                .reduce(Boolean::logicalOr)
                .orElse(false);
    }

    /**
     * Checks if the given item is currently tracked by this tracker. More formally, returns true if and only
     * if there exist a moving item in this tracker such that is equal to the given item
     * (independently of its position).
     *
     * @param item the moving item to check.
     * @return true if this tracker is already tracking an item that is equal to the given one.
     */
    boolean isPresent (I item);

    /**
     * Removes all the tracked items from this tracker.
     */
    void clear();

    /**
     * Returns the current position associated with the given item according to this tracker. More formally,
     * returns the position of a moving item in this tracker such that is equal to the given item.
     * If the given item is not present in this tracker, an empty {@link Optional<P>} is returned.
     *
     * @param item a moving item.
     * @return the current position of the given item in this tracker.
     */
    Optional<P> getCurrentPosition (I item);

    /**
     * Returns the current state of the tracker as a map that associates each item tracked to
     * their position.
     *
     * @return the current stat of this tracker.
     */
    Map<I, P> getMapping ();

    /**
     * Returns the set of moving item that are currently tracked by this tracker.
     *
     * @return the set of items tracked.
     */
    default Set<I> getItems() {
        return this.getMapping().keySet();
    }

    /**
     * Moves all the items tacked by this tracker according to their motion configuration for the given
     * amount of units of time, and updates the state of this tracker accordingly.
     *
     * @param time the amount of time to make items move.
     */
    void moveAll (double time);
}
