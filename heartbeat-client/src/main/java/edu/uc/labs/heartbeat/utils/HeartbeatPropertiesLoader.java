/*
 * This class has the simple job of
 * finding the properties files and 
 * loading them
 */
package edu.uc.labs.heartbeat.utils;

import com.typesafe.config.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


/**
 * @author silbermm
 */
public class HeartbeatPropertiesLoader {


    private HeartbeatPropertiesLoader() {
    }

    public static Properties getProperties(Config config) throws IOException {
        String propFile = config.getString("heartbeat.installDir") + "/" + config.getString("heartbeat.propfile");
        Properties props = new Properties();
        try {
            File f = new File(propFile);
            FileInputStream fis = new FileInputStream(f);
            props.load(fis);
            fis.close();
        } catch (FileNotFoundException ex) {
            // unable to find the file
            return null;
        }
        return props;

    }

    public static String getFilename(Config config) {
        return config.getString("heartbeat.installDir") + "/" + config.getString("heartbeat.propfile");
    }

    public static boolean fileExists(Config config) {
        String propFile = config.getString("heartbeat.installDir") + "/" + config.getString("heartbeat.propfile");
        File f = new File(propFile);
        return f.exists();
    }

}
