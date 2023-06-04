package it.unicam.cs.followme.model.simulation;

import java.util.Optional;
import java.util.function.Predicate;

public final class ProgramCondition<I> implements ProgramLine<I> {

    private Predicate<I> condition;
    private Optional<ProgramLine<I>> nextIfTrue;
    private Optional<ProgramLine<I>> nextIfFalse;

    public ProgramCondition (Predicate<I> condition) {
        this.condition = condition;
        this.nextIfTrue = Optional.empty();
        this.nextIfFalse = Optional.empty();
    }

    public void setIfTrue(Optional<ProgramLine<I>> ifTrue) {
        this.nextIfTrue = ifTrue;
    }
    public void setIfFalse(Optional<ProgramLine<I>> ifFalse) {
        this.nextIfFalse = ifFalse;
    }

    @Override
    public Optional<ProgramLine<I>> execute(I item) {
        if (condition.test(item) && nextIfTrue.isPresent()) return nextIfTrue.get().execute(item);
        else if (!condition.test(item) && nextIfFalse.isPresent()) return nextIfFalse.get().execute(item);
        else return Optional.empty();
    }
}
