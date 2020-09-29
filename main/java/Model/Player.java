package Model;

import Resources.Actions;
import Resources.Constants;
import java.util.List;
import javafx.scene.shape.Rectangle;

/**
 * Der Spieler: 1 und 2!
 */
public class Player {
    /**
     * Rechteckgrafik auf der UI-Szene
     */
    private final Rectangle player_graphic;
    private int point;
    private int satz;
    private final String name;
    private List<Actions> actions; // Jeder Spieler hat eine Actions-Liste (spezielle Moves)
    
    /**
     * "Player" könnte entweder Spieler 1 (linke Seite) oder Spieler 2 (rechte Seite) -> beim Instanziieren "coord_X" angeben!
     * @param coord_X
     * @param name
     * @param actions
     */
    public Player(double coord_X, String name, List<Actions> actions){
        // "Player" könnte entweder Spieler 1 (linke Seite) oder Spieler 2 (rechte Seite) -> beim Instanziieren "coord_X" angeben!
        player_graphic = new Rectangle(coord_X, (Constants.GAME_FIELD_HEIGHT - Constants.PADDLE_HEIGHT) / 2
                                    , Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT);
        player_graphic.setFill(Actions.DEFAULT.getColor());
        point = 0;
        satz = 0;
        this.name = name;
        this.actions = actions;
    }

    /**
     * @return
     */
    public Rectangle getPlayer_graphic() {
        return player_graphic;
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
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public List<Actions> getActions() {
        return actions;
    }

    /**
     * @param actions
     */
    public void setActions(List<Actions> actions) {
        this.actions = actions;
    }   
}
