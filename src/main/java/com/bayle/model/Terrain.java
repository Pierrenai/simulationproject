package com.bayle.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.bayle.service.Simulation;
import com.bayle.util.ObjectType;
import com.bayle.util.Utils;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class Terrain extends Pane {
    private Simulation simulation;

    public void setSimulatiotn(Simulation simulation) {
        this.simulation = simulation;
    }

    private static int PADDING = 100;
    private static int MAX_NEAR = 50;

    public int getSimPadding() {
        return PADDING;
    }

    private List<Pane> objects;

    public List<Character> getCharacters() {
        return objects.stream()
                .filter(obj -> obj instanceof Character)
                .map(obj -> (Character) obj)
                .collect(Collectors.toList());
    }

    public List<Carotte> getCarottes() {
        return objects.stream()
                .filter(obj -> obj instanceof Carotte)
                .map(obj -> (Carotte) obj)
                .collect(Collectors.toList());
    }

    public List<Cow> getCows() {
        return objects.stream()
                .filter(obj -> obj instanceof Cow)
                .map(obj -> (Cow) obj)
                .collect(Collectors.toList());
    }

    public List<House> getHouses() {
        return objects.stream()
                .filter(obj -> obj instanceof House)
                .map(obj -> (House) obj)
                .collect(Collectors.toList());
    }

    public Terrain(Scene scene) {
        this.setPrefHeight(scene.getHeight());
        this.setPrefWidth(scene.getWidth());
        this.setStyle("-fx-background-color: #5E8C31;");

        objects = new ArrayList<>();

        addHouse();
    }

    public void update() {

    }

    /* Objectif : centralisé l'ajout d'objets */
    public void addObject(ObjectType objectType) {
        switch (objectType) {
            case CHARACTER:

                break;

            case CAROTTE:

                break;

            case HOUSE:

                break;
        }
    }

    public void addCharacter() {
        Character character = new Character(simulation, "/com/bayle/images/character.png", 50);
        character.setHouse(getHouse(character));

        character.setTranslateX(100);
        character.setTranslateY(100);
        objects.add(character);
        getChildren().add(character);
    }

    public void addHouse() {
        House house = new House();
        boolean isOverlapping;
        int attempts = 0;

        do {
            isOverlapping = false;
            double x = Utils.getRandom(PADDING, (int) getWidth() - PADDING);
            double y = Utils.getRandom(PADDING, (int) getHeight() - PADDING);

            house.setTranslateX(x);
            house.setTranslateY(y);

            // Vérifier le chevauchement avec les autres carottes
            for (House existingHouse : getHouses()) {
                if (isOverlapping(house, existingHouse)) {
                    isOverlapping = true;
                    break;
                }
            }
            attempts++;
            // Limiter le nombre de tentatives pour éviter une boucle infinie
            if (attempts > 100) {
                throw new RuntimeException("Impossible de placer la maison sans chevauchement après 100 tentatives.");
            }
        } while (isOverlapping);

        getChildren().add(house);
        objects.add(house);
    }

    public House getHouse(Character character) {
        for (House house : getHouses()) {
            if (house.addCharacter(character)) {
                return house;
            }
        }
        House new_house = new House();
        objects.add(new_house);
        return new_house;
    }

    public void addCarotte(int amountOfCarotte) {
        if (amountOfCarotte > 0) {

            for (int i = 0; i < amountOfCarotte; i++) {
                addCarotte();
            }
        }
    }

    public void addCarotte() {
        double width = getWidth();
        double height = getHeight();

        Carotte carotte = new Carotte();
        boolean isOverlapping;
        int attempts = 0;

        do {
            isOverlapping = false;
            double x = Utils.getRandom(PADDING, (int) width - PADDING);
            double y = Utils.getRandom(PADDING, (int) height - PADDING);

            carotte.setTranslateX(x);
            carotte.setTranslateY(y);

            // Vérifier le chevauchement avec les autres carottes
            for (Carotte existingCarotte : getCarottes()) {
                if (isOverlapping(carotte, existingCarotte)) {
                    isOverlapping = true;
                    break;
                }
            }
            attempts++;
            // Limiter le nombre de tentatives pour éviter une boucle infinie
            if (attempts > 100) {
                throw new RuntimeException("Impossible de placer la carotte sans chevauchement après 100 tentatives.");
            }
        } while (isOverlapping);

        objects.add(carotte);
        getChildren().add(carotte);

    }

    public void addCharacter(int nb) {
        for (int i = 0; i < nb; i++) {
            addCharacter();
        }
    }

    public void addCow() {
        Cow cow = new Cow(simulation, 30);

        cow.setTranslateX(getWidth() / 2);
        cow.setTranslateY(-30);

        objects.add(cow);
        getChildren().add(cow);

    }

    public void addCow(int nb) {
        for (int i = 0; i < nb; i++) {
            addCow();
        }
    }

    // Get a random pane without overlapping any objects in the panes list
    private void randomPosition(Pane pane, List<Pane> panes) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'randomPosition'");
    }

    private boolean isOverlapping(Pane pane1, Pane pane2) {
        return false;

        // double x1 = pane1.getTranslateX();
        // double y1 = pane1.getTranslateY();
        // double x2 = pane1.getTranslateX() + pane1.getWidth();
        // double y2 = pane1.getTranslateY() + pane1.getPrefHeight();

        // double x3 = pane2.getTranslateX();
        // double y3 = pane2.getTranslateY();
        // double x4 = pane2.getTranslateX() + pane2.getPrefWidth();
        // double y4 = pane2.getTranslateY() + pane2.getPrefHeight();

        // double x1 = pane1.getTranslateX() - (pane1.getPrefWidth() / 2);
        // double y1 = pane1.getTranslateY() - (pane1.getPrefHeight() / 2);
        // double x2 = pane1.getTranslateX() + (pane1.getPrefWidth() / 2);
        // double y2 = pane1.getTranslateY() + (pane1.getPrefHeight() / 2);

        // double x3 = pane2.getTranslateX() - (pane2.getPrefWidth() / 2);
        // double y3 = pane2.getTranslateY() - (pane2.getPrefHeight() / 2);
        // double x4 = pane2.getTranslateX() + (pane2.getPrefWidth() / 2);
        // double y4 = pane2.getTranslateY() + (pane2.getPrefHeight() / 2);

        // if (x1 > x4 // A droite
        // || x2 < x3 // A gauche
        // || y1 > y4 // En dessous
        // || y2 < y3 // Au dessus
        // ) {
        // return false;
        // } else {
        // return true;
        // }
    }

    public void removeObject(Pane object) {
        objects.remove(object);
        getChildren().remove(object);
    }

}
