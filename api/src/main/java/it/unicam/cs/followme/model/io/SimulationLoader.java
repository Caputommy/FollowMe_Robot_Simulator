package it.unicam.cs.followme.model.io;

import it.unicam.cs.followme.model.simulation.SimulationExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Classes implementing this interface are used to build one or more {@link SimulationExecutor}, loading
 * their settings from the given data.
 *
 * @param <P> type representing the positions in the simulated system to build.
 * @param <I> type representing the items in the simulated system to build.
 */
public interface SimulationLoader<P, I> {

    /**
     * Loads the environment described by the given string into this loader.
     * If the environment is loaded successfully, subsequent calls to <code>getExecutor</code> method will provide
     * a {@link SimulationExecutor} based on the given environment until another environment is successfully loaded.
     *
     * @param data the string containing the description of the environment.
     * @return a string describing the result of the parsing.
     * */
    String loadEnvironment(String data);

    /**
     * Loads the environment described by the file referenced by the given path into this loader.
     * If the environment is loaded successfully, subsequent calls to <code>getExecutor</code> method will provide
     * a {@link SimulationExecutor} based on the given environment until another environment is successfully loaded.
     *
     * @param path the path to the file containing the description of the environment.
     * @return a string describing the result of the parsing.
     * @throws IOException if an I/O error occurs reading from the file.
     * */
    default String loadEnvironment(Path path) throws IOException {
        return loadEnvironment(Files.readString(path));
    };

    /**
     * Loads the environment described by the given file into this loader.
     * If the environment is loaded successfully, subsequent calls to <code>getExecutor</code> method will provide
     * a {@link SimulationExecutor} based on the given environment until another environment is successfully loaded.
     *
     * @param file the file containing the description of the environment.
     * @return a string describing the result of the parsing.
     * @throws IOException if an I/O error occurs from the file.
     * */
    default String loadEnvironment(File file) throws IOException {
        return loadEnvironment(file.toPath());
    };

    /**
     * Loads the program described by the given string into this loader.
     * If the program is loaded successfully, subsequent calls to <code>getExecutor</code> method will provide
     * a {@link SimulationExecutor} based on the given environment until another program is successfully loaded.
     *
     * @param program the string containing the code of the program.
     * @return a string describing the result of the parsing.
     * */
    String loadProgram(String program);

    /**
     * Loads the program described by the file referenced by the given path into this loader.
     * If the program is loaded successfully, subsequent calls to <code>getExecutor</code> method will provide
     * a {@link SimulationExecutor} based on the given program until another program is successfully loaded.
     *
     * @param path the path to the file containing the code of the program.
     * @return a string describing the result of the parsing.
     * @throws IOException if an I/O error occurs reading from the file.
     * */
    default String loadProgram(Path path) throws IOException {
        return loadProgram(Files.readString(path));
    };

    /**
     * Loads the program described by the given file into this loader.
     * If the program is loaded successfully, subsequent calls to <code>getExecutor</code> method will provide
     * a {@link SimulationExecutor} based on the given program until another program is successfully loaded.
     *
     * @param file the file containing the code of the program.
     * @return a string describing the result of the parsing.
     * @throws IOException if an I/O error occurs from the file.
     * */
    default String loadProgram(File file) throws IOException {
        return loadProgram(file.toPath());
    };

    /**
     * Returns a fresh new {@link SimulationExecutor} based on the latest environment and program loaded.
     * If the environment and/or the program were not successfully loaded into this loader before,
     * a new executor having empty raw environment and/or empty program is provided.
     *
     * @return the executor based on the current state of the loader.
     */
    SimulationExecutor<P, I> getExecutor();
}
