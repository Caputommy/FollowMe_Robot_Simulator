package it.unicam.cs.followme.app;


import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

import javafx.fxml.FXMLLoader;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/main_scene.fxml")));
        primaryStage.setTitle("FollowMe App");
        primaryStage.setScene(new Scene(root, 1280, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
