package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main Class
 */
public class Main extends Application{

    /**
     * static attributes
     */
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

    /**
     * for handling closing the window
     */
    public static void close() {
        window.close();
    }

    /**
     * used to change the size of the current window.
     * @param width
     *      the new width of the window
     * @param height
     *      the new height of the window
     */
    public static void changeSize(int width, int height) {
        window.setHeight(height * 40);
        window.setWidth(width * 40);
    }

}
