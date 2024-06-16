package com.bayle.archives;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Character extends Pane {

    //Character Configuration
    ImageView imageView;
    int count = 3;
    int columns = 3;
    int offsetX = 0;
    int offsetY = 0;
    int width = 40;     //size of the character 
    int height = 40;    //size of the character 
    int score = 0;

    Rectangle removeRect = null;
    public SpriteAnimation animation;

    public Character(ImageView imageView){
        this.imageView = imageView;
        this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        animation = new SpriteAnimation(imageView, Duration.millis(200), count, columns, offsetX, offsetY, width, height);
        getChildren().add(imageView);
    }

    public void moveX(int x){
        boolean right = x>0?true:false;
        for(int i = 0; i < Math.abs(x); i++){
            if(right) this.setTranslateX(this.getTranslateX() + 1);
                else this.setTranslateX(this.getTranslateX() - 1);
            // isBonus();
        }
    }

    public void moveY(int y){
        boolean right = y>0?true:false;
        for(int i = 0; i < Math.abs(y); i++){
            if(right) this.setTranslateY(this.getTranslateY() + 1);
                else this.setTranslateY(this.getTranslateY() - 1);
            // isBonus();
        }
    }

    /*
     * 
     How to use is :

     character.animation.play();
    character.animation.setOffsetY(45);
    character.moveX(1);
        */
        
}
