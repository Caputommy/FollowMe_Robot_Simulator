package it.unicam.cs.followme.model.simulation;

import java.util.Optional;

/**
 * Instances of this class represent an execution flow of a program applied on a certain programmed item.
 *
 * @param <I> type of the programmed item.
 */
public final class ProgramExecution<I> {
    private final I programmedItem;
    private Optional<ProgramLine<I>> currentLine;

    public ProgramExecution (ProgramLine<I> program, I programmedItem) {
        this.currentLine = Optional.ofNullable(program);
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
     * Executes the next instruction of this program execution.
     */
    public void executeOneStep() {
        if (!this.hasTerminated()) {
            currentLine = currentLine.get().execute(programmedItem);
        }
    }
}
