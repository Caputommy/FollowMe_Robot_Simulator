package it.unicam.cs.followme.model;

public interface Position<D> {
    public D getDirectionFrom(Position<D> position);
}
