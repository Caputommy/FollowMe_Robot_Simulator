package it.unicam.cs.followme.model;

import it.unicam.cs.followme.model.environment.*;
import it.unicam.cs.followme.model.environment.SurfaceCircleArea;
import it.unicam.cs.followme.model.environment.SurfaceRectangleArea;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class SurfaceEnvironmentTest {

    private static List<Area<SurfacePosition, FollowMeLabel>> sampleAreas;
    private static Environment<SurfacePosition, FollowMeLabel> env;
    
    @BeforeAll
    public static void buildEnvironment() {
        sampleAreas = new ArrayList<>(List.of(
                new SurfaceCircleArea<>(new FollowMeLabel("Circle_label_1"), 3.0),
                new SurfaceCircleArea<>(new FollowMeLabel("Circle_label_2"), 6.0),
                new SurfaceRectangleArea<>(new FollowMeLabel("Rectangle_label_1"), 4.0, 6.0),
                new SurfaceRectangleArea<>(new FollowMeLabel("Rectangle_label_2"), 10.0, 2.0)
        ));

        env = new SurfaceEnvironment<>();
        env.addArea(sampleAreas.get(0), new SurfacePosition(-2, 2));
        env.addArea(sampleAreas.get(1), new SurfacePosition(8, -8));
        env.addArea(sampleAreas.get(2), new SurfacePosition(4, -2));
        env.addArea(sampleAreas.get(3), new SurfacePosition(3, -1));
    }

    @Test
    public void shouldGetAreas1() {
        Set<Area<SurfacePosition, FollowMeLabel>> expected = new HashSet<>();
        expected.add(sampleAreas.get(0));
        expected.add(sampleAreas.get(3));
        assertEquals(expected, env.getAreas(new SurfacePosition(-1, -0.5)));
    }

    @Test
    public void shouldGetAreas2() {
        Set<Area<SurfacePosition, FollowMeLabel>> expected = new HashSet<>();
        expected.add(sampleAreas.get(2));
        expected.add(sampleAreas.get(3));
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
        expected.add(sampleAreas.get(1));
        expected.add(sampleAreas.get(3));
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

    @Test
    public void shouldGetMapping() {
        Map<Area<SurfacePosition, FollowMeLabel>, Set<SurfacePosition>> mapping = env.getMapping();

        for (int i=0; i<4; i++) {
            assertTrue(mapping.containsKey(sampleAreas.get(i)));
            assertEquals(1, mapping.get(sampleAreas.get(i)).size());
        }

        assertTrue(mapping.get(sampleAreas.get(0)).contains(new SurfacePosition(-2, 2)));
        assertTrue(mapping.get(sampleAreas.get(1)).contains(new SurfacePosition(8, -8)));
        assertTrue(mapping.get(sampleAreas.get(2)).contains(new SurfacePosition(4, -2)));
        assertTrue(mapping.get(sampleAreas.get(3)).contains(new SurfacePosition(3, -1)));
    }
}
