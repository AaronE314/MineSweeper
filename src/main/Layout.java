package main;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.*;

/**
 * Creates a Layout for the game, consisting of a borderPane with a GridPane in the centre, and a MenuHandler at the top.
 *
 * When you create the layout you need to pass in the difficulty of the game as an int[] to determine the amount of rows and columns to make
 * for the grid
 */
public class Layout {

    /**
     * Constants
     */
    public static final String[][] MENU_ITEMS = {{"File", "Help"},
            {"New Game", "Custom Game","Sep","Options","Sep", "Close"},
            {"About", "Help"}};

    /**
     * Attributes
     */
    private BorderPane layout;
    public Panel[][] gridArray;
    private MenuHandler menu;

    /**
     * Constructor
     * @param diff
     *      int[] of the difficulty in the form {length, width, ...rest}
     */
    public Layout(int[] diff) {

        int length = diff[0];
        int height = diff[1];

        layout = new BorderPane();

        //Create grid
        gridArray = new Panel[length][height];
        GridPane grid = createGrid(length, height);

        //Create MenuHandler
        menu = createMenu(MENU_ITEMS);

        layout.setTop(menu);
        layout.setCenter(grid);


    }

    /**
     *Creates a gridPane with the dimensions of length and height, and fills each
     * grid section with a new Panel, and stores that panel in an array of panels.
     *
     * @param col
     *      amount of cols in the grid
     * @param row
     *      amount of rows in the grid
     * @return
     *      the gridPane that was created
     */
    public GridPane createGrid(int col, int row){
        GridPane gp = new GridPane();

        //Set cols
        for (int i = 0; i < col; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.SOMETIMES);
            gp.getColumnConstraints().add(colConstraints);
        }

        //set rows
        for (int i = 0; i < row; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.SOMETIMES);
            gp.getRowConstraints().add(rowConstraints);
        }

        //add panels
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                gridArray[i][j] = new Panel(i,j);
                gp.add(gridArray[i][j], i, j);
            }
        }

        //set the grid to each panel, and calculates the neighbours of the panel
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                gridArray[i][j].setGrid(gridArray);
                gridArray[i][j].addNeighbours();
            }
        }

        gp.setGridLinesVisible(true);
        gp.setAlignment(Pos.CENTER);


        return gp;
    }

    /**
     * Creates a menuBar in the form of a MenuHandler
     * @see MenuHandler
     *
     * @param menuItems
     *      the items in the menu as a 2D string array in the form
     *      {{main1, main2,...},
     *      {main1Sub1, main1Sub2, ...},
     *      {main2Sub1, main2Sub2, ...},
     *       ...}
     *       with the string "sep" for a separator.
     * @return
     *      the created MenuHandler with all the menus added
     */
    public MenuHandler createMenu(String[][] menuItems) {
        MenuHandler mb = new MenuHandler(menuItems);

        return mb;
    }

    /**
     * Creates a new grid overriding the old one.
     *
     * Used for resetting the grid for a new game
     *
     * @param col
     *      number of cols in new grid
     * @param row
     *      number of rows in new grid
     */
    public void newGrid(int col, int row) {

        gridArray = new Panel[col][row];

        layout.setCenter(createGrid(col,row));
    }

    /**
     * getter for the layout
     * @return
     *      the layout attribute
     */
    public BorderPane getLayout() {return layout;}

    /**
     * getter for the grid array
     * @return
     *      the gridArray attribute
     */
    public Panel[][] getGridArray() {return gridArray;}

    /**
     * getter for the menuHandler attribute
     * @return
     *      the menu attribute
     */
    public MenuHandler getMenuHandler() {return menu;}

}