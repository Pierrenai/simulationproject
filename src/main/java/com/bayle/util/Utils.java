package com.bayle.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

public class Utils {

    static public int getRandom(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }

    /* Shape part */
    static public double distanceBetweenTwoShapes(Shape shape1, Shape shape2) {
        return Math
                .sqrt(Math.pow(shape1.getLayoutX() - shape2.getLayoutX(), 2) + Math.pow(shape1.getLayoutY() - shape2.getLayoutY(), 2));
    }

    static public double distanceBetweenTwoPane(Pane pane1, Pane pane2) {
        System.out.println("pane1 x:" + pane1.getTranslateX() + " y:" + pane1.getTranslateY());
        System.out.println("pane2 x:" + pane2.getTranslateX() + " y:" + pane2.getTranslateY());
        return Math
                .sqrt(Math.pow(pane1.getTranslateX() - pane2.getTranslateX(), 2) + Math.pow(pane1.getTranslateY() - pane2.getTranslateY(), 2));
    }

    static public Vecteur calculateDirectionBetweenTwoPane(Pane pane1, Pane pane2) {
        double dx = pane2.getTranslateX() - pane1.getTranslateX();
        double dy = pane2.getTranslateY() - pane1.getTranslateY();
        return new Vecteur(dx, dy);
    }


    static public Map<String, Double> getVectorOneBetweenTwoShapes(Shape startShape, Shape endShape) {
        Map<String, Double> vector = new HashMap<>();
        double x = endShape.getLayoutX() - startShape.getLayoutX();
        double y = endShape.getLayoutY() - startShape.getLayoutY();
        double sum = Math.abs(x) + Math.abs(y);
        vector.put("x", x / sum);
        vector.put("y", y / sum);
        return vector;
    }

    static public Shape getNearestShapeFromList(Shape shape, ArrayList<Shape> shapes){
        Shape nearestShape = null;
        double minDistance = Double.MAX_VALUE;
        for (Shape s: shapes) {
            double distance = Utils.distanceBetweenTwoShapes(s, shape);
            if (distance < minDistance) {
                minDistance = distance;
                nearestShape = s;
            }
        }
        return nearestShape;
    }

}
