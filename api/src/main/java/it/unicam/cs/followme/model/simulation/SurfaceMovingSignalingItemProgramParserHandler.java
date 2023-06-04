package it.unicam.cs.followme.model.simulation;

import it.unicam.cs.followme.model.environment.Environment;
import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.items.ConditionSignaler;
import it.unicam.cs.followme.model.items.SurfaceDirection;
import it.unicam.cs.followme.model.items.UniformMotionMovingItem;
import it.unicam.cs.followme.utilities.FollowMeParserHandler;

import java.util.Optional;
import java.util.Stack;

public class SurfaceMovingSignalingItemProgramParserHandler<L,
        I extends UniformMotionMovingItem<SurfacePosition> & ConditionSignaler<L>> implements FollowMeParserHandler {
    private final Environment<SurfacePosition, L> environment;
    private final SignaledConditionTracker<SurfacePosition, L, I> conditionTracker;
    private boolean parsingDone;
    private ProgramLine<I> headLine;
    private ProgramLine<I> currentLine;
    private Stack<ProgramCondition<I>> conditionStack;

    public SurfaceMovingSignalingItemProgramParserHandler(Environment<SurfacePosition, L> environment,
                                            SignaledConditionTracker<SurfacePosition, L, I> conditionTracker) {
        this.environment = environment;
        this.conditionTracker = conditionTracker;
        this.parsingDone = false;
    }
    @Override
    public void parsingStarted() {
        this.headLine = new ProgramCondition<I>((item) -> true);
        this.currentLine = this.headLine;
        this.conditionStack = new Stack<>();
    }

    @Override
    public void parsingDone() {
        this.parsingDone = true;
    }

    @Override
    public void moveCommand(double[] args) {
        ProgramInstruction<I> instruction = new ProgramInstruction<>((item) -> {
            item.setCurrentDirection(new SurfaceDirection(args[0], args[1]));
            item.setCurrentVelocity(args[2]);
        });

    }

    @Override
    public void moveRandomCommand(double[] args) {

    }

    @Override
    public void signalCommand(String label) {

    }

    @Override
    public void unsignalCommand(String label) {

    }

    @Override
    public void followCommand(String label, double[] args) {

    }

    @Override
    public void stopCommand() {

    }

    @Override
    public void waitCommand(int s) {

    }

    @Override
    public void repeatCommandStart(int n) {

    }

    @Override
    public void untilCommandStart(String label) {

    }

    @Override
    public void doForeverStart() {

    }

    @Override
    public void doneCommand() {

    }

    private void setCurrentLine (ProgramLine<I> newProgramLine) {
        if (!this.conditionStack.empty() && this.currentLine.equals(this.conditionStack.peek())) {
                this.conditionStack.peek().setIfTrue(Optional.ofNullable(newProgramLine));
            }
        }
        //TODO
    }
}
