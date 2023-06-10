package it.unicam.cs.followme.model.environment;

import it.unicam.cs.followme.model.environment.Area;
import it.unicam.cs.followme.model.environment.SurfacePosition;

public abstract class SurfaceArea<L> implements Area<SurfacePosition, L> {
    L label;

    public SurfaceArea(L label) {
        this.label = label;
    }

    @Override
    public L getLabel() {
        return label;
    }
}
