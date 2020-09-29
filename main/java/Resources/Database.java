package Resources;

import Model.Player_Infos;
import Utility.InStream_and_OutStream;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.KeyCode;

/**
 * "Database" Klasse speichert wichtige Daten für spätere Referenzen im Laufe des Spiels
 */
public class Database {
    private int ball_speed;
    /**
     * Die Konstante "k" in Gleichung: V(Bot) = k * V(Ball) (V: Geschwindigkeit)
     */
    private double bot_diffi_ratio; 
    /**
     * Für den Spieler im "SinglePlayer" Modus
     */
    private List<Actions> actions; 
    /**
     * Eine Liste von "Player_Infos" (Klasse) für "Highscores" Tabelle im "SinglePlayer" Modus
     */
    private List<Player_Infos> highscores; 
    private KeyCode move_up_key;
    private KeyCode move_down_key;
    private KeyCode action_key;
    // "Settings Multiplayer" wird bisher existierende Einstellungen von "Settings Singleplayer" überschreiben
    // Idee: Beim Multiplayer-Modus wird das Settings-Menü nochmal angezeigt für beide Seiten: Host und Client
    /* Die Einstellungen werden nochmal vorgenommen von 2 Seiten -> und danach die schon in Datenbank zu findenden Einstellungen
    vom Singleplayer überschreiben und die neuen ins gemeinsame Spiel einsetzen!
    */   
    private List<Actions> actions_server; //Für den Host
    private List<Actions> actions_client; //Für den Client
    
    /**
     * Bei "List<Player_Infos> highscores" ruft die Datenbank die Highscores-Tabelle in dem gelagerten File ab, jedes Mal, wenn
        das Spiel angestartet wird
     * @param highscores
     */
    public Database(List<Player_Infos> highscores){
        ball_speed = Constants.BALL_MEDIUM_SPEED; //Als "Default"
        bot_diffi_ratio = Constants.BOT_MEDIUM_RATIO; //Default
        actions = new ArrayList<>(); //"actions" Liste hat Kapazität von 3, mit 1 Voraussetzung: maximal 1 Feuerball
        actions.add(Actions.ACCELERATE); //Als "Default" hat die Liste einmal von jeder Action-Variante 
        actions.add(Actions.CHANGE_ORBIT);
        actions.add(Actions.FIREBALL);
        if(highscores == null){
            // "Highscores" wurde noch nicht erstellt (es existiert noch keine Tabelle in dem gelagerten File)
            this.highscores = new ArrayList<>();
        }
        else{
            this.highscores = highscores;
        }
        move_up_key = KeyCode.W; //Default
        move_down_key = KeyCode.S;
        action_key = KeyCode.J;
    }

    /**
     * @return
     */
    public int getBall_speed() {
        return ball_speed;
    }

    /**
     * @param ball_speed
     */
    public void setBall_speed(int ball_speed) {
        this.ball_speed = ball_speed;
    }

    /**
     * @return
     */
    public double getBot_diffi_ratio() {
        return bot_diffi_ratio;
    }

    /**
     * @param bot_diffi_ratio
     */
    public void setBot_diffi_ratio(double bot_diffi_ratio) {
        this.bot_diffi_ratio = bot_diffi_ratio;
    }

    /**
     * Für den Spieler im "SinglePlayer"! Um Veränderungen an der originellen "Actions"-Liste in Datenbank während des Spielverlaufs 
        zu vermeiden (durch "Call by Reference"), spuckt diese Methode nur eine Kopie der Liste, statt die Liste selber, aus, jedes Mal, 
        wenn sie vom Spieler abgefragt wird
     * @return
     */
    public List<Actions> getActions() {
        List<Actions> list = new ArrayList<>();
        for(Actions action : actions){
            if(action.equals(Actions.ACCELERATE)){
                list.add(Actions.ACCELERATE);
            }
            if(action.equals(Actions.CHANGE_ORBIT)){
                list.add(Actions.CHANGE_ORBIT);
            }
            if(action.equals(Actions.FIREBALL)){
                list.add(Actions.FIREBALL);
            }
        }
        return list;
    }

    /**
     * @param actions
     */
    public void setActions(List<Actions> actions) {
        this.actions = actions;
    }

    /**
     * @return
     */
    public List<Player_Infos> getHighscores() {
        return highscores;
    }

    /**
     * Für den 1. Spieler im "Multiplayer"! Um Veränderungen an der originellen "Actions"-Liste in Datenbank während des Spielverlaufs 
        zu vermeiden (durch "Call by Reference"), spuckt diese Methode nur eine Kopie der Liste, statt die Liste selber, aus, jedes Mal, 
        wenn sie vom Spieler abgefragt wird
     * @return
     */
    public List<Actions> getActions_server() {
        List<Actions> list = new ArrayList<>();
        for(Actions action : actions_server){
            if(action.equals(Actions.ACCELERATE)){
                list.add(Actions.ACCELERATE);
            }
            if(action.equals(Actions.CHANGE_ORBIT)){
                list.add(Actions.CHANGE_ORBIT);
            }
            if(action.equals(Actions.FIREBALL)){
                list.add(Actions.FIREBALL);
            }
        }
        return list;
    }

    /**
     * @param actions_server
     */
    public void setActions_server(List<Actions> actions_server) {
        this.actions_server = actions_server;
    }

    /**
     * Für den 2. Spieler! Um Veränderungen an der originellen "Actions"-Liste in Datenbank während des Spielverlaufs zu vermeiden
        (durch "Call by Reference"), spuckt diese Methode nur eine Kopie der Liste, statt die Liste selber, aus, jedes Mal, 
        wenn sie vom Spieler abgefragt wird
     * @return
     */
    public List<Actions> getActions_client() {
        List<Actions> list = new ArrayList<>();
        for(Actions action : actions_client){
            if(action.equals(Actions.ACCELERATE)){
                list.add(Actions.ACCELERATE);
            }
            if(action.equals(Actions.CHANGE_ORBIT)){
                list.add(Actions.CHANGE_ORBIT);
            }
            if(action.equals(Actions.FIREBALL)){
                list.add(Actions.FIREBALL);
            }
        }
        return list;
    }

    /**
     * @param actions_client
     */
    public void setActions_client(List<Actions> actions_client) {
        this.actions_client = actions_client;
    }

    /**
     * @return
     */
    public KeyCode getMove_up_key() {
        return move_up_key;
    }

    /**
     * @param move_up_key
     */
    public void setMove_up_key(KeyCode move_up_key) {
        this.move_up_key = move_up_key;
    }

    /**
     * @return
     */
    public KeyCode getMove_down_key() {
        return move_down_key;
    }

    /**
     * @param move_down_key
     */
    public void setMove_down_key(KeyCode move_down_key) {
        this.move_down_key = move_down_key;
    }

    /**
     * @return
     */
    public KeyCode getAction_key() {
        return action_key;
    }

    /**
     * @param action_key
     */
    public void setAction_key(KeyCode action_key) {
        this.action_key = action_key;
    }
    
    /**
     * Prüft, ob ein Spieler schon in der "Player_Infos" Liste registriert ist
     * @param name
     * @return
     */
    public boolean player_already_exists(String name){
        boolean a = false;
        for(Player_Infos player : highscores){
            if(player.getName().equals(name)){
                a = true;
                break;
            }
        }
        return a;
    }
    
    /**
     * Aktualisiere das Spieler-profil (SinglePlayer) und dieses in einen File schreiben, zum dauerhaften Speichern
     * @param name
     * @param win
     */
    public void update_and_save_player_infos(String name, boolean win){
        boolean player_already_exists = false;
        for(Player_Infos player : highscores){
            if(player.getName().equals(name)){
                //wenn der Spieler-Profil schon in Datenbank existiert -> aktualisiere sofort diesen
                if(win){
                    player.setWins(player.getWins() + 1);
                    player.add_Points_attained(win, ball_speed, bot_diffi_ratio);
                }
                else{
                    player.setLosts(player.getLosts() + 1);
                    player.add_Points_attained(win, ball_speed, bot_diffi_ratio);
                }
                player_already_exists = true;
                break;
            }
        }
        //Wenn der Spieler-Profil neu ist -> Erzeug ein komplett neues Profil und in Datenbank speichern
        if(!player_already_exists){
            Player_Infos profile = new Player_Infos(name, win);
            profile.add_Points_attained(win, ball_speed, bot_diffi_ratio);
            highscores.add(profile);
        }
        //Diese Veränderung in eine Datei abspeichern
        InStream_and_OutStream.write_Highscores_in_Backup_file(highscores);
    }
}
