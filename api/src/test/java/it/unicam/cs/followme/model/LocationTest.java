package it.unicam.cs.followme.model;

import it.unicam.cs.followme.model.Location;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LocationTest {
    @Test
    public void shouldCalulateAverage () {
        List<Location> list = new ArrayList<>();
        list.add(new Location(3.5, 5));
        list.add(new Location(2.5, 8));
        list.add(new Location(4.5, 11));
        assertEquals(new Location(3.5, 8), Location.averageLocation(list));
    }
}
