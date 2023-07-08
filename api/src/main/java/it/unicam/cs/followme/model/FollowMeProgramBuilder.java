package it.unicam.cs.followme.model;

import it.unicam.cs.followme.model.program.ProgramCondition;
import it.unicam.cs.followme.model.program.ProgramInstruction;
import it.unicam.cs.followme.model.program.ProgramLine;
import it.unicam.cs.followme.utilities.FollowMeParserHandler;

import java.util.Optional;
import java.util.Stack;

/**
 * Instances of this class are used to build an executable FollowMe program according to the FollowMe commands
 * encountered by a parser.
 *
 * @param <I> type representing the programmed item.
 */

public class FollowMeProgramBuilder<I> implements FollowMeParserHandler {
    private final FollowMeCommandEvaluator<I> evaluator;
    private boolean parsingDone;
    private ProgramLine<I> headLine;
    private ProgramLine<I> currentLine;
    private Stack<ProgramCondition<I>> conditionStack;

    /**
     * Constructs a program builder that can produce a structured FollowMe program based on the
     * semantics provided by the given {@link FollowMeCommandEvaluator}, that defines an interpretation
     * for each one of the encountered commands by the parser.
     *
     * @param evaluator the evaluator used to evaluate commands.
     */
    public FollowMeProgramBuilder(FollowMeCommandEvaluator<I> evaluator) {
        this.evaluator = evaluator;
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
        this.headLine = new ProgramCondition<>((item) -> false);
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
        handleNewLine(evaluator.evalMoveCommand(args));
    }

    @Override
    public void moveRandomCommand(double[] args) {
        handleNewLine(evaluator.evalMoveRandomCommand(args));
    }

    @Override
    public void signalCommand(String label) {
        handleNewLine(evaluator.evalSignalCommand(label));
    }

    @Override
    public void unsignalCommand(String label) {
        handleNewLine(evaluator.evalUnsignalCommand(label));
    }

    @Override
    public void followCommand(String label, double[] args) {
        handleNewLine(evaluator.evalFollowCommand(label, args));
    }

    @Override
    public void stopCommand() {
        handleNewLine(evaluator.evalStopCommand());
    }

    @Override
    public void continueCommand(int s) {
        while (s-- > 0) {
            handleNewLine(evaluator.evalContinueCommand());
        }
    }

    @Override
    public void repeatCommandStart(int n) {
        handleNewLine(evaluator.evalRepeatCommand(n));
    }

    @Override
    public void untilCommandStart(String label) {
        handleNewLine(evaluator.evalUntilCommand(label));
    }

    @Override
    public void doForeverStart() {
        handleNewLine(evaluator.evalDoForeverCommand());
    }

    @Override
    public void doneCommand() {
        setCurrentLine(conditionStack.pop());
    }

    //Method invoked whenever a new program line needs to be added to the program.
    private void handleNewLine(ProgramLine<I> programLine) {
        setCurrentLine(programLine);
        if (programLine instanceof ProgramCondition<I> condition) {
            conditionStack.push(condition);
        }
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
}
