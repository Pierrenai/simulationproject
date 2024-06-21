package com.bayle.model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class House extends Pane {

    private static final double WIDTH = 110; // Largeur de la carotte
    private static final double HEIGHT = 80; // Hauteur de la carotte

    private ImageView imageView;

    private List<Character> characters;
    private int space = 5;

    public House() {
        // Initialiser l'image
        Image image = new Image(getClass().getResourceAsStream("/com/bayle/images/house.png"));
        imageView = new ImageView(image);

        // Limiter la taille de l'image
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);

        // Centrer l'image
        imageView.setTranslateX(WIDTH / (-2));
        imageView.setTranslateY(HEIGHT / (-2));


        // Ajouter l'image au Pane
        getChildren().add(imageView);


        characters = new ArrayList<>();
    }

    public boolean addCharacter(Character character){
        if(characters.size() < space){
            characters.add(character);
            return true;
        } else {
            return false;
        }
    }

}
