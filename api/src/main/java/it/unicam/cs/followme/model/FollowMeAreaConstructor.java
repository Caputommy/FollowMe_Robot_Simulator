package it.unicam.cs.followme.model;

import it.unicam.cs.followme.utilities.ShapeData;

@FunctionalInterface
public interface FollowMeAreaConstructor {
    public FollowMeArea constructShape(ShapeData data);

    FollowMeAreaConstructor DEFAULT_CONSTRUCTOR = (data) ->
            switch (data.shape()) {
                case "CIRCLE" -> new FollowMeCircleArea(data.label(), data.args()[2]);
                case "RECTANGLE" -> new FollowMeRectangleArea(data.label(), data.args()[2], data.args()[3]);
                default -> throw new IllegalArgumentException();
    };
}
