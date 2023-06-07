package it.unicam.cs.followme.model.followme;

import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.utilities.ShapeData;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@FunctionalInterface
public interface FollowMeAreaConstructor {
    public Pair<FollowMeArea, SurfacePosition> constructArea(ShapeData data);

    FollowMeAreaConstructor DEFAULT_CONSTRUCTOR = (data) -> {
        SurfacePosition position = new SurfacePosition(data.args()[0], data.args()[1]);
        FollowMeArea area = switch (data.shape()) {
            case "CIRCLE" -> new FollowMeCircleArea(data.label(), data.args()[2]);
            case "RECTANGLE" -> new FollowMeRectangleArea(data.label(), data.args()[2], data.args()[3]);
            default -> throw new IllegalArgumentException();
        };
        return new ImmutablePair<>(area, position);
    };
}
