package it.unicam.cs.followme;

import it.unicam.cs.followme.io.FollowMeSimulationLoader;
import it.unicam.cs.followme.model.FollowMeLabel;
import it.unicam.cs.followme.model.environment.Area;
import it.unicam.cs.followme.model.environment.Position;
import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.items.ConditionSignaler;
import it.unicam.cs.followme.model.items.MovingItem;
import it.unicam.cs.followme.io.SimulationLoader;
import it.unicam.cs.followme.model.items.Robot;
import it.unicam.cs.followme.model.simulation.SimulationExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Controller<P extends Position<P>, L, I extends MovingItem<P> & ConditionSignaler<L>>{

    private final SimulationLoader<P, L, I> loader;
    private SimulationExecutor<P, I> executor;
    private Map<Area<P, L>, Set<P>> currentEnvironmentMap;
    private String currentSourceCode;

    public static Controller<SurfacePosition, FollowMeLabel, Robot<SurfacePosition, FollowMeLabel>> getFollowMeController() {
        return new Controller<>(new FollowMeSimulationLoader());
    }

    /**
     * Creates a new controller based on the given {@link SimulationLoader}, used to build an executable simulation
     * from source files.
     *
     * @param loader the simulation loader.
     */
    public Controller(SimulationLoader<P, L, I> loader) {
        this.loader = loader;
        this.executor = loader.getExecutor();
        this.currentEnvironmentMap = new HashMap<>();
        this.currentSourceCode = "";
    }

    /**
     * Loads an environment from file and updates the current map of the environment.
     * If the environment is loaded successfully, the current simulation execution is replaced with a fresh new one
     * with the new loaded setting.
     *
     * @param file the file containing the description of the environment.
     * @throws IOException if an I/O error occurs from the file or if an error occurred during the parsing.
     */
    public void openEnvironment(File file) throws IOException {
        this.currentEnvironmentMap = loader.loadEnvironment(file).getMapping();
        buildExecutor();
    }

    /**
     * Loads a program from file and updates the current source code.
     * If the program is loaded successfully, the current simulation execution is replaced with a fresh new one
     * with the new loaded setting.
     *
     * @param file the file containing the code of the program.
     * @throws IOException if an I/O error occurs from the file or if an error occurred during the parsing.
     */
    public void openProgram(File file) throws IOException {
        loader.loadProgram(file);
        this.currentSourceCode = Files.readString(file.toPath());
        buildExecutor();
    }

    /**
     * Replaces the current simulation execution with a fresh new one with the new given instruction
     * pace time, i.e. the simulation time interval between the execution of each instruction.
     *
     * @param s the instruction pace time to set.
     */
    public void setInstructionPace(double s) {
        loader.setInstructionPaceTime(s);
        buildExecutor();
    }

    /**
     * Returns a copy of the map of areas that are present in the currently loaded environment
     * mapped with their positions.
     *
     * @return the mapping of areas in the loaded environment.
     */
    public Map<Area<P, L>, Set<P>> getCurrentEnvironmentMap() {
        return this.currentEnvironmentMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new HashSet<>(e.getValue())));
    }

    /**
     * Returns the current successfully loaded source code.
     *
     * @return the last loaded source code.
     */
    public String getCurrentSourceCode() {
        return this.currentSourceCode;
    }

    /**
     * Returns the current time in the current simulation execution.
     *
     * @return the current simulation time.
     */
    public double getSimulationCurrentTime() {
        return this.executor.getCurrentTime();
    }

    /**
     * Adds the given mapped items into the current simulation execution in the mapped positions.
     *
     * @param mappedItems the items to add in the mapped positions.
     */
    public void placeItems(Map<I, P> mappedItems) {
        this.executor.addItemsToSimulation(mappedItems);
    }

    /**
     * Removes all the items added into the current simulation execution.
     */
    public void clearPlacedItems() {
        this.executor.clearItems();
    }

    /**
     * Returns the position mapping of the items in the current state of the current simulation execution.
     *
     * @return the map of items and their position.
     */
    public Map<I, P> getCurrentItemMap() {
        return this.executor.getCurrentItemMap();
    }

    /**
     * Runs the current simulation execution for the given time, starting from its current time of simulation.
     *
     * @param s the time to run in the simulation.
     */
    public void runFor(double s) {
        this.executor.runSimulation(s);
    }

    private void buildExecutor() {
        this.executor = loader.getExecutor();
    }

}
