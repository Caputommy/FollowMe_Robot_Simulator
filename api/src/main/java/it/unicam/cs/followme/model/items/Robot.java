package it.unicam.cs.followme.model.items;

import java.util.*;

public class Robot<P, L> extends UniformMotionMovingItem<P> implements ConditionSignaler<L> {
    private Set<L> signaledConditions;

    public Robot(Direction<P> direction) {
        super(direction);
        this.signaledConditions = new HashSet<>();
    }

    public Robot(Direction<P> direction, double velocity) {
        super(direction, velocity);
        this.signaledConditions = new HashSet<>();
    }

    @Override
    public Set<L> getConditions() {
        return new HashSet<>(this.signaledConditions);
    }

    @Override
    public boolean signal(L label) {
        return this.signaledConditions.add(label);
    }

    @Override
    public boolean unsignal(L label) {
        return this.signaledConditions.remove(label);
    }
}
