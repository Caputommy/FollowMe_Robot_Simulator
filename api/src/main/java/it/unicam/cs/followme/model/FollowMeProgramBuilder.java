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
import it.unicam.cs.followme.utilities.FollowMeParserHandler;

import java.util.Optional;
import java.util.Set;
import java.util.Stack;

/**
 * Instances of this class are used to build an executable FollowMe program according to the FollowMe commands
 * encountered by a parser.
 *
 * @param <I> type representing the programmed item.
 */

public class FollowMeProgramBuilder<I extends UniformMotionMovingItem<SurfacePosition> & ConditionSignaler<FollowMeLabel>>
        implements FollowMeParserHandler {
    private final Environment<SurfacePosition, FollowMeLabel> environment;
    private final SignalingMovingItemTracker<SurfacePosition, FollowMeLabel, I> itemTracker;
    private boolean parsingDone;
    private ProgramLine<I> headLine;
    private ProgramLine<I> currentLine;
    private Stack<ProgramCondition<I>> conditionStack;

    /**
     * Constructs a program builder that binds the program it produces to the given {@link Environment} and
     * {@link SignalingMovingItemTracker}. When instructions or condition that act upon the state of a certain
     * environment or item tracker are parsed, the given parameters will be used.
     *
     * @param environment the environment to bind the program to.
     * @param itemTracker the itemTracker to bind the program to.
     */
    public FollowMeProgramBuilder(Environment<SurfacePosition, FollowMeLabel> environment,
                                  SignalingMovingItemTracker<SurfacePosition, FollowMeLabel, I> itemTracker) {
        this.environment = environment;
        this.itemTracker = itemTracker;
        this.parsingDone = false;
    }

    /**
     * Returns the current parsing state of this builder. Returns true if the last parsing has successfully
     * concluded and the associated complete program can be obtained from <code>getProgramHeadLine()</code>.
     *
     * @return true if the last parsing has concluded.
     */
    public boolean isParsingDone() {
        return parsingDone;
    }

    /**
     * Returns the first line of the last parsed program built by this builder.
     * If no parsing has started, a null value is returned.
     * If the current parsing has started but has not ended (i.e. <code>isParsingDone()</code> is false),
     * an incomplete program may be returned.
     *
     * @return the last built program by this builder.
     */
    public ProgramLine<I> getProgramHeadLine() {
        return headLine;
    }

    @Override
    public void parsingStarted() {
        this.headLine = new ProgramCondition<I>((item) -> false);
        this.currentLine = this.headLine;
        this.conditionStack = new Stack<>();
        this.parsingDone = false;
    }

    @Override
    public void parsingDone() {
        this.parsingDone = true;
    }

    @Override
    public void moveCommand(double[] args) {
        ProgramInstruction<I> moveInstruction = new ProgramInstruction<>((item) -> {
            item.setCurrentDirection(new SurfaceDirection(args[0], args[1]));
            item.setCurrentVelocity(args[2]);
        });
        setCurrentLine(moveInstruction);
    }

    //Assuming the given range of positions counts as absolute.
    @Override
    public void moveRandomCommand(double[] args) {
        ProgramInstruction<I> moveRandomInstruction = new ProgramInstruction<>((item) -> {
            DoubleRange rangeX = new DoubleRange(args[0], args[1]);
            DoubleRange rangeY = new DoubleRange(args[2], args[3]);
            setRandomDirectionFromAbsoluteRanges(item, rangeX, rangeY);
            item.setCurrentVelocity(args[4]);
        });
        setCurrentLine(moveRandomInstruction);
    }

    @Override
    public void signalCommand(String label) {
        ProgramInstruction<I> signalInstruction = new ProgramInstruction<>((item) -> {
            item.signal(new FollowMeLabel(label));
        });
        setCurrentLine(signalInstruction);
    }

    @Override
    public void unsignalCommand(String label) {
        ProgramInstruction<I> unsignalInstruction = new ProgramInstruction<>((item) -> {
            item.unsignal(new FollowMeLabel(label));
        });
        setCurrentLine(unsignalInstruction);
    }

    @Override
    public void followCommand(String label, double[] args) {
        ProgramInstruction<I> followInstruction = new ProgramInstruction<>((item) -> {
            Set<SurfacePosition> inRangePositions = itemTracker.getSourcePositions(item, new FollowMeLabel(label), args[0]);
            if (inRangePositions.isEmpty()) {
                DoubleRange range = new DoubleRange(-args[0], args[0]);
                setRandomDirectionFromRelativeRanges(item, range, range);
            }
            else item.setCurrentDirection(new SurfaceDirection(SurfacePosition.averageLocation(inRangePositions).get()));
            item.setCurrentVelocity(args[1]);
        });
        setCurrentLine(followInstruction);
    }

    @Override
    public void stopCommand() {
        ProgramInstruction<I> stopInstruction = new ProgramInstruction<>((item) -> {
            item.setCurrentVelocity(0);
        });
        setCurrentLine(stopInstruction);
    }

    @Override
    public void continueCommand(int s) {
        if (s > 0) {
            ProgramInstruction<I> waitInstruction = new ProgramInstruction<>((item) -> {});
            setCurrentLine(waitInstruction);
            continueCommand(s-1);
        }
    }

    @Override
    public void repeatCommandStart(int n) {
        ProgramVariable<I, Integer> counterVariable = new ProgramVariable<>(0);
        ProgramCondition<I> repeatCondition = new ProgramCondition<>((item) -> {
            counterVariable.setValue(item, counterVariable.getValue(item)+1);
            return (counterVariable.getValue(item)%(n+1) != 0);
        });
        setCurrentLine(repeatCondition);
        conditionStack.push(repeatCondition);
    }

    @Override
    public void untilCommandStart(String label) {
        ProgramCondition<I> untilCondition = new ProgramCondition<>((item) -> (
            !environment
                    .getLabels(itemTracker.getCurrentPosition(item).get())
                    .contains(new FollowMeLabel(label))
        ));
        setCurrentLine(untilCondition);
        conditionStack.push(untilCondition);
    }

    @Override
    public void doForeverStart() {
        ProgramCondition<I> foreverCondition = new ProgramCondition<>((item) -> true);
        setCurrentLine(foreverCondition);
        conditionStack.push(foreverCondition);
    }

    @Override
    public void doneCommand() {
        setCurrentLine(this.conditionStack.pop());
    }

    /*
        Chains the given newProgramLine to the current one according to the following logic:
        - if the currentLine is a condition, checks if the newProgramLine has to be set as its ifTrue or the ifFalse branch.
        - if the currentLine is an instruction, simply sets its next line to the newProgramLine.
     */
    private void setCurrentLine (ProgramLine<I> newProgramLine) {
        if (this.currentLine instanceof ProgramCondition<I> currentCondition) {
            if (!this.conditionStack.isEmpty() && this.conditionStack.peek().equals(currentCondition))
                currentCondition.setIfTrue(Optional.of(newProgramLine));
            else currentCondition.setIfFalse(Optional.of(newProgramLine));
        }
        else if (this.currentLine instanceof ProgramInstruction<I> currentInstruction) {
            currentInstruction.setNext(Optional.of(newProgramLine));
        }
        this.currentLine = newProgramLine;
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
}
