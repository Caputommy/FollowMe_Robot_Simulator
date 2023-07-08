package it.unicam.cs.followme.app;

import it.unicam.cs.followme.Controller;
import it.unicam.cs.followme.model.FollowMeLabel;
import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.items.Robot;
import it.unicam.cs.followme.model.items.SurfaceDirection;
import it.unicam.cs.followme.util.DoubleRange;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JavaFX Controller of the Add Robots stage of the FollowMeApp.
 */
public class FollowMeAddRobotsController {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;

    //Model controller obtained from FollowMeAppController.
    private final Controller<SurfacePosition, FollowMeLabel, Robot<SurfacePosition, FollowMeLabel>> controller;

    //Axes from FollowMeAppController used to get default values.
    private final NumberAxis xAxis;
    private final NumberAxis yAxis;

    //Fields of lists used to store the generated spinners.
    private final List<List<Spinner<Double>>> positionsTableList;
    private final List<Spinner<Integer>> robotNumberList;
    private final List<List<Spinner<Double>>> rangesTableList;

    @FXML
    private GridPane positionsTable;

    @FXML
    private GridPane rangesTable;


    public FollowMeAddRobotsController(Controller<SurfacePosition, FollowMeLabel, Robot<SurfacePosition, FollowMeLabel>> controller, NumberAxis xAxis, NumberAxis yAxis) {
        this.controller = controller;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.positionsTableList = new ArrayList<>();
        this.robotNumberList = new ArrayList<>();
        this.rangesTableList = new ArrayList<>();
    }

    private void initialize() {}

    /**
     * This is the method invoked when the button used to add a row to the positions table is pressed.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onAddPositionRowCommand(Event event) {
        positionsTableList.add(new ArrayList<>());
        int rowIndex = positionsTableList.size();
        positionsTableList.get(rowIndex-1).addAll(List.of(
                getCoordinateSpinner(getAxisMidValue(xAxis)),
                getCoordinateSpinner(getAxisMidValue(yAxis)),
                getCoordinateSpinner(1),
                getCoordinateSpinner(0),
                getVelocitySpinner()
        ));

        for (int i = 0; i< positionsTableList.get(rowIndex-1).size(); i++) {
            positionsTable.add(positionsTableList.get(rowIndex-1).get(i), i, rowIndex);
        }
    }

    /**
     * This is the method invoked when the button used to add a row to the ranges table is pressed.
     *
     * @param event the triggering event.
     */
    @FXML
    private void OnAddRangeRowCommand(Event event) {
        rangesTableList.add(new ArrayList<>());
        int rowIndex = rangesTableList.size();
        robotNumberList.add(getNaturalNumberSpinner());
        rangesTableList.get(rowIndex-1).addAll(List.of(
                getCoordinateSpinner(xAxis.getLowerBound()),
                getCoordinateSpinner(xAxis.getUpperBound()),
                getCoordinateSpinner(yAxis.getLowerBound()),
                getCoordinateSpinner(yAxis.getUpperBound()),
                getCoordinateSpinner(1),
                getCoordinateSpinner(0),
                getVelocitySpinner()
        ));

        rangesTable.add(robotNumberList.get(rowIndex-1), 0, rowIndex);
        for (int i=0; i<rangesTableList.get(rowIndex-1).size(); i++) {
            rangesTable.add(rangesTableList.get(rowIndex-1).get(i), i+1, rowIndex);
        }
    }

    /**
     * Returns the value that sits in the middle of the given axis, calculated as the arithmetic
     * mean between the lower bound and the upper bound of the axis.
     *
     * @param axis the axis to calculate the mid-value of.
     * @return the mid-value of the axis.
     */
    private double getAxisMidValue(NumberAxis axis) {
        return (axis.getLowerBound() + axis.getUpperBound())/2;
    }

    /**
     * Returns a spinner used as input for a coordinate value.
     *
     * @param defaultValue the initial value of the spinner.
     * @return the coordinate spinner.
     */
    private Spinner<Double> getCoordinateSpinner(double defaultValue) {
        Spinner<Double> spinner = new Spinner<>(-Double.MAX_VALUE, Double.MAX_VALUE, defaultValue, 0.1);
        spinner.setEditable(true);
        return spinner;
    }

    /**
     * Returns a spinner used as input for a velocity value.
     *
     * @return the velocity spinner.
     */
    private Spinner<Double> getVelocitySpinner() {
        Spinner<Double> spinner = new Spinner<>(0, Double.MAX_VALUE, 0, 0.1);
        spinner.setEditable(true);
        return spinner;
    }

    /**
     * Returns a spinner used as input for a positive natural number.
     *
     * @return the natural number spinner.
     */
    private Spinner<Integer> getNaturalNumberSpinner() {
        Spinner<Integer> spinner = new Spinner<>(1, Integer.MAX_VALUE, 1, 1);
        spinner.setEditable(true);
        return spinner;
    }

    /**
     * This is the method invoked when the add robots button is pressed.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onAddRobotsCommand(Event event) {
        if (!positionsTableList.isEmpty()) placeRobotsFromPositions();
        if (!rangesTableList.isEmpty()) placeRobotsFromRanges();
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    /**
     * This is the method invoked when the cancel button is pressed.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onCancelCommand(Event event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    /**
     * This method is used to place new robots into the simulation through the controller
     * from data in the <code>positionsTable</code>.
     */
    private void placeRobotsFromPositions() {
        Map<Robot<SurfacePosition, FollowMeLabel>, SurfacePosition> robotMap = new HashMap<>();
        this.positionsTableList
                .stream()
                .forEach(list -> putRobotFromPositionList(robotMap, list));

        this.controller.placeItems(robotMap);
    }

    private void putRobotFromPositionList(Map<Robot<SurfacePosition, FollowMeLabel>, SurfacePosition> map, List<Spinner<Double>> list) {
        map.put(
                new Robot<>(
                        new SurfaceDirection(list.get(2).getValue(), list.get(3).getValue()),
                        list.get(4).getValue()),
                new SurfacePosition(list.get(0).getValue(), list.get(1).getValue())
        );
    }


    /**
     * This method is used to place new robots into the simulation through the controller
     * from data in the <code>rangesTable</code>.
     */
    private void placeRobotsFromRanges() {
        Map<Robot<SurfacePosition, FollowMeLabel>, SurfacePosition> robotMap = new HashMap<>();
        this.rangesTableList
                .stream()
                .forEach(list -> {
                        for(int i=0; i<robotNumberList.get(rangesTableList.indexOf(list)).getValue(); i++) {
                            putRobotFromRangeList(robotMap, list);
                        }
                    });
        this.controller.placeItems(robotMap);
    }

    private void putRobotFromRangeList(Map<Robot<SurfacePosition, FollowMeLabel>, SurfacePosition> map, List<Spinner<Double>> list) {
        map.put(
            new Robot<>(
                    new SurfaceDirection(list.get(4).getValue(), list.get(5).getValue()),
                    list.get(6).getValue()),
            SurfacePosition.randomPositionInRanges(
                    new DoubleRange(list.get(0).getValue(), list.get(1).getValue()),
                    new DoubleRange(list.get(2).getValue(), list.get(3).getValue()))
        );
    }
}
