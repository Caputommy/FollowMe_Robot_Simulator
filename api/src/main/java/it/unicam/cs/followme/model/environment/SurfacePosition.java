package it.unicam.cs.followme.model.environment;

import it.unicam.cs.followme.model.simulation.ProgramLine;
import it.unicam.cs.followme.util.DoubleBiFunction;
import it.unicam.cs.followme.util.DoubleRange;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

/**
 * Represents a point in a two-dimensional space, defined by a couple of real numbers as
 * coordinates of the point.
 */
public class SurfacePosition implements Position<SurfacePosition> {

    /**
     * Returns the average position calculated among a set of positions <code>positions</code>.
     * If there are no positions in the set, an empty {@link Optional<SurfacePosition>} is returned.
     *
     * @param positions the set of positions.
     * @return the average position.
     */
    public static Optional<SurfacePosition> averageLocation(Set<SurfacePosition> positions) {
        long n = positions.size();
        if (n == 0) return Optional.empty();
        return Optional.ofNullable(
                positions
                .stream()
                .reduce((l1, l2) -> l1.combineCoordinates(Double::sum, l2))
                .get()
                .mapCoordinates(c -> c/ n)
        );
    }

    /**
     * Returns a random position such that his coordinates are chosen randomly respectively in the
     * given ranges rangeX and rangeY.
     *
     * @param rangeX the range from witch x coordinate is chosen
     * @param rangeY the range from witch y coordinate is chosen
     * @return the random position.
     */
    public static SurfacePosition getRandomPositionInRanges(DoubleRange rangeX, DoubleRange rangeY) {
        return new SurfacePosition(rangeX.getRandomDoubleInRange(), rangeY.getRandomDoubleInRange());
    }

    /**
     * Returns a seeded random position such that his coordinates are chosen randomly respectively in the
     * given ranges rangeX and rangeY.
     *
     * @param rangeX the range from witch x coordinate is chosen.
     * @param rangeY the range from witch y coordinate is chosen.
     * @param seed the seed of the random position.
     * @return the random position.
     */
    public static SurfacePosition getRandomPositionInRanges(DoubleRange rangeX, DoubleRange rangeY, long seed) {
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
     *
     * @return the Euclidean distance.
     */
    public double getDistanceFrom() {
        return this.getDistanceFrom(ORIGIN);
    }

    /**
     * Calculates the Euclidean distance between the position and the given position <code>from</code>.
     *
     * @param other the position to calculate the distance from.
     * @return the Euclidean distance
     */
    public double getDistanceFrom(SurfacePosition other) {
        return Math.sqrt(this
                .combineCoordinates(((x1, x2) -> x1 - x2), other)
                .mapCoordinates((x) -> x*x)
                .reducePosition((Double::sum)));
    }

    /**
     * Returns the position obtained applying the given unary operator <code>op</code> separately
     * on each coordinate of the original position.
     *
     * @param op the unary operator to apply.
     * @return the location obtained from the appliance of <code>op</code>.
     */
    public SurfacePosition mapCoordinates (DoubleUnaryOperator op) {
        return new SurfacePosition(op.applyAsDouble(this.getX()), op.applyAsDouble(this.getY()));
    }

    /**
     * Returns the position obtained applying the given binary operator <code>op</code> separately
     * on each coordinate of the original location and the given position <code>otherPosition</code>.
     *
     * @param op the unary operator to apply.
     * @param otherPosition the position to combine.
     * @return the position obtained from the appliance of <code>op</code> with <code>otherPosition</code>
     */
    public SurfacePosition combineCoordinates (DoubleBinaryOperator op, SurfacePosition otherPosition) {
        return new SurfacePosition(
                op.applyAsDouble(this.getX(), otherPosition.getX()),
                op.applyAsDouble(this.getY(), otherPosition.getY()));
    }

    /**
     * Applies the given function <code>function</code> using the coordinates of the
     * position as arguments.
     *
     * @param function the function to apply.
     * @return the result of the application.
     * @param <T> the type of the output.
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
