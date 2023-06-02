package it.unicam.cs.followme.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Classes implementing this interface are used to represent a spatial configuration of a set of
 * deployed labeled areas.
 * @param <P>
 * @param <L>
 * @param <A>
 */
public interface Field <P, L, A extends Area<P, L>> {

    /**
     * Returns the list of all the deployed areas of the field that contains the given position.
     * @param position the position to evaluate.
     * @return the list of labeled areas.
     */
    List<Area<P, L>> getAreas (P position);

    /**
     * Deploys an area in this field in such a way its center will sit in the given position.
     * @param area the area to deploy.
     * @param position the position where to deploy the area
     */
    void addArea (Area<P, L> area, P position);

    /**
     * Returns the list of all the active labels of the field in the given position. The returned list
     * contains the labels obtained from the areas that include the given position plus the
     * labels that are active on the whole field.
     * @param position the position.
     * @return the full list of active labels on the position.
     */
    default List<L> getLabels (P position) {
        List<L> labels = getAreas(position)
                .stream()
                .map(Area::getLabel)
                .collect(Collectors.toList());
        labels.addAll(getUniversalLabels());
        return labels;
    }

    List<L> getUniversalLabels ();
}
