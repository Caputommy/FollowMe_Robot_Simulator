package it.unicam.cs.followme.model;

/**
 * Represents a direction in the bidimensional surface as a vector of magnitude one, having its end point
 * in a specified <code>SurfacePosition</code> and application point in the origin.
 */
public class SurfaceDirection implements Direction<SurfacePosition>{

    /**
     * End point of the vector, such that has an Ecuclidean norm equal to one.
     */
    private SurfacePosition normalizedPosition;

    /**
     * Constructs a direction based on the given position <code>end</code> relative to the
     * position <code>start</code>, that is the direction going from the <code>start</code> to
     * the <code>end</code> position.
     * @param start the first position
     * @param end the second position
     * @throws IllegalArgumentException if one of the given position is null or the two positions coincide
     */
    public SurfaceDirection (SurfacePosition start, SurfacePosition end) {
        this(end.combineCoordinates(((x1, x2) -> x1 - x2), start));
    }

    /**
     * Constructs a direction based on the given coordinates representing an absolute position,
     * that is the direction obtained from the origin to the given point.
     * @param x the x coordinate of the absolute position
     * @param y the y coordinate of the absolute position
     * @throws IllegalArgumentException if the argument is null or is the origin position
     */
    public SurfaceDirection (double x, double y) {
        this(new SurfacePosition(x, y));
    }

    /**
     * Constructs a direction based on the given absolute position, that is the
     * direction obtained from the origin to the given point.
     * @param end the absolute position
     * @throws IllegalArgumentException if the argument is null or is the origin position
     */
    public SurfaceDirection (SurfacePosition end) throws IllegalArgumentException {
        if (end == null || end.equals(SurfacePosition.ORIGIN))
            throw new IllegalArgumentException("Cannot construct a direction from a null position");
        this.normalizedPosition = normalizePosition(end);
    }

    private static SurfacePosition normalizePosition(SurfacePosition position) {
        return position.mapCoordinates(x -> x/position.getDistance());
    }

    public SurfacePosition getNormalizedPosition() {
        return this.normalizedPosition;
    }

    @Override
    public SurfacePosition translate(SurfacePosition pos, double meters) {
        return pos.combineCoordinates((Double::sum), this.getNormalizedPosition().mapCoordinates(x -> x*meters));
    }
}
