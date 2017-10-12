package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

    public static Game game;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        game = new Game();

        primaryStage.setTitle("Mine Sweeper");
        primaryStage.setScene(new Scene(game.getLayout().getLayout(), 800, 800));
        primaryStage.show();
    }
}
