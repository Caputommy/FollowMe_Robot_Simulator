package it.unicam.cs.followme.model;

import it.unicam.cs.followme.model.items.MovingItem;

import java.util.Map;

/**
 * Classes implementing this interface are used to run a simulation of a system that can evolve over time.
 *
 * @param <P> type representing the positions in the system.
 * @param <I> type representing the items in the system.
 */
public interface SimulationExecutor<P, I> {

    /**
     * Returns the current simulation time of this execution (in seconds), where 0 represent the beginning
     * of the simulation.
     *
     * @return the current simulation time.
     */
    double getCurrentTime();

    /**
     * Returns the position mapping of the items in the current state of the simulation.
     *
     * @return the map of items and their position.
     */
    Map<I, P> getCurrentItemMap();

    /**
     * Adds the given mapped items into the current state simulation in the relative mapped positions.
     *
     * @param mappedItems the items to add in the mapped positions.
     */
    void addItemsToSimulation(Map<I, P> mappedItems);

    /**
     * Runs the simulation for the given time, starting from the current time of simulation.
     *
     * @param time the time to run in the simulation.
     */
    void runSimulation(double time);
}
