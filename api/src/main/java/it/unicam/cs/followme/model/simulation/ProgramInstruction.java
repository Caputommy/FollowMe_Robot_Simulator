package it.unicam.cs.followme.model.simulation;

import java.util.Optional;
import java.util.function.Consumer;

public final class ProgramInstruction<I> implements ProgramLine<I> {

    private Consumer<I> instruction;
    private Optional<ProgramLine<I>> next;

    public ProgramInstruction (Consumer<I> instruction) {
        this.instruction = instruction;
        this.next = Optional.empty();
    }

    public void setNext (Optional<ProgramLine<I>> next) {
        this.next = next;
    }

    @Override
    public Optional<ProgramLine<I>> execute(I item) {
        this.instruction.accept(item);
        return this.next;
    }
}
