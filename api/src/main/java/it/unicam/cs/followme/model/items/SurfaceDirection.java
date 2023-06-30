package it.unicam.cs.followme.model.items;

import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.util.DoubleRange;

/**
 * Represents a direction in the two-dimensional surface as a vector of magnitude one, having its end point
 * in a specified <code>SurfacePosition</code> and application point in the origin.
 */
public class SurfaceDirection implements Direction<SurfacePosition>{

    /**
     * End point of the vector, such that has a Euclidean norm equal to one.
     */
    private final SurfacePosition normalizedPosition;

    /**
     * Constructs a direction based on the given position <code>end</code> relative to the
     * position <code>start</code>, that is the direction going from the <code>start</code> to
     * the <code>end</code> position.
     *
     * @param start the first position.
     * @param end the second position.
     * @throws IllegalArgumentException if one of the given position is null or the two positions coincide.
     */
    public SurfaceDirection (SurfacePosition start, SurfacePosition end) {
        this(end.combineCoordinates(((x1, x2) -> x1 - x2), start));
    }

    /**
     * Constructs a direction based on the given coordinates representing an absolute position,
     * that is the direction obtained from the origin to the given point.
     *
     * @param x the x coordinate of the absolute position.
     * @param y the y coordinate of the absolute position.
     * @throws IllegalArgumentException if the argument is null or is the origin position.
     */
    public SurfaceDirection (double x, double y) {
        this(new SurfacePosition(x, y));
    }

    /**
     * Constructs a direction based on the given absolute position, that is the
     * direction obtained from the origin to the given point.
     *
     * @param end the absolute position.
     * @throws IllegalArgumentException if the argument is null or is the origin position.
     */
    public SurfaceDirection (SurfacePosition end) throws IllegalArgumentException {
        if (end == null || end.equals(SurfacePosition.ORIGIN))
            throw new IllegalArgumentException("Cannot construct a direction from a null or origin position");
        this.normalizedPosition = normalizePosition(end);
    }

    /**
     * Returns a random direction, obtained as the direction from the origin to a point with
     * both coordinates chosen randomly in a range from -1 and 1.
     *
     * @return the random direction.
     */
    public static SurfaceDirection randomDirection () {
        SurfacePosition randomPosition;
        do randomPosition = SurfacePosition.randomPositionInRanges(new DoubleRange(-1, 1), new DoubleRange(-1, 1));
        while (randomPosition.equals(SurfacePosition.ORIGIN));
        return new SurfaceDirection(randomPosition);
    }

    /**
     * Returns a random direction, obtained as the direction from the origin to a point with
     * both coordinates chosen randomly in a range from -1 and 1 using the given seed.
     *
     * @param seed the seed used to generate the random direction.
     * @return the random direction.
     */
    public static SurfaceDirection randomDirection (long seed) {
        SurfacePosition randomPosition;
        do randomPosition = SurfacePosition.randomPositionInRanges(new DoubleRange(-1, 1), new DoubleRange(-1, 1), seed);
        while (randomPosition.equals(SurfacePosition.ORIGIN));
        return new SurfaceDirection(randomPosition);
    }

    private static SurfacePosition normalizePosition(SurfacePosition position) {
        return position.mapCoordinates(x -> x/position.getDistanceFrom());
    }

    /**
     * Returns the normalized position corresponding to this surface direction, as the end point
     * of the magnitude one vector associated with this direction.
     *
     * @return the normalized position of this direction.
     */
    public SurfacePosition getNormalizedPosition() {
        return this.normalizedPosition;
    }

    @Override
    public SurfacePosition shift(SurfacePosition pos, double meters) {
        return pos.combineCoordinates((Double::sum), this.getNormalizedPosition().mapCoordinates(x -> x*meters));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurfaceDirection other = (SurfaceDirection) o;
        return this.normalizedPosition.equals(other.normalizedPosition);
    }

    @Override
    public int hashCode() {
        return this.normalizedPosition.hashCode();
    }

    @Override
    public String toString() {
        return "<" + this.normalizedPosition.getX() + ", " + this.normalizedPosition.getY() + ">";
    }
}
