package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

    public static Game game;
    public static Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        //Prefs.write();

        window = primaryStage;

        window.setOnCloseRequest(e->close());

        game = new Game();

        primaryStage.setTitle("MineSweeper");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(game.getLayout().getLayout(), 800, 832));
        primaryStage.show();
    }

    public static void close() {
        window.close();
    }

    public static void changeSize(int width, int height) {
        window.setHeight(height);
        window.setWidth(width);
    }

}
