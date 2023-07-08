package it.unicam.cs.followme.model.environment;

public abstract class SurfaceArea<L> implements Area<SurfacePosition, L> {
    private final L label;

    public SurfaceArea(L label) {
        this.label = label;
    }

    @Override
    public final L getLabel() {
        return label;
    }
}
