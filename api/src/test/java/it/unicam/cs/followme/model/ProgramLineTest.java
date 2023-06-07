package it.unicam.cs.followme.model;

import it.unicam.cs.followme.model.simulation.ProgramCondition;
import it.unicam.cs.followme.model.simulation.ProgramExecution;
import it.unicam.cs.followme.model.simulation.ProgramInstruction;
import it.unicam.cs.followme.model.simulation.ProgramLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProgramLineTest {

    private Optional<ProgramLine<List<String>>> headLine;
    private List<ProgramCondition<List<String>>> conditions;
    private List<ProgramInstruction<List<String>>> instructions;

    @BeforeEach
    public void setLines() {
        conditions = new ArrayList<>();
        instructions = new ArrayList<>();
        conditions.add(new ProgramCondition<>(l -> (l.size() <= 3)));
        conditions.add(new ProgramCondition<>(l -> (l.size() <= 2)));
        instructions.add(new ProgramInstruction<>(l -> l.add("string1")));
        instructions.add(new ProgramInstruction<>(l -> l.add("string2")));
        instructions.add(new ProgramInstruction<>(l -> l.add("string3")));
    }

    private void buildProgram1() {
        conditions.get(0).setIfTrue(Optional.of(instructions.get(0)));
        conditions.get(0).setIfFalse(Optional.of(instructions.get(2)));
        instructions.get(0).setNext(Optional.of(instructions.get(1)));
        instructions.get(1).setNext(Optional.of(conditions.get(0)));
        headLine = Optional.of(conditions.get(0));
    }

    private void buildProgram2() {
        conditions.get(0).setIfTrue(Optional.of(conditions.get(1)));
        conditions.get(0).setIfFalse(Optional.of(instructions.get(2)));
        conditions.get(1).setIfTrue(Optional.of(instructions.get(0)));
        conditions.get(1).setIfFalse(Optional.of(instructions.get(1)));
        instructions.get(0).setNext(Optional.of(conditions.get(1)));
        instructions.get(1).setNext(Optional.of(conditions.get(0)));
        headLine = Optional.of(conditions.get(0));
    }

    @Test
    public void shouldExecuteProgram1() {
        buildProgram1();
        List<String> l = new ArrayList<>();
        Optional<ProgramLine<List<String>>> currentLine = headLine;

        currentLine = currentLine.get().execute(l);
        List<String> expected = new ArrayList<>();
        expected.add("string1");
        assertEquals(expected, l);

        while (currentLine.isPresent())  currentLine = currentLine.get().execute(l);
        expected.addAll(List.of("string2", "string1", "string2", "string3"));
        assertEquals(expected, l);
    }

    @Test
    public void shouldExecuteProgram1WithExecutor() {
        buildProgram1();
        List<String> l = new ArrayList<>();
        ProgramExecution<List<String>> execution = new ProgramExecution<>(headLine, l);
        List<String> expected = new ArrayList<>();

        execution.executeOneStep();
        expected.add("string1");
        assertEquals(expected, l);

        execution.executeSteps(2);
        expected.addAll(List.of("string2", "string1"));
        assertEquals(expected, l);

        execution.executeUntilEnd();
        expected.addAll(List.of("string2", "string3"));
        assertEquals(expected, l);
    }

    @Test
    public void shouldExecuteProgram2() {
        buildProgram2();
        List<String> l = new ArrayList<>();
        Optional<ProgramLine<List<String>>> currentLine = headLine;

        currentLine = currentLine.get().execute(l);
        List<String> expected = new ArrayList<>();
        expected.add("string1");
        assertEquals(expected, l);

        while (currentLine.isPresent())  currentLine = currentLine.get().execute(l);
        expected.addAll(List.of("string1", "string1", "string2", "string3"));
        assertEquals(expected, l);
    }

    @Test
    public void shouldExecuteProgram2WithExecutor() {
        buildProgram2();
        List<String> l = new ArrayList<>();
        ProgramExecution<List<String>> execution = new ProgramExecution<>(headLine, l);
        List<String> expected = new ArrayList<>();

        execution.executeOneStep();
        expected.add("string1");
        assertEquals(expected, l);

        execution.executeSteps(2);
        expected.addAll(List.of("string1", "string1"));
        assertEquals(expected, l);

        execution.executeUntilEnd();
        expected.addAll(List.of("string2", "string3"));
        assertEquals(expected, l);
    }
}
