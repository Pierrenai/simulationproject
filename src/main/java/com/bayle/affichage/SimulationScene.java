package com.bayle.affichage;

import java.io.IOException;

import com.bayle.App;
import com.bayle.model.Terrain;

import javafx.beans.NamedArg;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class SimulationScene extends Scene {
    Terrain terrain;

    public Terrain getTerrain(){
        return terrain;
    }

    public SimulationScene(@NamedArg(value = "root",defaultValue = "new Group()") Group root, int i, int j) {
        super(root, i, j);

        terrain = new Terrain(this);
        root.getChildren().add(terrain);
    }

}
