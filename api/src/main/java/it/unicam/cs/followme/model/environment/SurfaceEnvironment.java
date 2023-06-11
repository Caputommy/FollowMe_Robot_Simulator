package it.unicam.cs.followme.model.environment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An instance of this class is a hash map implemented {@link Environment} based on {@link SurfacePosition} positions.
 *
 * @param <L> type representing the labels.
 */
public class SurfaceEnvironment<L> implements Environment<SurfacePosition, L> {

    private final Map<Area<SurfacePosition, L>, Set<SurfacePosition>> map;

    public SurfaceEnvironment() {
        this.map = new HashMap<>();
    }

    @Override
    public void addArea(Area<SurfacePosition, L> area, SurfacePosition position) {
        this.map.putIfAbsent(area, new HashSet<>());
        this.map.get(area).add(position);
    }

    @Override
    public Map<Area<SurfacePosition, L>, Set<SurfacePosition>> getMapping() {
        return this.map.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new HashSet<>(e.getValue())));
    }

    @Override
    public Set<Area<SurfacePosition, L>> getAreas(SurfacePosition position) {
        return this.map.entrySet()
                .stream()
                .filter(e -> e.getValue()
                                .stream()
                                .anyMatch(p -> e.getKey().includes(position.combineCoordinates((x, y) -> x - y, p)))
                )
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
