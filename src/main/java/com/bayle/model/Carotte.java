package com.bayle.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Carotte extends Pane {

    private static final double WIDTH = 30; // Largeur de la carotte
    private static final double HEIGHT = 60; // Hauteur de la carotte

    private ImageView imageView;

    public Carotte() {
        // Initialiser l'image
        Image image = new Image(getClass().getResourceAsStream("/com/bayle/images/Carotte.png"));
        imageView = new ImageView(image);

        // Limiter la taille de l'image à 50px de largeur et 80px de hauteur
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);

        // Ajouter l'image au Pane
        getChildren().add(imageView);
    }
}
