package com.bayle.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

import com.bayle.model.Carotte;
import com.bayle.model.Character;
import com.bayle.affichage.ObjectRender;
import com.bayle.controller.SimulationController;
import com.bayle.util.Utils;
import com.bayle.util.Vecteur;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Simulation {

    private final int simulationPadding = 100;

    private double simulationSpeedMultiplier;
    private int potatoPerSecond;
    private int potatoAtStart;

    private int nbFrameParSecond;

    private Pane myScene;

    private List<Character> characters;
    private List<Carotte> carottes;

    private boolean isRunning;

    public boolean isRunning() {
        return isRunning;
    }

    private Timer timer;
    private long lastUpdate = 0;

    private int secondsElapsed;
    private double timeElapsed = 0;
    private int simulationTime = 60; // in seconds

    public int getRemainingTime() {
        return simulationTime - secondsElapsed;
    }

    private int potatoCount;

    public int getPotatoesCount() {
        return potatoCount;
    }

    public Simulation(Pane scene) {
        this.carottes = new ArrayList<>();
        this.characters = new ArrayList<>();

        myScene = scene;

        this.simulationSpeedMultiplier = 1;

        isRunning = false;
        secondsElapsed = 0;

        this.potatoAtStart = 10;
        this.potatoPerSecond = 0;
    }

    public Simulation(Pane scene, int nbFrameParSecond) {
        this.carottes = new ArrayList<>();
        this.characters = new ArrayList<>();

        myScene = scene;

        this.nbFrameParSecond = nbFrameParSecond;

        this.simulationSpeedMultiplier = 1;

        isRunning = false;
        secondsElapsed = 0;

        this.potatoAtStart = 10;
        this.potatoPerSecond = 0;
    }

    public Simulation(Pane scene, int simulationSpeedMultiplier, List<Character> characters, int potatoAtStart,
            int potatoPerSecond) {
        this.carottes = new ArrayList<>();
        this.characters = characters;

        myScene = scene;

        this.simulationSpeedMultiplier = simulationSpeedMultiplier;

        isRunning = false;
        secondsElapsed = 0;

        this.potatoAtStart = potatoAtStart;
        this.potatoPerSecond = potatoPerSecond;
    }

    public void start(SimulationController simctrl) {
        if (characters.isEmpty()) {
            addCarotte(potatoAtStart);
            addCharacter();

            Vecteur direction = new Vecteur(50, 150); // Déplacement vers la droite
            for (Character character : characters) {
                character.move(direction);
            }

            potatoCount = 0;
            secondsElapsed = 0;
        }
        timer = new Timer();

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
                updateSimulation(now);
            }
        }.start();

        // Rendu de la scene
        // ObjectRender.Render(myScene);


        
    }

    public void reset() {
        stop();

        secondsElapsed = 0;
        // updateTimer();
        potatoCount = 0;
        // updatePotatoCounter();

        for (Pane character : characters) {
            myScene.getChildren().remove(character);
        }
        for (Carotte carotte : carottes) {
            myScene.getChildren().remove(carotte);
        }

        characters.clear();
        carottes.clear();
    }

    public void stop() {
        timer.cancel();
        // for (Character character : characters) {
        // System.out.println("Character collected " + character.getPotatoCollected() +
        // " potatoes. He have "
        // + character.getSpeed() + " speed");
        // }
        isRunning = false;
    }

    public void updateSimulation(double now) {
        checkEndCondition();

        // moveCharacters(now);
        for (Character character : characters) {
            character.update();;
        }


        

        checkPotatoCollisions();

        // ObjectRender.Render(myScene);
    }

    private void checkEndCondition() {
        if (secondsElapsed >= simulationTime) {
            stop();
        }
    }

    public void moveCharacters(double now) {
        if ((now - timeElapsed) >= 1_000_000) {
            for (Character character : characters) {
                // Shape nearestPotato = Utils.getNearestShapeFromList((Shape) character, new
                // ArrayList<>(potatoes));
                // character.move(nearestPotato, now, false);
                // Déplacer le personnage
                // Vecteur direction = new Vecteur(50, 150); // Déplacement vers la droite
                // character.move(direction);
                character.update();

            }
            timeElapsed = now;
        }

    }

    public void addCarotte() {
        double width = myScene.getWidth();
        double height = myScene.getHeight();

        Carotte carotte = new Carotte();
        carotte.setTranslateX(Utils.getRandom(simulationPadding, (int) width - simulationPadding));
        carotte.setTranslateY(Utils.getRandom(simulationPadding, (int) height - simulationPadding));

        myScene.getChildren().add(carotte);
        carottes.add(carotte);
    }

    public void addCarotte(int count) {
        for (int i = 0; i < count; i++) {
            addCarotte();
        }

        for (Carotte carotte : carottes) {
            System.out.println("carotte x:" + carotte.getTranslateX() + " y:" + carotte.getTranslateY());
        }
    }

    public void removeCarotte(Carotte carotte){
        myScene.getChildren().remove(carotte);
        carottes.remove(carotte);
    }

    public void addCharacter() {
        Character character = new Character(this, "/com/bayle/images/character.png", 1000);
        characters.add(character);
        myScene.getChildren().add(character);
    }

    public void addCharacter(int count) {
        for (int i = 0; i < count; i++) {
            addCharacter();
        }
    }

    // trouver une autre fonction parce que le character qui est en haut de la pile
    // est toujours favorisé pour la récupération des patatess
    public void checkPotatoCollisions() {
        Iterator<Carotte> potatoIterator = carottes.iterator();
        while (potatoIterator.hasNext()) {
            Carotte potato = potatoIterator.next();
            // for (Character character : characters) {
            // if (Utils.distanceBetweenTwoShapes(potato, character) <= 20) {
            // myScene.getChildren().remove(potato);
            // potatoIterator.remove();
            // potatoCount++;
            // //updatePotatoCounter();
            // character.collectPotato();
            // break;
            // }
            // }
        }
    }


    public List<Carotte> getCarottes() {
        return this.carottes;
    }

    public Pane getmyScene() {
        return myScene;
    }

}
