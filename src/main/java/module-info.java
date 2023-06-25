module fx.games.shootergame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens fx.games.shootergame to javafx.fxml;
    exports fx.games.shootergame;
}