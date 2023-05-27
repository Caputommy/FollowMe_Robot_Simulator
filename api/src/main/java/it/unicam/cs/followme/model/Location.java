package it.unicam.cs.followme.model;

import java.util.List;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Stream;

public record Location(double x, double y) {

    public Location (double x, double y) {
        if (!(Double.isFinite(x) && Double.isFinite(x))) throw new IllegalArgumentException();
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the avarage location calculated among a non-empty list of locations <code> locations </code>.
     * @param locations
     * @throws IllegalArgumentException if the list of location is empty
     * @return the average location
     */
    public static Location averageLocation(List<Location> locations) {
        long n = locations.size();
        if (n == 0) throw new IllegalArgumentException("Cannot calculate average of zero locations");
        return locations
                .stream()
                .reduce((l1, l2) -> l1.combineCoordinates(Double::sum, l2))
                .orElse(new Location(0,0))
                .mapCoordinates(c -> c/ n);
    }

    /**
     * Returns the location obtained applying separately the given unary operator <code> op </code>
     * on each coordinate of the original location.
     * @param op
     * @return the location obtained from the appliance of <code> op </code>
     */
    public Location mapCoordinates (DoubleUnaryOperator op) {
        return new Location (op.applyAsDouble(this.x()), op.applyAsDouble(this.y()));
    }

    /**
     * Returns the location obtained applying separately the given binary operator <code> op </code>
     * on each coordinate of the original location and the given location <code>otherLocation</code>.
     * @param op
     * @param otherLocation
     * @return the location obtained from the appliance of <code> op </code> with <code> otherLocation </code>
     */
    public Location combineCoordinates (DoubleBinaryOperator op, Location otherLocation) {
        return new Location (
                op.applyAsDouble(this.x(), otherLocation.x()),
                op.applyAsDouble(this.y(), otherLocation.y()));
    }
}
