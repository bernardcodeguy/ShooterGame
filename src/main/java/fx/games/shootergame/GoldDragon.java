package fx.games.shootergame;

import javafx.scene.image.ImageView;

public class GoldDragon extends ImageView {

    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    public static final int UP = 3;
    public static final int DOWN = 4;

    public GoldDragon() {
        super();

        setLayoutX(0); // set the initial X position to 0
        setLayoutY(0); // set the initial Y position to 0
    }

    public void setPosition(double x, double y) {
        setLayoutX(x);
        setLayoutY(y);
    }

    public void moveUP() {
        if(this.getTranslateY() < -300 ){
            return;
        }
        this.setTranslateY(this.getTranslateY()-5);
    }


    public  void moveDown() {
        if(this.getTranslateY() > 0 ){
            return;
        }
        this.setTranslateY(this.getTranslateY()+5);
    }

    public void moveLeft() {
        if(this.getTranslateX() < -344){
            return;
        }

        this.setTranslateX(this.getTranslateX()-5);
    }

    public void moveRight() {
        if(this.getTranslateX() > 229){
            return;
        }
        this.setTranslateX(this.getTranslateX()+5);
    }
}
