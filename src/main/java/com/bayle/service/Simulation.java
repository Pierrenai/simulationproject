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
import javafx.scene.layout.Pane;

public class Simulation {

    private final int simulationPadding = 100;
    private final int amountOfCarotte = 30;
    private final int amountOfCow = 1;

    private Terrain terrain;

    public Terrain getTerrain() {
        return terrain;
    }

    private boolean isRunning = false;

    public boolean isRunning() {
        return isRunning;
    }

    private int secondsElapsed;
    private int simulationTime = 60; // in seconds

    public int getRemainingTime() {
        return simulationTime - secondsElapsed;
    }

    public Simulation(SimulationScene scene) {
        terrain = scene.getTerrain();
        terrain.setSimulatiotn(this);

        isRunning = false;
        secondsElapsed = 0;
    }

    public void start() {
        if (terrain.getCharacters().isEmpty()) {
            terrain.addCarotte(amountOfCarotte);
            terrain.addCharacter(2);
            terrain.addCow();

            secondsElapsed = 0;
        }

        isRunning = true;

        new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
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
        if (isRunning) {
            terrain.update();

            for (Character character : terrain.getCharacters()) {
                character.update();
            }
            for (Cow cow : terrain.getCows()) {
                cow.update();
            }

            // Toujours avoir le nombre de carotte sur la carte
            // ps: peut-être changer pour faire ce spawn de carotte toutes les x secondes
            terrain.addCarotte(amountOfCarotte - terrain.getCarottes().size());
           // terrain.addCow(amountOfCow - terrain.getCows().size());

            // ObjectRender.Render(myScene);
        }
    }

    public Pane getmyScene() {
        return terrain;
    }

}
