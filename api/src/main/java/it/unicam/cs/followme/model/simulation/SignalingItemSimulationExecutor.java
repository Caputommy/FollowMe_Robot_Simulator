package it.unicam.cs.followme.model.simulation;

import it.unicam.cs.followme.model.environment.Environment;
import it.unicam.cs.followme.model.environment.Position;
import it.unicam.cs.followme.model.items.ConditionSignaler;
import it.unicam.cs.followme.model.items.MovingItem;
import it.unicam.cs.followme.model.items.SignalingMovingItemTracker;
import it.unicam.cs.followme.model.program.ProgramExecution;
import it.unicam.cs.followme.model.program.ProgramLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is used to execute a simulation of programmed, moving and signaling items in a specific environment.
 *
 * @param <P> type representing the positions of the space.
 * @param <L> type representing the labels.
 * @param <I> type representing the items.
 */
public final class SignalingItemSimulationExecutor<P extends Position<P>, L, I extends MovingItem<P> & ConditionSignaler<L>>
        implements SimulationExecutor<P, I>{

    private final SignalingMovingItemTracker<P, L, I> tracker;
    private final ProgramLine<I> program;
    private final double instructionPaceTime;
    private List<ProgramExecution<I>> programExecutions;
    private double currentTime;

    public static final double DEFAULT_INSTRUCTION_PACE_TIME = 1;

    /**
     * Creates a new executor from the given information about the initial state of the simulation,
     * where the interval time between the execution of each instruction among the items is 1 (second).
     *
     * @param tracker the initial mapping of the items.
     * @param program the first line of the program to execute on the items.
     */
    public SignalingItemSimulationExecutor(SignalingMovingItemTracker<P, L, I> tracker, ProgramLine<I> program) {
        this(tracker, program, SignalingItemSimulationExecutor.DEFAULT_INSTRUCTION_PACE_TIME);
    }

    /**
     * Creates a new executor from the given information about the initial state of the simulation.
     *
     * @param tracker the initial mapping of the items.
     * @param program the first line of the program to execute on the items.
     * @param executionPaceTime the time interval between each instruction execution of the program.
     */
    public SignalingItemSimulationExecutor(SignalingMovingItemTracker<P, L, I> tracker, ProgramLine<I> program, double executionPaceTime) {
        this.tracker = tracker;
        this.program = program;
        this.instructionPaceTime = executionPaceTime;
        this.currentTime = 0;
        loadProgramOnItems();
    }

    private void loadProgramOnItems() {
        this.programExecutions = new ArrayList<>();
        this.tracker.getItems()
                .stream()
                .forEach(r -> this.programExecutions.add(new ProgramExecution<>(program, r)));
    }

    /**
     * Returns the current simulation time of this execution (in seconds), where 0 represent the beginning
     * of the simulation.
     *
     * @return the current simulation time.
     */
    public double getCurrentTime() {
        return currentTime;
    }

    /**
     * Returns the position mapping of the items in the current state of the simulation.
     *
     * @return the map of items and their position.
     */
    public Map<I, P> getCurrentItemMap() {
        return this.tracker.getMapping();
    }

    /**
     * Adds the given item into the current simulation state in the given position.
     * For that new added item, a new execution of the simulation program is applied, starting from
     * the first instruction of the program.
     *
     * @param item the items to add.
     * @param position the position where to place the item.
     */
    public void addItemToSimulation(I item, P position) {
        tracker.addItem(item, position);
        programExecutions.add(new ProgramExecution<>(program, item));
    }

    /**
     * Adds the given mapped items into the current simulation state in the relative mapped positions.
     * For those new added items, a new execution of the simulation program is applied, starting from
     * the first instruction of the program.
     *
     * @param mappedItems the items to add in the mapped positions.
     */
    public void addItemsToSimulation(Map<I, P> mappedItems) {
        mappedItems.entrySet()
                .stream()
                .forEach(e -> addItemToSimulation(e.getKey(), e.getValue()));
    }

    /**
     * Removes all the items currently into the simulation along with their program executions.
     */
    public void clearItems() {
        this.tracker.clear();
        this.programExecutions.clear();
    }

    /**
     * Runs the simulation for the given time, starting from the current time of simulation, making the
     * items able to move.
     * Whenever, during this process, the current time reaches a multiple of the instruction interval time,
     * a single instruction of the simulation program is executed on all the items in the simulation, depending
     * on the individual execution flow of the items.
     *
     * @param time the time to run in the simulation.
     */
    public void runSimulation(double time) {
        double timeToNextInstruction;
        while (instructionsExecuted(currentTime + time) > instructionsExecuted(currentTime)) {
            timeToNextInstruction = instructionPaceTime * (instructionsExecuted(currentTime)+1) - currentTime;
            evolveSimulation(timeToNextInstruction);
            executeOneStepOfProgram();
            time -= timeToNextInstruction;
        }
        evolveSimulation(time);
    }

    private int instructionsExecuted(double time) {
        return (int) Math.floor(time/instructionPaceTime);
    }

    private void evolveSimulation(double time) {
        tracker.moveAll(time);
        currentTime += time;
    }

    private void executeOneStepOfProgram() {
        programExecutions.stream()
                .forEach(exec -> exec.executeOneStep());
    }
}
