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

    private static final double WIDTH = 30; // Largeur de la carotte
    private static final double HEIGHT = 50; // Hauteur de la carotte

    private final int step = 10000;
    private final int proximityThreshold = 100;

    private Simulation simulation;

    // Variable graphique
    private ImageView imageView;
    private Text scoreText;

    // Variable
    private House house;

    public void setHouse(House house) {
        this.house = house;
    }

    private int padding;
    private int score;
    private double speedPerSecond;

    private boolean isMoving = false;
    private boolean isCollecting = false;

    public boolean isCollecting() {
        return this.isCollecting;
    }

    private Cow cowInCollecting = null;
    private RotateTransition rotateTransition;
    private TranslateTransition translateTransition;

    public Character(Simulation simulation, String imagePath, double speedPerSecond) {
        this.simulation = simulation;
        padding = simulation.getTerrain().getSimPadding();

        // Initialiser l'image
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        imageView = new ImageView(image);

        // Limiter la taille de l'image à 50px de largeur et 80px de hauteur
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);

        // Centrer l'image
        imageView.setTranslateX(WIDTH / (-2));
        imageView.setTranslateY(HEIGHT / (-2));

        // Ajouter l'image au Pane
        getChildren().add(imageView);

        // Initialiser le score
        score = 0;
        scoreText = new Text("" + score);
        scoreText.setTranslateY((HEIGHT / -2) - 5); // Positionner le score au-dessus de la tête
        scoreText.setTranslateX(0); // Positionner au milieu
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
        if (isCollecting == false && cowInCollecting != null) {
            // Récupérer la carotte la plus proche
            Carotte carotteMin = null;
            double minDistance = Double.MAX_VALUE;
            for (Carotte carotte : simulation.getTerrain().getCarottes()) {
                if (carotte.isCollectable()) {
                    double distance = Utils.distanceBetweenTwoPane(this, carotte);
                    if (distance < minDistance) {
                        carotteMin = carotte;
                        minDistance = distance;
                    }
                }
            }

            Cow cowMin = null;
            double minCowDistance = Double.MAX_VALUE;
            for (Cow cow : simulation.getTerrain().getCows()) {
                if (cow.isCollectable()) {
                    double distance = Utils.distanceBetweenTwoPane(this, cow);
                    if (distance < minCowDistance) {
                        cowMin = cow;
                        minCowDistance = distance;
                    }
                }
            }

            Character nearestCharacter = tryCollectCow(cowMin);

            if (minCowDistance < proximityThreshold && cowMin != null && nearestCharacter != null) {
                final Cow cow = cowMin;
                cowMin.collect();
                isCollecting = true;
                if (translateTransition != null)
                    translateTransition.stop();
                move(cowMin, true, () -> {
                    // Callback triggered when nearestCharacter's move with cowMin completes
                    // Now trigger the move for thisCharacter with cowMin
                    nearestCharacter.stopMove();
                    nearestCharacter.move(cow, false, () -> {
                        simulation.getTerrain().removeObject(cow);
                        
                        incrementScore(5);
                        this.isCollecting = false;
                        this.cowInCollecting = null;

                        nearestCharacter.incrementScore(5);
                        nearestCharacter.isCollecting = false;
                        nearestCharacter.cowInCollecting = null;
                    }); // null or another callback if needed);
                });

            } else if (minDistance < proximityThreshold && carotteMin != null && carotteMin.collect()) {
                isCollecting = true;
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
            simulation.getTerrain().removeObject(carotte);
            isCollecting = false;
            incrementScore(1);
            update(); // Arrêter la rotation après la fin du déplacement
        });

        translateTransition.play();
        update(); // Démarrer la rotation lorsque le déplacement commence
    }

    public Character tryCollectCow(Cow cow) {
        Character nearestCharacter = null;
        double distanceChara = Double.MAX_VALUE;
        for (Character character : simulation.getTerrain().getCharacters()) {
            if (character.isCollecting() == false) {
                double new_dist = Utils.distanceBetweenTwoPane(this, character);
                if (new_dist < distanceChara) {
                    distanceChara = new_dist;
                    nearestCharacter = character;
                }
            }
        }
        if (nearestCharacter != null) {
            nearestCharacter.isCollecting = true;
            return nearestCharacter;
        } else {
            return null;
        }
    }

    // Callback interface for move completion
    public interface MoveCompletionCallback {
        void onMoveComplete();
    }

    public void move(Cow cow, boolean first, MoveCompletionCallback moveCompletionCallback) {
        isCollecting = true;
        cowInCollecting = cow;
        Vecteur direction = calculateDirectionTo(cow);

        this.isMoving = true;
        // Calcul de la durée de la translation en fonction de la distance et de la
        // vitesse
        double distance = Math.sqrt(Math.pow(direction.directionX, 2) + Math.pow(direction.directionY, 2));
        double durationInSeconds = distance / speedPerSecond;

        // Animation de translation
        translateTransition = new TranslateTransition(Duration.seconds(durationInSeconds), this);
        translateTransition.setByX(direction.directionX - 30);
        translateTransition.setByY(direction.directionY);

        // Ajouter un listener pour détecter la fin de l'animation
        translateTransition.setOnFinished(event -> {
            this.isMoving = false;
            if (moveCompletionCallback != null) {
                moveCompletionCallback.onMoveComplete();
            }
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

            if (Utils.debug) {

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

    public void stopMove() {
        if (translateTransition != null) {
            translateTransition.stop(); // Arrêter l'animation de translation
            isMoving = false; // Mettre à jour l'état du personnage
        }
    }
}
