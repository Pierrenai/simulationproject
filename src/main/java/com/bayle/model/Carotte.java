package com.bayle.model;

import java.lang.classfile.Signature.TypeArg.WildcardIndicator;

import com.bayle.util.Utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Carotte extends Pane {

    private static final double WIDTH = 30; // Largeur de la carotte
    private static final double HEIGHT = 60; // Hauteur de la carotte

    private ImageView imageView;
    private boolean isCollectable = true;

    public Carotte() {
        // Initialiser l'image
        Image image = new Image(getClass().getResourceAsStream("/com/bayle/images/Carotte.png"));
        imageView = new ImageView(image);

        // Limiter la taille de l'image à 50px de largeur et 80px de hauteur
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);

        // Centrer l'image au milieu
        imageView.setTranslateX(WIDTH / (-2));
        imageView.setTranslateY(HEIGHT / (-2));

        // Ajouter l'image au Pane
        getChildren().add(imageView);

        if (Utils.debug) {
            // Mettre un point au 0 de l'image
            Circle circle = new Circle();
            circle.setRadius(1);

            circle.setFill(Paint.valueOf("BLUE"));
            circle.setTranslateX(WIDTH / (-2));
            circle.setTranslateY(HEIGHT / (-2));
            getChildren().add(circle);

            Circle circle2 = new Circle();
            circle2.setRadius(1);

            circle2.setFill(Paint.valueOf("BLUE"));
            circle2.setTranslateX(WIDTH / (2));
            circle2.setTranslateY(HEIGHT / (2));
            getChildren().add(circle2);
        }
    }

    public boolean collect() {
        if (isCollectable == true) {
            isCollectable = false;
            return true;
        } else {
            return false;
        }
    }

    public boolean isCollectable() {
        return isCollectable;
    }
}
