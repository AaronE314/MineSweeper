package main;


import javafx.beans.binding.StringBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.Arrays;

public abstract class Menus {

    public static void newGame(Game game, boolean same) {
        if(!same) {
            game.newGame(Game.stringToIntArray(Prefs.read("diff")), true);
        } else {
            game.newGame();
        }
    }

    public static void newGame(Game game) {
        newGame(game, false);
    }

    public static void customGame(Game game) {


    }

    public static void gameOver(Game game) {

        VBox vBox = new VBox();
        HBox hBox = new HBox();

        Label label = new Label();
        label.setText("Better Luck Next Time!");

        Scene scene = new Scene(vBox, 400,100);

        Stage window = setupWindow("Game Over", scene);

        Button newGame = new Button("New Game");
        newGame.setOnAction(e -> {
            newGame(game);
            window.close();
        });
        Button sameGame = new Button("Play Same Board");
        sameGame.setOnAction(e-> {
            newGame(game, true);
            window.close();
        });
        Button viewBoard = new Button("View Board");
        viewBoard.setOnAction(e->window.close());
        Button close = new Button("Close");
        close.setOnAction(e->{
            window.close();
            Main.close();
        });

        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);

        hBox.getChildren().addAll(newGame,sameGame,viewBoard,close);

        vBox.getChildren().addAll(label, hBox);

        window.showAndWait();

    }

    public static void gameWon(Game game) {
        VBox vBox = new VBox();
        HBox hBox = new HBox();

        Label label = new Label();
        label.setText("Congratulations!");

        Scene scene = new Scene(vBox, 400,100);

        Stage window = setupWindow("Game Won!", scene);

        Button newGame = new Button("New Game");
        newGame.setOnAction(e -> {
            newGame(game);
            window.close();
        });
        Button viewBoard = new Button("View Board");
        viewBoard.setOnAction(e->window.close());
        Button close = new Button("Close");
        close.setOnAction(e->{
            window.close();
            Main.close();
        });

        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);

        hBox.getChildren().addAll(newGame,viewBoard,close);

        vBox.getChildren().addAll(label, hBox);

        window.showAndWait();
    }

    public static void help() {
        HBox hBox = new HBox();

        Scene scene = new Scene(hBox, 200,200);

        Stage stage = setupWindow("Help",scene);

        Text text = new Text();

        text.setText("Add instructions here");

        hBox.getChildren().addAll(text);

        stage.showAndWait();
    }

    public static void options(Game game) {
        VBox vBox = new VBox();

        VBox deffBox = new VBox();

        String[] settings = new String[4];
        Scene scene = new Scene(vBox, 200,200);
        Stage stage = setupWindow("Options",scene);

        ToggleGroup group = new ToggleGroup();

        RadioButton easy = new RadioButton("Easy 10 mines, 9x9");
        easy.setUserData(Constants.EASYS);
        easy.setToggleGroup(group);
        RadioButton medium = new RadioButton("Medium 40 mines 16x16");
        medium.setUserData(Constants.MEDIUMS);
        medium.setToggleGroup(group);
        RadioButton hard = new RadioButton("Hard 99 mines 16x30");
        hard.setUserData(Constants.HARDS);
        hard.setToggleGroup(group);

        System.out.println(Arrays.toString(game.getDiff()));
        if(Arrays.equals(game.getDiff(), Constants.EASY)) {
            easy.setSelected(true);
        } else if (Arrays.equals(game.getDiff(), Constants.MEDIUM)) {
            medium.setSelected(true);
        } else if (Arrays.equals(game.getDiff(), Constants.HARD)) {
            hard.setSelected(true);
        }

        CheckBox sound = new CheckBox("Play Sound");
        sound.setSelected(game.getSound());
        CheckBox question = new CheckBox("Use Question Box");
        question.setSelected(game.getQuestion());

        ColorPicker picker = new ColorPicker();
        picker.setValue(game.getBackColor());

        VBox checks = new VBox();

        checks.getChildren().addAll(sound, question, picker);


        Button close = new Button("Close");
        close.setOnAction(e-> stage.close());
        Button save = new Button("Save");
        save.setOnAction(e-> {

            settings[0] = group.getSelectedToggle().getUserData().toString();
            settings[1] = String.valueOf(sound.isSelected());
            settings[2] = String.valueOf(question.isSelected());
            settings[3] = picker.getValue().toString();
            Prefs.write(settings);
            stage.close();
        });

        HBox buttons = new HBox();

        buttons.getChildren().addAll(save,close);
        buttons.setAlignment(Pos.CENTER);

        deffBox.getChildren().addAll(easy,medium,hard);

        vBox.getChildren().addAll(deffBox,checks,buttons);



        stage.showAndWait();
    }

    public static void about() {

        HBox hBox = new HBox();

        Scene scene = new Scene(hBox, 200,200);

        Stage stage = setupWindow("about",scene);

        Text text = new Text();

        text.setText("Add about stuff here");

        hBox.getChildren().addAll(text);

        stage.showAndWait();
    }

    private static Stage setupWindow(String title, Scene scene) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);

        window.setScene(scene);

        window.setResizable(false);

        return window;
    }
}
