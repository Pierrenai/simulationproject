package com.bayle.affichage;

import com.bayle.archives.SimulationController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class ObjectRender {

    public static void Render(Pane scene){
        ObservableList<Node> childrens = scene.getChildren();

        // Trier les nodes en fonction de leur position dans la scène
        childrens.sort((node1, node2) -> {
            double z1 = node1.getTranslateY();
            double z2 = node2.getTranslateY();

            if (z1 < z2) {
                return -1;
            } else if (z1 > z2) {
                return 1;
            } else {
                return 0;
            }
        });

        ObservableList<Node> sortedChildrens = FXCollections.observableArrayList(childrens); // Make a copy

        scene.getChildren().clear(); // Clear the scene

        scene.getChildren().addAll(sortedChildrens); // Re-add all nodes from the copy
    }

}
