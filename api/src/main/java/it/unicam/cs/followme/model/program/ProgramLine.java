package it.unicam.cs.followme.model.program;

import java.util.Optional;

/**
 * Instances of this class are the building blocks of a sequential deterministic program that
 * consists of instructions that can determine their successor during execution.
 * Possible instances of this class are <code>ProgramCondition</code> and <code>ProgramInstruction</code>.
 *
 * @param <I> type of the item that receives the instructions of the program.
 */
public sealed interface ProgramLine<I> permits ProgramCondition, ProgramInstruction {

    /**
     * Executes the next program instruction encountered from this program line on the given programmed item and
     * returns its next program line in the program.
     * If there are no program lines left in the program, an empty {@link Optional<ProgramLine>} is returned.
     *
     * @return the next program line in the program.
     */
    public Optional<ProgramLine<I>> execute(I item);
}
