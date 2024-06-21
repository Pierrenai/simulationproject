package com.bayle.model;

import com.bayle.service.Simulation;
import com.bayle.util.Utils;
import com.bayle.util.Vecteur;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Cow extends Pane {

    private static final double WIDTH = 100; // Largeur de la carotte
    private static final double HEIGHT = 60; // Hauteur de la carotte

    private final int step = 10000;
    private final int padding;

    private double speedPerSecond;

    private ImageView imageView;
    private boolean isCollectable = true;
    public boolean isCollectable() {
        return isCollectable;
    }
    public boolean collect() {
        if (isCollectable == true) {
            isCollectable = false;
            return true;
        } else {
            return false;
        }
    }
    private boolean isMoving = false;

    private Simulation simulation;

    private RotateTransition rotateTransition;
    private TranslateTransition translateTransition;

    public Cow(Simulation simulation, double speedPerSecond) {
        this.simulation = simulation;
        padding = simulation.getTerrain().getSimPadding();

        this.speedPerSecond = speedPerSecond;


        // Initialiser l'image
        Image image = new Image(getClass().getResourceAsStream("/com/bayle/images/cow.png"));
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


        // Initialiser l'animation de rotation
        rotateTransition = new RotateTransition(Duration.seconds(0.5), this);
        rotateTransition.setFromAngle(-10);
        rotateTransition.setToAngle(10);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.setAutoReverse(true); // Retourner à la position initiale

    }

    public void update(){
        if (isCollectable == true) {
            move(simulation.getmyScene());
        } else {
            isMoving = false;
            translateTransition.stop();
        }

        if (isMoving) {
            // Démarrer l'animation de rotation si elle n'est pas déjà en cours
            if (rotateTransition.getStatus() != RotateTransition.Status.RUNNING) {
                rotateTransition.play();
            }
        } else {
            // Arrêter l'animation de rotation
            rotateTransition.stop();
            this.setRotate(0); // Réinitialiser l'angle de rotation
        }
    }

    public void move(Pane scene) {
        if (!isMoving) {
            isMoving = true;

            Vecteur direction = new Vecteur(Utils.getRandom(-step, step), Utils.getRandom(-step, step));
            double destinationX = this.getTranslateX() + direction.directionX;
            double destinationY = this.getTranslateY() + direction.directionY;

            if(Utils.debug){
                System.out.println("destinationX:" + destinationX);
                System.out.println("destinationY:" + destinationY);
            }

            if (destinationX < padding)
                direction.directionX = direction.directionX - destinationX + padding;
            if (destinationX > scene.getWidth() - padding)
                direction.directionX = scene.getWidth() - this.getTranslateX() - padding;

            if (destinationY < padding)
                direction.directionY = direction.directionY - destinationY + padding;
            if (destinationY > scene.getHeight() - padding)
                direction.directionY = scene.getHeight() - this.getTranslateY() - padding;

            move(direction);
        }
    }


    public void move(Vecteur direction) {
        // Calcul de la durée de la translation en fonction de la distance et de la
        // vitesse
        double distance = Math.sqrt(Math.pow(direction.directionX, 2) + Math.pow(direction.directionY, 2));
        double durationInSeconds = distance / speedPerSecond;

        // Animation de translation
        translateTransition = new TranslateTransition(Duration.seconds(durationInSeconds), this);
        translateTransition.setByX(direction.directionX);
        translateTransition.setByY(direction.directionY);

        // Ajouter un listener pour détecter la fin de l'animation
        translateTransition.setOnFinished(event -> {
            isMoving = false;
            update(); // Arrêter la rotation après la fin du déplacement
        });

        translateTransition.play();
        update(); // Démarrer la rotation lorsque le déplacement commence
    }

}
