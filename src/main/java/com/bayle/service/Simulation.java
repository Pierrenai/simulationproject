package com.bayle.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.bayle.model.Carotte;
import com.bayle.model.Character;
import com.bayle.model.Cow;
import com.bayle.model.Terrain;
import com.bayle.affichage.ObjectRender;
import com.bayle.affichage.SimulationScene;
import com.bayle.util.Utils;
import com.bayle.util.Vecteur;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class Simulation {

    private final int simulationPadding = 100;
    private final int amountOfCarotte = 15;
    private final int amountOfCow = 7;
    private final int SIMULATIONTIME = 60;

    private Terrain terrain;
    private Label timerLabel;

    public Terrain getTerrain() {
        return terrain;
    }

    private boolean isDayTime = false;
    // private boolean isRunning = false;

    // public boolean isRunning() {
    // return isRunning;
    // }

    private int secondsElapsed;
    private int simulationTime = SIMULATIONTIME; // in seconds

    public int getRemainingTime() {
        return simulationTime - secondsElapsed;
    }

    public Simulation(SimulationScene scene) {
        terrain = scene.getTerrain();
        terrain.setSimulatiotn(this);

        timerLabel = scene.getTimerLabel();

        secondsElapsed = 0;
    }

    public void start() {
        if (terrain.getCharacters().isEmpty()) {
            terrain.addCarotte(amountOfCarotte);
            terrain.addCharacter(15);
            terrain.addCow();

            secondsElapsed = 0;
        }

        isDayTime = true;

        new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // init
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }

                if (((now - lastUpdate) / 1_000_000_000.0) >= 1) { // update toutes les 1 secondes
                    lastUpdate = now;
                    secondsElapsed++;
                }

                // Update simulation
                update();
            }
        }.start();

        // Rendu de la scene
        // ObjectRender.Render(myScene);

    }

    public void update() {
        if (isDayTime == true) {
            updateTimer();

            for (Character character : terrain.getCharacters()) {
                character.update();
            }
            for (Cow cow : terrain.getCows()) {
                cow.update();
            }

            // Toujours avoir le nombre de carotte sur la carte
            // ps: peut-être changer pour faire ce spawn de carotte toutes les x secondes
            terrain.addCarotte(amountOfCarotte - terrain.getCarottes().size());
            terrain.addCow(amountOfCow - terrain.getCows().size());

            checkEndCondition();

            // ObjectRender.Render(myScene);
        } else {
            setTimer(60);
        }
    }

    public Pane getmyScene() {
        return terrain;
    }

    private void updateTimer() {
        // Mettre à jour le Label avec le temps écoulé

        int time = getRemainingTime();

        int hours = time / 3600;
        int minutes = (time % 3600) / 60;
        int seconds = time % 60;

        timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    private void setTimer(int secondsRemaining) {
        // Mettre à jour le Label avec le temps écoulé

        int time = secondsRemaining;

        int hours = time / 3600;
        int minutes = (time % 3600) / 60;
        int seconds = time % 60;

        timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    private void checkEndCondition() {
        if (secondsElapsed >= simulationTime) {
            isDayTime = false;
            for (Cow cow : terrain.getCows()) {
                terrain.removeObject(cow);
            }
            for (Carotte carotte : terrain.getCarottes()) {
                terrain.removeObject(carotte);
            }
            for (Character character : terrain.getCharacters()) {
                character.stopMove();
                character.returnToHouse(() -> {
                    if (terrain.isAllAtHouse() == true) {
                        startDay();
                    }
                });
            }
        }
    }

    private void startDay() {
        for (Character character : terrain.getCharacters()) {
            character.startDay();
        }
        simulationTime = SIMULATIONTIME;
        secondsElapsed = 0;
        System.out.println("Number of character " + terrain.getCharacters().size());
        isDayTime = true;
    }

}
