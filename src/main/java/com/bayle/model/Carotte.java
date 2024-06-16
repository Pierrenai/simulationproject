package com.bayle.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Carotte extends Pane {

    private static final double WIDTH = 30; // Largeur de la carotte
    private static final double HEIGHT = 80; // Hauteur de la carotte

    private ImageView imageView;
    private Text text;

    public Carotte() {
        // Initialiser l'image
        Image image = new Image(getClass().getResourceAsStream("/com/bayle/images/Carotte.png"));
        imageView = new ImageView(image);

        // Limiter la taille de l'image à 50px de largeur et 80px de hauteur
        imageView.setFitWidth(30);
        imageView.setFitHeight(50);

        // Ajouter l'image au Pane
        getChildren().add(imageView);
    }
}
