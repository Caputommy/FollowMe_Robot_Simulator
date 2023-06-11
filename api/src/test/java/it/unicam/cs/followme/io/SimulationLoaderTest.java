package it.unicam.cs.followme.io;

import it.unicam.cs.followme.model.FollowMeLabel;
import it.unicam.cs.followme.model.environment.*;
import it.unicam.cs.followme.model.items.Robot;
import it.unicam.cs.followme.model.items.SurfaceDirection;
import it.unicam.cs.followme.model.program.ProgramExecution;
import it.unicam.cs.followme.model.program.ProgramLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOError;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.testng.AssertJUnit.*;

public class SimulationLoaderTest {

    private SimulationLoader<SurfacePosition, FollowMeLabel, Robot<SurfacePosition, FollowMeLabel>> loader;

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
            "       SKIP 2\n" +
            "   DONE\n" +
            "   MOVE 0 1 2\n" +
            "DONE";

    @BeforeEach
    public void createLoader() {
        loader = new FollowMeSimulationLoader();
    }

    @Test
    public void shouldParseEnvironment1() {
        assertDoesNotThrow(() -> loader.loadEnvironment(ENV1));
        try {
            Environment<SurfacePosition, FollowMeLabel> parsedEnv = loader.loadEnvironment(ENV1);

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
        catch (IOException e) {}
    }
    @Test
    public void shouldParseEnvironment2() {
        assertDoesNotThrow(() -> loader.loadEnvironment(ENV2));
        try {
            Environment<SurfacePosition, FollowMeLabel> parsedEnv = loader.loadEnvironment(ENV2);

            Map<Area<SurfacePosition, FollowMeLabel>, Set<SurfacePosition>> expectedMap = new HashMap<>();
            Set<SurfacePosition> set = new HashSet<>();
            set.add(new SurfacePosition(-8, 18));
            set.add(new SurfacePosition(-10, 20));
            expectedMap.put(new SurfaceCircleArea<>(new FollowMeLabel("LABEL_1"), 1.5), set);

            assertEquals(expectedMap, parsedEnv.getMapping());
        }
        catch (IOException e) {}
    }

    @Test
    public void shouldParseProgram1() {
        assertDoesNotThrow(() -> loader.loadProgram(PROGRAM1));
        try {
            ProgramLine<Robot<SurfacePosition, FollowMeLabel>> program = loader.loadProgram(PROGRAM1);
            Robot<SurfacePosition, FollowMeLabel> robot = new Robot<>(new SurfaceDirection(1,0));
            ProgramExecution<Robot<SurfacePosition, FollowMeLabel>> execution = new ProgramExecution<>(program, robot);

            execution.executeOneStep();
            assertEquals(new SurfaceDirection(0,1), robot.getCurrentDirection());
            assertEquals(2.0, robot.getCurrentVelocity());

            execution.executeOneStep();
            Set<FollowMeLabel> expectedLabels = new HashSet<>();
            expectedLabels.add(new FollowMeLabel("LABEL_1"));
            assertEquals(expectedLabels, robot.getConditions());

            execution.executeOneStep();
            expectedLabels.remove(new FollowMeLabel("LABEL_1"));
            assertEquals(expectedLabels, robot.getConditions());

            execution.executeOneStep();
            assertEquals(0.0, robot.getCurrentVelocity());

            assertTrue(execution.hasTerminated());
        }
        catch (IOException e) {}
    }

    @Test
    public void shouldParseProgram2() {
        assertDoesNotThrow(() -> loader.loadProgram(PROGRAM2));
        try {
            ProgramLine<Robot<SurfacePosition, FollowMeLabel>> program = loader.loadProgram(PROGRAM2);
            Robot<SurfacePosition, FollowMeLabel> robot = new Robot<>(new SurfaceDirection(1,0));
            ProgramExecution<Robot<SurfacePosition, FollowMeLabel>> execution = new ProgramExecution<>(program, robot);

            execution.executeOneStep();
            assertEquals(new SurfaceDirection(1,0), robot.getCurrentDirection());
            assertEquals(0.0, robot.getCurrentVelocity());

            execution.executeSteps(7);
            assertEquals(new SurfaceDirection(1,0), robot.getCurrentDirection());
            assertEquals(0.0, robot.getCurrentVelocity());

            execution.executeOneStep();
            assertEquals(2.0, robot.getCurrentVelocity());

            assertFalse(execution.hasTerminated());
        }
        catch (IOException e) {}
    }

    @Test
    public void shouldLoadSimulation() {
        //TODO
    }
}
