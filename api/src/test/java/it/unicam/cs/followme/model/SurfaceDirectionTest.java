package it.unicam.cs.followme.model;

import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.items.SurfaceDirection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SurfaceDirectionTest {
    @Test
    public void shouldConstructNormalizedDirection () {
        SurfaceDirection dir = new SurfaceDirection(new SurfacePosition(3,4));
        SurfacePosition expectedNormalizedPosition = new SurfacePosition((3/25), (4/25));
        assertEquals(expectedNormalizedPosition, dir.getNormalizedPosition());
    }

    @Test
    public void shouldConstructNormalizedDirection2 () {
        SurfaceDirection dir = new SurfaceDirection(
                new SurfacePosition(8,-8), new SurfacePosition(3,4));
        SurfacePosition expectedNormalizedPosition = new SurfacePosition((-5.0/13), (12.0/13));
        assertEquals(expectedNormalizedPosition, dir.getNormalizedPosition());
    }

    @Test
    public void shouldBeInvalidDirection () {
        assertThrows(IllegalArgumentException.class, () -> new SurfaceDirection(0,0));
    }
}
