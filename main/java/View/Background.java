package View;

import Resources.Constants;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Hintergrund für die UI-Szene
 */
public class Background extends Canvas {
    /**
     * Standardhintergrund
     */
    public Background(){
        super(Constants.GAME_FIELD_WIDTH, Constants.GAME_FIELD_HEIGHT);
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, Constants.GAME_FIELD_WIDTH, Constants.GAME_FIELD_HEIGHT);
    } 
    
    /**
     * Spielfeld Hintergrund
     * @param middle_line_width
     */
    public Background(double middle_line_width){
        super(Constants.GAME_FIELD_WIDTH, Constants.GAME_FIELD_HEIGHT);
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, Constants.GAME_FIELD_WIDTH, Constants.GAME_FIELD_HEIGHT);
        gc.setFill(Color.WHITE);
        gc.fillRect(Constants.GAME_FIELD_WIDTH/2 - middle_line_width/2, 0, middle_line_width, Constants.GAME_FIELD_HEIGHT);
    } 
    
    /**
     * Benutzerdefiniert!
     * @param width
     * @param height
     * @param color
     */
    public Background(double width, double height, Color color){
        super(width, height);
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(color);
        gc.fillRect(0, 0, width, height);
    } 
}
