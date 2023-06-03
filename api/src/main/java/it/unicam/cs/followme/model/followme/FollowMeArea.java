package it.unicam.cs.followme.model.followme;

import it.unicam.cs.followme.model.environment.Area;
import it.unicam.cs.followme.model.environment.SurfacePosition;

public abstract class FollowMeArea implements Area<SurfacePosition, FollowMeLabel> {
    FollowMeLabel label;
    public FollowMeArea(String label) {
        this.label = new FollowMeLabel(label);
    }

    public FollowMeArea(FollowMeLabel label) {
        this.label = label;
    }

    @Override
    public FollowMeLabel getLabel() {
        return label;
    }
}
