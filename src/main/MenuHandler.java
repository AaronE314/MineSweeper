package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Menu;

/**
 * Handles creating and managing the menuBar and area just below the menu bar.
 *
 * The Handler is a VBox that contains a menuBar at the top and creates a HBox just below the menuBar.
 *
 */
public class MenuHandler extends VBox implements EventHandler<ActionEvent>{

    /**
     * attributes
     */
    private Game game;
    private Label bombsLeft;

    /**
     * Constructor
     *
     * @param menuItems
     *      a string[][] that holds the menu Item names and layout
     *      @see Layout
     */
    public MenuHandler(String[][] menuItems) {
        super();

        MenuBar menu1 = new MenuBar();

        HBox hBox = new HBox();

        Menu[] menus = new Menu[menuItems[0].length];

        //Creates Top menus
        for(int i = 0; i < menuItems[0].length; i++){
            menus[i] = new Menu(menuItems[0][i]);
        }

        //Creates sub menus
        for(int j = 0; j<menus.length; j++) {
            for (int i = 0; i < menuItems[j+1].length; i++) {
                if (menuItems[j+1][i].equals("Sep")) {
                    menus[j].getItems().add(new SeparatorMenuItem());
                } else {
                    MenuItem menu = new MenuItem(menuItems[j + 1][i]);
                    menu.setOnAction(this);
                    menus[j].getItems().add(menu);
                }
            }
        }

        this.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);

        Label bombText = new Label("Bombs Remaining: ");
        bombsLeft = new Label("0");

        menu1.getMenus().addAll(menus);

        hBox.getChildren().addAll(bombText, bombsLeft);

        this.getChildren().addAll(menu1, hBox);
    }

    /**
     * Handles clicks on the menu items and calls the right function based on the action required
     * @param event
     *      the event that triggered the call
     */
    @Override
    public void handle(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();

        switch (source.getText()) {
            case "New Game":
                Menus.newGame(game);
                break;
            case "Custom Game":
                Menus.customGame(game);
                break;
            case "Options":
                Menus.options(game);
                break;
            case "Close":
                Main.close();
                break;
            case "About":
                Menus.about();
                break;
            case "Help":
                Menus.help();
                break;
            default:
                System.err.println("Menu " + source.getText() + " not handled");
        }
    }

    /**
     * used to set a reference to the current game object
     * @param game
     *      the game object
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * getter for the amount of bombs left that are not flagged.
     * @return
     *      the amount of bombs left as a label
     */
    public Label getBombsLeft() {return bombsLeft;}
}































