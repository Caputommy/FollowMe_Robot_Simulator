package it.unicam.cs.followme.model.environment;

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
}
