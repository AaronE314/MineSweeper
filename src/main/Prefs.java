package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

public class Prefs {

    public static final String[] FIELDS = { "diff" , "sound" , "question" , "backColor"};
    public static final String[] DEFAULTS = { "9,9,10", "true", "true", "LIGHTGREY" };

    public static void write(String[] settings) {
        Properties prop = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream("src/main/assets/prefs.properties");

            for (int i = 0; i < FIELDS.length; i++) {
                prop.setProperty(FIELDS[i], settings[i]);
            }

            prop.store(output, null);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void write() {
        write(DEFAULTS);
    }

    public static String read(String property) {

        Properties prop = new Properties();
        InputStream input = null;

        String value = null;

        String file = "src/main/assets/prefs.properties";

        try {
            input = new FileInputStream(file);

            prop.load(input);

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

    public static String[] readAll(){
        Properties prop = new Properties();
        InputStream input = null;

        ArrayList<String> values = new ArrayList<>();

        String file = "src/main/assets/prefs.properties";

        try {
            input = new FileInputStream(file);

            prop.load(input);

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
