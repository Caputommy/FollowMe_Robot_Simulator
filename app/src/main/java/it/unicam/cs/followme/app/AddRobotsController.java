package it.unicam.cs.followme.app;

import it.unicam.cs.followme.Controller;
import it.unicam.cs.followme.model.FollowMeLabel;
import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.items.Robot;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.util.converter.DoubleStringConverter;

import java.util.ArrayList;
import java.util.List;

public class AddRobotsController {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;

    //Model controller from app controller.
    private final Controller<SurfacePosition, FollowMeLabel, Robot<SurfacePosition, FollowMeLabel>> controller;

    //Axes from app controller used to get default values.
    private final NumberAxis xAxis;
    private final NumberAxis yAxis;

    //Fields of lists used to store the generated spinners.
    private final List<List<Spinner<Double>>> positionList;
    private final List<Spinner<Integer>> robotNumberList;
    private final List<List<Spinner<Double>>> rangesList;

    @FXML
    private GridPane positionsTable;

    @FXML
    private GridPane rangesTable;

    @FXML
    private Button addPositionRowButton;

    @FXML
    private Button addRangeRowButton;

    @FXML
    private Button addRobotsButton;

    @FXML
    private Button cancelButton;


    public AddRobotsController (Controller<SurfacePosition, FollowMeLabel, Robot<SurfacePosition, FollowMeLabel>> controller, NumberAxis xAxis, NumberAxis yAxis) {
        this.controller = controller;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.positionList = new ArrayList<>();
        this.robotNumberList = new ArrayList<>();
        this.rangesList = new ArrayList<>();
    }

    private void initialize() {}

    /**
     * This is the method invoked when the button used to add a row to the positions table is pressed.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onAddPositionRowCommand(Event event) {
        positionList.add(new ArrayList<>());
        positionList.get(positionList.size()-1).add(getCoordinateSpinner(getAxisMidValue(xAxis)));
        positionList.get(positionList.size()-1).add(getCoordinateSpinner(getAxisMidValue(yAxis)));
        positionsTable.addRow(positionsTable.getRowCount(),
                positionList.get(positionList.size()-1).get(0),
                positionList.get(positionList.size()-1).get(1));

    }

    /**
     * This is the method invoked when the button used to add a row to the ranges table is pressed.
     *
     * @param event the triggering event.
     */
    @FXML
    private void OnAddRangeRowCommand(Event event) {
        rangesTable.addRow(rangesTable.getRowCount(),
                getNaturalNumberSpinner(),
                getCoordinateSpinner(xAxis.getLowerBound()),
                getCoordinateSpinner(xAxis.getUpperBound()),
                getCoordinateSpinner(yAxis.getLowerBound()),
                getCoordinateSpinner(yAxis.getUpperBound()));
    }

    /**
     * Returns the value that sits in the middle of the given axis, calculated as the arithmetic
     * mean between the lower bound and the upper bound of the axis.
     *
     * @param axis the axis to calculate the mid value of.
     * @return the mid value of the axis.
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
        addRobotsFromPositions();
        addRobotsFromRanges();
    }

    /**
     * This is the method invoked when the cancel button is pressed.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onCancelCommand(Event event) {

    }

    private void addRobotsFromPositions() {
        //TODO
    }

    private void addRobotsFromRanges() {
        //TODO
    }





}
