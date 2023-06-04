package it.unicam.cs.followme.model.environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Instances of this class are hash map implemented environments based on surface positions.
 *
 * @param <L> type representing the labels
 */
public class SurfaceEnvironment<L> implements Environment<SurfacePosition, L> {

    private Map<Area<SurfacePosition, L>, SurfacePosition> map;

    public SurfaceEnvironment() {
        this.map = new HashMap<>();
    }

    @Override
    public void addArea(Area<SurfacePosition, L> area, SurfacePosition position) {
        this.map.put(area, position);
    }

    @Override
    public Set<Area<SurfacePosition, L>> getAreas(SurfacePosition position) {
        return this.map.entrySet()
                .stream()
                .filter(e -> e.getKey().includes(position.combineCoordinates((x, y) -> x - y, e.getValue())))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
