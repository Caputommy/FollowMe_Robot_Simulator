package it.unicam.cs.followme.model;

import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.followme.FollowMeLabel;
import it.unicam.cs.followme.model.items.Robot;
import it.unicam.cs.followme.model.items.SurfaceDirection;
import it.unicam.cs.followme.model.program.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProgramTest {

    private Optional<ProgramLine<List<String>>> listHeadLine;
    private List<ProgramCondition<List<String>>> listConditions;
    private List<ProgramInstruction<List<String>>> listInstructions;

    private Optional<ProgramLine<Robot<SurfacePosition, FollowMeLabel>>> robotHeadLine;
    private List<ProgramCondition<Robot<SurfacePosition, FollowMeLabel>>> robotConditions;
    private List<ProgramInstruction<Robot<SurfacePosition, FollowMeLabel>>> robotInstructions;

    @BeforeEach
    public void setLines() {
        listConditions = new ArrayList<>();
        listInstructions = new ArrayList<>();
        listConditions.add(new ProgramCondition<>(l -> (l.size() <= 3)));
        listConditions.add(new ProgramCondition<>(l -> (l.size() <= 2)));
        listInstructions.add(new ProgramInstruction<>(l -> l.add("string1")));
        listInstructions.add(new ProgramInstruction<>(l -> l.add("string2")));
        listInstructions.add(new ProgramInstruction<>(l -> l.add("string3")));

        robotConditions = new ArrayList<>();
        robotInstructions = new ArrayList<>();
        ProgramVariable<Robot<SurfacePosition, FollowMeLabel>, Integer> var = new ProgramVariable<>(100);
        robotConditions.add(new ProgramCondition<>(r -> (var.getValue(r) >= 10)));
        robotInstructions.add(new ProgramInstruction<>(r -> {
            r.signal(new FollowMeLabel("varValue_"+var.getValue(r)));
            var.setValue(r, var.getValue(r)-40);
        }));
        robotInstructions.add(new ProgramInstruction<>(r -> r.signal(new FollowMeLabel("label_1"))));
    }

    private void buildProgram1() {
        listConditions.get(0).setIfTrue(Optional.of(listInstructions.get(0)));
        listConditions.get(0).setIfFalse(Optional.of(listInstructions.get(2)));
        listInstructions.get(0).setNext(Optional.of(listInstructions.get(1)));
        listInstructions.get(1).setNext(Optional.of(listConditions.get(0)));
        listHeadLine = Optional.of(listConditions.get(0));
    }

    private void buildProgram2() {
        listConditions.get(0).setIfTrue(Optional.of(listConditions.get(1)));
        listConditions.get(0).setIfFalse(Optional.of(listInstructions.get(2)));
        listConditions.get(1).setIfTrue(Optional.of(listInstructions.get(0)));
        listConditions.get(1).setIfFalse(Optional.of(listInstructions.get(1)));
        listInstructions.get(0).setNext(Optional.of(listConditions.get(1)));
        listInstructions.get(1).setNext(Optional.of(listConditions.get(0)));
        listHeadLine = Optional.of(listConditions.get(0));
    }

    private void buildProgram3() {
        robotConditions.get(0).setIfTrue(Optional.of(robotInstructions.get(0)));
        robotConditions.get(0).setIfFalse(Optional.of(robotInstructions.get(1)));
        robotInstructions.get(0).setNext(Optional.of(robotConditions.get(0)));
        robotHeadLine = Optional.of(robotConditions.get(0));
    }

    @Test
    public void shouldExecuteProgram1() {
        buildProgram1();
        List<String> l = new ArrayList<>();
        Optional<ProgramLine<List<String>>> currentLine = listHeadLine;

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
        ProgramExecution<List<String>> execution = new ProgramExecution<>(listHeadLine, l);
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
        Optional<ProgramLine<List<String>>> currentLine = listHeadLine;

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
        ProgramExecution<List<String>> execution = new ProgramExecution<>(listHeadLine, l);
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

    @Test
    public void shouldExecuteProgram3() {
        buildProgram3();
        Robot<SurfacePosition, FollowMeLabel> r = new Robot<>(new SurfaceDirection(1, 0));
        Optional<ProgramLine<Robot<SurfacePosition, FollowMeLabel>>> currentLine = robotHeadLine;

        currentLine = currentLine.get().execute(r);
        Set<FollowMeLabel> expected = new HashSet<>();
        expected.add(new FollowMeLabel("varValue_100"));
        assertEquals(expected, r.getConditions());

        while (currentLine.isPresent())  currentLine = currentLine.get().execute(r);
        expected.addAll(List.of(new FollowMeLabel("varValue_60"), new FollowMeLabel("varValue_20"), new FollowMeLabel("label_1")));
        assertEquals(expected, r.getConditions());
    }

    @Test
    public void shouldExecuteProgram3WithExecutor() {
        buildProgram3();
        Robot<SurfacePosition, FollowMeLabel> r = new Robot<>(new SurfaceDirection(1, 0));
        ProgramExecution<Robot<SurfacePosition, FollowMeLabel>> execution = new ProgramExecution<>(robotHeadLine, r);
        Set<FollowMeLabel> expected = new HashSet<>();

        execution.executeOneStep();
        expected.add(new FollowMeLabel("varValue_100"));
        assertEquals(expected, r.getConditions());

        execution.executeUntilEnd();
        expected.addAll(List.of(new FollowMeLabel("varValue_60"), new FollowMeLabel("varValue_20"), new FollowMeLabel("label_1")));
        assertEquals(expected, r.getConditions());
    }
}
