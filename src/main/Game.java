package main;

import java.util.Random;

public class Game {

    private Layout layout;

    private boolean firstClick = true;
    private Panel[][] grid;

    private Random rand = new Random();

    public Game() {

        layout = new Layout(Constants.EASY);
        grid = layout.getGridArray();

        newGame(Constants.EASY);

    }


    public void newGame(int[] diff) {
        addBombs(diff);

        for(Panel[] panels : grid) {
            for (Panel p : panels) {
                p.setGame(this);
                p.calculateNeighbours();
            }
        }

    }

    public void gameOver() {
        System.out.println("Game over");

        revealAll();
    }

    public void revealAll() {
        for(Panel[] panels : grid) {
            for (Panel panel : panels) {
                panel.reveal();
            }
        }
    }

    public void resetBomb(Panel panel) {
        panel.setBomb(false);

        grid[0][0].setBomb(true);

        for(Panel[] panels : grid) {
            for (Panel p : panels) {
                p.calculateNeighbours();
            }
        }
    }

    public Layout getLayout() { return layout;}

    public void addBombs(int[] diff) {
        for (int i = 0; i < diff[2]; i++) {
            int x = rand.nextInt(diff[0]);
            int y = rand.nextInt(diff[1]);
            while (grid[x][y].isBomb() || (x == 0 && y == 0)) {
                x = rand.nextInt(diff[0]);
                y = rand.nextInt(diff[1]);
            }
            grid[x][y].setBomb(true);
        }
    }

    public void checkWin() {

    }

    public boolean getFirstClick() { return firstClick;}
    public void setFirstClick(boolean click) {firstClick = click;}





}
