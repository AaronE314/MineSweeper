package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import static main.Constants.*;

/**
 * an abstract class that will read to and write from a .properties file.
 */
public abstract class Prefs {

    /**
     * given a string of setting will write to the preference file
     *
     * @param settings
     *      a string array of the settings to write in order {difficulty, sound, question, background Color}
     */
    public static void write(String[] settings) {
        Properties prop = new Properties();
        OutputStream output = null;
        File file = null;

        try {

//            output = new FileOutputStream(PREFS_FILE_STRING);

            file = new File(System.getProperty("user.home"), PREFS_FILE_STRING);
            file.getParentFile().mkdir();


            for (int i = 0; i < FIELDS.length; i++) {
                prop.setProperty(FIELDS[i], settings[i]);
            }

//            prop.store(output, null);
            prop.store(new FileOutputStream(file), null);

        } catch (IOException e) {
            e.printStackTrace();
        }
//        finally {
//            if (file != null){//output != null) {
//                try {
//                    output.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    /**
     * will write the default settings to the prefs file
     */
    public static void write() {
        write(DEFAULTS);
    }

    /**
     * will read a specific property from the prefs file
     *
     * @param property
     *      a string of the name of the property
     * @return
     *      the string representation of the data at the property
     */
    public static String read(String property) {

        Properties prop = new Properties();
        InputStream input = null;

        String value = null;

        try {
            input = Constants.getResourceStream(PREFS_FILE);

            prop.load(input);

            prop = new Properties(prop);
            File file = new File(System.getProperty("user.home"), PREFS_FILE_STRING);

            if (file.isFile()) {
                prop.load(new FileInputStream(file));
            }


            value = prop.getProperty(property);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return value;
    }

    /**
     * will return all of the data in the Prefs file as a string array
     *
     * @return
     *      a string[] will all the settings
     */
    public static String[] readAll(){

        Properties prop = new Properties();
        InputStream input = null;

        ArrayList<String> values = new ArrayList<>();

        try {
            input = Constants.getResourceStream(PREFS_FILE);

            prop.load(input);

            prop = new Properties(prop);
            File file = new File(System.getProperty("user.home"), PREFS_FILE_STRING);

            if (file.isFile()) {
                prop.load(new FileInputStream(file));
            }

            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                values.add(prop.getProperty(key));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return values.toArray(new String[0]);
    }
}
