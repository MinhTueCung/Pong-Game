package View;

import Model.Bot;
import Model.Player;
import Resources.Actions;
import Resources.Constants;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Diese Klasse hat den Zugriff auf die UI-Komponente (Punkte, Satz, Nachrichten,...) auf der UI-Szene, zur besseren Kontrolle.
 * 
 * Im "Singleplayer" Modus, wenn der Spieler z.B einen Punkt kriegt, werden 2 separate Aktionen ausgeführt
 * 
 * 1. Aktualisiere Punkt-Attribut vom Spieler im Hintergrund (Player.getPoint()). Dies wird vom Controller ("Referee" Klasse) ausgeführt!
 * 
 * 2. Aktualisiere Punkt-Darstellung auf der Szene. Dies wird vom Grap_Viewer (Diese Klasse) ausgeführt!
 * 
 * 
 * Im "Multiplayer"-Modus, wenn ein Spieler einen Punkt kriegt, 
 * 
 * 1. Der "Referee" vom Server aktualisiert das "Point"-Attribut dieses Spielers
 * 
 * 2. Die Punkt-Darstellungen auf der UI (Server) werden durch "grap_viewer.update_point_satz_Multiplayer()" aktualisiert
 * 
 * 3. Der Server teilt dem Client diese Aktualisierung mit
 * 
 * 4. Der Client empfängt Nachricht und aktualisiert das "Point"-Attribut 
 * 
 * 5. Auf der UI-Szene (Client) werden Punkt-Darstellungen immer auf dem neuesten Stand gehalten, durch "grap_viewer.update_point_satz_Multiplayer()"
   (siehe"Game_Thread_Client")
 */
public class Game_Graphic_Components {
    private Player player_1;
    private Label player_1_point;
    private Label player_1_satz;
    private HBox player_1_actions_box;
    private Bot bot;
    private Label bot_point;
    private Label bot_satz;
    private Player player_2;
    private Label player_2_point;
    private Label player_2_satz;
    private HBox player_2_actions_box;
    private Label who_wins_message;
    private Label push_enter_to_start;
    private Label paused_message;
    
    /**
     * SinglePlayer
     * @param player_1
     * @param bot
     */
    public Game_Graphic_Components(Player player_1, Bot bot){
        this.player_1 = player_1;
        setup_player_1_components();
        this.bot = bot;
        setup_bot_components();
        setup_other_components();
    } 
    
    /**
     * Multiplayer
     * @param player_1
     * @param player_2
     */
    public Game_Graphic_Components(Player player_1, Player player_2){
        this.player_1 = player_1;
        setup_player_1_components();
        this.player_2 = player_2;
        setup_player_2_components();
        setup_other_components();
    } 

    /**
     *
     * @return
     */
    public Label getPlayer_1_point() {
        return player_1_point;
    }

    /**
     *
     * @return
     */
    public Label getPlayer_1_satz() {
        return player_1_satz;
    }

    /**
     *
     * @return
     */
    public HBox getPlayer_1_actions_box() {
        return player_1_actions_box;
    }

    /**
     *
     * @return
     */
    public Label getBot_point() {
        return bot_point;
    }

    /**
     *
     * @return
     */
    public Label getBot_satz() {
        return bot_satz;
    }

    /**
     *
     * @return
     */
    public Label getPlayer_2_point() {
        return player_2_point;
    }

    /**
     *
     * @return
     */
    public Label getPlayer_2_satz() {
        return player_2_satz;
    }
    
    /**
     *
     * @return
     */
    public HBox getPlayer_2_actions_box() {
        return player_2_actions_box;
    }

    /**
     *
     * @return
     */
    public Label getPush_enter_to_start() {
        return push_enter_to_start;
    }

    /**
     *
     * @return
     */
    public Label getPaused_message() {
        return paused_message;
    }

    /**
     *
     * @return
     */
    public Label getWho_wins_message() {
        return who_wins_message;
    }
    
    private void setup_player_1_components(){
        player_1_point = new Label(Integer.toString(player_1.getPoint()));
        player_1_point.setTextFill(Color.WHITE);
        player_1_point.setFont(new Font(Constants.FONT, 30));
        player_1_satz = new Label(Integer.toString(player_1.getSatz()));
        player_1_satz.setTextFill(Color.WHITE);
        player_1_satz.setFont(new Font(Constants.FONT, 25));
        player_1_actions_box = new HBox(20);
    }
    
    private void setup_player_2_components(){
        player_2_point = new Label(Integer.toString(player_2.getPoint()));
        player_2_point.setFont(new Font(Constants.FONT, 30));
        player_2_point.setTextFill(Color.WHITE);
        player_2_satz = new Label(Integer.toString(player_2.getSatz()));
        player_2_satz.setFont(new Font(Constants.FONT, 25));
        player_2_satz.setTextFill(Color.WHITE);
        player_2_actions_box = new HBox(20);
    }
    
    private void setup_bot_components(){
        bot_point = new Label(Integer.toString(bot.getPoint()));
        bot_point.setFont(new Font(Constants.FONT, 30));
        bot_point.setTextFill(Color.WHITE);
        bot_satz = new Label(Integer.toString(bot.getSatz()));
        bot_satz.setFont(new Font(Constants.FONT, 25));
        bot_satz.setTextFill(Color.WHITE);
    }
    
    private void setup_other_components(){
        push_enter_to_start = new Label(Constants.PUSH_ENTER_TO_START_MESSAGE);
        push_enter_to_start.setFont(new Font(Constants.FONT, 20));
        push_enter_to_start.setTextFill(Color.WHITE);
        paused_message = new Label(Constants.PAUSED_WORD);
        paused_message.setFont(new Font(Constants.FONT, 20));
        paused_message.setTextFill(Color.WHITE);
        who_wins_message = new Label("You win!");
        who_wins_message.setFont(new Font(Constants.FONT, 30));
        who_wins_message.setTextFill(Color.WHITE);
    }
    
    /**
     *
     */
    public void setup_the_actions_box_SinglePlayer(){
        for(Actions action : player_1.getActions()){
            player_1_actions_box.getChildren().add(action.getGraphic());
        }
        player_1_actions_box.setAlignment(Pos.CENTER);
    }
    
    /**
     *
     */
    public void setup_the_actions_boxes_MultiPlayer(){
        // Die Actions-Listen auf der UI einrichten!
        for(Actions action : player_1.getActions()){
            player_1_actions_box.getChildren().add(action.getGraphic());
        }
        player_1_actions_box.setAlignment(Pos.CENTER);
        for(Actions action : player_2.getActions()){
            player_2_actions_box.getChildren().add(action.getGraphic());
        }
        player_2_actions_box.setAlignment(Pos.CENTER);
    }
    
    /**
     *
     */
    public void update_point_satz_SinglePlayer(){
        Platform.runLater(() -> {
            player_1_point.setText(Integer.toString(player_1.getPoint()));
            bot_point.setText(Integer.toString(bot.getPoint()));
            player_1_satz.setText(Integer.toString(player_1.getSatz()));
            bot_satz.setText(Integer.toString(bot.getSatz()));
        });  
    }
    
    /**
     *
     */
    public void update_point_satz_Multiplayer(){
        Platform.runLater(() -> {
            player_1_point.setText(Integer.toString(player_1.getPoint()));
            player_2_point.setText(Integer.toString(player_2.getPoint()));
            player_1_satz.setText(Integer.toString(player_1.getSatz()));
            player_2_satz.setText(Integer.toString(player_2.getSatz()));
        }); 
    }
}
