package it.unicam.cs.followme.model.simulation;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Instances of this class represent a condition which is part of sequential program that can be
 * applied on a programmed item.
 * A condition consists of a crossroads in the program structure it is part of, and defines the
 * predicate that determines which path has to be taken during execution.
 *
 * @param <I> type of the programmed item.
 */
public final class ProgramCondition<I> implements ProgramLine<I> {

    private final Predicate<I> condition;
    private Optional<ProgramLine<I>> nextIfTrue;
    private Optional<ProgramLine<I>> nextIfFalse;

    public ProgramCondition (Predicate<I> condition) {
        this.condition = condition;
        this.nextIfTrue = Optional.empty();
        this.nextIfFalse = Optional.empty();
    }

    /**
     * Sets the next program line to be executed after checking this condition in the case
     * it is evaluated as true.
     *
     * @param ifTrue the next program line in the program if this condition is true.
     */
    public void setIfTrue(Optional<ProgramLine<I>> ifTrue) {
        this.nextIfTrue = ifTrue;
    }

    /**
     * Sets the next program line to be executed after checking this condition in the case
     * it is evaluated as false.
     *
     * @param ifFalse the next program line in the program if this condition is false.
     */
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
