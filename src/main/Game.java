package main;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.Random;

public class Game {

    private boolean done = false;
    private Layout layout;
    private boolean hitBomb = false;

    private boolean firstClick = true;
    private Panel[][] grid;

    private Random rand = new Random();

    private int[] diff;
    private boolean sound;
    private boolean question;
    private Color backColor = Color.LIGHTGREY;

    public Game() {

        getPrefs();

        layout = new Layout(diff);
        layout.getMenuHandler().setGame(this);
        grid = layout.getGridArray();

        newGame(diff);

    }


    public void newGrid(int length, int height) {
        layout.newGrid(length, height);
        layout.getMenuHandler().setGame(this);
        grid = layout.getGridArray();
    }


    public void newGame(int[] diff) {

        getPrefs();

        playSound(Constants.NEW_GAME_SOUND);

        updateBombs(diff[2]);

        done = false;
        firstClick = true;
        hitBomb = false;

        addBombs(diff);

        for (Panel[] panels : grid) {
            for (Panel p : panels) {
                p.setGame(this);
                p.calculateNeighbours();
            }
        }

    }

    public void newGame(int[] diff, boolean reset) {
        if (reset) {
            newGrid(diff[0], diff[1]);
        }
        newGame(diff);
    }

    public void newGame() {
        Panel[][] board = new Panel[diff[0]][diff[1]];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                board[i][j] = grid[i][j];
            }
        }

        newGrid(board.length, board[0].length);

        done = false;
        hitBomb = false;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {

                grid[i][j].setBomb(board[i][j].isBomb());
                grid[i][j].setIsRevealed(false);
                grid[i][j].setIsFlagged(false);
                grid[i][j].setGame(this);
            }
        }

        for (Panel[] panels : grid) {
            for (Panel p : panels) {
                p.calculateNeighbours();
            }
        }

    }

    public void updateBombs(int amount) {
        layout.getMenuHandler().getBombsLeft().setText(String.valueOf(amount));
    }

    public void lowerBombCount(int count) {
        updateBombs(Integer.valueOf(layout.getMenuHandler().getBombsLeft().getText()) + count);
    }

    public void gameOver() {

        done = true;

        revealAll();

        Menus.gameOver(this);
    }

    public void revealAll() {
        for (Panel[] panels : grid) {
            for (Panel panel : panels) {
                panel.reveal();
            }
        }
    }

    public void resetBomb(Panel panel) {
        panel.setBomb(false);

        grid[0][0].setBomb(true);

        for (Panel[] panels : grid) {
            for (Panel p : panels) {
                p.calculateNeighbours();
            }
        }
    }

    public Layout getLayout() {
        return layout;
    }

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



    public boolean checkWin() {

        if (this.hitBomb) {
            return false;
        }
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (!grid[i][j].isRevealed() && !grid[i][j].isBomb()) {
                    return false;
                }
            }
        }

        win();

        return true;

    }

    public void win() {

        done = true;

        playSound(Constants.WIN_SOUND);

        Menus.gameWon(this);
    }

    public boolean getFirstClick() {
        return firstClick;
    }

    public void setFirstClick(boolean click) {
        firstClick = click;
    }

    public void setHitBomb(boolean value) {
        hitBomb = value;
    }

    public boolean isDone() {
        return done;
    }

    public int[] getDiff() {
        return diff;
    }

    public boolean getSound() {
        return sound;
    }
    public boolean getQuestion() {
        return question;
    }
    public Color getBackColor() {
        return backColor;
    }

    public void getPrefs() {

        String[] pref = Prefs.readAll();

        this.backColor = Color.valueOf(pref[0]);
        this.question = Boolean.valueOf(pref[1]);
        this.sound = Boolean.valueOf(pref[2]);
        this.diff = stringToIntArray(pref[3]);

    }

    public void playSound(final String url) {
        if(sound) {
            new Thread(() -> {
                try {
                    Media sound = new Media(new File(url).toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.play();
                } catch (MediaException e) {
                    System.err.println(e.getMessage());
                }
            }).start();
        }
    }

    public static int[] stringToIntArray(String string) {
        String[] temp = string.split(",");

        int[] ints = new int[temp.length];

        for (int i = 0; i < temp.length; i++) {
            ints[i] = Integer.parseInt(temp[i]);
        }

        return ints;
    }

}
