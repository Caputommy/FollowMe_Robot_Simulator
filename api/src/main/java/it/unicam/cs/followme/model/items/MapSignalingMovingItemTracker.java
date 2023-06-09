package it.unicam.cs.followme.model.items;

import it.unicam.cs.followme.model.environment.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Instances of this class are moving and signaling item trackers that are internally represented
 * with a hash map, that link each item to its position.
 * This is the default implementation of {@link SignalingMovingItemTracker} interface.
 *
 * @param <P> type representing the position of the items.
 * @param <L> type representing the label of the signaled conditions.
 * @param <I> type representing the moving items.
 */
public class MapSignalingMovingItemTracker<P extends Position<P>, L, I extends ConditionSignaler<L> & MovingItem<P>>
        extends SignalingMovingItemTracker<P, L, I>{

    private final Map<I, P> map;

    public MapSignalingMovingItemTracker() {
        this.map = new HashMap<>();
    }

    @Override
    public boolean addItem(I item, P position) {
        if (map.containsKey(item)) return false;
        else {
            map.put(item, position);
            return true;
        }
    }

    @Override
    public boolean isPresent(I item) {
        return map.containsKey(item);
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public Optional<P> getCurrentPosition(I item) {
        return Optional.ofNullable(map.get(item));
    }

    @Override
    public Map<I, P> getMapping() {
        return map.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void moveAll(double time) {
        map.entrySet()
                .stream()
                .forEach(e -> e.setValue(e.getKey().moveFrom(e.getValue(), time)));
    }
}
