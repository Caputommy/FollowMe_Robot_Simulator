package it.unicam.cs.followme.model;

/**
 * Classes implementing this interface represent a position in a space that can evaluate their distance
 * related to another position.
 *
 * @param <P> type of the other position
 */
public interface Position<P> {

    double getDistanceFrom (P otherPosition);
}
