package fx.games.shootergame;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class GameController implements Initializable {

    @FXML
    private Pane pane;

    @FXML
    private ProgressBar playerHealth;

    @FXML
    private ProgressBar villainHealth;

    int delta = (int) (1+Math.random()*4);
    private static int flag;

    int columns = 4;
    int totalNumberOfFrames = 8;

    int columns2 = 3;
    int totalNumberOfFrames2 = 3;
    int FrameWidth = 204;
    int FrameHeight = 137;

    int FrameWidth2 = 143;
    int FrameHeight2 = 100;
    int FPS = 24;
    int rows = 2;
    int rows2 = 1;

    protected MediaPlayer mediaPlayer;

    protected Media sound;

    private Boolean mLeft = false;
    private Boolean moveVertical = false;

    List<ImageView> shoots = new ArrayList<>();

    public static Set<KeyCode> activeKeys;
    public static Set<KeyCode> releaseKeys;

    private Boolean hit = false;

    private Boolean hitPlayer = false;
    private RedDragon p;
    private GoldDragon player;

    public static boolean won = false;

    public static boolean defeated = false;
    private Stage stage;

    private boolean pause = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shoots.clear();
        won = false;
        defeated = false;
        playerHealth.setProgress(1.0);
        villainHealth.setProgress(1.0);
        p = new RedDragon();
        //pause = true;
        player = new GoldDragon();

        activeKeys = new HashSet<>();
        releaseKeys = new HashSet<>();

        try {
            sound = new Media(getClass().getResource("background_music.wav").toURI().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

        p.setLayoutX(50);
        p.setTranslateY(50);
        p.setFitWidth(FrameWidth/2);
        p.setFitHeight(FrameHeight/2);
        p.setImage(new Image(getClass().getResource("red_dragon.png").toExternalForm()));
        ImageViewSprite anim = new ImageViewSprite(p,  columns, rows, totalNumberOfFrames, FrameWidth, FrameHeight, FPS);
        anim.start();


        ImageView dragonFire = new ImageView();
        dragonFire.setFitHeight(25);
        dragonFire.setFitWidth(25);
        dragonFire.setLayoutX(p.getLayoutX()+40);
        dragonFire.setLayoutY(p.getLayoutY()+71);

        dragonFire.setImage(new Image(getClass().getResourceAsStream("fire.png")));

        playerHealth.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
               double progress = (double) t1;
                if(progress < 0.0){
                    defeated = true;
                }
                if(progress < 0.5){
                    playerHealth.setStyle("-fx-accent: red;");
                }

            }
        });

        villainHealth.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                double progress = (double) t1;
                if(progress < 0.0){
                    won = true;
                }
                if(progress < 0.5){
                    villainHealth.setStyle("-fx-accent: red;");
                }
            }
        });

        player.setLayoutX(300);
        player.setLayoutY(729);
        player.setFitWidth(FrameWidth2-40);
        player.setFitHeight(FrameHeight2-40);
        player.setImage(new Image(getClass().getResource("gold_dragon.png").toExternalForm()));
        ImageViewSprite anim2 = new ImageViewSprite(player,  columns2, rows2, totalNumberOfFrames2, FrameWidth2, FrameHeight2, FPS-16);
        anim2.start();



        pane.getChildren().addAll(player,p);

        pane.getChildren().add(dragonFire);

        pane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                System.out.println("Hello");
            }
        });

        flag = 1;


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage = (Stage) pane.getScene().getWindow();

                stage.setOnCloseRequest(e ->{
                    Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
                    dialog.setTitle("Quit Confirmation");
                    dialog.setHeaderText("Confirm quit game");
                    dialog.setContentText("Are you sure you want to quit game.?");
                    ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                    ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
                    dialog.getButtonTypes().setAll(yesButton, noButton);

                    Optional<ButtonType> result = dialog.showAndWait();
                    if(result.get() != yesButton){
                        e.consume();
                    }
                });
            }
        });


        new AnimationTimer(){

            @Override
            public void handle(long l) {
                if(pause){
                    return;
                }
                if(won){
                    mediaPlayer.stop();
                    this.stop();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            mediaPlayer.stop();
                            try {

                                Parent root = FXMLLoader.load(getClass().getResource("game_over.fxml"));

                                stage.setTitle("Game Over");

                                Scene scene = new Scene(root);
                                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                                stage.setScene(scene);


                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                        }
                    });
                }


                if(defeated){
                    mediaPlayer.stop();
                    this.stop();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            mediaPlayer.stop();
                            try {

                                Parent root = FXMLLoader.load(getClass().getResource("game_over.fxml"));

                                stage.setTitle("Game Over");

                                Scene scene = new Scene(root);
                                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                                stage.setScene(scene);


                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                        }
                    });
                }

                if(activeKeys.contains(KeyCode.UP)){
                    player.moveUP();
                }else if(activeKeys.contains(KeyCode.DOWN)){
                    player.moveDown();
                }else if(activeKeys.contains(KeyCode.LEFT)){
                    player.moveLeft();
                }else if(activeKeys.contains(KeyCode.RIGHT)){
                    player.moveRight();
                }


                if (shoots.size() > 0) {
                    Iterator<ImageView> iterator = shoots.iterator();
                    while (iterator.hasNext()) {
                        ImageView s = iterator.next();
                        s.setTranslateY(s.getTranslateY() - 2);

                        if (s.getTranslateY() < -709) {
                            iterator.remove();
                            pane.getChildren().remove(s);
                        } else if (s.getBoundsInParent().intersects(p.getBoundsInParent())) {
                            iterator.remove();
                            pane.getChildren().remove(s);
                            hit = true;
                        }
                    }
                }

                if(hit){
                    AudioClip clip = null;
                    try {
                        clip = new AudioClip(getClass().getResource("hit.wav").toURI().toString());
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                    clip.play();
                    villainHealth.setProgress(villainHealth.getProgress()-0.05);
                    hit = false;
                }



                if(dragonFire.getTranslateY() > 800){
                   /* AudioClip clip = null;
                    try {
                        clip = new AudioClip(getClass().getResource("fire2.mp3").toURI().toString());
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }*/
                    /*clip.play();*/
                    dragonFire.setTranslateY(p.getTranslateY()-30);
                    dragonFire.setTranslateX(p.getTranslateX());
                }



                if(dragonFire.getBoundsInParent().intersects(player.getBoundsInParent())){
                    dragonFire.setTranslateY(p.getTranslateY()-30);
                    dragonFire.setTranslateX(p.getTranslateX());
                    hitPlayer = true;
                }

                if(hitPlayer){
                    AudioClip clip = null;
                    try {
                        clip = new AudioClip(getClass().getResource("fall.wav").toURI().toString());
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                    clip.play();
                    playerHealth.setProgress(playerHealth.getProgress()-0.2);
                    hitPlayer = false;
                }



                if(delta == 1 || delta == 3){
                    moveVertical = true;
                    if(p.getTranslateY() >= 300){
                        flag = -1;
                        delta = (int) (1+Math.random()*4);
                    }else if(p.getTranslateY() < 0+50){
                        flag = 1;
                        delta = (int) (1+Math.random()*4);
                    }

                }else{
                    moveVertical = false;
                }

                 if(p.getLayoutX()+p.getTranslateX()+p.getFitWidth() >= 600+3){
                    flag = -1;
                    delta = (int) (1+Math.random()*4);
                }else if(p.getLayoutX()+p.getTranslateX() <= 0){
                     flag = 1;
                     delta = (int) (1+Math.random()*4);
                 }



                if(!moveVertical){
                    p.setTranslateX(p.getTranslateX()+(delta *flag));
                }else{
                    p.setTranslateY(p.getTranslateY()+(delta *flag));
                }

                dragonFire.setTranslateY(dragonFire.getTranslateY()+4);



            }
        }.start();

    }




    public void shoot() {

        if(shoots.size() > 5){
            return;
        }
        ImageView fire = new ImageView(new Image(getClass().getResourceAsStream("fire_main.png")));
        fire.setFitHeight(15);
        fire.setFitWidth(15);
        fire.setTranslateX(player.getLayoutX()+player.getTranslateX() + 46);
        fire.setTranslateY(player.getLayoutY()+player.getTranslateY());
        pane.getChildren().add(fire);
        shoots.add(fire);

        for (ImageView shoot : shoots) {
            if(!pane.getChildren().contains(shoot)){
                pane.getChildren().add(shoot);
            }



        }

    }


}
