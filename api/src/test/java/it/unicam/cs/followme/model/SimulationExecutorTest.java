package it.unicam.cs.followme.model;

import it.unicam.cs.followme.io.FollowMeSimulationLoader;
import it.unicam.cs.followme.io.SimulationLoader;
import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.items.Direction;
import it.unicam.cs.followme.model.items.Robot;
import it.unicam.cs.followme.model.items.SurfaceDirection;
import it.unicam.cs.followme.model.simulation.SimulationExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class SimulationExecutorTest {

    private SimulationLoader<SurfacePosition, FollowMeLabel, Robot<SurfacePosition, FollowMeLabel>> loader;
    private List<Robot<SurfacePosition, FollowMeLabel>> robots;
    private SimulationExecutor<SurfacePosition, Robot<SurfacePosition, FollowMeLabel>> simulationExecutor;

    private static final String ENV1 =
            "Label_1 CIRCLE 8 -8 6\n" +
                    "Label_2 CIRCLE -2 2 1.5\n" +
                    "Label_3 RECTANGLE 3 -1 10 2\n" +
                    "Label_4 RECTANGLE 4 -2 4 6";

    private static final String PROGRAM1 =
            "MOVE 0 1 2\n" +
                    "SIGNAL Label_X\n" +
                    "UNSIGNAL Label_X\n" +
                    "STOP";

    @BeforeEach
    public void createLoader() {
        loader = new FollowMeSimulationLoader();
    }

    @BeforeEach
    public void initRobots() {
        robots = new ArrayList<>();
        Direction<SurfacePosition> dir = new SurfaceDirection(0,1);
        for (int i=0; i<3; i++) robots.add(new Robot<>(dir));
        for (int i=1; i<=3; i++) {
            for (int j=i-1; j>=0; j--) robots.get(j).signal(new FollowMeLabel("LABEL_"+i));
        }
    }

    private Map<Robot<SurfacePosition, FollowMeLabel>, SurfacePosition> getRobotMap1() {
        Map<Robot<SurfacePosition, FollowMeLabel>, SurfacePosition> map = new HashMap<>();
        map.put(robots.get(0), new SurfacePosition(6,6));
        map.put(robots.get(1), new SurfacePosition(-2,4));
        map.put(robots.get(2), new SurfacePosition(-10,-5));
        return map;
    }

    private void initSimulation(String environment, String code) {
        assertDoesNotThrow(() -> loader.loadEnvironment(environment));
        assertDoesNotThrow(() -> loader.loadProgram(code));
        try {
            loader.loadEnvironment(environment);
            simulationExecutor = loader.getExecutor();
        }
        catch (IOException e) {}
    }

    @Test
    public void shouldAddItemToSimulation() {
        initSimulation(ENV1, PROGRAM1);
        simulationExecutor.addItemToSimulation(robots.get(0), new SurfacePosition(0.6,-2));

        assertEquals(new SurfacePosition(0.6,-2), simulationExecutor.getCurrentItemMap().get(robots.get(0)));
    }

    @Test
    public void shouldRunSimulation() {
        initSimulation(ENV1, PROGRAM1);
        simulationExecutor.addItemToSimulation(robots.get(2), new SurfacePosition(0,0));

        simulationExecutor.runSimulation(2);
        assertEquals(2.0, robots.get(2).getCurrentVelocity());
        assertEquals(Set.of(new FollowMeLabel("LABEL_3"),new FollowMeLabel("LABEL_X")), robots.get(2).getConditions());

        simulationExecutor.runSimulation(0.5);
        assertEquals(Set.of(new FollowMeLabel("LABEL_3"),new FollowMeLabel("LABEL_X")), robots.get(2).getConditions());
    }

    @Test
    public void shouldGetCurrentTime() {
        initSimulation(ENV1, PROGRAM1);
        assertEquals(0.0, simulationExecutor.getCurrentTime());

        simulationExecutor.runSimulation(2.6);
        assertEquals(2.6, simulationExecutor.getCurrentTime());

        simulationExecutor.runSimulation(1.4);
        assertEquals(4.0, simulationExecutor.getCurrentTime());
    }

    @Test
    public void shouldGetCurrentItemMap() {
        initSimulation(ENV1, PROGRAM1);
        simulationExecutor.addItemsToSimulation(getRobotMap1());

        assertEquals(getRobotMap1(), simulationExecutor.getCurrentItemMap());
    }

    @Test
    public void shouldGetCurrentItemMap2() {
        initSimulation(ENV1, PROGRAM1);
        simulationExecutor.addItemsToSimulation(getRobotMap1());

        simulationExecutor.runSimulation(2);

        Map<Robot<SurfacePosition, FollowMeLabel>, SurfacePosition> expectedMap = getRobotMap1()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().combineCoordinates(Double::sum, new SurfacePosition(0, 2))));

        assertEquals(expectedMap, simulationExecutor.getCurrentItemMap());
    }

    @Test
    public void shouldClearItems() {
        initSimulation(ENV1, PROGRAM1);
        simulationExecutor.addItemsToSimulation(getRobotMap1());

        simulationExecutor.clearItems();
        assertTrue(simulationExecutor.getCurrentItemMap().isEmpty());
    }
}
