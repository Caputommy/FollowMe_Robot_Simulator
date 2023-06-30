package it.unicam.cs.followme.model;

import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.items.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SignalingMovingItemTrackerTest {

    private SignalingMovingItemTracker<SurfacePosition, FollowMeLabel, Robot<SurfacePosition, FollowMeLabel>> tracker;
    private List<Robot<SurfacePosition, FollowMeLabel>> robots;
    private static final long SEED = 123456789;

    @BeforeEach
    public void initTracker() {
        tracker = new MapSignalingMovingItemTracker<>();
        robots = new ArrayList<>();
        for (int i=0; i<10; i++) robots.add(new Robot<>(SurfaceDirection.randomDirection(SEED+i)));
        for (int i=0; i<8; i++) robots.get(i).signal(new FollowMeLabel("Label_"+i));
        for (int i=0; i<10; i++) tracker.addItem(robots.get(i), new SurfacePosition(5-i, 3-i));
    }

    @Test
    public void shouldCaptureSignals() {
        Set<FollowMeLabel> expected = new HashSet<>(Set.of(
                new FollowMeLabel("Label_1"), new FollowMeLabel("Label_2"), new FollowMeLabel("Label_3")));
        assertEquals(expected, tracker.captureSignaledConditions(new SurfacePosition(4,0), 3));
    }

    @Test
    public void shouldNotCaptureSignals() {
        Set<FollowMeLabel> expected = new HashSet<>(Set.of(
                new FollowMeLabel("Label_1"), new FollowMeLabel("Label_2"), new FollowMeLabel("Label_3")));

        robots.get(2).signal(new FollowMeLabel("Label_2_1"));
        robots.get(3).signal(new FollowMeLabel("Label_3_1"));
        robots.get(3).signal(new FollowMeLabel("Label_3_2"));
        assertNotEquals(expected, tracker.captureSignaledConditions(new SurfacePosition(4,0), 3));
    }

    @Test
    public void shouldCaptureNewSignals() {
        Set<FollowMeLabel> expected = new HashSet<>(Set.of(
                new FollowMeLabel("Label_1"), new FollowMeLabel("Label_2"), new FollowMeLabel("Label_3"),
                new FollowMeLabel("Label_2_1"), new FollowMeLabel("Label_3_1"), new FollowMeLabel("Label_3_2")));

        robots.get(2).signal(new FollowMeLabel("Label_2_1"));
        robots.get(3).signal(new FollowMeLabel("Label_3_1"));
        robots.get(3).signal(new FollowMeLabel("Label_3_2"));
        assertEquals(expected, tracker.captureSignaledConditions(new SurfacePosition(4,0), 3));
    }

    @Test
    public void shouldGetSourcePositionsFromPosition() {
        Set<SurfacePosition> expected = new HashSet<>();
        expected.add(new SurfacePosition(1, -1));
        assertEquals(expected, tracker.getSourcePositions(
                new SurfacePosition(-0.5,-0.5), new FollowMeLabel("Label_4"), 2));

        robots.get(5).signal(new FollowMeLabel("Label_4"));
        expected.add(new SurfacePosition(0, -2));
        assertEquals(expected, tracker.getSourcePositions(
                new SurfacePosition(-0.5,-0.5), new FollowMeLabel("Label_4"), 2));
    }

    @Test
    public void shouldGetSourcePositionsFromRobot() {
        for (int i=0; i<5; i++) robots.get(i).signal(new FollowMeLabel("Label_X"));
        Set<SurfacePosition> expected = new HashSet<>();
        expected.add(new SurfacePosition(1, -1));
        expected.add(new SurfacePosition(2, 0));
        assertEquals(expected, tracker.getSourcePositions(
                robots.get(6), new FollowMeLabel("Label_X"), 5));

        robots.get(6).signal(new FollowMeLabel("Label_X"));
        expected.add(new SurfacePosition(-1, -3));
        assertEquals(expected, tracker.getSourcePositions(
                robots.get(6), new FollowMeLabel("Label_X"), 5));
    }
}
