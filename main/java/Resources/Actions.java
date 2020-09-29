package Resources;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Eine Auflistung der Actions (spezielle Moves)!
 * 
    DEFAULT: Nichts passiert
    ACCELERATE: die Geschwindigkeit des Balls verdoppeln 
    CHANGE_ORBIT: dem Ball eine neue, zufällige Bewegungsrichtung zuordnen
    FIREBALL: Ball mit Feuer durchschlägt den Paddle des Gegners
 */
public enum Actions {

    /**
     */
    DEFAULT(Color.WHITE),

    /**
     */
    ACCELERATE(Color.BLUE),

    /**
     */
    CHANGE_ORBIT(Color.YELLOW),

    /**
     */
    FIREBALL(Color.FIREBRICK);
    
    private final Color color;
    
    private Actions(Color color){
        this.color = color;
    }
    
    /**
     * @return
     */
    public Color getColor(){
        return color;
    }
    
    /**
     * @return
     */
    public char getName_of_the_Color(){
        char color_name = 'x';
        if(color.equals(Color.WHITE)){
            color_name = 'w';
        }
        if(color.equals(Color.BLUE)){
            color_name = 'b';
        }
        if(color.equals(Color.YELLOW)){
            color_name = 'y';
        }
        if(color.equals(Color.FIREBRICK)){
            color_name = 'f';
        }
        return color_name;
    }
    
    /**
     * @return
     */
    public Circle getGraphic(){
        return new Circle(Constants.ACTIONS_SYMBOL_RADIUS, color);
    }
}
