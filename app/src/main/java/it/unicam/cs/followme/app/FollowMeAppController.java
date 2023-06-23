package it.unicam.cs.followme.app;

import it.unicam.cs.followme.Controller;
import it.unicam.cs.followme.model.FollowMeLabel;
import it.unicam.cs.followme.model.environment.SurfacePosition;
import it.unicam.cs.followme.model.items.Robot;
import it.unicam.cs.followme.model.simulation.SignalingItemSimulationExecutor;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    //private final Image stopImage = new Image("app/src/main/resources/icons/PlayIcon.png");
    //private final Image playImage = new Image("app/src/main/resources/icons/StopIcon.png");


    public void initialize() {
        initSpinners();
        initZoomSlider();
        //simulationStateIcon.setImage(playImage);
    }

    private void initSpinners() {
        initPaceTimeSpinner();
        initPlaySecondsSpinner();
        initPlayStepsSpinner();
    }

    private void initPaceTimeSpinner() {
        paceTimeSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                0.1, Double.POSITIVE_INFINITY,
                SignalingItemSimulationExecutor.DEFAULT_INSTRUCTION_PACE_TIME, 0.1));
    }

    private void initPlaySecondsSpinner() {
        playSecondsSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                0.01, Double.POSITIVE_INFINITY,
                SignalingItemSimulationExecutor.DEFAULT_INSTRUCTION_PACE_TIME, 0.01));
    }

    private void initPlayStepsSpinner() {
        playStepsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                1, Integer.MAX_VALUE, 1, 1));
    }

    private void initZoomSlider() {
        zoomViewSlider = new Slider(0, 10, 5);
    }

}
