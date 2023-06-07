package it.unicam.cs.followme.model.simulation;

import java.util.Optional;

/**
 * Instances of this class represent an execution flow of a sequential program applied on a certain programmed item.
 *
 * @param <I> type of the programmed item.
 */
public final class ProgramExecution<I> {
    private final I programmedItem;
    private Optional<ProgramLine<I>> currentLine;

    public ProgramExecution (ProgramLine<I> program, I programmedItem) {
        this(Optional.ofNullable(program), programmedItem);
    }

    public ProgramExecution (Optional<ProgramLine<I>> program, I programmedItem) {
        this.currentLine = program;
        this.programmedItem = programmedItem;
    }

    /**
     * Returns the state of this program execution.
     *
     * @return true if this program executor has no more instructions to execute.
     */
    public boolean hasTerminated() {
        return currentLine.isEmpty();
    }

    /**
     * Executes the next instruction in this program execution.
     */
    public void executeOneStep() {
        if (!this.hasTerminated()) {
            currentLine = currentLine.get().execute(programmedItem);
        }
    }

    /**
     * Executes the next <code>n</code> instructions in this program execution.
     * If there are less than <code>n</code> instructions left to execute, the execution proceeds until
     * it has terminated.
     */
    public void executeSteps(int n) {
        while (!this.hasTerminated() && (n-- > 0)) this.executeOneStep();
    }

    /**
     * Executes all the instructions left in this program execution.
     */
    public void executeUntilEnd() {
        while (!this.hasTerminated()) this.executeOneStep();
    }
}
