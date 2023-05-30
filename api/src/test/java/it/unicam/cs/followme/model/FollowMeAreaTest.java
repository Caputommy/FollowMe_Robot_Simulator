package it.unicam.cs.followme.model;

import it.unicam.cs.followme.util.DoubleRange;
import it.unicam.cs.followme.utilities.ShapeData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FollowMeAreaTest {
    private static final long SEED = 123456789;
    private static final double DEFAULT_CIRCLE_RADIUS = 3.0;
    private static final double DEFAULT_RECTANGLE_WIDTH = 4.0;
    private static final double DEFAULT_RECTANGLE_HEIGHT = 6.5;

    @Test
    public void shouldConstructCircleFromData() {
        ShapeData circleData = new ShapeData("circle_label", "CIRCLE", new double[]{0, 0, DEFAULT_CIRCLE_RADIUS});
        FollowMeArea area = FollowMeAreaConstructor.DEFAULT_CONSTRUCTOR.constructShape(circleData);
        assertTrue(area instanceof FollowMeCircleArea);
        FollowMeCircleArea circle = (FollowMeCircleArea) area;
        assertTrue(circle.getLabel().equals(new FollowMeLabel("circle_label")));
        assertTrue(Double.compare(DEFAULT_CIRCLE_RADIUS, circle.getRadius()) == 0);
    }

    public void shouldConstructRectangleFromData() {
        ShapeData rectangleData = new ShapeData("rectangle_label", "RECTANGLE",
                new double[]{DEFAULT_RECTANGLE_WIDTH, DEFAULT_RECTANGLE_HEIGHT});
        FollowMeArea area = FollowMeAreaConstructor.DEFAULT_CONSTRUCTOR.constructShape(rectangleData);
        assertTrue(area instanceof FollowMeRectangleArea);
        FollowMeRectangleArea rectangle = (FollowMeRectangleArea) area;
        assertTrue(rectangle.getLabel().equals(new FollowMeLabel("rectangle_label")));
        assertTrue(Double.compare(DEFAULT_RECTANGLE_WIDTH, rectangle.getWidth()) == 0);
        assertTrue(Double.compare(DEFAULT_RECTANGLE_HEIGHT, rectangle.getHeight()) == 0);
    }


    @Test
    public void shouldBeIncludedInCircle () {
        FollowMeCircleArea circle = new FollowMeCircleArea("circleLabel", DEFAULT_CIRCLE_RADIUS);
        SurfacePosition randomPosition;
        double inscribedSquareSide = DEFAULT_CIRCLE_RADIUS*Math.sqrt(2);
        DoubleRange admittedRange = new DoubleRange(-(inscribedSquareSide/2), inscribedSquareSide/2);
        for (int i=0; i<10; i++) {
            randomPosition = SurfacePosition.getRandomPositionInRanges(admittedRange, admittedRange, SEED+i);
            assertTrue(circle.includes(randomPosition));
        }
    }

    public void shouldBeIncludedInCircle2 () {
        //TODO
    }

    public void shouldBeIncludedInRectangle () {
        FollowMeRectangleArea rectangle =
                new FollowMeRectangleArea("circleLabel", DEFAULT_RECTANGLE_WIDTH, DEFAULT_RECTANGLE_HEIGHT);
        SurfacePosition randomPosition;
        DoubleRange admittedWidthRange = new DoubleRange(-(DEFAULT_RECTANGLE_WIDTH/2), DEFAULT_RECTANGLE_HEIGHT/2);
        DoubleRange admittedHeightRange = new DoubleRange(-(DEFAULT_RECTANGLE_HEIGHT/2), DEFAULT_RECTANGLE_HEIGHT/2);
        for (int i=0; i<10; i++) {
            randomPosition = SurfacePosition.getRandomPositionInRanges(admittedWidthRange, admittedHeightRange, SEED+i);
            assertTrue(rectangle.includes(randomPosition));
        }
    }

    public void shouldBeIncludedInRectangle2 () {
        //TODO
    }
}
