package it.unicam.cs.followme.model.environment;

import java.util.Objects;

public class SurfaceCircleArea<L> extends SurfaceArea<L> {

    private final double radius;

    private static final double DEFAULT_RADIUS = 1.0;

    public SurfaceCircleArea(L label) {
        this(label, DEFAULT_RADIUS);
    }
    public SurfaceCircleArea(L label, double radius) {
        super(label);
        this.radius = radius;
    }

    public double getRadius() {
        return this.radius;
    }

    @Override
    public boolean includes(SurfacePosition position) {
        return position.getDistanceFrom(SurfacePosition.ORIGIN) <= this.radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurfaceCircleArea<?> that = (SurfaceCircleArea<?>) o;
        return getLabel().equals(that.getLabel()) &&
                Double.compare(that.getRadius(), getRadius()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRadius());
    }

    @Override
    public String toString() {
        return "SurfaceCircleArea{" +
                "label=" + label +
                ", radius=" + radius +
                '}';
    }
}
