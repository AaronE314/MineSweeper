package main;

import javafx.geometry.Pos;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.*;

public class Layout {

    public static final String[][] MENU_ITEMS = {{"File", "Help"},
            {"New Game", "Custom Game","Sep","Options","Sep", "Close"},
            {"About", "Help"}};

    private BorderPane layout;
    private GridPane grid;
    public Panel[][] gridArray;
    private MenuHandler menu;
    private int length;
    private int height;

    public Layout(int[] diff) {

        this.length = diff[0];
        this.height = diff[1];

        layout = new BorderPane();

        //Create grid
        gridArray = new Panel[length][height];
        grid = createGrid(length,height);

        //Create MenuHandler
        menu = createMenu(MENU_ITEMS);

        layout.setTop(menu);
        layout.setCenter(grid);

    }

    public GridPane createGrid(int length, int height){
        GridPane gp = new GridPane();
        for (int i = 0; i < length; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.SOMETIMES);
            gp.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0; i < height; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.SOMETIMES);
            gp.getRowConstraints().add(rowConstraints);
        }

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j++) {
                gridArray[i][j] = new Panel(i,j);
                gp.add(gridArray[i][j], i, j);
            }
        }

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j++) {
                gridArray[i][j].setGrid(gridArray);
                gridArray[i][j].addNeighbours();
            }
        }

        gp.setGridLinesVisible(true);
        gp.setAlignment(Pos.CENTER);


        return gp;
    }

    public MenuHandler createMenu(String[][] menuItems) {
        MenuHandler mb = new MenuHandler(menuItems);

        return mb;
    }

    public void newGrid(int length, int height) {

        gridArray = new Panel[length][height];

        layout.setCenter(createGrid(length,height));
    }

    public BorderPane getLayout() {return layout;}
    public Panel[][] getGridArray() {return gridArray;}
    public MenuHandler getMenuHandler() {return menu;}

}