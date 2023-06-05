package it.unicam.cs.followme.model;

import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.util.DoubleRange;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SurfacePositionTest {

    private long seed = 123456789;
    @Test
    public void shouldCalulateAverage () {
        Set<SurfacePosition> set = new HashSet<>();
        set.add(new SurfacePosition(3.5, 5));
        set.add(new SurfacePosition(2.5, 8));
        set.add(new SurfacePosition(4.5, 11));
        assertEquals(new SurfacePosition(3.5, 8), SurfacePosition.averageLocation(set));
    }

    @Test
    public void shouldCaluclateDistance () {
        SurfacePosition p1 = new SurfacePosition(13, 18);
        SurfacePosition p2 = new SurfacePosition(5, 3);
        double expectedDistance = 17;
        assertTrue(p1.getDistanceFrom(p2) - expectedDistance <= SurfacePosition.EPSILON);
    }

    @Test
    public void shouldCaluclateDistance2 () {
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
            p = SurfacePosition.getRandomPositionInRanges(rx, ry, seed);
            assertTrue((i <= p.getX() && p.getX() <= i+4) && (i <= p.getY() && p.getY() <= i+4));
        }

    }
}
