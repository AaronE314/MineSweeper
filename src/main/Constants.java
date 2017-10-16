package main;

import javafx.scene.paint.Color;

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
    public static final String NEW_GAME_SOUND =  "src/main/assets/sounds/New_Game.wav";
    public static final String CLICK_SOUND =  "src/main/assets/sounds/Click.wav";
    public static final String LONG_BOMB_SOUND =  "src/main/assets/sounds/Long_Bomb.wav";
    public static final String WIN_SOUND =  "src/main/assets/sounds/Win.wav";


    /**
     * Locations of images
     */
    public static final String FLAG_IMG = "file:src/main/assets/img/flag.png";
    public static final String MINE_IMG = "file:src/main/assets/img/mine.png";
    public static final String QUESTION_IMG = "file:src/main/assets/img/question.png";

    /**
     * constant for the colour of numbers on panels
     */
    public static final Color[] TEXT_COLOUR = {Color.BLACK, Color.BLUE, Color.rgb(21, 148, 8), Color.RED, Color.rgb(0, 0, 100), Color.rgb(165, 102, 21), Color.rgb(0, 185, 185), Color.BLACK, Color.GRAY,};

    /**
     * file location of the properties file
     */
    public static final String PREFS_FILE = "src/main/assets/prefs.properties";

    /**
     * properties fields
     */
    public static final String[] FIELDS = { "diff" , "sound" , "question" , "backColor"};

    /**
     * default property values
     */
    public static final String[] DEFAULTS = { "9,9,10", "true", "true", "LIGHTGREY" };

}
