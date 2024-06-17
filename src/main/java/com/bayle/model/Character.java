package com.bayle.model;

import java.util.HashMap;

import com.bayle.service.Simulation;
import com.bayle.util.Utils;
import com.bayle.util.Vecteur;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Character extends Pane {

    private final int step = 10000;
    private final int proximityThreshold = 100;

    private Simulation simulation;

    // Variable graphique
    private ImageView imageView;
    private Text scoreText;


    // Variable
    private House house;
    public void setHouse(House house){
        this.house = house;
    }

    private int padding;
    private int score;
    private double speedPerSecond;

    private boolean isMoving = false;
    private boolean isGettingCarotte = false;
    private RotateTransition rotateTransition;
    private TranslateTransition translateTransition;

    public Character(Simulation simulation, String imagePath, double speedPerSecond) {
        this.simulation = simulation;
        padding = simulation.getTerrain().getSimPadding();

        // Initialiser l'image
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        imageView = new ImageView(image);

        // Limiter la taille de l'image à 50px de largeur et 80px de hauteur
        imageView.setFitWidth(30);
        imageView.setFitHeight(50);

        // Ajouter l'image au Pane
        getChildren().add(imageView);

        // Initialiser le score
        score = 0;
        scoreText = new Text("" + score);
        scoreText.setTranslateY(-10); // Positionner le score au-dessus de la tête
        scoreText.setTranslateX(10); // Positionner au milieu
        getChildren().add(scoreText);

        this.speedPerSecond = speedPerSecond;

        // Initialiser l'animation de rotation
        rotateTransition = new RotateTransition(Duration.seconds(0.5), this);
        rotateTransition.setFromAngle(-10);
        rotateTransition.setToAngle(10);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.setAutoReverse(true); // Retourner à la position initiale
    }

    // Définition de la loop
    public void update() {
        if (isGettingCarotte == false) {
            // Récupérer la carotte la plus proche
            Carotte carotteMin = null;
            double minDistance = Double.MAX_VALUE;
            int i = 0;
            for (Carotte carotte : simulation.getCarottes()) {
                i++;
                System.out.println("i:" + i);
                double distance = Utils.distanceBetweenTwoPane(this, carotte);
                if (distance < minDistance) {
                    carotteMin = carotte;
                    minDistance = distance;
                }
            }

            if (minDistance < proximityThreshold) {
                isGettingCarotte = true;
                if (translateTransition != null)
                    translateTransition.stop();
                move(carotteMin);
            } else {
                move(simulation.getmyScene());
            }
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

    private Vecteur calculateDirectionTo(Pane pane2) {
        double dx = pane2.getTranslateX() - this.getTranslateX();
        double dy = pane2.getTranslateY() - this.getTranslateY();
        return new Vecteur(dx, dy);
    }

    public void incrementScore(int points) {
        score += points;
        scoreText.setText("" + score);
    }

    public void move(Vecteur direction) {
        this.isMoving = true;
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
            this.isMoving = false;
            update(); // Arrêter la rotation après la fin du déplacement
        });

        translateTransition.play();
        update(); // Démarrer la rotation lorsque le déplacement commence
    }


    public void move(Carotte carotte) {
        Vecteur direction = calculateDirectionTo(carotte);

        this.isMoving = true;
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
            this.isMoving = false;
            simulation.removeCarotte(carotte);
            isGettingCarotte = false;
            incrementScore(1);
            update(); // Arrêter la rotation après la fin du déplacement
        });

        translateTransition.play();
        update(); // Démarrer la rotation lorsque le déplacement commence
    }

    public void move(Pane scene) {
        if (!isMoving) {
            Vecteur direction = new Vecteur(Utils.getRandom(-step, step), Utils.getRandom(-step, step));
            double destinationX = this.getTranslateX() + direction.directionX;
            double destinationY = this.getTranslateY() + direction.directionY;

            System.out.println("destinationX:" + destinationX);
            System.out.println("destinationY:" + destinationY);

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

    public void stopMove() {
        if (translateTransition != null) {
            translateTransition.stop(); // Arrêter l'animation de translation
            isMoving = false; // Mettre à jour l'état du personnage
        }
    }

    public void printPos() {
        System.out.println("Position X: " + this.getTranslateX());
    }
}
