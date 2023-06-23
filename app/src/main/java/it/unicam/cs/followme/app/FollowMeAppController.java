package it.unicam.cs.followme.app;

import it.unicam.cs.followme.Controller;
import it.unicam.cs.followme.model.FollowMeLabel;
import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.items.Robot;
import it.unicam.cs.followme.model.simulation.SignalingItemSimulationExecutor;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
}
