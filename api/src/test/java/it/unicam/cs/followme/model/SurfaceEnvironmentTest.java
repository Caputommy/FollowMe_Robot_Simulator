package it.unicam.cs.followme.model;

import it.unicam.cs.followme.model.environment.*;
import it.unicam.cs.followme.model.followme.FollowMeCircleArea;
import it.unicam.cs.followme.model.followme.FollowMeLabel;
import it.unicam.cs.followme.model.followme.FollowMeRectangleArea;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SurfaceEnvironmentTest {

    private static Area<SurfacePosition, FollowMeLabel>[] sampleAreas;
    private static Environment<SurfacePosition, FollowMeLabel> env;
    @BeforeAll
    public static void buildEnvironment() {
        sampleAreas = new Area[]{
                new FollowMeCircleArea("Circle_label_1", 3.0),
                new FollowMeCircleArea("Circle_label_2", 6.0),
                new FollowMeRectangleArea("Rectangle_label_1", 4.0, 6.0),
                new FollowMeRectangleArea("Rectangle_label_2", 10.0, 2.0)
        };
        env = new SurfaceEnvironment<>();
        env.addArea(sampleAreas[0], new SurfacePosition(-2, 2));
        env.addArea(sampleAreas[1], new SurfacePosition(8, -8));
        env.addArea(sampleAreas[2], new SurfacePosition(4, -2));
        env.addArea(sampleAreas[3], new SurfacePosition(3, -1));
    }

    @Test
    public void shouldGetAreas1() {
        Set<Area<SurfacePosition, FollowMeLabel>> expected = new HashSet<>();
        expected.add(sampleAreas[0]);
        expected.add(sampleAreas[3]);
        assertEquals(expected, env.getAreas(new SurfacePosition(-1, -0.5)));
    }

    @Test
    public void shouldGetAreas2() {
        Set<Area<SurfacePosition, FollowMeLabel>> expected = new HashSet<>();
        expected.add(sampleAreas[2]);
        expected.add(sampleAreas[3]);
        assertEquals(expected, env.getAreas(new SurfacePosition(4, -1)));
    }

    @Test
    public void shouldGetAreas3() {
        Set<Area<SurfacePosition, FollowMeLabel>> expected = new HashSet<>();
        assertEquals(expected, env.getAreas(new SurfacePosition(1, -3)));
    }

    @Test
    public void shouldGetAreas4() {
        Set<Area<SurfacePosition, FollowMeLabel>> expected = new HashSet<>();
        expected.add(sampleAreas[1]);
        expected.add(sampleAreas[3]);
        assertEquals(expected, env.getAreas(new SurfacePosition(8, -2)));
    }

    @Test
    public void shouldGetLabels1() {
        Set<FollowMeLabel> expected = new HashSet<>();
        expected.add(new FollowMeLabel("Circle_label_1"));
        expected.add(new FollowMeLabel("Rectangle_label_2"));
        assertEquals(expected, env.getLabels(new SurfacePosition(-1, -0.5)));
    }

    @Test
    public void shouldGetLabels2() {
        Set<FollowMeLabel> expected = new HashSet<>();
        expected.add(new FollowMeLabel("Rectangle_label_2"));
        assertEquals(expected, env.getLabels(new SurfacePosition(-1, -1)));
    }
}
