package it.unicam.cs.followme.model;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classes implementing this interface are used to represent a spatial configuration of a set of
 * deployed labeled areas.
 *
 * @param <P> type representing the positions
 * @param <L> type representing the labels
 */
public interface Environment<P, L> {

    /**
     * Deploys an area in this environment in such a way that its center will sit in the given position.
     *
     * @param area the area to be deployed.
     * @param position the position where to deploy the area
     */
    void addArea (Area<P, L> area, P position);

    /**
     * Returns the list of all the deployed areas of the environment that contains the given position.
     *
     * @param position the position to be evaluated.
     * @return the list of labeled areas.
     */
    List<Area<P, L>> getAreas (P position);

    /**
     * Returns the list of all the active labels of the environment in the given position. The returned list
     * contains the labels obtained from the areas that include the given position.
     *
     * @param position the position.
     * @return the list of active labels on the position.
     */
    default List<L> getLabels (P position) {
        return getAreas(position)
                .stream()
                .map(Area::getLabel)
                .collect(Collectors.toList());
    }
}
