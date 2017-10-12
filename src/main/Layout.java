package main;

import javafx.geometry.Pos;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;

public class Layout {

    public static final String[][] MENU_ITEMS = {{"File", "Help"},
            {"New Game", "Custom Game", "Close"},
            {"About", "Help"}};

    private BorderPane layout;
    private GridPane grid;
    public Panel[][] gridArray;
    private MenuBar menu;
    private int length;
    private int height;

    public Layout(int[] diff) {

        this.length = diff[0];
        this.height = diff[1];

        layout = new BorderPane();

        //Create grid
        gridArray = new Panel[length][height];
        grid = createGrid(length,height);

        //Create Menu
        menu = createMenu(MENU_ITEMS);


        grid.setGridLinesVisible(true);

        grid.setAlignment(Pos.CENTER);

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


        return gp;
    }

    public MenuBar createMenu(String[][] menuItems) {
        MenuBar mb = new MenuBar();

        Menu[] menus = new Menu[menuItems[0].length];


        for(int i = 0; i < menuItems[0].length; i++){
            menus[i] = new Menu(menuItems[0][i]);
        }

        for(int j = 0; j<menus.length; j++) {
            for (int i = 0; i < menuItems[j+1].length; i++) {
                menus[j].getItems().add(new MenuItem(menuItems[j+1][i]));
            }
        }

        mb.getMenus().addAll(menus);

        return mb;
    }

    public BorderPane getLayout() {return layout;}
    public GridPane getGrid() {return grid;}
    public Panel[][] getGridArray() {return gridArray;}
    public MenuBar getMenu() {return menu;}

}