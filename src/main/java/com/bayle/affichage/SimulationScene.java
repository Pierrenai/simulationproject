package com.bayle.affichage;

import java.io.IOException;

import com.bayle.App;
import com.bayle.model.Terrain;

import javafx.beans.NamedArg;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class SimulationScene extends Scene {
    Terrain terrain;
    Label timerLabel;

    public Terrain getTerrain() {
        return terrain;
    }

    public Label getTimerLabel(){
        return timerLabel;
    }

    public SimulationScene(@NamedArg(value = "root", defaultValue = "new Group()") Group root, int i, int j) {
        super(root, i, j);

        terrain = new Terrain(this);
        root.getChildren().add(terrain);

        timerLabel = new Label();
        timerLabel.setLayoutX(14);
        timerLabel.setLayoutY(0);
        timerLabel.setTextFill(Paint.valueOf("WHITE"));
        timerLabel.setFont(Font.font("System", FontWeight.BOLD, 35));
        timerLabel.setText("00:00:00");
        root.getChildren().add(timerLabel);
    }

}
