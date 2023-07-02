package it.unicam.cs.followme.model;

import it.unicam.cs.followme.model.program.ProgramLine;

/**
 * Classes implementing this interface provide a semantic interpretation of the FollowMe commands in
 * terms of executable {@link ProgramLine}s.
 *
 * @param <I> type representing the programmed item.
 */
public interface FollowMeCommandEvaluator<I> {

    /**
     * Provides the interpretation for command "MOVE".
     *
     * @param args command arguments.
     */
    ProgramLine<I> evalMoveCommand(double[] args);

    /**
     * Provides the interpretation for command "MOVE RANDOM".
     *
     * @param args command arguments.
     */
    ProgramLine<I> evalMoveRandomCommand(double[] args);

    /**
     * Provides the interpretation for command "SIGNAL".
     *
     * @param label label to signal.
     */
    ProgramLine<I> evalSignalCommand(String label);

    /**
     * Provides the interpretation for command "UNSIGNAL".
     *
     * @param label label to unsignal.
     */
    ProgramLine<I> evalUnsignalCommand(String label);

    /**
     * Provides the interpretation for command "FOLLOW".
     *
     * @param label label to follow.
     * @param args command arguments.
     */
    ProgramLine<I> evalFollowCommand(String label, double[] args);

    /**
     * Provides the interpretation for command "STOP".
     */
    ProgramLine<I> evalStopCommand();

    /**
     * Provides the interpretation for command "CONTINUE" (1 second/step).
     */
    ProgramLine<I> evalContinueCommand();

    /**
     * Provides the interpretation for command "REPEAT".
     *
     * @param n number of iterations.
     */
    ProgramLine<I> evalRepeatCommand(int n);

    /**
     * Provides the interpretation for command "UNTIL".
     *
     * @param label name of a label
     */
    ProgramLine<I> evalUntilCommand(String label);

    /**
     * Provides the interpretation for command "DO FOREVER".
     */
    ProgramLine<I> evalDoForeverCommand();

}
