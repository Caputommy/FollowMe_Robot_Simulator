package it.unicam.cs.followme.model;

/**
 * Classes implementing this interface are moving objects that can predict their future position based
 * on their own current movement configuration.
 * @param <P> the type of positions the item can move among
 */

public interface MovingItem<P> {
    /**
     * Returns the predicted new position of the item given a starting position and an amount of time to do
     * the motion.
     * @param startingPosition the initial position of the item.
     * @param time the time given to do the motion.
     * @return the final position of the item.
     */
    P moveFrom (P startingPosition, double time);
}
