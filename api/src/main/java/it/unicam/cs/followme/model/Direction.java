package it.unicam.cs.followme.model;

/**
 * Represents a unitary direction
 * @param <P>
 */

public interface Direction<P> {

    /**
     * Applies the direction to a given position, translating it for the given space units
     * in such direction.
     * @param pos the starting position
     * @param meters the amount of space units of the translation
     * @return the end position of the translation
     */
    P translate(P pos, double meters);
}
