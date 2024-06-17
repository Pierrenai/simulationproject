package com.bayle.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bayle.model.Carotte;
import com.bayle.model.Character;
import com.bayle.affichage.ObjectRender;
import com.bayle.controller.SimulationController;
import com.bayle.util.Utils;
import com.bayle.util.Vecteur;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

public class Simulation {

    private final int simulationPadding = 100;
    private final int amountOfCarotte = 30;

    private Pane myScene;

    private List<Character> characters;
    private List<Carotte> carottes;

    private boolean isRunning;

    public boolean isRunning() {
        return isRunning;
    }

    private int secondsElapsed;
    private int simulationTime = 60; // in seconds

    public int getRemainingTime() {
        return simulationTime - secondsElapsed;
    }

    public Simulation(Pane scene) {
        this.carottes = new ArrayList<>();
        this.characters = new ArrayList<>();

        myScene = scene;

        isRunning = false;
        secondsElapsed = 0;
    }

    public void start(SimulationController simctrl) {
        if (characters.isEmpty()) {
            addCarotte(amountOfCarotte);
            addCharacter();

            Vecteur direction = new Vecteur(50, 150); // Déplacement vers la droite
            for (Character character : characters) {
                character.move(direction);
            }
            
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
                updateSimulation(now);
            }
        }.start();

        // Rendu de la scene
        // ObjectRender.Render(myScene);
        
    }

    // Reset the simulation
    public void reset() {
        stop();

        secondsElapsed = 0;

        for (Pane character : characters) {
            myScene.getChildren().remove(character);
        }
        for (Carotte carotte : carottes) {
            myScene.getChildren().remove(carotte);
        }

        characters.clear();
        carottes.clear();
    }

    // Stop the simulation
    public void stop() {

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
