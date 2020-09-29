package org.openjfx.ea_aufgabe_2_pong;

/**
 *
 * @author Minh Tue
 */
public class SystemInfo {

    /**
     *
     * @return
     */
    public static String javaVersion() {
        return System.getProperty("java.version");
    }

    /**
     *
     * @return
     */
    public static String javafxVersion() {
        return System.getProperty("javafx.version");
    }

}