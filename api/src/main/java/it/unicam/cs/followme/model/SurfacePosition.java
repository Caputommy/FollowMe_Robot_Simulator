package it.unicam.cs.followme.model;

import it.unicam.cs.followme.util.DoubleBiFunction;
import it.unicam.cs.followme.util.DoubleRange;

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
     * Returns the avarage position calculated among a non-empty list of positions <code> positions </code>.
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
     * Returns a random position such that his coordinates are choosen randomly respectively in the
     * the given ranges rangeX and rangeY.
     * @param rangeX the range from witch x coordinate is choosen
     * @param rangeY the range from witch y coordinate is choosen
     * @return the random position
     */
    SurfacePosition getRandomPositionInRanges(DoubleRange rangeX, DoubleRange rangeY) {
        return new SurfacePosition(rangeX.getRandomDoubleInRange(), rangeY.getRandomDoubleInRange());
    }

    /**
     * Returns a seeded random position such that his coordinates are choosen randomly respectively in the
     * the given ranges rangeX and rangeY.
     * @param rangeX the range from witch x coordinate is choosen
     * @param rangeY the range from witch y coordinate is choosen
     * @param seed the seed of the random position
     * @return the random position
     */
    SurfacePosition getRandomPositionInRanges(DoubleRange rangeX, DoubleRange rangeY, long seed) {
        return new SurfacePosition(rangeX.getRandomDoubleInRange(seed), rangeY.getRandomDoubleInRange(seed));
    }

    private final double x;
    private final double y;

    public static final SurfacePosition ORIGIN = new SurfacePosition(0, 0);

    /**
     * Constant used as maximum deviation between coordinates in order to be considered equal.
     */
    public static final double EPSILON = 10E-6;

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

    /**
     * Calculates the Euclidean distance of the position from the origin.
     * @return the Euclidean distance
     */
    public double getDistance() {
        return this.getDistance(ORIGIN);
    }

    /**
     * Calculates the Euclidean distance beetween the position and the given position <code>from</code>.
     * @param from the position to calculate the distance from
     * @return the Euclidean distance
     */
    public double getDistance(SurfacePosition from) {
        return Math.sqrt(this
                .combineCoordinates(((x1, x2) -> x1 - x2), from)
                .mapCoordinates((x) -> x*x)
                .reducePosition((Double::sum)));
    }

    /**
     * Returns the position obtained applying the given unary operator <code> op </code> separately
     * on each coordinate of the original position.
     * @param op
     * @return the location obtained from the appliance of <code> op </code>
     */
    public SurfacePosition mapCoordinates (DoubleUnaryOperator op) {
        return new SurfacePosition(op.applyAsDouble(this.getX()), op.applyAsDouble(this.getY()));
    }

    /**
     * Returns the position obtained applying the given binary operator <code> op </code> separately
     * on each coordinate of the original location and the given postion <code>otherPosition</code>.
     * @param op
     * @param otherPosition
     * @return the position obtained from the appliance of <code> op </code> with <code> otherPosition </code>
     */
    public SurfacePosition combineCoordinates (DoubleBinaryOperator op, SurfacePosition otherPosition) {
        return new SurfacePosition(
                op.applyAsDouble(this.getX(), otherPosition.getX()),
                op.applyAsDouble(this.getY(), otherPosition.getY()));
    }

    /**
     * Applies the given function <code>function</code> using the coordinates of the
     * position as arguments.
     * @param function the function to apply
     * @return the result of the application
     * @param <T> the type of the output
     */
    public <T> T reducePosition (DoubleBiFunction<T> function) {
        return function.apply(this.getX(), this.getY());
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
