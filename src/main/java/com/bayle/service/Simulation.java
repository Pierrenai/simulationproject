package com.bayle.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.bayle.model.Carotte;
import com.bayle.model.Character;
import com.bayle.model.Terrain;
import com.bayle.affichage.ObjectRender;
import com.bayle.affichage.SimulationScene;
import com.bayle.archives.SimulationController;
import com.bayle.util.Utils;
import com.bayle.util.Vecteur;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class Simulation {

    private final int simulationPadding = 100;
    private final int amountOfCarotte = 30;

    private Terrain terrain;
    public Terrain getTerrain(){
        return terrain;
    }

    private List<Pane> sceneObjects;

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
        this.sceneObjects = new ArrayList<>();
        terrain = scene.getTerrain();

        isRunning = false;
        secondsElapsed = 0;
    }

    public void start() {
        if (sceneObjects.isEmpty()) {
            addCarotte(amountOfCarotte);
            addCharacter(2);

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
            for (Character character : getCharacters()) {
                character.update();
            }

            // Toujours avoir le nombre de carotte sur la carte
            // ps: peut-être changer pour faire ce spawn de carotte toutes les x secondes
            addCarotte(amountOfCarotte - getCarottes().size());

            // ObjectRender.Render(myScene);
        }
    }

    public void addCarotte() {
        double width = terrain.getWidth();
        double height = terrain.getHeight();

        Carotte carotte = new Carotte();
        carotte.setTranslateX(Utils.getRandom(simulationPadding, (int) width - simulationPadding));
        carotte.setTranslateY(Utils.getRandom(simulationPadding, (int) height - simulationPadding));

        terrain.getChildren().add(carotte);
        sceneObjects.add(carotte);
    }

    public void addCarotte(int count) {
        if (count > 0) {

            for (int i = 0; i < count; i++) {
                addCarotte();
            }

            for (Carotte carotte : getCarottes()) {
                System.out.println("carotte x:" + carotte.getTranslateX() + " y:" + carotte.getTranslateY());
            }
        }
    }

    public void removeCarotte(Carotte carotte) {
        terrain.getChildren().remove(carotte);
        sceneObjects.remove(carotte);
    }

    public void addCharacter() {
        Character character = new Character(this, "/com/bayle/images/character.png", 50);
        character.setTranslateX(100);
        character.setTranslateY(100);
        sceneObjects.add(character);
        terrain.getChildren().add(character);
    }

    public void addCharacter(int count) {
        for (int i = 0; i < count; i++) {
            addCharacter();
        }
    }

    public List<Character> getCharacters() {
        return sceneObjects.stream()
                .filter(obj -> obj instanceof Character)
                .map(obj -> (Character) obj)
                .collect(Collectors.toList());
    }

    public List<Carotte> getCarottes() {
        return sceneObjects.stream()
                .filter(obj -> obj instanceof Carotte)
                .map(obj -> (Carotte) obj)
                .collect(Collectors.toList());
    }

    public Pane getmyScene() {
        return terrain;
    }

}
