package com.bayle.archives;

import java.util.Map;

import com.bayle.util.Utils;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class CircleCharacter extends Circle {
    private double speed;
    private double lastUpdateTime;

    private int potatoCollected = 0;

    public CircleCharacter(double posX, double posY, double radius, double speed) {
        this.setLayoutX(posX);
        this.setLayoutY(posY);

        this.setRadius(radius);

        this.setFill(Paint.valueOf("DODGERBLUE"));

        // px par seconds
        this.speed = speed;
        //this.lastUpdateTime = System.nanoTime();
        this.lastUpdateTime = 0;

    }

    public double getSpeed() {
        return speed;
    }

    public void move(Shape nearestPotato, long now) {
        double elapsedTime = now - lastUpdateTime;
        double interval = 1_000_000_000 / (speed); // Si
        // Si interval = 1_000_000_000 = 1px par seconds

        if (elapsedTime >= interval) {
            if (nearestPotato != null) {
                Map<String, Double> vector = Utils.getVectorOneBetweenTwoShapes(this, nearestPotato);
                double directionX = vector.get("x");
                double directionY = vector.get("y");

                setLayoutX(getLayoutX() + directionX);
                setLayoutY(getLayoutY() + directionY);
            }
            lastUpdateTime = now; // Update last update time
        }
    }

    public void move(Shape nearestPotato, double time, boolean useless) {
        double value = speed * (time - lastUpdateTime);
        if (value >= 1) {
            if (nearestPotato != null) {
                Map<String, Double> vector = Utils.getVectorOneBetweenTwoShapes(this, nearestPotato);
                double directionX = vector.get("x") * value;
                double directionY = vector.get("y") * value;

                setLayoutX(getLayoutX() + directionX);
                setLayoutY(getLayoutY() + directionY);
            }
            lastUpdateTime = time;
        }
        System.out.println("Time: " + time);
        System.out.println("lastUpdateTime: " + lastUpdateTime);
    }

    public void collectPotato() {
        potatoCollected++;
    }

    public int getPotatoCollected() {
        return potatoCollected;
    }

    // <Circle fx:id="myCharacter" fill="DODGERBLUE" layoutX="250.0" layoutY="250.0"
    // radius="14.0" stroke="BLACK" strokeType="INSIDE" />
}
