package it.unicam.cs.followme.model.environment;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classes implementing this interface are used to represent a spatial configuration of a set of
 * deployed labeled {@link Area}s.
 *
 * @param <P> type representing the positions.
 * @param <L> type representing the labels.
 */
public interface Environment<P, L> {

    /**
     * Deploys an area in this environment in such a way that its reference point will sit in the given
     * absolute position.
     *
     * @param area the area to be deployed.
     * @param position the position where to deploy the area.
     */
    void addArea (Area<P, L> area, P position);

    /**
     * Returns the set of all the deployed areas in the environment that include the given absolute position.
     *
     * @param position the position to be evaluated.
     * @return the set of labeled areas that include the given position.
     */
    Set<Area<P, L>> getAreas (P position);

    /**
     * Returns a map of the areas in this environment and the absolute position of their reference point
     * in the environment.
     *
     * @return the areas in this environment mapped with their deployment position.
     */
    Map<Area<P, L>, Set<P>> getMapping();

    /**
     * Returns the set of all the active labels of the environment in the given position. The returned set
     * contains the labels obtained from the areas that include the given position.
     *
     * @param position the position.
     * @return the set of active labels on the position.
     */
    default Set<L> getLabels (P position) {
        return getAreas(position)
                .stream()
                .map(Area::getLabel)
                .collect(Collectors.toSet());
    }
}
