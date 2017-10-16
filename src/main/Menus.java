package main;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;

public abstract class Menus {

    public static void newGame(Game game, boolean same) {
        if (!same) {
            if (game.isGameInProgress()) {
                newGameConfirm(game);
            } else {
                game.newGame(Game.stringToIntArray(Prefs.read("diff")), true);
            }
        } else {
            game.newGame();
        }
    }

    public static void newGame(Game game) {
        newGame(game, false);
    }

    public static void customGame(Game game) {
        VBox vBox = new VBox();

        VBox deffBox = new VBox();

        VBox customBox = new VBox();

        HBox deffs = new HBox();

        Scene scene = new Scene(vBox, 400, 200);
        Stage stage = setupWindow("Options", scene);

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
        RadioButton custom = new RadioButton("Custom");
        custom.setUserData("custom");
        custom.setToggleGroup(group);


        DigitField length = new DigitField();
        DigitField width = new DigitField();
        DigitField mines = new DigitField();

        length.setText("9");
        length.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (Integer.parseInt(length.getText()) < 9) {
                length.setText("9");
            } else if (Integer.parseInt(length.getText()) > 24) {
                length.setText("24");
            }
            updateCustom(mines, width, length);
        });
        length.setEditable(custom.isSelected());
        width.setText("9");
        width.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (Integer.parseInt(width.getText()) < 9) {
                width.setText("9");
            } else if (Integer.parseInt(width.getText()) > 30) {
                width.setText("30");
            }
            updateCustom(mines, width, length);
        });
        width.setEditable(custom.isSelected());
        mines.setText("10");
        mines.focusedProperty().addListener((observable, oldValue, newValue) -> updateCustom(mines, width, length));
        mines.setEditable(custom.isSelected());
        custom.selectedProperty().addListener((observable, oldValue, newValue) -> {
            length.setEditable(custom.isSelected());
            width.setEditable(custom.isSelected());
            mines.setEditable(custom.isSelected());
        });


        Label widthLabel = new Label("Width (9-30): ");
        Label heightLabel = new Label("Height (9-24): ");
        Label minesLabel = new Label("Mines (10-667): ");


        if (Arrays.equals(game.getDiff(), Constants.EASY)) {
            easy.setSelected(true);
        } else if (Arrays.equals(game.getDiff(), Constants.MEDIUM)) {
            medium.setSelected(true);
        } else if (Arrays.equals(game.getDiff(), Constants.HARD)) {
            hard.setSelected(true);
        } else {
            custom.setSelected(true);
        }

        Button close = new Button("Cancel");
        close.setOnAction(e -> stage.close());
        Button start = new Button("Start");
        start.setOnAction(e -> {
            String diff = group.getSelectedToggle().getUserData().toString();
            if (diff.equals("custom")) {
                diff = width.getText() + "," + length.getText() + "," + mines.getText();
            }

            String[] prefs = Prefs.readAll();

            prefs[3] = diff;

            for (int i = 0; i < prefs.length / 2; i++) {
                String temp = prefs[i];
                prefs[i] = prefs[prefs.length - i - 1];
                prefs[prefs.length - i - 1] = temp;
            }

            Prefs.write(prefs);

            newGame(game);

            stage.close();
        });

        HBox buttons = new HBox();

        HBox heightBox = new HBox();
        HBox widthBox = new HBox();
        HBox mineBox = new HBox();

        heightBox.getChildren().addAll(heightLabel, length);
        widthBox.getChildren().addAll(widthLabel, width);
        mineBox.getChildren().addAll(minesLabel, mines);


        buttons.getChildren().addAll(start, close);
        buttons.setAlignment(Pos.CENTER);

        deffBox.getChildren().addAll(easy, medium, hard);
        customBox.getChildren().addAll(custom, heightBox, widthBox, mineBox);
        deffs.getChildren().addAll(deffBox, customBox);

        vBox.getChildren().addAll(deffs, buttons);

        stage.showAndWait();

    }

    public static void gameOver(Game game) {

        VBox vBox = new VBox();
        HBox hBox = new HBox();

        Label label = new Label();
        label.setText("Better Luck Next Time!");

        Scene scene = new Scene(vBox, 400, 100);

        Stage window = setupWindow("Game Over", scene);

        Button newGame = new Button("New Game");
        newGame.setOnAction(e -> {
            newGame(game);
            window.close();
        });
        Button sameGame = new Button("Play Same Board");
        sameGame.setOnAction(e -> {
            newGame(game, true);
            window.close();
        });
        Button viewBoard = new Button("View Board");
        viewBoard.setOnAction(e -> window.close());
        Button close = new Button("Close");
        close.setOnAction(e -> {
            window.close();
            Main.close();
        });

        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);

        hBox.getChildren().addAll(newGame, sameGame, viewBoard, close);

        vBox.getChildren().addAll(label, hBox);

        window.showAndWait();

    }

    public static void gameWon(Game game) {
        VBox vBox = new VBox();
        HBox hBox = new HBox();

        Label label = new Label();
        label.setText("Congratulations!");

        Scene scene = new Scene(vBox, 400, 100);

        Stage window = setupWindow("Game Won!", scene);

        Button newGame = new Button("New Game");
        newGame.setOnAction(e -> {
            newGame(game);
            window.close();
        });
        Button viewBoard = new Button("View Board");
        viewBoard.setOnAction(e -> window.close());
        Button close = new Button("Close");
        close.setOnAction(e -> {
            window.close();
            Main.close();
        });

        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);

        hBox.getChildren().addAll(newGame, viewBoard, close);

        vBox.getChildren().addAll(label, hBox);

        window.showAndWait();
    }

    public static void help() {
        HBox hBox = new HBox();

        Scene scene = new Scene(hBox, 200, 200);

        Stage stage = setupWindow("Help", scene);

        Text text = new Text();

        text.setText("Add instructions here");

        hBox.getChildren().addAll(text);

        stage.showAndWait();
    }

    public static void options(Game game) {
        VBox vBox = new VBox();

        VBox deffBox = new VBox();

        VBox customBox = new VBox();

        HBox deffs = new HBox();

        String[] settings = new String[4];
        Scene scene = new Scene(vBox, 400, 200);
        Stage stage = setupWindow("Options", scene);

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
        RadioButton custom = new RadioButton("Custom");
        custom.setUserData("custom");
        custom.setToggleGroup(group);


        DigitField length = new DigitField();
        DigitField width = new DigitField();
        DigitField mines = new DigitField();
        length.setText("9");
        length.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (Integer.parseInt(length.getText()) < 9) {
                length.setText("9");
            } else if (Integer.parseInt(length.getText()) > 24) {
                length.setText("24");
            }
            updateCustom(mines, width, length);
        });
        length.setEditable(custom.isSelected());
        width.setText("9");
        width.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (Integer.parseInt(width.getText()) < 9) {
                width.setText("9");
            } else if (Integer.parseInt(width.getText()) > 30) {
                width.setText("30");
            }
            updateCustom(mines, width, length);
        });
        width.setEditable(custom.isSelected());
        mines.setText("10");
        mines.focusedProperty().addListener((observable, oldValue, newValue) -> updateCustom(mines, width, length));
        mines.setEditable(custom.isSelected());
        custom.selectedProperty().addListener((observable, oldValue, newValue) -> {
            length.setEditable(custom.isSelected());
            width.setEditable(custom.isSelected());
            mines.setEditable(custom.isSelected());
        });


        Label widthLabel = new Label("Width (9-30): ");
        Label heightLabel = new Label("Height (9-24): ");
        Label minesLabel = new Label("Mines (10-667): ");


        if (Arrays.equals(game.getDiff(), Constants.EASY)) {
            easy.setSelected(true);
        } else if (Arrays.equals(game.getDiff(), Constants.MEDIUM)) {
            medium.setSelected(true);
        } else if (Arrays.equals(game.getDiff(), Constants.HARD)) {
            hard.setSelected(true);
        } else {
            custom.setSelected(true);
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
        close.setOnAction(e -> stage.close());
        Button save = new Button("Save");
        save.setOnAction(e -> {
            String diff = group.getSelectedToggle().getUserData().toString();
            if (diff.equals("custom")) {
                diff = width.getText() + "," + length.getText() + "," + mines.getText();
            }
            settings[0] = diff;
            settings[1] = String.valueOf(sound.isSelected());
            settings[2] = String.valueOf(question.isSelected());
            settings[3] = picker.getValue().toString();

            String[] old = Prefs.readAll();

            Prefs.write(settings);

            if (!old[3].equals(settings[0])) {
                confirm(game);
            }

            game.getPrefs();

            stage.close();
        });
        Button reset = new Button("Restore defaults");
        reset.setOnAction(e -> {
            easy.setSelected(true);
            sound.setSelected(true);
            question.setSelected(true);
            picker.setValue(Color.LIGHTGREY);

            Prefs.write();
        });

        HBox buttons = new HBox();

        HBox heightBox = new HBox();
        HBox widthBox = new HBox();
        HBox mineBox = new HBox();

        heightBox.getChildren().addAll(heightLabel, length);
        widthBox.getChildren().addAll(widthLabel, width);
        mineBox.getChildren().addAll(minesLabel, mines);


        buttons.getChildren().addAll(save, close, reset);
        buttons.setAlignment(Pos.CENTER);

        deffBox.getChildren().addAll(easy, medium, hard);
        customBox.getChildren().addAll(custom, heightBox, widthBox, mineBox);
        deffs.getChildren().addAll(deffBox, customBox);

        vBox.getChildren().addAll(deffs, checks, buttons);

        stage.showAndWait();
    }

    private static void updateCustom(DigitField mines, DigitField width, DigitField length) {
        int val = Integer.parseInt(mines.getText());
        int maxVal = (Integer.parseInt(width.getText()) - 1) * (Integer.parseInt(length.getText()) - 1);
        System.out.println(maxVal);
        if (val < 10) {
            mines.setText("9");
        } else if (val > maxVal) {
            mines.setText(Integer.toString(maxVal));
        }
    }

    public static void about() {

        HBox hBox = new HBox();

        Scene scene = new Scene(hBox, 200, 200);

        Stage stage = setupWindow("about", scene);

        Text text = new Text();

        text.setText("Add about stuff here");

        hBox.setAlignment(Pos.CENTER);

        hBox.getChildren().addAll(text);

        stage.showAndWait();
    }


    public static void confirm(Game game) {
        VBox vBox = new VBox();

        Scene scene = new Scene(vBox, 200, 100);

        Stage window = setupWindow("Confirm", scene);

        Label text = new Label("These settings won't apply to the game in progress. What do you want to do?");

        Button restart = new Button("Quit and start a new game with the new settings");
        restart.setOnAction(e -> {
            game.setGameInProgress(false);
            newGame(game);
            window.close();
        });
        Button finish = new Button("Finish the game");
        finish.setOnAction(e -> window.close());

        vBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(text, restart, finish);


        window.showAndWait();
    }

    public static void newGameConfirm(Game game) {
        VBox vBox = new VBox();

        Scene scene = new Scene(vBox, 200, 100);

        Stage window = setupWindow("New Game", scene);


        Label text = new Label("What do you want to do with the game in progress?");

        Button newGame = new Button("Quit and start a new game");
        newGame.setOnAction(e -> {
            game.setGameInProgress(false);
            newGame(game);
            window.close();
        });
        Button restart = new Button("Restart this game");
        restart.setOnAction(e -> {
            game.setGameInProgress(false);
            newGame(game, true);
            window.close();
        });
        Button finish = new Button("Keep Playing");
        finish.setOnAction(e -> window.close());

        vBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(text, newGame, restart, finish);


        window.showAndWait();
    }

    private static Stage setupWindow(String title, Scene scene) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);

        //scene.getStylesheets().clear();
        scene.getStylesheets().add("main/assets/mainStyle.css");

        window.setScene(scene);

        window.setResizable(false);

        return window;
    }
}
