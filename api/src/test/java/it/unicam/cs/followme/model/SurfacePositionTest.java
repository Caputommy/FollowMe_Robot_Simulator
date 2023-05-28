package it.unicam.cs.followme.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SurfacePositionTest {
    @Test
    public void shouldCalulateAverage () {
        List<SurfacePosition> list = new ArrayList<>();
        list.add(new SurfacePosition(3.5, 5));
        list.add(new SurfacePosition(2.5, 8));
        list.add(new SurfacePosition(4.5, 11));
        assertEquals(new SurfacePosition(3.5, 8), SurfacePosition.averageLocation(list));
    }
}
