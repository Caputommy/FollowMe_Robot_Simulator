package it.unicam.cs.followme.app;

import it.unicam.cs.followme.Controller;
import it.unicam.cs.followme.model.FollowMeLabel;
import it.unicam.cs.followme.model.environment.*;
import it.unicam.cs.followme.model.items.Robot;
import it.unicam.cs.followme.model.items.SurfaceDirection;
import it.unicam.cs.followme.model.simulation.SignalingItemSimulationExecutor;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * JavaFX Controller of FollowMeApp.
 */
public class FollowMeAppController {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 600;

    @FXML
    private TextArea programTextArea;

    @FXML
    private Label stopwatchLabel;
    @FXML
    private ImageView simulationStateIcon;

    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    @FXML
    private AnchorPane environmentPane;
    @FXML
    private AnchorPane robotsPane;

    @FXML
    private Button loadEnvironmentButton;

    @FXML
    private Button loadProgramButton;

    @FXML
    private Spinner<Double> paceTimeSpinner;

    @FXML
    private Button addRobotsButton;

    @FXML
    private Button clearRobotsButton;

    @FXML
    private Spinner<Double> playSecondsSpinner;
    @FXML
    private Button playSecondsButton;

    @FXML
    private Spinner<Integer> playStepsSpinner;
    @FXML
    private Button playStepsButton;

    @FXML
    private Button playOneStepButton;

    @FXML
    private Button moveViewLeftButton;
    @FXML
    private Button moveViewUpButton;
    @FXML
    private Button moveViewDownButton;
    @FXML
    private Button moveViewRightButton;

    @FXML
    private Slider zoomViewSlider;


    private final Controller<SurfacePosition, FollowMeLabel, Robot<SurfacePosition, FollowMeLabel>> controller =
            Controller.getFollowMeController();

    private final Image stopImage = new Image("/icons/StopIcon.png");
    private final Image playImage = new Image("/icons/PlayIcon.png");

    /**
     * Color used to fill the labeled areas in the environment.
     */
    private final Color areaColor = new Color(0.0, 1.0, 1.0, 0.25);

    /**
     * Style used for the labels.
     */
    private final String labelStyle =
            "-fx-font-family: \"Roboto\";" +
            "-fx-font-style: italic;" +
            "-fx-font-size: 12px;";

    /**
     * Factor that represent the proportion of the view that is hidden/showed in response to
     * any view movement command.
     */
    private final double moveViewFactor = 0.1;


    public void initialize() {
        initSpinners();
    }

    private void initSpinners() {
        initPaceTimeSpinner();
        initPlaySecondsSpinner();
        initPlayStepsSpinner();
    }

    private void initPaceTimeSpinner() {
        paceTimeSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                0.1, Double.MAX_VALUE,
                SignalingItemSimulationExecutor.DEFAULT_INSTRUCTION_PACE_TIME, 0.1));
    }

    private void initPlaySecondsSpinner() {
        playSecondsSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                0.01, Double.MAX_VALUE,
                SignalingItemSimulationExecutor.DEFAULT_INSTRUCTION_PACE_TIME, 0.01));
    }

    private void initPlayStepsSpinner() {
        playStepsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                1, Integer.MAX_VALUE, 1, 1));
    }

    /**
     * Resets the stopwatch and clears the robot map.
     * This method is invoked whenever the current simulation execution is reset due to the change of
     * one of its core settings.
     */
    private void showNewSimulationSetting() {
        stopwatchLabel.setText("0:00");
        //Clear shown robots
        //TODO
    }

    /**
     * Resets the shown program source code.
     */
    private void refreshProgramCode() {
        programTextArea.clear();
        programTextArea.appendText(controller.getCurrentSourceCode());
    }


    /**
     * Method used to handle the load environment command.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onLoadEnvironmentCommand(Event event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Environment File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Txt Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            try {
                controller.openEnvironment(selectedFile);
                showNewSimulationSetting();
                drawEnvironment();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Loading error");
                alert.setHeaderText(e.getMessage());
            }
        }
    }

    @FXML
    private void onLoadProgramCommand() {
        //TODO
    }

    /**
     * This method is used to draw the new environment obtained from the controller, replacing the current one shown.
     */
    private void drawEnvironment() {
        environmentPane.getChildren().clear();
        controller.getCurrentEnvironmentMap().entrySet()
                .stream()
                .forEach(e -> e.getValue().forEach(
                        pos -> showArea(e.getKey(), pos)
                ));
        clipPane(environmentPane);
    }

    /**
     * This method is used to show an area on the environment pane according to the position of its center.
     *
     * @param area the area to be shown.
     * @param position the absolute position of its centre in the environment.
     */
    private void showArea(Area<SurfacePosition, FollowMeLabel> area, SurfacePosition position) {
        if (area instanceof SurfaceCircleArea circleArea) showCircleArea(circleArea, position);
        if (area instanceof SurfaceRectangleArea rectangleArea) showRectangleArea(rectangleArea, position);
    }

    /**
     * This method is used to show a circle area on the environment pane according to the position of its center.
     *
     * @param circleArea the circle area to be shown.
     * @param position the absolute position of its centre in the environment.
     */
    private void showCircleArea(SurfaceCircleArea<FollowMeLabel> circleArea, SurfacePosition position){
        Circle circle = new Circle(scale(circleArea.getRadius()), areaColor);
        Label label = getLabelFromArea(circleArea);
        label.setMaxSize(circle.getRadius()*2, circle.getRadius()*2);
        StackPane circlePane = new StackPane(circle, label);
        showChildOnPane(environmentPane, circlePane, position.mapCoordinates(x -> x - circleArea.getRadius()));
    }

    /**
     * This method is used to show a rectangle area on the environment pane according to the position of its center.
     *
     * @param rectangleArea the rectangle area to be shown.
     * @param position the absolute position of its centre in the environment.
     */
    private void showRectangleArea(SurfaceRectangleArea<FollowMeLabel> rectangleArea, SurfacePosition position){
        Rectangle rectangle =
                new Rectangle(scale(rectangleArea.getWidth()), scale(rectangleArea.getHeight()), areaColor);
        Label label = getLabelFromArea(rectangleArea);
        label.setMaxSize(rectangle.getWidth(), rectangle.getHeight());
        StackPane rectanglePane = new StackPane(rectangle, label);
        showChildOnPane(environmentPane, rectanglePane, new SurfacePosition(
                position.getX()-rectangleArea.getWidth()/2,
                position.getY()-rectangleArea.getHeight()/2
        ));
    }

    /**
     * Returns the label to be shown on a labeled FollowMe area.
     *
     * @param area the labeled area.
     * @return the label object to be shown.
     */
    private Label getLabelFromArea(Area<?, FollowMeLabel> area) {
        Label labelText = new Label(area.getLabel().label());
        labelText.setStyle(labelStyle);
        labelText.setAlignment(Pos.CENTER);
        return labelText;
    }

    /**
     * This method is used to show the given child on the given anchor pane in the given absolute position.
     * The effective position is calculated upon the current state of the axes and represents the distance between
     * the left-bottom corner of the child and the left-bottom corner of the pane.
     *
     * @param pane the anchor pane where to show the child.
     * @param child the child node to be shown.
     * @param absPosition the axis-related absolute position.
     */
    private void showChildOnPane(AnchorPane pane, Node child, SurfacePosition absPosition) {
        AnchorPane.setLeftAnchor(child, scale(absPosition.getX()-xAxis.getLowerBound()));
        AnchorPane.setBottomAnchor(child, scale(absPosition.getY()-yAxis.getLowerBound()));
        pane.getChildren().add(child);
    }

    /**
     * This method is used to clip all child nodes of an AnchorPane that go beyond its boundaries.
     * The child nodes must already be added to the pane before the invocation in order to be clipped.
     *
     * @param pane the anchor pane to be clipped.
     */
    private void clipPane(AnchorPane pane) {
        Rectangle clipRect = new Rectangle();
        clipRect.widthProperty().bind(pane.widthProperty());
        clipRect.heightProperty().bind(pane.heightProperty());
        pane.setClip(clipRect);
    }

    /**
     * Returns the visual units measure of a value according to the current axes scaling.
     *
     * @param val the value to be scaled.
     * @return the equivalent visual units of the value.
     */
    private double scale(double val) {
        return val*(yAxis.getHeight()/getCurrentAxesSize());
    }

    /**
     * This is the method invoked when the view is moved on the left.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onMoveViewLeftCommand(Event event) {
        moveView(new SurfaceDirection(-1, 0));
    }

    /**
     * This is the method invoked when the view is moved up.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onMoveViewUpCommand(Event event) {
        moveView(new SurfaceDirection(0, 1));
    }

    /**
     * This is the method invoked when the view is moved down.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onMoveViewDownCommand(Event event) {
        moveView(new SurfaceDirection(0, -1));
    }

    /**
     * This is the method invoked when the view is moved on the right.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onMoveViewRightCommand(Event event) {
        moveView(new SurfaceDirection(1, 0));
    }

    /**
     * This is the method invoked when the zoom slider is manipulated.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onZoomViewCommand(Event event) {
        double sizeDiff = zoomViewSlider.getValue()-getCurrentAxesSize();
        if (sizeDiff != 0) {
            xAxis.setLowerBound(xAxis.getLowerBound()-sizeDiff);
            xAxis.setUpperBound(xAxis.getUpperBound()+sizeDiff);
            yAxis.setLowerBound(yAxis.getLowerBound()-(sizeDiff/2));
            yAxis.setUpperBound(yAxis.getUpperBound()+(sizeDiff/2));
            drawEnvironment();
            //TODO drawRobots
        }
    }

    /**
     * This is the method invoked to handle key events.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT  -> moveView(new SurfaceDirection(-1, 0));
            case UP    -> moveView(new SurfaceDirection(0, 1));
            case DOWN  -> moveView(new SurfaceDirection(0, -1));
            case RIGHT -> moveView(new SurfaceDirection(1, 0));
        }
    }

    /**
     * This method is used to scroll the view in the given direction.
     *
     * @param direction the direction of the movement.
     */
    private void moveView(SurfaceDirection direction) {
        moveAxes(direction);
        drawEnvironment();
        //TODO drawRobots
    }

    /**
     * This method is used to shift the axes in the given direction according to the <code> moveViewFactor</code>
     * specified for this controller.
     *
     * @param direction the direction of the movement.
     */
    private void moveAxes(SurfaceDirection direction) {
        double axesSize = getCurrentAxesSize();
        xAxis.setLowerBound(xAxis.getLowerBound() + axesSize*moveViewFactor*direction.getNormalizedPosition().getX());
        xAxis.setUpperBound(xAxis.getUpperBound() + axesSize*moveViewFactor*direction.getNormalizedPosition().getX());
        yAxis.setLowerBound(yAxis.getLowerBound() + axesSize*moveViewFactor*direction.getNormalizedPosition().getY());
        yAxis.setUpperBound(yAxis.getUpperBound() + axesSize*moveViewFactor*direction.getNormalizedPosition().getY());
    }

    /**
     * Returns the current height of the y-axis, that represents the current size of both axes.
     * (x-axis is always double-sized compared to y-axis)
     *
     * @return the current size of the y-axis
     */
    private double getCurrentAxesSize() {
        return yAxis.getUpperBound() - yAxis.getLowerBound();
    }
}
