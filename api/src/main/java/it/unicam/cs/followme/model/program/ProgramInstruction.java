package it.unicam.cs.followme.model.program;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Instances of this class represent a procedure which is part of sequential program that can be
 * applied on a programmed item.
 *
 * @param <I> type of the programmed item.
 */
public final class ProgramInstruction<I> implements ProgramLine<I> {

    private final Consumer<I> instruction;
    private Optional<ProgramLine<I>> next;

    public ProgramInstruction (Consumer<I> instruction) {
        this(instruction, Optional.empty());
    }

    public ProgramInstruction (Consumer<I> instruction, Optional<ProgramLine<I>> nextInstruction) {
        this.instruction = instruction;
        this.next = nextInstruction;
    }

    public Consumer<I> getInstruction() {
        return instruction;
    }

    public Optional<ProgramLine<I>> getNext() {
        return next;
    }

    /**
     * Sets the next program line to be executed after this instruction.
     *
     * @param next the next program line in the program.
     */
    public void setNext (Optional<ProgramLine<I>> next) {
        this.next = next;
    }

    @Override
    public Optional<ProgramLine<I>> execute(I item) {
        getInstruction().accept(item);
        return getNext();
    }
}
