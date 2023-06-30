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
     * @return the associated label.
     */
    public L getLabel();

    /**
     * Verifies if a given position is included inside this area. The inclusion is evaluated considering
     * the given position as a position relative to the reference point of this area, such as its center.
     *
     * @param position the relative position.
     * @return true if the given position is included.
     */
    boolean includes(P position);

}
