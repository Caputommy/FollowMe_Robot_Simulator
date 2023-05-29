package it.unicam.cs.followme.model;

public class FollowMeCircleArea extends FollowMeArea {

    private final double radius;

    private static final double DEFAULT_RADIUS = 1.0;

    public FollowMeCircleArea(String label, double radius) {
        this(new FollowMeLabel(label), radius);
    }

    public FollowMeCircleArea(String label) {
        this(new FollowMeLabel(label));
    }
    public FollowMeCircleArea(FollowMeLabel label) {
        this(label, DEFAULT_RADIUS);
    }

    public FollowMeCircleArea(FollowMeLabel label, double radius) {
        super(label);
        this.radius = radius;
    }

    public double getRadius() {
        return this.radius;
    }

    @Override
    public boolean includes(SurfacePosition position) {
        return position.getDistance(SurfacePosition.ORIGIN) <= this.radius;
    }
}
