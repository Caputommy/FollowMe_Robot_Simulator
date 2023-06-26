package it.unicam.cs.followme.app;

import it.unicam.cs.followme.Controller;
import it.unicam.cs.followme.model.FollowMeLabel;
import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.items.Robot;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class AddRobotsController {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;

    //Model controller from app controller.
    private final Controller<SurfacePosition, FollowMeLabel, Robot<SurfacePosition, FollowMeLabel>> controller;

    //Axes from app controller used to get default values.
    private final NumberAxis xAxis;
    private final NumberAxis yAxis;

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
    }

    private void initialize() {}

    @FXML
    private void onAddPositionRowCommand(Event event) {

    }

    @FXML
    private void OnAddRangeRowCommand(Event event) {

    }

    @FXML
    private void onAddRobotsCommand(Event event) {

    }

    @FXML
    private void onCancelCommand(Event event) {

    }



}
