package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MenuHandler extends VBox implements EventHandler<ActionEvent>{


    private Game game;

    private MenuBar menu;

    private Label bombsLeft, time;

    public MenuHandler(String[][] menuItems) {
        super();

        menu = new MenuBar();

        HBox hBox = new HBox();

        Menu[] menus = new javafx.scene.control.Menu[menuItems[0].length];


        for(int i = 0; i < menuItems[0].length; i++){
            menus[i] = new javafx.scene.control.Menu(menuItems[0][i]);
        }

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

        menu.getMenus().addAll(menus);

        hBox.getChildren().addAll(bombText, bombsLeft);

        this.getChildren().addAll(menu, hBox);
    }

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

    public void setGame(Game game) {
        this.game = game;
    }


    public Label getBombsLeft() {return bombsLeft;}

    public Label getTime() {return time;}
}































