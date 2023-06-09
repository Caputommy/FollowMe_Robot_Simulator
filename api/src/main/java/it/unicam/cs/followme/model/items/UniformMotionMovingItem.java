package it.unicam.cs.followme.model.items;

/**
 * A class extending this abstract class is a particular {@link MovingItem} which follow a uniform
 * motion that can be described with a {@link Direction} and velocity.
 *
 * @param <P> the type representing the positions the item can move among.
 */
public abstract class UniformMotionMovingItem<P> implements MovingItem<P> {
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
