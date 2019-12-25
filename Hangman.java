import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public class Hangman extends Application {
    private boolean game_over = false;

    public void setGame_over() {
        game_over = true;
    }


    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) throws URISyntaxException {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws FileNotFoundException, URISyntaxException {

        displayFormat display = new displayFormat();
        BorderPane border = new BorderPane();

        border.setTop(display.topButtons(primaryStage));
        GridPane keybd = display.middleDisplay();
        keybd.setMaxSize(10, 40);
        border.setLeft(keybd);
        border.setBottom(display.bottomLabelStartPlay());
        display.showPicVisible(0);
        Scene scene = new Scene(border, 900, 500);
        //Disable the key buttons
        if (!game_over) {
            scene.setOnKeyPressed((KeyEvent e) -> {
                if (!alphabet.contains(e.getText().substring(0, 1).toUpperCase())) {
                    e.consume();
                } else {
                    boolean gameOver = display.handleCharEvent(e.getText());
                    if (gameOver)
                        setGame_over();
                }
            });
        }

        primaryStage.setTitle("Hangman");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}
