package Model;

import Resources.Constants;
import java.io.Serializable;

/**
 * Diese Klasse speichert die Sieg/Niederlage/Punkte-Infos für einen einzelnen Spieler (SinglePlay-Modus) --> Highscores-Tabelle
 */
/**
 * "implements Serializable", um später diese Infos in einen File zum dauerhaften Speichern schreiben zu können
 */
public class Player_Infos implements Serializable {
    private final String name;
    private int wins;
    private int losts;
    private int point;
    
    /**
     * @param name
     * @param win
     */
    public Player_Infos(String name, boolean win){
        this.name = name;
        if(win){
            wins = 1;
            losts = 0;
        }
        else{
            wins = 0;
            losts = 1;
        }
        point = 0;
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
    public int getWins() {
        return wins;
    }

    /**
     * @param wins
     */
    public void setWins(int wins) {
        this.wins = wins;
    }

    /**
     * @return
     */
    public int getLosts() {
        return losts;
    }

    /**
     * @param losts
     */
    public void setLosts(int losts) {
        this.losts = losts;
    }

    /**
     * @return
     */
    public int getPoint() {
        return point;
    }
    
    /**
     * @param win
     * @param ball_speed
     * @param bot_diffi_ratio
     */
    public void add_Points_attained(boolean win, int ball_speed, double bot_diffi_ratio){
        add_Points_for_a_Win(win);
        add_Bonus_points_for_Ball_speed(ball_speed);
        add_Bonus_points_for_Bot_difficulty(bot_diffi_ratio);
    }
    
    private void add_Points_for_a_Win(boolean win){
        if(win){
            point += 2; // Ein Sieg ergibt 2 Punkte, eine Niederlage dagegen nichts!
        }
    }
    
    /**
     * Egal Sieg oder Niederlage kriegt der Spieler extra Punkte je nach Schwierigkeitsgrad vom Ball. "Easy" Ball ergibt nichts!
     * @param ball_speed 
     */
    private void add_Bonus_points_for_Ball_speed(int ball_speed){
        switch(ball_speed){
            case Constants.BALL_MEDIUM_SPEED:
                point += 1;
                break;
            case Constants.BALL_FAST_SPEED:
                point += 2;
                break;
            case Constants.BALL_SLOW_SPEED : 
                break;
        }
    }
    
    /**
     * Dasselbe Prinzip wie beim "Bonus_points_for_ball"
     * @param bot_diffi_ratio 
     */
    private void add_Bonus_points_for_Bot_difficulty(double bot_diffi_ratio){
        if(bot_diffi_ratio == Constants.BOT_MEDIUM_RATIO){
            point += 1;
        }
        if(bot_diffi_ratio == Constants.BOT_HARD_RATIO){
            point += 2;
        }
    }
}
