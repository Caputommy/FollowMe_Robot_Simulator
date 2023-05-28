package it.unicam.cs.followme.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
