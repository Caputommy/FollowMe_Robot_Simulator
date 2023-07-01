package it.unicam.cs.followme.model;

import it.unicam.cs.followme.model.environment.SurfaceArea;
import it.unicam.cs.followme.model.environment.SurfaceCircleArea;
import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.environment.SurfaceRectangleArea;
import it.unicam.cs.followme.utilities.ShapeData;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@FunctionalInterface
public interface FollowMeAreaConstructor {
    Pair<SurfaceArea<FollowMeLabel>, SurfacePosition> constructArea(ShapeData data);

    FollowMeAreaConstructor DEFAULT_CONSTRUCTOR = (data) -> {
        try {
            SurfacePosition position = new SurfacePosition(data.args()[0], data.args()[1]);
            SurfaceArea<FollowMeLabel> area = switch (data.shape()) {
                case "CIRCLE"    -> new SurfaceCircleArea<>(new FollowMeLabel(data.label()), data.args()[2]);
                case "RECTANGLE" -> new SurfaceRectangleArea<>(new FollowMeLabel(data.label()), data.args()[2], data.args()[3]);
                default          -> throw new IllegalArgumentException();
            };
            return new ImmutablePair<>(area, position);
        } catch (Exception e) {
            return new ImmutablePair<>(new SurfaceCircleArea<>(new FollowMeLabel(data.label()), 0), SurfacePosition.ORIGIN);
        }
    };
}
