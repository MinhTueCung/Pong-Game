package Utility;

import java.util.Random;

/**
 * Zum Berechnen eines zufälligen Winkels für die Ballrichtung!
 *  
 * Wir nehmen keinen "flachen" Winkel für die Ballrichtung, nur diagonal, sonst wird das Spiel sehr langweilig!
 */
public class Random_Angle {
    /**
     * Rekursive Methode! Wenn flacher Winkel -> noch mal rekursiv die Methode durchlaufen lassen
     * @return
     */
    public static int between_0_and_360_without_flat_angles(){
        int random_angle = new Random().nextInt(361);
        if(angle_in_interval_0_to_20(random_angle) || angle_in_interval_70_to_110(random_angle) 
                || angle_in_interval_160_to_200(random_angle) || angle_in_interval_250_to_290(random_angle)
                || angle_in_interval_340_to_360(random_angle)){
            return between_0_and_360_without_flat_angles();
        }
        return random_angle;
    }
    
    /**
     * Für "Change Orbit" Action von dem Spieler linker Seite des Feldes (Spieler 1). Dasselbe Prinzip: kein flacher Winkel
     * @return
     */
    public static int between_minus_90_and_90_without_flat_angles(){
        int random_angle = new Random().nextInt(181) - 90;
        if(angle_in_interval_minus_90_to_minus_70(random_angle) || angle_in_interval_minus_20_to_0(random_angle)
                || angle_in_interval_0_to_20(random_angle) || angle_in_interval_70_to_110(random_angle)){
            return between_minus_90_and_90_without_flat_angles();
        }
        return random_angle;
    }
    
    /**
     * Für "Change Orbit" Action von dem Spieler rechter Seite des Feldes (Spieler 2). Dasselbe Prinzip: kein flacher Winkel
     * @return
     */
    public static int between_90_and_270_without_flat_angles(){
        int random_angle = new Random().nextInt(181) + 90;
        if(angle_in_interval_70_to_110(random_angle) || angle_in_interval_160_to_200(random_angle) 
                || angle_in_interval_250_to_290(random_angle)){
            return between_90_and_270_without_flat_angles();
        }
        return random_angle;
    }
    
    private static boolean angle_in_interval_0_to_20(int random_angle){
        return random_angle >= 0 && random_angle <= 20;
    }
    
    private static boolean angle_in_interval_70_to_110(int random_angle){
        return random_angle >= 70 && random_angle <= 110;
    }
    
    private static boolean angle_in_interval_160_to_200(int random_angle){
        return random_angle >= 160 && random_angle <= 200;
    }
    
    private static boolean angle_in_interval_250_to_290(int random_angle){
        return random_angle >= 250 && random_angle <= 290;
    }
    
    private static boolean angle_in_interval_340_to_360(int random_angle){
        return random_angle >= 340 && random_angle <= 360;
    }
    
    private static boolean angle_in_interval_minus_90_to_minus_70(int random_angle){
        return random_angle >= -90 && random_angle <= -70;
    }
    
    private static boolean angle_in_interval_minus_20_to_0(int random_angle){
        return random_angle >= -20 && random_angle <= 0;
    }
}
