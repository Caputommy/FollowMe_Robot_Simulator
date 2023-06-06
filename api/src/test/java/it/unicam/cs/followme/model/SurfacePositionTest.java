package it.unicam.cs.followme.model;

import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.util.DoubleRange;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SurfacePositionTest {

    private static final long SEED = 123456789;

    @Test
    public void shouldMapCoordinates () {
        SurfacePosition position = new SurfacePosition(9, 81);

        SurfacePosition expected1 = (new SurfacePosition(9/2, 81/2));
        assertEquals(expected1, position.mapCoordinates(x -> x/2));

        SurfacePosition expected2 = (new SurfacePosition(3, 9));
        assertEquals(expected2, position.mapCoordinates(Math::sqrt));
    }

    @Test
    public void shouldCombineCoordinates () {
        SurfacePosition position1 = new SurfacePosition(2, 10);
        SurfacePosition position2 = new SurfacePosition(3, 0);

        SurfacePosition expected1 = new SurfacePosition(6, 0);
        assertEquals(expected1, position1.combineCoordinates((x1, x2) -> x1 * x2, position2));

        SurfacePosition expected2 = new SurfacePosition(3, 10);
        assertEquals(expected2, position1.combineCoordinates(Double::max, position2));
    }

    @Test
    public void shouldReducePosition () {
        SurfacePosition position = new SurfacePosition(2, 5);

        SurfacePosition expected = new SurfacePosition(2, 5);
        assertEquals(expected, position.reducePosition(SurfacePosition::new));

        assertEquals(32.0, position.reducePosition(Math::pow));

        assertTrue(position.reducePosition(Double::compare) < 0);
    }

    @Test
    public void shouldCalculateAverage () {
        Set<SurfacePosition> set = new HashSet<>();
        set.add(new SurfacePosition(3.5, 5));
        set.add(new SurfacePosition(2.5, 8));
        set.add(new SurfacePosition(4.5, 11));
        assertEquals(new SurfacePosition(3.5, 8), SurfacePosition.averageLocation(set).get());

        set.add(new SurfacePosition(8, 5));
        assertEquals(new SurfacePosition(4.625, 7.25), SurfacePosition.averageLocation(set).get());
    }

    @Test
    public void shouldCalculateDistance () {
        SurfacePosition p1 = new SurfacePosition(13, 18);
        SurfacePosition p2 = new SurfacePosition(5, 3);
        double expectedDistance = 17;
        assertTrue(p1.getDistanceFrom(p2) - expectedDistance <= SurfacePosition.EPSILON);
    }

    @Test
    public void shouldCalculateDistance2 () {
        SurfacePosition p = new SurfacePosition(9, 40);
        double expectedDistance = 41;
        assertTrue(p.getDistanceFrom() - expectedDistance <= SurfacePosition.EPSILON);
    }

    @Test
    public void shouldGenerateRandomPosition () {
        DoubleRange rx, ry;
        SurfacePosition p;
        for (int i = -2; i<2; i++) {
            rx = new DoubleRange(i, i+4);
            ry = new DoubleRange(i, i+4);
            p = SurfacePosition.randomPositionInRanges(rx, ry, SEED);
            assertTrue((i <= p.getX() && p.getX() <= i+4) && (i <= p.getY() && p.getY() <= i+4));
        }

    }
}
