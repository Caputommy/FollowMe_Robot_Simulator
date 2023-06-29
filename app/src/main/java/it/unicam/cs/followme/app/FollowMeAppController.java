package it.unicam.cs.followme.app;

import it.unicam.cs.followme.Controller;
import it.unicam.cs.followme.model.FollowMeLabel;
import it.unicam.cs.followme.model.environment.*;
import it.unicam.cs.followme.model.items.Robot;
import it.unicam.cs.followme.model.items.SurfaceDirection;
import it.unicam.cs.followme.model.simulation.SignalingItemSimulationExecutor;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
    private GridPane controlsPane;

    @FXML
    private Spinner<Double> paceTimeSpinner;

    @FXML
    private Spinner<Double> playSecondsSpinner;

    @FXML
    private CheckBox playSecondsAnimated;

    @FXML
    private Spinner<Integer> playStepsSpinner;

    @FXML
    private CheckBox playStepsAnimated;

    @FXML
    private Slider zoomViewSlider;


    private final Controller<SurfacePosition, FollowMeLabel, Robot<SurfacePosition, FollowMeLabel>> controller =
            Controller.getFollowMeController();

    /**
     * Map used to associate each robot of the model to a shape in the <code>robotsPane</code>, in order
     * to execute animations.
     */
    private final Map<Robot<SurfacePosition, FollowMeLabel>, HBox> shownRobotsMap = new HashMap<>();

    /**
     * Int value in milliseconds of the displayed elapsed time on the stop watch.
     */
    private int currentStopWatchMillis = 0;

    private final Image stopImage = new Image("/icons/StopIcon.png");
    private final Image playImage = new Image("/icons/PlayIcon.png");

    /**
     * Color used to fill the labeled areas in the environment.
     */
    private final Color areaColor = new Color(0.0, 1.0, 1.0, 0.25);

    /**
     * Style used for area labels.
     */
    private final String areaLabelStyle =
            "-fx-font-family: \"Roboto\";" +
                    "-fx-font-style: italic;" +
                    "-fx-font-size: 12px;";

    /**
     * Radius dimension of shown robots.
     */
    private final double robotRadius = 0.1;

    /**
     * Style used for robot condition labels.
     */
    private final String robotLabelStyle =
            "-fx-text-fill:blue;"+
                    "-fx-padding:0 0 0 3";

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
        paceTimeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                controller.setInstructionPace(newValue);
                showNewSimulationSetting();
            });
    }

    private void initPlaySecondsSpinner() {
        playSecondsSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                0.01, Double.MAX_VALUE,
                SignalingItemSimulationExecutor.DEFAULT_INSTRUCTION_PACE_TIME, 0.05));
    }

    private void initPlayStepsSpinner() {
        playStepsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                1, Integer.MAX_VALUE, 1, 1));
    }

    /**
     * Method used to handle the "Load environment" command.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onLoadEnvironmentCommand(Event event) {
        File selectedFile = getTxtFileChooser("Load Environment File")
                .showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            try {
                controller.openEnvironment(selectedFile);
                showNewSimulationSetting();
                drawEnvironment();
            } catch (IOException e) {
                showErrorAlert(e.getMessage());
            }
        }
    }

    /**
     * This method is invoked whenever the current simulation execution is reset due to the change of
     * one of its core settings.
     */
    private void showNewSimulationSetting() {
        setStopWatch(0);
    }

    /**
     * Method used to handle the "Load program" command.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onLoadProgramCommand(Event event) {
        File selectedFile = getTxtFileChooser("Load Program File")
                .showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            try {
                controller.openProgram(selectedFile);
                showNewSimulationSetting();
                refreshProgramCode();
            } catch (IOException e) {
                showErrorAlert(e.getMessage());
            }
        }
    }

    /**
     * Resets the shown program source code.
     */
    private void refreshProgramCode() {
        programTextArea.clear();
        programTextArea.appendText(controller.getCurrentSourceCode());
    }

    /**
     * Creates a {@link FileChooser} with the given title to choose a txt file.
     *
     * @param title the title of the file chooser.
     * @return the txt file chooser.
     */
    private FileChooser getTxtFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Txt Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        return fileChooser;
    }

    /**
     * Shows an error alert window with the given error message.
     *
     * @param message the error message to show.
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Loading error");
        alert.setHeaderText(message);
        alert.showAndWait();
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
        showChildOnPane(environmentPane, circlePane, new SurfacePosition(
                position.getX()-circleArea.getRadius(),
                position.getY()+circleArea.getRadius()
        ));
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
                position.getY()+rectangleArea.getHeight()/2
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
        labelText.setStyle(areaLabelStyle);
        labelText.setAlignment(Pos.CENTER);
        return labelText;
    }

    /**
     * This method is used to show the given child on the given pane in the given absolute position.
     * The effective position is calculated upon the current state of the axes and represents the distance between
     * the given position and the left-bottom corner of the pane.
     *
     * @param pane the anchor pane where to show the child.
     * @param child the child node to be shown.
     * @param absPosition the axis-related absolute position.
     */
    private void showChildOnPane(Pane pane, Node child, SurfacePosition absPosition) {
        child.setTranslateX(getLayoutXValue(absPosition.getX()));
        child.setTranslateY(getLayoutYValue(absPosition.getY()));
        pane.getChildren().add(child);
    }

    /**
     * Returns the corresponding layout x-coordinate on the simulation pane from a given x-coordinate.
     *
     * @param xCoordinate the x coordinate value used in the simulation.
     * @return the corresponding layout value.
     */
    private double getLayoutXValue(double xCoordinate) {
        return scale(xCoordinate-xAxis.getLowerBound());
    }

    /**
     * Returns the corresponding layout y-coordinate on the simulation pane from a given y-coordinate.
     *
     * @param yCoordinate the y coordinate value used in the simulation.
     * @return the corresponding layout value.
     */
    private double getLayoutYValue(double yCoordinate) {
        return robotsPane.getHeight() - scale(yCoordinate-yAxis.getLowerBound());
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
            drawRobots();
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
        drawRobots();
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

    /**
     * This is the method invoked when the "Add robots" button is pressed.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onAddRobotsCommand(Event event) {
        try {
            getAddRobotStage().showAndWait();
            drawRobots();
        } catch (IOException e) {
            showErrorAlert(e.getMessage());
        }
    }

    /**
     * Builds and returns a new stage offering functionalities to add new robots to the simulation.
     *
     * @return the stage used to add robots.
     * @throws IOException if an I/O error occurs while leading the stage scene.
     */
    private Stage getAddRobotStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_robots_scene.fxml"));
        loader.setController(new AddRobotsController(controller, xAxis, yAxis));
        Scene addRobotScene = new Scene(loader.load(), AddRobotsController.WIDTH, AddRobotsController.HEIGHT);
        Stage addRobotStage = new Stage();
        addRobotStage.setTitle("Add Robots");
        addRobotStage.getIcons().add(new Image("/icons/AddRobotIcon.png"));
        addRobotStage.setScene(addRobotScene);
        addRobotStage.setResizable(false);
        addRobotStage.initModality(Modality.APPLICATION_MODAL);
        return addRobotStage;
    }

    /**
     * This method is used to draw the new robot map obtained from the controller, replacing the current one shown
     * and updating the <code>shownRobotsMap</code> accordingly.
     */
    private void drawRobots() {
        robotsPane.getChildren().clear();
        shownRobotsMap.clear();
        controller.getCurrentItemMap().entrySet()
                .stream()
                .forEach(e -> {
                    HBox robotBox = getRobotHBox(e.getKey());
                    showChildOnPane(robotsPane, robotBox, new SurfacePosition(
                            e.getValue().getX() - robotRadius,
                            e.getValue().getY() + robotRadius));
                    shownRobotsMap.put(e.getKey(), robotBox);
                });
        clipPane(robotsPane);
    }

    /**
     * Returns a new HBox as the graphic representation of a robot, containing a Circle and a label
     * for each condition signaled by the robot.
     *
     * @return a HBox representing a robot.
     */
    private HBox getRobotHBox(Robot<SurfacePosition, FollowMeLabel> robot) {
        Circle robotCircle = getRobotCircle();
        HBox robotBox = new HBox(robotCircle);
        if (!robot.getConditions().isEmpty()) {
            robotBox.getChildren().add(getMegaphoneImageView());
            robotBox.getChildren().addAll(robot.getConditions()
                    .stream()
                    .map(c -> {
                        Label conditionLabel = new Label(c.label());
                        conditionLabel.setMaxHeight(robotCircle.getRadius());
                        conditionLabel.setStyle(robotLabelStyle);
                        return conditionLabel;
                    })
                    .toList());
        }
        return robotBox;
    }

    /**
     * Returns a new {@link Circle} for the visual representation of a robot.
     *
     * @return a circle representing a robot.
     */
    private Circle getRobotCircle() {
        Circle robotCircle = new Circle(scale(robotRadius), new Color(0.7, 0.7, 0.7, 1.0));
        robotCircle.setStroke(new Color(0.0, 0.0, 0.0, 1.0));
        robotCircle.setStrokeWidth(1);
        return robotCircle;
    }

    /**
     * Returns a new instance of a resized megaphone {@link ImageView}, used to decorate labels signaled by robots.
     *
     * @return a megaphone image view.
     */
    private ImageView getMegaphoneImageView() {
        ImageView megaphone = new ImageView("/icons/MegaphoneIcon.png");
        megaphone.setFitHeight(25);
        megaphone.setFitWidth(25);
        return megaphone;
    }

    /**
     * This is the method invoked when the "Clear robots" button is pressed.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onClearRobotsCommand(Event event) {
        controller.clearPlacedItems();
        drawRobots();
    }

    /**
     * This is the method invoked when the "Play seconds" button is pressed.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onPlaySecondsCommand(Event event) {
        double secondsToRun = playSecondsSpinner.getValue();
        if (playSecondsAnimated.isSelected()) runWithAnimation(secondsToRun);
        else runWithoutAnimation(secondsToRun);
    }

    /**
     * This is the method invoked when the "Play steps" button is pressed.
     * Note that playing one step of the simulation will always get the simulation to the next point
     * in time when the current instruction time interval has ended.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onPlayStepsCommand(Event event) {
        double secondsToRun = ((playStepsSpinner.getValue()-1)*paceTimeSpinner.getValue()) + secondsToNextInstruction();
        if (playStepsAnimated.isSelected()) runWithAnimation(secondsToRun);
        else runWithoutAnimation(secondsToRun);
    }

    /**
     * This is the method invoked when the "Play one step" button is pressed.
     * Note that playing one step of the simulation will always get the simulation to the next point
     * in time when the current instruction time interval has ended.
     * Furthermore, this method always runs the simulation without any animation.
     *
     * @param event the triggering event.
     */
    @FXML
    private void onPlayOneStepCommand(Event event) {
        runWithoutAnimation(secondsToNextInstruction());
    }

    /**
     * Runs the simulation for the given seconds without any animation, showing the new state of
     * the simulation immediately.
     *
     * @param seconds the seconds to run the simulation.
     */
    private void runWithoutAnimation(double seconds) {
        if (seconds > 0) {
            controller.runFor(seconds);
            drawRobots();
            setStopWatch((int)controller.getSimulationCurrentTime()*1000);
        }
    }

    /**
     * Runs the simulation for the given seconds, showing a real-time animation of the robots' movement and makeing
     * the stopwatch timer flow.
     * For each step of the simulation, a different animation is run and possible changes to the set of conditions
     * signaled by each robot are showed at the end of each animation.
     *
     * @param seconds the seconds to run the animated simulation.
     */
    private void runWithAnimation(double seconds) {
        if (seconds > 0) {
            double secondsToNextInstruction = secondsToNextInstruction();
            controller.runFor(secondsToNextInstruction);
            ParallelTransition animation = getTransitionAnimation(controller.getCurrentItemMap(), secondsToNextInstruction);
            attachStopWatchToAnimation(animation);
            setAnimationMode(true);
            animation.play();
            animation.setOnFinished(event -> {
                drawRobots();
                setStopWatch((int)controller.getSimulationCurrentTime()*1000);
                runWithAnimation(seconds - secondsToNextInstruction);
            });
        }
        else setAnimationMode(false);
    }

    private double secondsToNextInstruction() {
        return paceTimeSpinner.getValue() - (controller.getSimulationCurrentTime()%paceTimeSpinner.getValue());
    }

    /**
     * Sets the scene animation mode according to the given flag, setting the simulation state icon
     * to play/stop icon and disabling/enabling controls.
     *
     * @param animated the flag that sets the animation mode.
     */
    private void setAnimationMode(boolean animated) {
        this.simulationStateIcon.setImage((animated ? playImage : stopImage));
        controlsPane.setDisable(animated);
    }

    /**
     * Returns an animation of the given duration to move robots from their current position
     * on the pane to the new given position map.
     *
     * @param newMap the new position of robots.
     * @param secondsDuration the duration of the animation.
     */
    private ParallelTransition getTransitionAnimation(Map<Robot<SurfacePosition, FollowMeLabel>, SurfacePosition> newMap, double secondsDuration) {
        ParallelTransition globalTransition = new ParallelTransition();
        newMap.entrySet()
                .stream()
                .map(e -> getTransitionToPosition(secondsDuration, shownRobotsMap.get(e.getKey()),
                        new SurfacePosition(e.getValue().getX()-robotRadius, e.getValue().getY()+robotRadius)))
                .forEach(t -> globalTransition.getChildren().add(t));
        return globalTransition;
    }

    /**
     * Returns the {@link TranslateTransition} object that moves the given <code>node</code> for the given
     * <code>seconds</code> to the layout position associated with the given simulation position
     * according to the current state of the axes.
     *
     * @param seconds the duration of the transition in seconds.
     * @param node the node to translate.
     * @param position the destination position of the translation.
     * @return the associated translate transition.
     */
    private TranslateTransition getTransitionToPosition(double seconds, Node node, SurfacePosition position) {
        TranslateTransition trans = new TranslateTransition(Duration.seconds(seconds), node);
        trans.setToX(getLayoutXValue(position.getX()));
        trans.setToY(getLayoutYValue(position.getY()));
        return trans;
    }

    /**
     * This method is used to set the time of the stopwatch and to display the time amount
     * on the stopwatch label accordingly.
     *
     * @param millis the time amount in millis to set and show.
     */
    private void setStopWatch(int millis) {
        this.stopwatchLabel.setText(String.format("%d:%02d:%02d",
                millis/60000, (millis/1000)%60, (millis/10)%100));
        this.currentStopWatchMillis = millis;
    }

    /**
     * This method is used to attach the stop watch timer to a given animation.
     * After the invocation of this method, the stopwatch timer will increase its time along with
     * time progression of the given animation.
     *
     * @param animation the animation to attach the stopwatch to.
     */
    private void attachStopWatchToAnimation(Animation animation) {
        animation.currentTimeProperty().addListener((obs, oldValue, newValue) ->
                setStopWatch(currentStopWatchMillis + (int)(newValue.toMillis() - oldValue.toMillis())));
    }
}
