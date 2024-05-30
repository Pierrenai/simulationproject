package com.bayle.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.bayle.model.CircleCharacter;
import com.bayle.util.Utils;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class Simulation {
    private double simulationSpeedMultiplier;
    private int potatoPerSecond;
    private int potatoAtStart;

    private Pane myScene;


    private List<CircleCharacter> characters;
    private List<Circle> potatoes;

    private boolean isRunning;

    public boolean isRunning(){
        return isRunning;
    }
    
    private Timer timer;
    private long lastUpdate = 0;


    private int secondsElapsed;
    private int simulationTime = 60; // in seconds


    public int getRemainingTime(){
        return simulationTime - secondsElapsed;
    }

    private int potatoCount;

    public int getPotatoesCount(){
        return potatoCount;
    }



    public Simulation(Pane scene){
        this.potatoes = new ArrayList<>();
        this.characters = new ArrayList<>();;

        myScene = scene;

        this.simulationSpeedMultiplier = 1;

        isRunning = false;
        secondsElapsed = 0;

        this.potatoAtStart = 1000;
        this.potatoPerSecond = 0;
    }


    public Simulation(Pane scene, int simulationSpeedMultiplier, List<CircleCharacter> characters, int potatoAtStart, int potatoPerSecond){
        this.potatoes = new ArrayList<>();
        this.characters = characters;

        myScene = scene;

        this.simulationSpeedMultiplier = simulationSpeedMultiplier;

        isRunning = false;
        secondsElapsed = 0;

        this.potatoAtStart = potatoAtStart;
        this.potatoPerSecond = potatoPerSecond;
    }


    public void start() {
        if (!isRunning) {
            if (characters.isEmpty()) {
                addPotato(potatoAtStart);
                addCharacter((int) (100 * simulationSpeedMultiplier));

                potatoCount = 0;
                secondsElapsed = 0;
            }
            isRunning = true;

            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    long now = System.nanoTime();
                    if (lastUpdate == 0 || now - lastUpdate >= (1_000_000_000 / simulationSpeedMultiplier)) {
                        Platform.runLater(() -> addPotato(potatoPerSecond));
                        secondsElapsed++;
                        lastUpdate = now;
                    }
                    Platform.runLater(() -> updateSimulation(now));
                }
            }, 0, 1); // Démarre immédiatement et se répète toutes les 1 ms
        } else {
            stop();
        }
    }


    public void reset() {
        stop();

        secondsElapsed = 0;
        //updateTimer();
        potatoCount = 0;
        //updatePotatoCounter();

        for (Circle character : characters) {
            myScene.getChildren().remove(character);
        }
        for (Circle potato : potatoes) {
            myScene.getChildren().remove(potato);
        }

        characters.clear();
        potatoes.clear();
    }

    public void stop() {
        timer.cancel();
        for (CircleCharacter character : characters) {
            System.out.println("Character collected " + character.getPotatoCollected() + " potatoes. He have "
                    + character.getSpeed() + " speed");
        }
        isRunning = false;
    }


    public void updateSimulation(long now) {
        checkEndCondition();

        moveCharacters(now);
        checkPotatoCollisions();
    }

    private void checkEndCondition() {
        if (secondsElapsed >= simulationTime) {
            stop();
        }
    }

    public void moveCharacters(long now) {
        for (CircleCharacter character : characters) {
            Shape nearestPotato = Utils.getNearestShapeFromList((Shape) character, new ArrayList<>(potatoes));
            character.move(nearestPotato, now);
        }
    }

    public void addPotato() {
        double width = myScene.getWidth();
        double height = myScene.getHeight();

        Circle potato = new Circle();
        potato.setRadius(7);
        potato.setLayoutX(Utils.getRandom(10, (int) width - 10));
        potato.setLayoutY(Utils.getRandom(10, (int) height - 10));

        potato.setFill(Paint.valueOf("YELLOW"));

        myScene.getChildren().add(potato);
        potatoes.add(potato);
    }

    public void addPotato(int count) {
        for (int i = 0; i < count; i++) {
            addPotato();
        }
    }

    public void addCharacter(int speed) {
        double width = myScene.getWidth();
        double height = myScene.getHeight();

        double posX = Utils.getRandom(10, (int) width - 10);
        double posY = Utils.getRandom(10, (int) height - 10);

        CircleCharacter character = new CircleCharacter(posX, posY, 14, speed);

        myScene.getChildren().add(character);
        characters.add(character);
    }

    public void addCharacter(int speed, int count) {
        for (int i = 0; i < count; i++) {
            addCharacter(speed);
        }
    }

    // trouver une autre fonction parce que le character qui est en haut de la pile
    // est toujours favorisé pour la récupération des patatess
    public void checkPotatoCollisions() {
        Iterator<Circle> potatoIterator = potatoes.iterator();
        while (potatoIterator.hasNext()) {
            Circle potato = potatoIterator.next();
            for (CircleCharacter character : characters) {
                if (Utils.distanceBetweenTwoShapes(potato, character) <= 20) {
                    myScene.getChildren().remove(potato);
                    potatoIterator.remove();
                    potatoCount++;
                    //updatePotatoCounter();
                    character.collectPotato();
                    break;
                }
            }
        }
    }

}
