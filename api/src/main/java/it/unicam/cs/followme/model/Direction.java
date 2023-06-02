package it.unicam.cs.followme.model;

/**
 * Represents a unitary rectilinear direction.
 * @param <P> The type of position that can be shifted by the direction
 */

public interface Direction<P> {

    /**
     * Applies the direction to a given position, shifting it for the given space units
     * in such direction.
     * @param position the starting position
     * @param distance the amount of space units of the shift
     * @return the end position of the shift
     */
    P shift(P position, double distance);
}
