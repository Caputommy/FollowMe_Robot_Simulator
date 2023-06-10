package it.unicam.cs.followme.model;

import it.unicam.cs.followme.model.FollowMeAreaConstructor;
import it.unicam.cs.followme.model.FollowMeLabel;
import it.unicam.cs.followme.model.FollowMeProgramBuilder;
import it.unicam.cs.followme.model.environment.Environment;
import it.unicam.cs.followme.model.environment.SurfaceEnvironment;
import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.io.SimulationLoader;
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

import java.util.List;

/**
 * A loader used to load a FollowMe simulation.
 * Files and strings loaded into this loader should follow the relative FollowMe syntax.
 */
public final class FollowMeSimulationLoader implements SimulationLoader<SurfacePosition, Robot<SurfacePosition, FollowMeLabel>> {

    private FollowMeParser parser;
    private FollowMeProgramBuilder<Robot<SurfacePosition,FollowMeLabel>> programBuilder;
    private Environment<SurfacePosition, FollowMeLabel> environment;
    private SignalingMovingItemTracker<SurfacePosition, FollowMeLabel, Robot<SurfacePosition, FollowMeLabel>> tracker;
    private String programSourceCode;
    private ProgramLine<Robot<SurfacePosition, FollowMeLabel>> program;

    private static final String CORRECT_PARSING_MESSAGE = "Parsing successfully done.";

    public FollowMeSimulationLoader() {
        this.environment = new SurfaceEnvironment<>();
        this.tracker = new MapSignalingMovingItemTracker<>();
        this.program = new ProgramCondition<>((r) -> false);
        this.programSourceCode = "";
        this.programBuilder = new FollowMeProgramBuilder<>(environment, tracker);
        this.parser = new FollowMeParser(programBuilder);
    }

    @Override
    public String loadEnvironment(String data) {
        try {
            buildAndSetEnvironment(parser.parseEnvironment(data));
            return CORRECT_PARSING_MESSAGE;
        }
        catch (FollowMeParserException e) {
            return e.getMessage();
        }
    }

    private void buildAndSetEnvironment(List<ShapeData> shapeDataList) throws FollowMeParserException {
        this.environment = new SurfaceEnvironment<>();
        shapeDataList
                .stream()
                .map(FollowMeAreaConstructor.DEFAULT_CONSTRUCTOR::constructArea)
                .forEach(p -> environment.addArea(p.getKey(), p.getValue()));
        buildAndSetProgram(this.programSourceCode);
    }

    @Override
    public String loadProgram(String program) {
        try {
            buildAndSetProgram(program);
            this.programSourceCode = program;
            return CORRECT_PARSING_MESSAGE;
        }
        catch (FollowMeParserException e) {
            return e.getMessage();
        }
    }

    //Uses the given program source code to build a program on the current environment (and tracker).
    private void buildAndSetProgram(String program) throws FollowMeParserException {
        this.programBuilder = new FollowMeProgramBuilder<>(environment, tracker);
        this.parser = new FollowMeParser(programBuilder);
        this.parser.parseRobotProgram(program);
        this.program = programBuilder.getProgramHeadLine();
    }

    @Override
    public SimulationExecutor<SurfacePosition, Robot<SurfacePosition, FollowMeLabel>> getExecutor() {
        return new SignalingItemSimulationExecutor<>(environment, tracker, program);
    }
}
