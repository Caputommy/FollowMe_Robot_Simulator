package it.unicam.cs.followme.model.environment;

/**
 * Classes implementing this interface are used to represent a labeled set of positions in a space.
 *
 * @param <P> type representing the positions that the area can include.
 * @param <L> type representing the label that identifies the area.
 */
public interface Area<P, L> {
    /**
     * Returns the label associated with this area.
     *
     * @return the label
     */
    public L getLabel();

    /**
     * Verifies if a position relative to the center of this area is included inside it.
     *
     * @param position the relative position
     * @return true if it is included
     */
    boolean includes(P position);

}