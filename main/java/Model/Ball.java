package Model;

import Utility.Random_Angle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import Resources.Constants;

/**
 * Der Ball für das Spiel
 */
public class Ball {
    /**
     * die Kreis-Grafik auf der UI-Szene
     */
    private final Circle ball_graphic; 
    private int speed;
    /**
     * "direction_einheit" ist ein Vektor, der die Bewegungsrichtung darstellt und immer die Länge von 1 hat ("Einheit")
     */
    private double direction_einheit_X; 
    private double direction_einheit_Y;
    
    /**
     * @param speed
     */
    public Ball(int speed){
        // Der Ball nimmt am Anfang die Mitteposition des Feldes  
        ball_graphic = new Circle(Constants.GAME_FIELD_WIDTH/2, Constants.GAME_FIELD_HEIGHT/2, Constants.BALL_RADIUS);
        ball_graphic.setFill(Color.WHITE);
        this.speed = speed;
        // Ziel: Jedes Mal, wenn der Ball "reset" wird, bekommt er eine zufällige Bewegungsrichtung
        // Die Richtung sollte nicht flache Richtung sein (0°, 90°, 180°, 360°), sonst wird das Spiel sehr langweilig oder man gewinnt nie!
        int random_angle_for_direction_einheit = Random_Angle.between_0_and_360_without_flat_angles();
        // wir haben nun nen Winkel + Länge für "direction_einheit" (Polardarstellung) -> Ziel: in kartesische Darstellung umwandeln
        direction_einheit_X = Math.cos((Math.PI * random_angle_for_direction_einheit) / 180);
        // Minus "sin", weil die positive Y-Achse der UI-Szene die Gegenrichtung zu der herkömmlichen positiven Sinus-Richtung hat
        direction_einheit_Y = - Math.sin((Math.PI * random_angle_for_direction_einheit) / 180);
    }
    
    /**
     */
    public void move(){
        // "direction_einheit" entscheidet die Richtung, "speed" entscheidet die Länge der zurückgelegten Strecke in einem Durchlauf des Game-Loops
        ball_graphic.setCenterX(ball_graphic.getCenterX() + (direction_einheit_X * speed));
        ball_graphic.setCenterY(ball_graphic.getCenterY() + (direction_einheit_Y * speed));
    }

    /**
     * @return
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * @param speed
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * @return
     */
    public Circle getBall_graphic() {
        return ball_graphic;
    }

    /**
     * @return
     */
    public double getDirection_einheit_X() {
        return direction_einheit_X;
    }

    /**
     * @param direction_einheit_X
     */
    public void setDirection_einheit_X(double direction_einheit_X) {
        this.direction_einheit_X = direction_einheit_X;
    }

    /**
     * @return
     */
    public double getDirection_einheit_Y() {
        return direction_einheit_Y;
    }

    /**
     * @param direction_einheit_Y
     */
    public void setDirection_einheit_Y(double direction_einheit_Y) {
        this.direction_einheit_Y = direction_einheit_Y;
    }
    
    /**
     * @param speed
     */
    public void reset(int speed){
        ball_graphic.setCenterX(Constants.GAME_FIELD_WIDTH / 2);
        ball_graphic.setCenterY(Constants.GAME_FIELD_HEIGHT / 2);
        this.speed = speed;
        int random_angle_for_direction_einheit = Random_Angle.between_0_and_360_without_flat_angles();
        direction_einheit_X = Math.cos((Math.PI * random_angle_for_direction_einheit) / 180);
        direction_einheit_Y = - Math.sin((Math.PI * random_angle_for_direction_einheit) / 180);
        ball_graphic.setFill(Color.WHITE);
        // wie im Konstruktor, mit "speed" vom "Database" rauszunehmen 
    }
}
