package it.unicam.cs.followme.model;

import it.unicam.cs.followme.model.environment.Environment;
import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.items.ConditionSignaler;
import it.unicam.cs.followme.model.items.SignalingMovingItemTracker;
import it.unicam.cs.followme.model.items.SurfaceDirection;
import it.unicam.cs.followme.model.items.UniformMotionMovingItem;
import it.unicam.cs.followme.model.program.ProgramCondition;
import it.unicam.cs.followme.model.program.ProgramInstruction;
import it.unicam.cs.followme.model.program.ProgramLine;
import it.unicam.cs.followme.model.program.ProgramVariable;
import it.unicam.cs.followme.util.DoubleRange;

import java.util.Set;

/**
 * Instances of this class provide the default interpretation for FollowMe commands.
 *
 * @param <I> type representing the programmed item.
 */
public class FollowMeDefaultCommandEvaluator<I extends UniformMotionMovingItem<SurfacePosition> & ConditionSignaler<FollowMeLabel>>
    implements FollowMeCommandEvaluator<I> {

    private final Environment<SurfacePosition, FollowMeLabel> environment;
    private final SignalingMovingItemTracker<SurfacePosition, FollowMeLabel, I> itemTracker;

    /**
     * Constructs a command evaluator that binds the {@link ProgramLine}s it produces to the given
     * {@link Environment} and {@link SignalingMovingItemTracker}. When the evaluation of instructions or conditions
     * that act upon the state of a certain environment or item tracker is required, the given parameters will be used.
     *
     * @param environment the environment to bind the program lines to.
     * @param itemTracker the itemTracker to bind the program lines to.
     */
    public FollowMeDefaultCommandEvaluator(Environment<SurfacePosition, FollowMeLabel> environment,
                                           SignalingMovingItemTracker<SurfacePosition, FollowMeLabel, I> itemTracker) {
        this.environment = environment;
        this.itemTracker = itemTracker;
    }

    @Override
    public ProgramLine<I> evalMoveCommand(double[] args) {
        return new ProgramInstruction<>((item) -> {
            item.setCurrentDirection(new SurfaceDirection(args[0], args[1]));
            item.setCurrentVelocity(args[2]);
        });
    }

    //Assuming the given ranges of positions count as absolute.
    @Override
    public ProgramLine<I> evalMoveRandomCommand(double[] args) {
        return new ProgramInstruction<>((item) -> {
            DoubleRange rangeX = new DoubleRange(args[0], args[1]);
            DoubleRange rangeY = new DoubleRange(args[2], args[3]);
            setRandomDirectionFromAbsoluteRanges(item, rangeX, rangeY);
            item.setCurrentVelocity(args[4]);
        });
    }

    @Override
    public ProgramLine<I> evalSignalCommand(String label) {
        return new ProgramInstruction<>((item) ->
            item.signal(new FollowMeLabel(label))
        );
    }

    @Override
    public ProgramLine<I> evalUnsignalCommand(String label) {
        return new ProgramInstruction<>((item) ->
            item.unsignal(new FollowMeLabel(label))
        );
    }

    @Override
    public ProgramLine<I> evalFollowCommand(String label, double[] args) {
        return new ProgramInstruction<>((item) -> {
            Set<SurfacePosition> inRangePositions = itemTracker.getSourcePositions(item, new FollowMeLabel(label), args[0]);
            if (inRangePositions.isEmpty() || !itemTracker.isPresent(item)) {
                DoubleRange range = new DoubleRange(-args[0], args[0]);
                setRandomDirectionFromRelativeRanges(item, range, range);
            }
            else setDirectionFromAbsolutePosition(item, SurfacePosition.averageLocation(inRangePositions).get());
            item.setCurrentVelocity(args[1]);
        });
    }

    @Override
    public ProgramLine<I> evalStopCommand() {
        return new ProgramInstruction<>((item) ->
            item.setCurrentVelocity(0)
        );
    }

    @Override
    public ProgramLine<I> evalContinueCommand() {
        return new ProgramInstruction<>((item) -> {});
    }

    @Override
    public ProgramLine<I> evalRepeatCommand(int n) {
        ProgramVariable<I, Integer> counterVariable = new ProgramVariable<>(0);
        return new ProgramCondition<>((item) -> {
            counterVariable.setValue(item, counterVariable.getValue(item)+1);
            return (counterVariable.getValue(item)%(n+1) != 0);
        });
    }

    @Override
    public ProgramLine<I> evalUntilCommand(String label) {
        return  new ProgramCondition<>((item) -> (
                !itemTracker.isPresent(item) ||
                !environment
                        .getLabels(itemTracker.getCurrentPosition(item).get())
                        .contains(new FollowMeLabel(label))
        ));
    }

    @Override
    public ProgramLine<I> evalDoForeverCommand() {
        return new ProgramCondition<>((item) -> true);
    }

    private void setRandomDirectionFromAbsoluteRanges (I item, DoubleRange rangeX, DoubleRange rangeY) {
        item.setCurrentDirection(new SurfaceDirection(
                SurfacePosition.randomPositionInRanges(rangeX, rangeY)
                        .combineCoordinates((x1, x2) -> x1 - x2, itemTracker.getCurrentPosition(item).get())
        ));
    }

    private void setRandomDirectionFromRelativeRanges (I item, DoubleRange rangeX, DoubleRange rangeY) {
        item.setCurrentDirection(new SurfaceDirection(
                SurfacePosition.randomPositionInRanges(rangeX, rangeY))
        );
    }

    private void setDirectionFromAbsolutePosition (I item, SurfacePosition position) {
        item.setCurrentDirection(new SurfaceDirection(
                position.combineCoordinates((x1, x2) -> x1 - x2, itemTracker.getCurrentPosition(item).get())));
    }
}
