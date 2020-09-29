package Model;

import Resources.Constants;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Der Bot für das SinglePlayer Modus
 */
public class Bot {
    /**
     * Bot-Grafik auf der UI-Szene
     */
    private final Rectangle bot_graphic; 
    private int point;
    private int satz;
    /**
     * ist definiert als: k * Speed(Ball) (k: Konstante vom User (Botschwierigkeitsgrad) konfiguriert und vom Database herauszunehmen)
     */
    private double speed; 
    
    /**
     * @param speed
     */
    public Bot(double speed){
        // Bot nimmt die rechte Seite auf dem Spielfeld. Spieler die linke
        bot_graphic = new Rectangle(Constants.GAME_FIELD_WIDTH - Constants.DISTANCE_FROM_PADDLE_TO_EDGE - Constants.PADDLE_WIDTH
                                    , (Constants.GAME_FIELD_HEIGHT - Constants.PADDLE_HEIGHT) / 2
                                    , Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT);
        bot_graphic.setFill(Color.WHITE);
        point = 0;
        satz = 0;
        this.speed = speed;
    }

    /**
     * @return
     */
    public Rectangle getBot_graphic() {
        return bot_graphic;
    }

    /**
     * @return
     */
    public int getPoint() {
        return point;
    }

    /**
     * @param point
     */
    public void setPoint(int point) {
        this.point = point;
    }

    /**
     * @return
     */
    public int getSatz() {
        return satz;
    }

    /**
     * @param satz
     */
    public void setSatz(int satz) {
        this.satz = satz;
    }
    
    /**
     * Der Bot behält die momentane Position des Balls im Auge, wenn der Ball auf seine Hälfte des Feldes eintritt
     * @param ball
     */
    public void move(Ball ball){
        // Der Bot behält die momentane Position des Balls im Auge, wenn der Ball auf seine Hälfte des Feldes eintritt
        if(ball.getBall_graphic().getCenterX() > Constants.GAME_FIELD_WIDTH / 2){
            // Der Bot versucht ständig, auf die momentane "Y" Position des Balls zu kommen, um den Ball quasi abzufangen
            // Die "Y"-Position des Bots ist die Stelle der oberen, linken Ecke der Rechteckgrafik
            if(ball.getBall_graphic().getCenterY() > bot_graphic.getY() + Constants.PADDLE_HEIGHT){
                bot_graphic.setY(bot_graphic.getY() + speed);
            }
            if(ball.getBall_graphic().getCenterY() < bot_graphic.getY()){
                bot_graphic.setY(bot_graphic.getY() - speed);
            }
        }
    }
}
