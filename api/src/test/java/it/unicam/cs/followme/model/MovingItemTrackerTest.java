package it.unicam.cs.followme.model;

import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.items.MapSignalingMovingItemTracker;
import it.unicam.cs.followme.model.items.MovingItemTracker;
import it.unicam.cs.followme.model.items.Robot;
import it.unicam.cs.followme.model.items.SurfaceDirection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MovingItemTrackerTest {
    private static MovingItemTracker<SurfacePosition, Robot<SurfacePosition, FollowMeLabel>> tracker;
    private static List<Robot<SurfacePosition, FollowMeLabel>> robots;
    private static final long SEED = 123456789;

    @BeforeAll
    public static void initTracker() {
        tracker = new MapSignalingMovingItemTracker<>();
        robots = new ArrayList<>();
        for (int i=0; i<10; i++) robots.add(new Robot<>(SurfaceDirection.randomDirection(SEED+i)));
    }

    @AfterEach
    public void clearTracker() {
        tracker.clear();
    }

    @Test
    public void shouldAddItem() {
        tracker.addItem(robots.get(1), new SurfacePosition(2,3));
        assertTrue(tracker.isPresent(robots.get(1)));

        tracker.addItem(robots.get(2), new SurfacePosition(5,-3));
        tracker.addItem(robots.get(3), new SurfacePosition(0.1,0.55));
        assertTrue(tracker.isPresent(robots.get(2)));
        assertTrue(tracker.isPresent(robots.get(3)));
    }
    @Test
    public void shouldNotBePresent() {
        tracker.addItem(robots.get(1), new SurfacePosition(2,3));
        tracker.addItem(robots.get(2), new SurfacePosition(5,-3));
        tracker.addItem(robots.get(3), new SurfacePosition(0.1,0.55));
        assertFalse(tracker.isPresent(robots.get(4)));
    }

    @Test
    public void shouldGetPosition() {
        tracker.addItem(robots.get(1), new SurfacePosition(2,3));
        assertEquals(new SurfacePosition(2,3), tracker.getCurrentPosition(robots.get(1)).get());

        tracker.addItem(robots.get(2), new SurfacePosition(5,-3));
        tracker.addItem(robots.get(3), new SurfacePosition(0.1,0.55));
        assertEquals(new SurfacePosition(5,-3), tracker.getCurrentPosition(robots.get(2)).get());
        assertEquals(new SurfacePosition(0.1,0.55000001), tracker.getCurrentPosition(robots.get(3)).get());
    }

    @Test
    public void shouldGetMapping() {
        tracker.addItem(robots.get(4), new SurfacePosition(2,3));
        tracker.addItem(robots.get(5), new SurfacePosition(5,-3));
        tracker.addItem(robots.get(6), new SurfacePosition(0.1,0.55));

        Map<Robot<SurfacePosition, FollowMeLabel>, SurfacePosition> mapping = tracker.getMapping();
        assertTrue(mapping.containsKey(robots.get(4)));
        assertTrue(mapping.size() == 3);
        assertEquals(new SurfacePosition(5,-3), mapping.get(robots.get(5)));
    }

    @Test
    public void shouldGetIndependentMapping() {
        tracker.addItem(robots.get(1), new SurfacePosition(2,3));
        tracker.addItem(robots.get(2), new SurfacePosition(5,-3));
        tracker.addItem(robots.get(3), new SurfacePosition(0.1,0.55));

        Map<Robot<SurfacePosition, FollowMeLabel>, SurfacePosition> mapping = tracker.getMapping();
        mapping.remove(robots.get(1));
        assertTrue(!mapping.containsKey(robots.get(1)) && tracker.isPresent(robots.get(1)));
        assertTrue(tracker.getMapping().size() == 3);
    }

    @Test
    public void shouldAddAll() {
        Map<Robot<SurfacePosition, FollowMeLabel>, SurfacePosition> map = new HashMap<>();
        for (int i=0; i<5; i++) map.put(robots.get(i), new SurfacePosition(i, i));

        tracker.addAllItems(map);
        for (int i=0; i<5; i++) assertTrue(tracker.isPresent(robots.get(i)));
    }

    @Test
    public void shouldMoveAll() {
        robots.get(1).setCurrentDirection(new SurfaceDirection (1, 0));
        robots.get(1).setCurrentVelocity(1);
        robots.get(2).setCurrentDirection(new SurfaceDirection (0.5, -0.5));
        robots.get(2).setCurrentVelocity(2);
        robots.get(3).setCurrentVelocity(0);

        tracker.addItem(robots.get(1), new SurfacePosition(2,3));
        tracker.addItem(robots.get(2), new SurfacePosition(5,-3));
        tracker.addItem(robots.get(3), new SurfacePosition(0.1,0.55));

        tracker.moveAll(1);

        assertEquals(new SurfacePosition (3, 3), tracker.getCurrentPosition(robots.get(1)).get());
        assertEquals(new SurfacePosition (5 + (2/Math.sqrt(2)), -3 - (2/Math.sqrt(2))),
                tracker.getCurrentPosition(robots.get(2)).get());
        assertEquals(new SurfacePosition (0.1, 0.55), tracker.getCurrentPosition(robots.get(3)).get());
    }
}
