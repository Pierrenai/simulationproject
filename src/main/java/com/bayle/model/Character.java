package com.bayle.model;

import com.bayle.service.Simulation;
import com.bayle.util.Utils;
import com.bayle.util.Vecteur;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Character extends Pane {

    private static final double WIDTH = 30; // Largeur du personnage
    private static final double HEIGHT = 50; // Hauteur du personnage

    private final int step = 10000;
    private final int proximityThreshold = 100;

    private Simulation simulation;
    private Terrain terrain;

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

    public boolean isMoving() {
        return this.isMoving;
    }

    private boolean isReadyToCollectedCow = false;

    public boolean isReadyToCollectedCow() {
        return this.isReadyToCollectedCow;
    }

    private boolean isCollecting = false;

    public boolean isCollecting() {
        return this.isCollecting;
    }

    private Cow cowInCollecting = null;
    private RotateTransition rotateTransition;
    private TranslateTransition translateTransition;

    public Character(Simulation simulation, String imagePath, double speedPerSecond) {
        // Initialiser les variables
        this.simulation = simulation;
        this.terrain = simulation.getTerrain();
        padding = terrain.getSimPadding();

        this.speedPerSecond = speedPerSecond;

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

        // Initialiser l'animation de rotation
        rotateTransition = new RotateTransition(Duration.seconds(0.5), this);
        rotateTransition.setFromAngle(-10);
        rotateTransition.setToAngle(10);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.setAutoReverse(true); // Retourner à la position initiale
    }

    // Définition de la loop
    public void update() {
        if (!isCollecting) {
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

            // Récupérer la vache la plus proche
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

            // Essai de collecter une vache
            if (cowMin != null) {
                Character someone = nearestSomeoneToCollectCow(cowMin);
                if (minCowDistance < proximityThreshold && someone != null) {
                    this.stopMove();
                    someone.stopMove();

                    this.collectCow(cowMin, true);
                    someone.collectCow(cowMin, false);
                }
            } else if (minDistance < proximityThreshold && carotteMin != null && carotteMin.collect()) {
                collectCarotte(carotteMin);
            } else if (!isMoving) {
                moveAround();
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

    // Callback interface for move completion
    public interface MoveCompletionCallback {
        void onMoveComplete();
    }

    private void collectCow(Cow cow, boolean first) {
        isReadyToCollectedCow = false;
        cow.collect(this);
        isCollecting = true;
        cowInCollecting = cow;

        Vecteur padding = new Vecteur(0, 0);
        if (first) {
            padding = new Vecteur(-30, 0);
        } else {
            padding = new Vecteur(30, 0);
        }
        moveTo(cow, padding, () -> {
            isReadyToCollectedCow = true;
            if (cow.allIsReady()) {
                cowInCollecting.getCollected();
                System.out.println("Cow collected!");
            }
        });
    }

    private boolean bothCharactersReady(Character otherCharacter) {
        return this.isReadyToCollectedCow && otherCharacter.isReadyToCollectedCow;
    }

    private void moveTo(Pane destinationPane, Vecteur padding, MoveCompletionCallback moveCompletionCallback) {
        Vecteur direction = calculateDirectionTo(destinationPane);
        direction = new Vecteur(direction.directionX + padding.directionX, direction.directionY + padding.directionY);

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
            if (moveCompletionCallback != null) {
                moveCompletionCallback.onMoveComplete();
            }
        });

        translateTransition.play();
        update(); // Démarrer la rotation lorsque le déplacement commence
    }

    private Character nearestSomeoneToCollectCow(Cow cow) {
        Character nearestCharacter = null;
        double minDistance = Double.MAX_VALUE;
        for (Character character : terrain.getCharacters()) {
            if (!character.isCollecting() && character != this) {
                double distance = Utils.distanceBetweenTwoPane(this, character);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestCharacter = character;
                }
            }
        }
        return nearestCharacter;
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

    public void moveTo(Vecteur direction) {
        if (translateTransition != null && translateTransition.getStatus() == Animation.Status.RUNNING) {
            translateTransition.stop();
        }
        this.isMoving = true;
        double distance = Math.sqrt(Math.pow(direction.directionX, 2) + Math.pow(direction.directionY, 2));
        double durationInSeconds = distance / speedPerSecond;

        translateTransition = new TranslateTransition(Duration.seconds(durationInSeconds), this);
        translateTransition.setByX(direction.directionX);
        translateTransition.setByY(direction.directionY);

        translateTransition.setOnFinished(event -> {
            this.isMoving = false;
        });

        translateTransition.play();
        update();
    }

    public void collectCarotte(Carotte carotte) {
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
        });

        translateTransition.play();
        update(); // Démarrer la rotation lorsque le déplacement commence
    }

    public void moveAround() {
        Pane scene = simulation.getmyScene();
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

        moveTo(direction);

    }

    public void stopMove() {
        if (translateTransition != null) {
            translateTransition.stop(); // Arrêter l'animation de translation
            isMoving = false; // Mettre à jour l'état du personnage
        }

    }

    public void resetCollecting() {
        isCollecting = false;
        cowInCollecting = null;
        isReadyToCollectedCow = false;
    }
}
