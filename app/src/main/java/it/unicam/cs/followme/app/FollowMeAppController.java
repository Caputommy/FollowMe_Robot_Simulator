package it.unicam.cs.followme.app;

import it.unicam.cs.followme.Controller;
import it.unicam.cs.followme.model.FollowMeLabel;
import it.unicam.cs.followme.model.environment.SurfaceCircleArea;
import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.environment.SurfaceRectangleArea;
import it.unicam.cs.followme.model.items.Robot;
import it.unicam.cs.followme.model.simulation.SignalingItemSimulationExecutor;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

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

    private final Color areaColor = new Color(0.0, 1.0, 1.0, 0.25);
    private final String labelStyle =
            "-fx-font-family: \"Roboto\";" +
            "-fx-font-style: italic;" +
            "-fx-font-size: 12px;";


    public void initialize() {
        initSpinners();
        initZoomSlider();
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

    private void initZoomSlider() {
        zoomViewSlider = new Slider(0, 10, 5);
    }

    /**
     * Resets the stopwatch and clears the robot map.
     * This method is invoked whenever the current simulation execution is reset due to the change of
     * one of its core settings.
     */
    private void showNewSimulationSetting() {
        //Set stopwatch
        //TODO
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
                //TODO drawEnvironment()
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error...");
                alert.setHeaderText(e.getMessage());
            }
        }
    }

    @FXML
    private void onLoadProgramCommand() {
        SurfaceCircleArea<FollowMeLabel> circle = new SurfaceCircleArea<>(new FollowMeLabel("LABEL_1"), 2);
        showCircleArea(circle, new SurfacePosition(5, 5));
        showCircleArea(circle, new SurfacePosition(9, 9));
        showCircleArea(circle, new SurfacePosition(-21, -10));

        SurfaceRectangleArea<FollowMeLabel> rectangle = new SurfaceRectangleArea<>(new FollowMeLabel("LABEL_1"), 2, 5);
        showRectangleArea(rectangle, new SurfacePosition(19, 5));
        showRectangleArea(rectangle, new SurfacePosition(0, 0));
        clipPane(environmentPane);
    }

    /**
     * Shows a circle area on the environment pane according to the position of its center.
     *
     * @param circleArea the circle area to be shown.
     * @param position the absolute position of its centre in the environment.
     */
    private void showCircleArea(SurfaceCircleArea<FollowMeLabel> circleArea, SurfacePosition position){
        Circle circle = new Circle(scale(circleArea.getRadius()), areaColor);
        StackPane circlePane = new StackPane(circle, getTextFromLabel(circleArea.getLabel()));
        showChildOnPane(environmentPane, circlePane, position.mapCoordinates(x -> x - circleArea.getRadius()));
    }

    /**
     * Shows a rectangle area on the environment pane according to the position of its center.
     *
     * @param rectangleArea the rectangle area to be shown.
     * @param position the absolute position of its centre in the environment.
     */
    private void showRectangleArea(SurfaceRectangleArea<FollowMeLabel> rectangleArea, SurfacePosition position){
        Rectangle rectangle =
                new Rectangle(scale(rectangleArea.getWidth()), scale(rectangleArea.getHeight()), areaColor);
        StackPane rectanglePane = new StackPane(rectangle, getTextFromLabel(rectangleArea.getLabel()));
        showChildOnPane(environmentPane, rectanglePane, new SurfacePosition(
                position.getX()-rectangleArea.getWidth()/2,
                position.getY()-rectangleArea.getHeight()/2
        ));
    }

    private Text getTextFromLabel(FollowMeLabel label) {
        Text labelText = new Text(label.label());
        labelText.setStyle(labelStyle);
        return labelText;
    }

    /**
     * Shows the given child on the given anchor pane in the given absolute position. The effective position
     * is calculated upon the current state of the axis and represents the distance between the left-bottom
     * corner of the child and the left-bottom corner of the pane.
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
     * Returns the visual units measure of a value according to the current axis scaling.
     *
     * @param val the value to be scaled.
     * @return the equivalent visual units of the value.
     */
    private double scale(double val) {
        return val*xAxis.getScale();
    }
}
