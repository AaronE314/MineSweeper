package main;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * The main game controller.
 * Controls and manages most of the attributes of the game.
 */
public class Game {

    /**
     * attributes
     */
    private boolean done = false;
    private Layout layout;
    private boolean hitBomb = false;

    private boolean firstClick = true;
    private Panel[][] grid;

    private boolean gameInProgress = false;

    private Random rand = new Random();

    private int[] diff;
    private boolean sound;
    private boolean question;
    private Color backColor = Color.LIGHTGREY;

    /**
     * Constructor
     */
    public Game() {

        getPrefs();

        layout = new Layout(diff);
        layout.getMenuHandler().setGame(this);
        grid = layout.getGridArray();

        newGame(diff);


    }


    /**
     * Creates a new grid given a length and height for thr new grid
     * @param length
     *      the length of the new grid
     * @param height
     *      the height of the new grid
     */
    public void newGrid(int length, int height) {
        layout.newGrid(length, height);
        layout.getMenuHandler().setGame(this);
        grid = layout.getGridArray();
    }


    /**
     * Creates a new game given a difficulty
     * @param diff
     *      an int[] containing the length, width, and mine count
     */
    public void newGame(int[] diff) {

        Main.changeSize(diff[0],diff[1]);

        getPrefs();

        playSound(Constants.NEW_GAME_SOUND);

        updateBombs(diff[2]);

        done = false;
        firstClick = true;
        hitBomb = false;
        gameInProgress = false;

        addBombs(diff);

        for (Panel[] panels : grid) {
            for (Panel p : panels) {
                p.setGame(this);
                p.calculateNeighbours();
            }
        }

    }

    /**
     * Creates a new game given difficulty and a boolean to reset the grid (ie change the size)
     * @param diff
     *      an int[] containing the length, width, and mine count
     * @param reset
     *      true to reset, false otherwise
     */
    public void newGame(int[] diff, boolean reset) {
        if (reset) {
            newGrid(diff[0], diff[1]);
        }
        newGame(diff);
    }

    /**
     * Creates a new game with the exact same grid as before.
     */
    public void newGame() {
        Panel[][] board = new Panel[diff[0]][diff[1]];

        playSound(Constants.NEW_GAME_SOUND);

        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, board[i], 0, grid[0].length);
        }

        newGrid(board.length, board[0].length);

        done = false;
        hitBomb = false;
        gameInProgress = false;

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

    /**
     * updates the bomb count label
     * @param amount
     *      value to set the count too
     */
    public void updateBombs(int amount) {
        layout.getMenuHandler().getBombsLeft().setText(String.valueOf(amount));
    }

    /**
     * increase or decrease the count on the bomb label
     * @param count
     *      the amount to increase or decrease by
     */
    public void lowerBombCount(int count) {
        updateBombs(Integer.valueOf(layout.getMenuHandler().getBombsLeft().getText()) + count);
    }

    /**
     * Used for when a game is lost, will reveal everything and open a gameOver window.
     */
    public void gameOver() {

        done = true;
        gameInProgress = false;

        revealAll();

        Menus.gameOver(this);
    }

    /**
     * will reveal every tile in the grid.
     */
    public void revealAll() {
        for (Panel[] panels : grid) {
            for (Panel panel : panels) {
                if (panel.isBomb()) {
                    panel.reveal();
                }
            }
        }
    }

    /**
     * moves a bomb from the given location to the top right corner of the grid (0,0).
     *
     * @param panel
     *      the panel that is getting changed
     */
    public void resetBomb(Panel panel) {
        panel.setBomb(false);

        grid[0][0].setBomb(true);

        for (Panel[] panels : grid) {
            for (Panel p : panels) {
                p.calculateNeighbours();
            }
        }
    }

    /**
     * getter for the layout
     *
     * @return
     *      the layout object
     */
    public Layout getLayout() {
        return layout;
    }

    /**
     * adds bombs to random tiles, with the amount added equal to the given difficulty
     *
     * @param diff
     *      an int[] containing the length, width, and mine count
     */
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


    /**
     * Checks to see if the game has been won
     *
     * @return
     *      will return true if game is won, false otherwise.
     */
    public boolean checkWin() {

        if (this.hitBomb) {
            return false;
        }
        for (Panel[] aGrid : grid) {
            for (Panel anAGrid : aGrid) {
                if (!anAGrid.isRevealed() && !anAGrid.isBomb()) {
                    return false;
                }
            }
        }

        win();

        return true;

    }

    /**
     * used for when a game is won.
     * will set the proper values, then opens a Win window.
     */
    public void win() {

        done = true;
        gameInProgress = false;

        playSound(Constants.WIN_SOUND);

        Menus.gameWon(this);
    }

    /**
     * will return whether or not a a first click has happened
     *
     * @return
     *      true if not clicked yet, flase otherwise.
     */
    public boolean getFirstClick() {
        return firstClick;
    }

    /**
     * sets the first click to the given boolean
     * @param click
     *      the new click value
     */
    public void setFirstClick(boolean click) {
        firstClick = click;
    }

    /**
     * sets the Hit bomb to the current boolean
     *
     * @param value
     *      the new hitBomb value
     */
    public void setHitBomb(boolean value) {
        hitBomb = value;
    }

    /**
     * gets whether a bomb has been hit or not
     * @return
     *      true is a bomb has been hit, false otherwise
     */
    public boolean getHitBomb() {
        return hitBomb;
    }

    /**
     * determines if a game if done.
     * @return
     *      true if game is done, false otherwise
     */
    public boolean isDone() {
        return done;
    }

    /**
     * getts the difficulty of the game.
     * @return
     *      an int[] with, {length, width, mineCount}
     */
    public int[] getDiff() {
        return diff;
    }

    /**
     * gets whether or not sound it to be played.
     * @return
     *      true for sound, false otherwise
     */
    public boolean getSound() {
        return sound;
    }

    /**
     * gets whether the question box will be used
     * @return
     *      true to use it, false otherwise
     */
    public boolean getQuestion() {
        return question;
    }

    /**
     * gets the background colour to use for the panels
     * @return
     *      the colour
     */
    public Color getBackColor() {
        return backColor;
    }

    /**
     * gets whether the game is in progress
     * @return
     *      true if it is in progress, false otherwise
     */
    public boolean isGameInProgress() {
        return gameInProgress;
    }

    /**
     * sets whether the game is in progress
     * @param game
     *      the new value
     */
    public void setGameInProgress(boolean game) {
        gameInProgress = game;
    }

    /**
     * gets and setts the preferences from prefs.properties
     * @see Prefs
     */
    public void getPrefs() {

        String[] pref = Prefs.readAll();

        this.backColor = Color.valueOf(pref[0]);
        this.question = Boolean.valueOf(pref[1]);
        this.sound = Boolean.valueOf(pref[2]);
        this.diff = stringToIntArray(pref[3]);


        if (grid != null) {
            for (Panel[] panels : grid) {
                for (Panel panel : panels) {
                    panel.update();
                }
            }
        }

    }

    /**
     * plays the sound at the given url in a new thread.
     *
     * @param url
     *      the location of the sound.
     */
    public void playSound(final String url) {
        if(sound) {
            new Thread(() -> {
                try {
//                    Media sound = new Media(new File(url).toURI().toString());
//                    MediaPlayer mediaPlayer = new MediaPlayer(sound);
//                    mediaPlayer.play();
                    InputStream in = Constants.getResourceStream(url);
                    AudioStream as = new AudioStream(in);
                    AudioPlayer.player.start(as);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }).start();
        }
    }

    /**
     * a helper function that will convert a string to an int[]
     *
     * @param string
     *      the string to convert in form "length,width,mineCount"
     * @return
     *      an int[] in for {length, width, mineCount}
     */
    public static int[] stringToIntArray(String string) {
        String[] temp = string.split(",");

        int[] ints = new int[temp.length];

        for (int i = 0; i < temp.length; i++) {
            ints[i] = Integer.parseInt(temp[i]);
        }

        return ints;
    }

}
