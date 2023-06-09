package it.unicam.cs.followme.model;

import it.unicam.cs.followme.model.environment.Environment;
import it.unicam.cs.followme.model.environment.SurfaceEnvironment;
import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.followme.FollowMeLabel;
import it.unicam.cs.followme.model.followme.FollowMeProgramBuilder;
import it.unicam.cs.followme.model.items.MapSignalingMovingItemTracker;
import it.unicam.cs.followme.model.items.Robot;
import it.unicam.cs.followme.model.items.SignalingMovingItemTracker;
import it.unicam.cs.followme.model.items.SurfaceDirection;
import it.unicam.cs.followme.model.program.ProgramCondition;
import it.unicam.cs.followme.model.program.ProgramExecution;
import it.unicam.cs.followme.model.program.ProgramInstruction;
import it.unicam.cs.followme.model.program.ProgramLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.testng.AssertJUnit.*;

public class FollowMeProgramBuilderTest {

    private ProgramLine<Robot<SurfacePosition, FollowMeLabel>> program;
    private Environment<SurfacePosition, FollowMeLabel> environment;
    private SignalingMovingItemTracker<SurfacePosition, FollowMeLabel, Robot<SurfacePosition, FollowMeLabel>> tracker;
    private Robot<SurfacePosition, FollowMeLabel> robot;

    @BeforeEach
    public void initVariables() {
        environment = new SurfaceEnvironment<>();
        tracker = new MapSignalingMovingItemTracker<>();
        robot = new Robot<>(new SurfaceDirection(1, 0), 1);
    }

    private void buildProgram1() {
        FollowMeProgramBuilder<Robot<SurfacePosition, FollowMeLabel>> builder =
                new FollowMeProgramBuilder<>(environment, tracker);
        builder.parsingStarted();
        builder.moveCommand(new double[]{0, 1, 1.5});
        builder.doForeverStart();
        builder.signalCommand("label_1");
        builder.unsignalCommand("label_1");
        builder.doneCommand();
        builder.parsingDone();
        program = builder.getProgramHeadLine();
    }

    private void buildProgram2() {
        Robot<SurfacePosition, FollowMeLabel> robot2 = new Robot<>(new SurfaceDirection(1,0), 0);
        robot2.signal(new FollowMeLabel("label_1"));
        tracker.addItem(robot2, new SurfacePosition(-4,0));
        tracker.addItem(robot, new SurfacePosition(3,0));
        FollowMeProgramBuilder<Robot<SurfacePosition, FollowMeLabel>> builder =
                new FollowMeProgramBuilder<>(environment, tracker);
        builder.parsingStarted();
        builder.followCommand("label_1", new double[]{10, 2});
        builder.stopCommand();
        builder.parsingDone();
        program = builder.getProgramHeadLine();
    }

    private void buildProgram3() {
        //TODO move - wait - repeat
    }

    private void buildProgram4() {
        //TODO add area environment, until - moverandom
    }

    @Test
    public void shouldBuildProgram() {
        buildProgram1();
        ProgramLine<Robot<SurfacePosition, FollowMeLabel>> currentLine = program;
        assertTrue(currentLine instanceof ProgramCondition<Robot<SurfacePosition, FollowMeLabel>>);
        ProgramCondition<Robot<SurfacePosition, FollowMeLabel>> condition =
                (ProgramCondition<Robot<SurfacePosition, FollowMeLabel>>) currentLine;
        assertFalse(condition.getCondition().test(robot));
        assertTrue(condition.getNextIfFalse().isPresent());

        ProgramInstruction<Robot<SurfacePosition, FollowMeLabel>> instruction =
                (ProgramInstruction<Robot<SurfacePosition, FollowMeLabel>>)condition.getNextIfFalse().get();
        instruction.getInstruction().accept(robot);
        assertEquals(new SurfaceDirection(0,1), robot.getCurrentDirection());
        assertEquals(1.5, robot.getCurrentVelocity());
    }
    @Test
    public void shouldBuildMove() {
        buildProgram1();
        ProgramExecution<Robot<SurfacePosition, FollowMeLabel>> execution = new ProgramExecution<>(program, robot);
        assertEquals(new SurfaceDirection(1,0), robot.getCurrentDirection());
        assertEquals(1.0, robot.getCurrentVelocity());

        execution.executeOneStep();
        assertEquals(new SurfaceDirection(0,1), robot.getCurrentDirection());
        assertEquals(1.5, robot.getCurrentVelocity());

    }

    @Test
    public void shouldBuildSignalUnsignal() {
        buildProgram1();
        ProgramExecution<Robot<SurfacePosition, FollowMeLabel>> execution = new ProgramExecution<>(program, robot);
        assertEquals(Set.of(), robot.getConditions());

        execution.executeSteps(2);
        assertEquals(Set.of(new FollowMeLabel("label_1")), robot.getConditions());

        execution.executeOneStep();
        assertEquals(Set.of(), robot.getConditions());
    }


    @Test
    public void shouldBuildDoForever() {
        buildProgram1();
        ProgramExecution<Robot<SurfacePosition, FollowMeLabel>> execution = new ProgramExecution<>(program, robot);
        assertEquals(Set.of(), robot.getConditions());

        execution.executeSteps(3);
        assertEquals(Set.of(), robot.getConditions());

        execution.executeSteps(101);
        assertEquals(Set.of(new FollowMeLabel("label_1")), robot.getConditions());

        execution.executeSteps(101);
        assertEquals(Set.of(), robot.getConditions());
    }

    @Test
    public void shouldBuildFollow() {
        buildProgram2();
        ProgramExecution<Robot<SurfacePosition, FollowMeLabel>> execution = new ProgramExecution<>(program, robot);
        assertEquals(new SurfaceDirection(1,0), robot.getCurrentDirection());
        assertEquals(1.0, robot.getCurrentVelocity());

        execution.executeOneStep();
        assertEquals(new SurfaceDirection(-1,0), robot.getCurrentDirection());
        assertEquals(2.0, robot.getCurrentVelocity());
    }

    @Test
    public void shouldBuildStop() {
        buildProgram2();
        ProgramExecution<Robot<SurfacePosition, FollowMeLabel>> execution = new ProgramExecution<>(program, robot);
        assertEquals(new SurfaceDirection(1,0), robot.getCurrentDirection());
        assertEquals(1.0, robot.getCurrentVelocity());

        execution.executeSteps(2);
        assertEquals(0.0, robot.getCurrentVelocity());
    }

    @Test
    public void shouldBuildWait() {
        buildProgram3();
        //TODO
    }

    @Test
    public void shouldBuildRepeat() {
        buildProgram3();
        //TODO
    }

    @Test
    public void shouldBuildUnitl() {
        buildProgram4();
        //TODO
    }


}

