package it.unicam.cs.followme.model.items;

/**
 * Classes extending this abstract class are particular moving items which follow a uniform rectilinear
 * motion that can be described with direction and velocity.
 *
 * @param <P> the type of positions the item can move among.
 * @param <D> the type of direction the item can follow.
 */
public abstract class UniformMotionMovingItem <P, D extends Direction<P>> implements MovingItem<P> {
    private Direction<P> currentDirection;
    private double currentVelocity;

    public UniformMotionMovingItem (Direction<P> direction) {
        this(direction, 0);
    }
    public UniformMotionMovingItem (Direction<P> direction, double velocity) {
        if (!(Double.isFinite(velocity)))
            throw new IllegalArgumentException("Velocity must be a finite number");
        this.currentDirection = direction;
        this.currentVelocity = velocity;
    }

    public Direction<P> getCurrentDirection() {
        return this.currentDirection;
    }

    public void setCurrentDirection(Direction<P> currentDirection) {
        this.currentDirection = currentDirection;
    }

    public double getCurrentVelocity() {
        return this.currentVelocity;
    }

    public void setCurrentVelocity(double currentVelocity) {
        this.currentVelocity = currentVelocity;
    }

    @Override
    public P moveFrom(P startingPosition, double time) {
        return this.currentDirection.shift(startingPosition, this.currentVelocity * time);
    }
}
