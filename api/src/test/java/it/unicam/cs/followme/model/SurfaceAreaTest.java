package it.unicam.cs.followme.model;

import it.unicam.cs.followme.model.environment.SurfaceArea;
import it.unicam.cs.followme.model.environment.SurfaceCircleArea;
import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.environment.SurfaceRectangleArea;
import it.unicam.cs.followme.util.DoubleRange;
import it.unicam.cs.followme.utilities.ShapeData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

public class SurfaceAreaTest {
    private static final long SEED = 123456789;
    private static final double DEFAULT_CIRCLE_RADIUS = 3.0;
    private static final double DEFAULT_RECTANGLE_WIDTH = 4.0;
    private static final double DEFAULT_RECTANGLE_HEIGHT = 6.5;

    @Test
    public void shouldConstructCircleFromData() {
        ShapeData circleData = new ShapeData("Circle_label", "CIRCLE", new double[]{0, 0, DEFAULT_CIRCLE_RADIUS});
        SurfaceArea<FollowMeLabel> area = FollowMeAreaConstructor.DEFAULT_CONSTRUCTOR.constructArea(circleData).getKey();
        assertTrue(area instanceof SurfaceCircleArea<FollowMeLabel>);
        SurfaceCircleArea<FollowMeLabel> circle = (SurfaceCircleArea<FollowMeLabel>) area;
        assertEquals(new FollowMeLabel("Circle_label"), circle.getLabel());
        assertEquals(DEFAULT_CIRCLE_RADIUS, circle.getRadius());
    }

    @Test
    public void shouldConstructRectangleFromData() {
        ShapeData rectangleData = new ShapeData("Rectangle_label", "RECTANGLE",
                new double[]{0, 0, DEFAULT_RECTANGLE_WIDTH, DEFAULT_RECTANGLE_HEIGHT});
        SurfaceArea<FollowMeLabel> area = FollowMeAreaConstructor.DEFAULT_CONSTRUCTOR.constructArea(rectangleData).getKey();
        assertTrue(area instanceof SurfaceRectangleArea<FollowMeLabel>);
        SurfaceRectangleArea<FollowMeLabel> rectangle = (SurfaceRectangleArea<FollowMeLabel>) area;
        assertEquals(new FollowMeLabel("Rectangle_label"), rectangle.getLabel());
        assertEquals(DEFAULT_RECTANGLE_WIDTH, rectangle.getWidth());
        assertEquals(DEFAULT_RECTANGLE_HEIGHT, rectangle.getHeight());
    }


    @Test
    public void shouldBeIncludedInCircle () {
        SurfaceCircleArea<FollowMeLabel> circle = new SurfaceCircleArea<>(new FollowMeLabel("Circle_label_1"), DEFAULT_CIRCLE_RADIUS);
        SurfacePosition randomPosition;
        double inscribedSquareSide = DEFAULT_CIRCLE_RADIUS*Math.sqrt(2);
        DoubleRange admittedRange = new DoubleRange(-(inscribedSquareSide/2), inscribedSquareSide/2);
        for (int i=0; i<10; i++) {
            randomPosition = SurfacePosition.randomPositionInRanges(admittedRange, admittedRange, SEED+i);
            assertTrue(circle.includes(randomPosition));
        }
    }

    @Test
    public void shouldBeIncludedInRectangle () {
        SurfaceRectangleArea<FollowMeLabel> rectangle =
                new SurfaceRectangleArea<>(new FollowMeLabel("Rectangle_label_1"), DEFAULT_RECTANGLE_WIDTH, DEFAULT_RECTANGLE_HEIGHT);
        SurfacePosition randomPosition;
        DoubleRange admittedWidthRange = new DoubleRange(-(DEFAULT_RECTANGLE_WIDTH/2), DEFAULT_RECTANGLE_WIDTH/2);
        DoubleRange admittedHeightRange = new DoubleRange(-(DEFAULT_RECTANGLE_HEIGHT/2), DEFAULT_RECTANGLE_HEIGHT/2);
        for (int i=0; i<10; i++) {
            randomPosition =
                    SurfacePosition.randomPositionInRanges(admittedWidthRange, admittedHeightRange, SEED+i);
            assertTrue(rectangle.includes(randomPosition));
        }
    }

    @Test
    public void shouldBeInvalidLabel() {
        assertThrows(IllegalArgumentException.class,
                () -> new SurfaceCircleArea<>(new FollowMeLabel("#@%!"), DEFAULT_CIRCLE_RADIUS));
        assertThrows(IllegalArgumentException.class,
                () -> new SurfaceCircleArea<>(new FollowMeLabel("Circle_l@bel"), DEFAULT_CIRCLE_RADIUS));
    }
}
