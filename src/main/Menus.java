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

/**
 * abstract class used to create menus for the various popup windows throughout the game.
 */
public abstract class Menus {

    /**
     * Will create a new Game when called
     * @param game
     *      the game object
     * @param same
     *      whether the new game will have the same mine distribution as the current grid.
     */
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

    /**
     * new Game but will always create a new grid.
     * @param game
     *      the game object
     */
    public static void newGame(Game game) {
        newGame(game, false);
    }

    /**
     * Opens a new window to select a difficulty before creating a new game
     * @param game
     *      the game object
     */
    public static void customGame(Game game) {

        //Layouts
        VBox vBox = new VBox();
        VBox diffBox = new VBox();
        VBox customBox = new VBox();
        HBox diffs = new HBox();
        HBox buttons = new HBox();
        HBox heightBox = new HBox();
        HBox widthBox = new HBox();
        HBox mineBox = new HBox();

        //scene and stage
        Scene scene = new Scene(vBox, 400, 200);
        Stage stage = setupWindow("Options", scene);

        //radio group
        ToggleGroup group = new ToggleGroup();

        //Radio buttons for the different difficulties
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


        //Text input fields for custom difficulty
        DigitField length = new DigitField();
        DigitField width = new DigitField();
        DigitField mines = new DigitField();

        //initial values and Listeners for the DigitFields
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


        //Labels for the DigitFields
        Label widthLabel = new Label("Width (9-30): ");
        Label heightLabel = new Label("Height (9-24): ");
        Label minesLabel = new Label("Mines (10-667): ");


        //Determines the default selected radio button based on the saved preferences
        if (Arrays.equals(game.getDiff(), Constants.EASY)) {
            easy.setSelected(true);
        } else if (Arrays.equals(game.getDiff(), Constants.MEDIUM)) {
            medium.setSelected(true);
        } else if (Arrays.equals(game.getDiff(), Constants.HARD)) {
            hard.setSelected(true);
        } else {
            custom.setSelected(true);
        }

        //Buttons for saving, and closing
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

        //adding children
        heightBox.getChildren().addAll(heightLabel, length);
        widthBox.getChildren().addAll(widthLabel, width);
        mineBox.getChildren().addAll(minesLabel, mines);

        buttons.getChildren().addAll(start, close);
        buttons.setAlignment(Pos.CENTER);

        diffBox.getChildren().addAll(easy, medium, hard);
        customBox.getChildren().addAll(custom, heightBox, widthBox, mineBox);
        diffs.getChildren().addAll(diffBox, customBox);

        vBox.getChildren().addAll(diffs, buttons);

        stage.showAndWait();

    }

    /**
     * the window popup for when a gameOver is achieved.
     * @param game
     *      the game object
     */
    public static void gameOver(Game game) {

        //layouts
        VBox vBox = new VBox();
        HBox hBox = new HBox();

        //Labels
        Label label = new Label();
        label.setText("Better Luck Next Time!");

        //Scene and stage
        Scene scene = new Scene(vBox, 400, 100);
        Stage window = setupWindow("Game Over", scene);

        //Listeners for buttons
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

        //alignment and adding Children
        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);

        hBox.getChildren().addAll(newGame, sameGame, viewBoard, close);

        vBox.getChildren().addAll(label, hBox);

        window.showAndWait();

    }

    /**
     * the window popup for when a game is won.
     * @param game
     *      the game object
     */
    public static void gameWon(Game game) {

        //layouts
        VBox vBox = new VBox();
        HBox hBox = new HBox();

        //label
        Label label = new Label();
        label.setText("Congratulations!");

        //scenes and stage
        Scene scene = new Scene(vBox, 400, 100);
        Stage window = setupWindow("Game Won!", scene);

        //listeners
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

        //alignment and adding children
        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);

        hBox.getChildren().addAll(newGame, viewBoard, close);

        vBox.getChildren().addAll(label, hBox);

        window.showAndWait();
    }

    /**
     * a window popup for a help menu
     */
    public static void help() {
        HBox hBox = new HBox();

        Scene scene = new Scene(hBox, 200, 200);

        Stage stage = setupWindow("Help", scene);

        Text text = new Text();

        text.setText("Add instructions here");

        hBox.getChildren().addAll(text);

        stage.showAndWait();
    }

    /**
     * a window popup for an options menu, will save the settings to the prefs.properties file
     * @see Prefs
     *
     * @param game
     *      the game object
     */
    public static void options(Game game) {

        //layouts
        VBox vBox = new VBox();
        VBox diffBox = new VBox();
        VBox customBox = new VBox();
        HBox diffs = new HBox();
        VBox checks = new VBox();
        HBox buttons = new HBox();
        HBox heightBox = new HBox();
        HBox widthBox = new HBox();
        HBox mineBox = new HBox();

        //settings string
        String[] settings = new String[4];

        //scene and stage
        Scene scene = new Scene(vBox, 400, 200);
        Stage stage = setupWindow("Options", scene);

        //radio button group
        ToggleGroup group = new ToggleGroup();

        //radio buttons for difficulty
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


        //digitFields for custom difficulty
        DigitField length = new DigitField();
        DigitField width = new DigitField();
        DigitField mines = new DigitField();

        //setup and listeners for the digitFields
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

        //Labels for digitFields
        Label widthLabel = new Label("Width (9-30): ");
        Label heightLabel = new Label("Height (9-24): ");
        Label minesLabel = new Label("Mines (10-667): ");

        //determine set RadioButton based on prefs
        if (Arrays.equals(game.getDiff(), Constants.EASY)) {
            easy.setSelected(true);
        } else if (Arrays.equals(game.getDiff(), Constants.MEDIUM)) {
            medium.setSelected(true);
        } else if (Arrays.equals(game.getDiff(), Constants.HARD)) {
            hard.setSelected(true);
        } else {
            custom.setSelected(true);
        }

        //setup for checkbox's and colour picker
        CheckBox sound = new CheckBox("Play Sound");
        sound.setSelected(game.getSound());
        CheckBox question = new CheckBox("Use Question Box");
        question.setSelected(game.getQuestion());

        ColorPicker picker = new ColorPicker();
        picker.setValue(game.getBackColor());

        checks.getChildren().addAll(sound, question, picker);

        //Buttons for saving, closing, and resetting options
        Button close = new Button("Close");
        Button save = new Button("Save");
        Button reset = new Button("Restore defaults");


        //Listeners for buttons using prefs
        close.setOnAction(e -> stage.close());
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
        reset.setOnAction(e -> {
            easy.setSelected(true);
            sound.setSelected(true);
            question.setSelected(true);
            picker.setValue(Color.LIGHTGREY);

            Prefs.write();
        });

        //adding children
        heightBox.getChildren().addAll(heightLabel, length);
        widthBox.getChildren().addAll(widthLabel, width);
        mineBox.getChildren().addAll(minesLabel, mines);

        buttons.getChildren().addAll(save, close, reset);
        buttons.setAlignment(Pos.CENTER);

        diffBox.getChildren().addAll(easy, medium, hard);
        customBox.getChildren().addAll(custom, heightBox, widthBox, mineBox);
        diffs.getChildren().addAll(diffBox, customBox);

        vBox.getChildren().addAll(diffs, checks, buttons);

        stage.showAndWait();
    }

    /**
     * a helper function to update the value in the custom difficulty mine DigitField
     * @param mines
     *      the field that is getting updated
     * @param width
     *      the field for the width of the grid
     * @param length
     *      the field for the length of the grid
     */
    private static void updateCustom(DigitField mines, DigitField width, DigitField length) {
        //current value
        int val = Integer.parseInt(mines.getText());
        //maximum value
        int maxVal = (Integer.parseInt(width.getText()) - 1) * (Integer.parseInt(length.getText()) - 1);

        //updates if the value if out of bounds
        if (val < 10) {
            mines.setText("9");
        } else if (val > maxVal) {
            mines.setText(Integer.toString(maxVal));
        }
    }

    /**
     * a window popup for about the creator
     */
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


    /**
     * a window popup to confirm a setting change and asks if the player wants to restart the game to apply the settings
     * @param game
     *      the game object
     */
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

    /**
     * a window popup to confirm whether the player wants to lose there current game to start a new game.
     * @param game
     *      the game object
     */
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

    /**
     * a helper function to do the setup for a new window.
     * @param title
     *      title of the window
     * @param scene
     *      the scene of the window
     * @return
     *      the created window
     */
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
