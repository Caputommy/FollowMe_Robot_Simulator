package it.unicam.cs.followme.model.environment;

/**
 * Classes implementing this interface represent a position in a space that can evaluate their distance
 * related to another position.
 *
 * @param <P> type of the other position
 */
public interface Position<P> {

    /**
     * Returns the distance of the given position related to this position.
     *
     * @param otherPosition the position to evaluate the distance of.
     * @return the distance from the given position.
     */
    double getDistanceFrom (P otherPosition);
}
