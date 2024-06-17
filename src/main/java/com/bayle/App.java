package com.bayle;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import com.bayle.affichage.SimulationScene;
import com.bayle.service.Simulation;

/**
 * JavaFX App
 */
public class App extends Application {

    private static SimulationScene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new SimulationScene(new Group(), 1280, 720);

        /* ABOUT THE STAGE */
        // Image icon = new Image("icon.png");
        // stage.getIcons().add(icon);
        stage.setTitle("Simulation Project - BAYLE - V0.1");

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        Simulation simulation = new Simulation(scene);
        simulation.start();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/bayle/views/" + fxml + ".fxml"));
        if (fxmlLoader.getLocation() == null) {
            throw new IOException("FXML file not found: " + fxml);
        }
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    /* This is an exemple to how to switch between fxml folder*/
    // @FXML
    // private void switchToExemple() throws IOException {
    //     App.setRoot("exemple");
    // }

}