package it.unicam.cs.followme.io;

import it.unicam.cs.followme.model.environment.Environment;
import it.unicam.cs.followme.model.program.ProgramLine;
import it.unicam.cs.followme.model.simulation.SimulationExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Classes implementing this interface are used to build one or more {@link SimulationExecutor} based on an
 * environment and a program, loading their settings from the given data.
 *
 * @param <P> type representing the positions in the simulated system to build.
 * @param <L> type representing the labels in the simulated system to build.
 * @param <I> type representing the items in the simulated system to build.
 */
public interface SimulationLoader<P, L, I> {

    /**
     * Loads the environment described by the given string into this loader.
     * If the environment is loaded successfully, subsequent calls to <code>getExecutor</code> method will provide
     * a {@link SimulationExecutor} based on the given environment until another environment is successfully loaded.
     *
     * @param data the string containing the description of the environment.
     * @return a deep copy of the environment obtained as a result of the parsing.
     * @throws IOException if an error occurred during the parsing.
     * */
    Environment<P, L> loadEnvironment(String data) throws IOException;

    /**
     * Loads the environment described by the file referenced by the given path into this loader.
     * If the environment is loaded successfully, subsequent calls to <code>getExecutor</code> method will provide
     * a {@link SimulationExecutor} based on the given environment until another environment is successfully loaded.
     *
     * @param path the path to the file containing the description of the environment.
     * @return a deep copy of the environment obtained as a result of the parsing.
     * @throws IOException if an I/O error occurs reading from the file or if an error occurred during the parsing.
     * */
    default Environment<P, L> loadEnvironment(Path path) throws IOException {
        return loadEnvironment(Files.readString(path));
    }

    /**
     * Loads the environment described by the given file into this loader.
     * If the environment is loaded successfully, subsequent calls to <code>getExecutor</code> method will provide
     * a {@link SimulationExecutor} based on the given environment until another environment is successfully loaded.
     *
     * @param file the file containing the description of the environment.
     * @return a deep copy of the environment obtained as a result of the parsing.
     * @throws IOException if an I/O error occurs from the file or if an error occurred during the parsing.
     * */
    default Environment<P, L> loadEnvironment(File file) throws IOException {
        return loadEnvironment(file.toPath());
    }

    /**
     * Loads the program described by the given string into this loader.
     * If the program is loaded successfully, subsequent calls to <code>getExecutor</code> method will provide
     * a {@link SimulationExecutor} based on the given environment until another program is successfully loaded.
     *
     * @param program the string containing the code of the program.
     * @return a deep copy of the program obtained as a result of the parsing.
     * @throws IOException if an error occurred during the parsing.
     * */
    ProgramLine<I> loadProgram(String program) throws IOException;

    /**
     * Loads the program described by the file referenced by the given path into this loader.
     * If the program is loaded successfully, subsequent calls to <code>getExecutor</code> method will provide
     * a {@link SimulationExecutor} based on the given program until another program is successfully loaded.
     *
     * @param path the path to the file containing the code of the program.
     * @return a deep copy of the program obtained as a result of the parsing.
     * @throws IOException if an I/O error occurs reading from the file or if an error occurred during the parsing.
     * */
    default ProgramLine<I> loadProgram(Path path) throws IOException {
        return loadProgram(Files.readString(path));
    }

    /**
     * Loads the program described by the given file into this loader.
     * If the program is loaded successfully, subsequent calls to <code>getExecutor</code> method will provide
     * a {@link SimulationExecutor} based on the given program until another program is successfully loaded.
     *
     * @param file the file containing the code of the program.
     * @return a deep copy of the program obtained as a result of the parsing.
     * @throws IOException if an I/O error occurs from the file or if an error occurred during the parsing.
     * */
    default ProgramLine<I> loadProgram(File file) throws IOException {
        return loadProgram(file.toPath());
    }

    /**
     * Sets the instruction pace time of the simulation in the loader, i.e. the simulation time interval
     * between the execution of each instruction.
     * Subsequent calls to <code>getExecutor</code> method will provide a {@link SimulationExecutor} based on the
     * given program instruction pace time until another one is set.
     *
     * @param s the instruction pace time of the executor to be built.
     */
    void setInstructionPaceTime(double s);

    /**
     * Returns a fresh new {@link SimulationExecutor} based on the latest environment and program loaded.
     * If the environment and/or the program were not successfully loaded into this loader before,
     * a new executor having empty environment and/or empty program is provided.
     *
     * @return the executor based on the current state of the loader.
     */
    SimulationExecutor<P, I> getExecutor();
}
