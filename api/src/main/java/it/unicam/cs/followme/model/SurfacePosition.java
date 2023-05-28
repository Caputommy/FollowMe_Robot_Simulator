package it.unicam.cs.followme.model;

import java.util.List;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

/**
 * Represents a point in a bidimensional space, defined by a couple of real numbers as
 * coordinates of the point.
 */
public class SurfacePosition {

    /**
     * Returns the avarage location calculated among a non-empty list of positions <code> positions </code>.
     * @param positions
     * @throws IllegalArgumentException if the list of location is empty
     * @return the average position
     */
    public static SurfacePosition averageLocation(List<SurfacePosition> positions) {
        long n = positions.size();
        if (n == 0) return ORIGIN;
        return positions
                .stream()
                .reduce((l1, l2) -> l1.combineCoordinates(Double::sum, l2))
                .get()
                .mapCoordinates(c -> c/ n);
    }

    /**
     * Returns the location obtained applying the given unary operator <code> op </code> separately
     * on each coordinate of the original location.
     * @param op
     * @return the location obtained from the appliance of <code> op </code>
     */
    public SurfacePosition mapCoordinates (DoubleUnaryOperator op) {
        return new SurfacePosition(op.applyAsDouble(this.getX()), op.applyAsDouble(this.getY()));
    }

    /**
     * Returns the location obtained applying the given binary operator <code> op </code> separately
     * on each coordinate of the original location and the given location <code>otherLocation</code>.
     * @param op
     * @param otherLocation
     * @return the location obtained from the appliance of <code> op </code> with <code> otherLocation </code>
     */
    public SurfacePosition combineCoordinates (DoubleBinaryOperator op, SurfacePosition otherLocation) {
        return new SurfacePosition(
                op.applyAsDouble(this.getX(), otherLocation.getX()),
                op.applyAsDouble(this.getY(), otherLocation.getY()));
    }

    private final double x;
    private final double y;

    public static final SurfacePosition ORIGIN = new SurfacePosition(0, 0);
    private static final double EPSILON = 10E-6;

    public SurfacePosition(double x, double y) {
        if (!(Double.isFinite(x) && Double.isFinite(x))) throw new IllegalArgumentException();
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        SurfacePosition other = (SurfacePosition) o;
        return (this.getX() - other.getX()) < EPSILON &&
                (this.getY() - other.getY()) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getX(), this.getY());
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
