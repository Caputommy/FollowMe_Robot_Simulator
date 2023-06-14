package it.unicam.cs.followme.io;

import it.unicam.cs.followme.model.FollowMeLabel;
import it.unicam.cs.followme.model.environment.*;
import it.unicam.cs.followme.model.items.Direction;
import it.unicam.cs.followme.model.items.Robot;
import it.unicam.cs.followme.model.items.SurfaceDirection;
import it.unicam.cs.followme.model.program.ProgramExecution;
import it.unicam.cs.followme.model.program.ProgramLine;
import it.unicam.cs.followme.model.simulation.SimulationExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.testng.AssertJUnit.*;

public class SimulationLoaderTest {
    private SimulationLoader<SurfacePosition, FollowMeLabel, Robot<SurfacePosition, FollowMeLabel>> loader;
    private Environment<SurfacePosition, FollowMeLabel> parsedEnv;
    private Robot<SurfacePosition, FollowMeLabel> robot;
    private ProgramExecution<Robot<SurfacePosition, FollowMeLabel>> execution;
    private SimulationExecutor<SurfacePosition, Robot<SurfacePosition, FollowMeLabel>> simulationExecutor;

    private static final String ENV1 =
            "Label_1 CIRCLE 8 -8 6\n" +
            "Label_2 CIRCLE -2 2 1.5\n" +
            "Label_3 RECTANGLE 3 -1 10 2\n" +
            "Label_4 RECTANGLE 4 -2 4 6";

    private static final String ENV2 =
            "Label_1 CIRCLE -8 18 1.5\n" +
            "Label_1 CIRCLE -10 20 1.5";


    private static final String PROGRAM1 =
            "MOVE 0 1 2\n" +
            "SIGNAL Label_1\n" +
            "UNSIGNAL Label_1\n" +
            "STOP";

    private static final String PROGRAM2 =
            "DO FOREVER\n" +
            "   REPEAT 4\n" +
            "       CONTINUE 2\n" +
            "   DONE\n" +
            "   MOVE 0 1 2\n" +
            "DONE";

    @BeforeEach
    public void createLoader() {
        loader = new FollowMeSimulationLoader();
    }

    private void initParsedEnvironment(String environment) {
        assertDoesNotThrow(() -> loader.loadEnvironment(environment));
        try {
            parsedEnv = loader.loadEnvironment(environment);
        }
        catch (IOException e) {}
    }

    @Test
    public void shouldParseEnvironment1() {
        initParsedEnvironment(ENV1);

        Map<Area<SurfacePosition, FollowMeLabel>, Set<SurfacePosition>> expectedMap = new HashMap<>();
        List<Set<SurfacePosition>> sets = new ArrayList<>();
        for (int i=0; i<4; i++) sets.add(new HashSet<>());
        sets.get(0).add(new SurfacePosition(8, -8));
        sets.get(1).add(new SurfacePosition(-2, 2));
        sets.get(2).add(new SurfacePosition(3, -1));
        sets.get(3).add(new SurfacePosition(4, -2));
        expectedMap.put(new SurfaceCircleArea<>(new FollowMeLabel("LABEL_1"), 6), sets.get(0));
        expectedMap.put(new SurfaceCircleArea<>(new FollowMeLabel("LABEL_2"), 1.5), sets.get(1));
        expectedMap.put(new SurfaceRectangleArea<>(new FollowMeLabel("LABEL_3"), 10, 2), sets.get(2));
        expectedMap.put(new SurfaceRectangleArea<>(new FollowMeLabel("LABEL_4"), 4,6), sets.get(3));

        assertEquals(expectedMap, parsedEnv.getMapping());
    }
    @Test
    public void shouldParseEnvironment2() {
        initParsedEnvironment(ENV2);

        Map<Area<SurfacePosition, FollowMeLabel>, Set<SurfacePosition>> expectedMap = new HashMap<>();
        Set<SurfacePosition> set = new HashSet<>();
        set.add(new SurfacePosition(-8, 18));
        set.add(new SurfacePosition(-10, 20));
        expectedMap.put(new SurfaceCircleArea<>(new FollowMeLabel("LABEL_1"), 1.5), set);

        assertEquals(expectedMap, parsedEnv.getMapping());
    }

    private void initExecution(String code) {
        assertDoesNotThrow(() -> loader.loadProgram(code));
        try {
            ProgramLine<Robot<SurfacePosition, FollowMeLabel>> program = loader.loadProgram(code);
            robot = new Robot<>(new SurfaceDirection(1,0));
            execution = new ProgramExecution<>(program, robot);
        }
        catch (IOException e) {}
    }

    @Test
    public void shouldParseProgram() {
        initExecution(PROGRAM1);

        execution.executeSteps(4);
        assertTrue(execution.hasTerminated());
    }

    @Test
    public void shouldParseMove() {
        initExecution(PROGRAM1);

        execution.executeOneStep();
        assertEquals(new SurfaceDirection(0,1), robot.getCurrentDirection());
        assertEquals(2.0, robot.getCurrentVelocity());
    }

    @Test
    public void shouldParseSignal() {
        initExecution(PROGRAM1);

        execution.executeSteps(2);
        Set<FollowMeLabel> expectedLabels = new HashSet<>();
        expectedLabels.add(new FollowMeLabel("LABEL_1"));
        assertEquals(expectedLabels, robot.getConditions());
    }

    @Test
    public void shouldParseUnsignal() {
        initExecution(PROGRAM1);

        execution.executeSteps(3);
        assertEquals(Set.of(), robot.getConditions());
    }

    @Test
    public void shouldParseStop() {
        initExecution(PROGRAM1);

        execution.executeSteps(4);
        assertEquals(0.0, robot.getCurrentVelocity());
    }

    @Test
    public void shouldParseContinue() {
        initExecution(PROGRAM2);

        for (int i=0; i<2; i++) {
            execution.executeOneStep();
            assertEquals(new SurfaceDirection(1,0), robot.getCurrentDirection());
            assertEquals(0.0, robot.getCurrentVelocity());
        }
    }

    @Test
    public void shouldParseRepeat() {
        initExecution(PROGRAM2);

        execution.executeSteps(8);
        assertEquals(new SurfaceDirection(1,0), robot.getCurrentDirection());
        assertEquals(0.0, robot.getCurrentVelocity());

        execution.executeOneStep();
        assertEquals(2.0, robot.getCurrentVelocity());
    }

    @Test
    public void shouldParseDoForever() {
        initExecution(PROGRAM2);

        execution.executeSteps(9);
        assertFalse(execution.hasTerminated());

        execution.executeSteps(100);
        assertFalse(execution.hasTerminated());

    }

    private void initSimulation(String environment, String code) {
        assertDoesNotThrow(() -> loader.loadEnvironment(environment));
        assertDoesNotThrow(() -> loader.loadProgram(code));
        try {
            ProgramLine<Robot<SurfacePosition, FollowMeLabel>> program = loader.loadProgram(code);
            parsedEnv = loader.loadEnvironment(environment);
            robot = new Robot<>(new SurfaceDirection(1,0));
            simulationExecutor = loader.getExecutor();
        }
        catch (IOException e) {}
    }

    @Test
    public void shouldAddItemToSimulation() {
        initSimulation(ENV1, PROGRAM1);
        simulationExecutor.addItemToSimulation(robot, new SurfacePosition(0,0));

        assertEquals(new SurfacePosition(0,0), simulationExecutor.getCurrentItemMap().get(robot));
    }

    @Test
    public void shouldRunSimulation() {
        initSimulation(ENV1, PROGRAM1);
        simulationExecutor.addItemToSimulation(robot, new SurfacePosition(0,0));

        simulationExecutor.runSimulation(2);
        assertEquals(2.0, robot.getCurrentVelocity());
        assertEquals(Set.of(new FollowMeLabel("LABEL_1")), robot.getConditions());

        simulationExecutor.runSimulation(0.5);
        assertEquals(Set.of(new FollowMeLabel("LABEL_1")), robot.getConditions());
    }

    private Map<Robot<SurfacePosition, FollowMeLabel>, SurfacePosition> getRobotMap1() {
        Map<Robot<SurfacePosition, FollowMeLabel>, SurfacePosition> map = new HashMap<>();
        Direction<SurfacePosition> dir = new SurfaceDirection(0,1);
        map.put(new Robot<>(dir), new SurfacePosition(6,6));
        for (Robot<SurfacePosition, FollowMeLabel> r : map.keySet()) r.signal(new FollowMeLabel("Label_1"));
        map.put(new Robot<>(dir), new SurfacePosition(-2,4));
        for (Robot<SurfacePosition, FollowMeLabel> r : map.keySet()) r.signal(new FollowMeLabel("Label_2"));
        map.put(new Robot<>(dir), new SurfacePosition(-10,-5));
        for (Robot<SurfacePosition, FollowMeLabel> r : map.keySet()) r.signal(new FollowMeLabel("Label_3"));
        return map;
    }
    @Test
    private void shouldParseFollow() {
        initSimulation(ENV1, PROGRAM2);
        simulationExecutor.addItemsToSimulation(getRobotMap1());
        simulationExecutor.addItemToSimulation(robot, new SurfacePosition(0,0));
        //TODO
    }
}
