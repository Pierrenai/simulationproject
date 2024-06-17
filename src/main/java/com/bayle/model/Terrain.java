package com.bayle.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.bayle.service.Simulation;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class Terrain extends Pane {
    private Simulation simulation;

    private static int padding = 100;
    public int getSimPadding(){
        return padding;
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

    public List<House> getHouses() {
        return objects.stream()
                .filter(obj -> obj instanceof House)
                .map(obj -> (House) obj)
                .collect(Collectors.toList());
    }


    public Terrain(Scene scene){
        this.setPrefHeight(scene.getHeight());
        this.setPrefWidth(scene.getWidth());
        this.setStyle("-fx-background-color: #5E8C31;");

        objects = new ArrayList<>();
        
        addHouse();
    }

    public void addCharacter() {
        Character character = new Character(simulation, "/com/bayle/images/character.png", 50);
        character.setHouse(getHouse(character));


        character.setTranslateX(100);
        character.setTranslateY(100);
        objects.add(character);
        getChildren().add(character);
    }

    public void addHouse(){
        House house = new House();
        house.setTranslateX(padding);
        house.setTranslateY(padding);
        getChildren().add(house);
        objects.add(house);
    }

    public House getHouse(Character character){
        for (House house : getHouses()) {
            if(house.addCharacter(character)){
                return house;
            }
        }
        House new_house = new House();
        objects.add(new_house);
        return new_house;
    }

}
