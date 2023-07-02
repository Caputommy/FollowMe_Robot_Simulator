package it.unicam.cs.followme.io;

import it.unicam.cs.followme.model.FollowMeAreaConstructor;
import it.unicam.cs.followme.model.FollowMeDefaultCommandEvaluator;
import it.unicam.cs.followme.model.FollowMeLabel;
import it.unicam.cs.followme.model.FollowMeProgramBuilder;
import it.unicam.cs.followme.model.environment.Environment;
import it.unicam.cs.followme.model.environment.SurfaceEnvironment;
import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.items.MapSignalingMovingItemTracker;
import it.unicam.cs.followme.model.items.Robot;
import it.unicam.cs.followme.model.items.SignalingMovingItemTracker;
import it.unicam.cs.followme.model.program.ProgramCondition;
import it.unicam.cs.followme.model.program.ProgramLine;
import it.unicam.cs.followme.model.simulation.SignalingItemSimulationExecutor;
import it.unicam.cs.followme.model.simulation.SimulationExecutor;
import it.unicam.cs.followme.utilities.FollowMeParser;
import it.unicam.cs.followme.utilities.FollowMeParserException;
import it.unicam.cs.followme.utilities.ShapeData;

import java.io.IOException;
import java.util.List;

/**
 * A loader used to load a FollowMe simulation.
 * Files and strings loaded into this loader should follow the relative FollowMe syntax.
 */
public final class FollowMeSimulationLoader implements SimulationLoader<SurfacePosition, FollowMeLabel, Robot<SurfacePosition, FollowMeLabel>> {

    private FollowMeParser parser;
    private FollowMeProgramBuilder<Robot<SurfacePosition,FollowMeLabel>> programBuilder;
    private Environment<SurfacePosition, FollowMeLabel> environment;
    private SignalingMovingItemTracker<SurfacePosition, FollowMeLabel, Robot<SurfacePosition, FollowMeLabel>> tracker;
    private String programSourceCode;
    private ProgramLine<Robot<SurfacePosition, FollowMeLabel>> program;
    private double instructionPaceTime;

    public FollowMeSimulationLoader() {
        this.environment = new SurfaceEnvironment<>();
        this.tracker = new MapSignalingMovingItemTracker<>();
        this.program = new ProgramCondition<>((r) -> false);
        this.programSourceCode = "";
        this.programBuilder = new FollowMeProgramBuilder<>(new FollowMeDefaultCommandEvaluator<>(environment, tracker));
        this.parser = new FollowMeParser(programBuilder);
        this.instructionPaceTime = SignalingItemSimulationExecutor.DEFAULT_INSTRUCTION_PACE_TIME;
    }

    @Override
    public Environment<SurfacePosition, FollowMeLabel> loadEnvironment(String data) throws IOException{
        try {
            this.environment = buildEnvironment(parser.parseEnvironment(data));
            this.program = buildProgram(programSourceCode);
            return buildEnvironment(parser.parseEnvironment(data));
        }
        catch (FollowMeParserException e) {
            throw new IOException(e.getMessage());
        }
    }

    private Environment<SurfacePosition, FollowMeLabel> buildEnvironment(List<ShapeData> shapeDataList) throws FollowMeParserException {
        Environment<SurfacePosition, FollowMeLabel> newEnvironment = new SurfaceEnvironment<>();
        shapeDataList
                .stream()
                .map(FollowMeAreaConstructor.DEFAULT_CONSTRUCTOR::constructArea)
                .forEach(p -> newEnvironment.addArea(p.getKey(), p.getValue()));
        return newEnvironment;
    }

    @Override
    public ProgramLine<Robot<SurfacePosition, FollowMeLabel>> loadProgram(String program) throws IOException{
        try {
            this.program = buildProgram(program);
            this.programSourceCode = program;
            return buildProgram(program);
        }
        catch (FollowMeParserException e) {
            throw new IOException(e.getMessage());
        }
    }

    //Uses the given program source code to build a program on the current environment (and tracker).
    private ProgramLine<Robot<SurfacePosition, FollowMeLabel>> buildProgram(String program) throws FollowMeParserException {
        this.programBuilder = new FollowMeProgramBuilder<>(new FollowMeDefaultCommandEvaluator<>(environment, tracker));
        this.parser = new FollowMeParser(programBuilder);
        this.parser.parseRobotProgram(program);
        return programBuilder.getProgramHeadLine();
    }

    @Override
    public void setInstructionPaceTime(double s) {
        this.instructionPaceTime = s;
    }

    @Override
    public SimulationExecutor<SurfacePosition, Robot<SurfacePosition, FollowMeLabel>> getExecutor() {
        return new SignalingItemSimulationExecutor<>(environment, tracker, program, instructionPaceTime);
    }
}
