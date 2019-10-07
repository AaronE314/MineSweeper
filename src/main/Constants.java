package main;

import javafx.scene.paint.Color;

import java.io.InputStream;
import java.io.OutputStream;

abstract class Constants {


    /**
     * Difficulty constants stored as arrays
     */
    public static final int[] EASY = {9,9,10};
    public static final int[] MEDIUM = {16,16,40};
    public static final int[] HARD = {16,30,99};

    /**
     * Difficulty constants stored as strings
     */
    public static final String EASYS = "9,9,10";
    public static final String MEDIUMS = "16,16,40";
    public static final String HARDS = "30,16,99";


    /**
     * Locations for sound files
     */
    public static final String NEW_GAME_SOUND = "/sounds/New_Game.wav";
    public static final String CLICK_SOUND =  "/sounds/Click.wav";
    public static final String LONG_BOMB_SOUND = "/sounds/Long_Bomb.wav";
    public static final String WIN_SOUND = "/sounds/Win.wav";


    /**
     * Locations of images
     */
    public static final String FLAG_IMG = getResource("/img/flag.png");
    public static final String MINE_IMG = getResource("/img/mine.png");
    public static final String QUESTION_IMG = getResource("/img/question.png");

    /**
     * constant for the colour of numbers on panels
     */
    public static final Color[] TEXT_COLOUR = {Color.BLACK, Color.BLUE, Color.rgb(21, 148, 8), Color.RED, Color.rgb(0, 0, 100), Color.rgb(165, 102, 21), Color.rgb(0, 185, 185), Color.BLACK, Color.GRAY,};

    /**
     * file location of the properties file
     */
    public static final String PREFS_FILE_STRING = "minesweeper/user_prefs.properties";
    public static final String PREFS_FILE = "/prefs.properties";
    /**
     * properties fields
     */
    public static final String[] FIELDS = { "diff" , "sound" , "question" , "backColor"};

    /**
     * default property values
     */
    public static final String[] DEFAULTS = { "9,9,10", "true", "true", "LIGHTGREY" };

    private static String getResource(String file) {
        System.getProperty("user.dir");
        return Constants.class.getResource(file).toString();
    }

    public static InputStream getResourceStream(String file) {
        System.getProperty("user.dir");
        return Constants.class.getResourceAsStream(file);
    }


}
