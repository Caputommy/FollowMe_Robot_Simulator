package it.unicam.cs.followme.model.simulation;

/**
 * Classes implementing this interface represent an execution flow of a program made
 * of instructions of a certain type.
 *
 * @param <J> the type of program lines executed
 */
public class ProgramExecutor<I> {

    private ProgramLine<I> currentLine;

    public ProgramExecutor (ProgramLine head) {
        this.currentLine = head;
    }

    /**
     * Executes the next instruction of the program.
     */
    void executeOneInstruction();

    /**
     * Returns the execution state of this program.
     *
     * @return true if this program executor has no more instructions to execute.
     */
    boolean isTerminated();
}
