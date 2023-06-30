package it.unicam.cs.followme.model.items;

/**
 * Classes implementing this interface represents a path in the space.
 *
 * @param <P> type representing the position that can be shifted by the direction.
 */
public interface Direction<P> {

    /**
     * Applies the direction to a given position, shifting it for the given space units
     * travelling along such direction.
     *
     * @param position the starting position.
     * @param distance the amount of space units of the shift.
     * @return the end position of the shift.
     */
    P shift(P position, double distance);
}
