package com.bayle.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.bayle.service.Simulation;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class SimulationController {
    private Simulation simulation;

    @FXML
    Pane myScene;

    @FXML
    public void initialize() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (simulation != null) {
                    update();
                }
            }
        };

        timer.start();
    }

    public void update() {
        updateTimer();

        if (simulation.isRunning()) {
            startButton.setText("Stop");
        } else {
            startButton.setText("Start");
        }
    }

    @FXML
    Button startButton;

    public void start() {
        if (simulation == null) {
            //simulation = new Simulation(myScene, 1, new ArrayList<>(), 1000, 0);
            simulation = new Simulation(myScene);
        }

        if (!simulation.isRunning()) {
            simulation.start(this);
        } else {
            simulation.stop();
        }
    }

    @FXML
    Button resetButton;

    public void reset() {
        simulation.reset();
    }

    @FXML
    Label timerLabel;

    private void updateTimer() {
        // Mettre à jour le Label avec le temps écoulé

        int time = simulation.getRemainingTime();

        int hours = time / 3600;
        int minutes = (time % 3600) / 60;
        int seconds = time % 60;

        timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

}
