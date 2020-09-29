package Utility;

import Model.Player;
import Resources.Constants;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Zum Bauen und Positionieren der UI-Komponente auf dem Spiel!
 */
public class Game_Builder {
    
    /**
     * "the_game" von "App" Klasse, "player_1_point", "player_1_satz", "player_1_actions_box" von "Game_Graphic_Components"
     * @param the_game
     * @param player_1
     * @param player_1_point
     * @param player_1_satz
     * @param player_1_actions_box
     */
    public static void build_player_1_infos_on_screen(Group the_game, Player player_1, Label player_1_point, 
                                                        Label player_1_satz, HBox player_1_actions_box){

        //Spieler-Punkte
        Label player_1_point_label = new Label("Point");
        player_1_point_label.setFont(new Font(Constants.FONT, 15));
        player_1_point_label.setTextFill(Color.WHITE);
        VBox player_1_point_box = new VBox(5);
        player_1_point_box.setMaxSize(100, 100);
        player_1_point_box.getChildren().addAll(player_1_point_label, player_1_point);
        player_1_point_box.setAlignment(Pos.CENTER);
        
        the_game.getChildren().add(player_1_point_box);
        
        player_1_point_box.setTranslateX(Constants.GAME_FIELD_WIDTH/2 - 80);
        player_1_point_box.setTranslateY(10);
        
        //Spieler-Infos
        Label player_1_label = new Label("Player");
        player_1_label.setFont(new Font(Constants.FONT, 15));
        player_1_label.setTextFill(Color.WHITE);
        Label player_1_name_label = new Label(player_1.getName());
        player_1_name_label.setFont(new Font(Constants.FONT, 15));
        player_1_name_label.setTextFill(Color.WHITE);
        HBox player_1_name_box = new HBox(15);
        player_1_name_box.getChildren().addAll(player_1_label, player_1_name_label);
        player_1_name_box.setAlignment(Pos.CENTER);
        
        Label player_1_satz_label = new Label("Wins");
        player_1_satz_label.setFont(new Font(Constants.FONT, 15));
        player_1_satz_label.setTextFill(Color.WHITE);
        HBox player_1_satz_box = new HBox(15);
        player_1_satz_box.getChildren().addAll(player_1_satz_label, player_1_satz);
        player_1_satz_box.setAlignment(Pos.CENTER);
        
        VBox player_1_name_satz_actions_box = new VBox(5);
        player_1_name_satz_actions_box.setMaxSize(100, 110);
        player_1_name_satz_actions_box.getChildren().addAll(player_1_name_box, player_1_satz_box, player_1_actions_box);
        player_1_name_satz_actions_box.setAlignment(Pos.CENTER);
        
        
        the_game.getChildren().add(player_1_name_satz_actions_box);

        player_1_name_satz_actions_box.setTranslateX(20);
        player_1_name_satz_actions_box.setTranslateY(10);
    }
    
    /**
     *
     * @param the_game
     * @param bot_point
     * @param bot_satz
     */
    public static void build_bot_infos_on_screen(Group the_game, Label bot_point, Label bot_satz){
        //Bot-Punkte
        Label bot_point_label = new Label("Point");
        bot_point_label.setFont(new Font(Constants.FONT, 15));
        bot_point_label.setTextFill(Color.WHITE);
        VBox bot_point_box = new VBox(5);
        bot_point_box.setMaxSize(100, 100);
        bot_point_box.getChildren().addAll(bot_point_label, bot_point);
        bot_point_box.setAlignment(Pos.CENTER);
              
        the_game.getChildren().add(bot_point_box);
        
        bot_point_box.setTranslateX(Constants.GAME_FIELD_WIDTH/2 + 45);
        bot_point_box.setTranslateY(10);
        
        //Bot-Infos
        Label bot_label = new Label("Bot");
        bot_label.setFont(new Font(Constants.FONT, 15));
        bot_label.setTextFill(Color.WHITE);
        
        Label bot_satz_label = new Label("Wins");
        bot_satz_label.setFont(new Font(Constants.FONT, 15));
        bot_satz_label.setTextFill(Color.WHITE);

        HBox bot_satz_box = new HBox(15);
        bot_satz_box.getChildren().addAll(bot_satz_label, bot_satz);
        bot_satz_box.setAlignment(Pos.CENTER);
        
        VBox bot_name_satz_box = new VBox(5);
        bot_name_satz_box.setMaxSize(100, 110);
        bot_name_satz_box.getChildren().addAll(bot_label, bot_satz_box);
        bot_name_satz_box.setAlignment(Pos.CENTER);
               
        the_game.getChildren().add(bot_name_satz_box);
        
        bot_name_satz_box.setTranslateX(Constants.GAME_FIELD_WIDTH - 80);
        bot_name_satz_box.setTranslateY(10);
    }
    
    /**
     *
     * @param the_game
     * @param player_2
     * @param player_2_point
     * @param player_2_satz
     * @param player_2_actions_box
     */
    public static void build_player_2_infos_on_screen(Group the_game, Player player_2, Label player_2_point, 
                                                        Label player_2_satz, HBox player_2_actions_box){
        //Spieler-Punkte
        Label player_2_point_label = new Label("Point");
        player_2_point_label.setFont(new Font(Constants.FONT, 15));
        player_2_point_label.setTextFill(Color.WHITE);
        VBox player_2_point_box = new VBox(5);
        player_2_point_box.setMaxSize(100, 100);
        player_2_point_box.getChildren().addAll(player_2_point_label, player_2_point);
        player_2_point_box.setAlignment(Pos.CENTER);
        
        the_game.getChildren().add(player_2_point_box);
        
        player_2_point_box.setTranslateX(Constants.GAME_FIELD_WIDTH/2 + 45);
        player_2_point_box.setTranslateY(10);
        
        //Spieler-Infos
        Label player_2_label = new Label("Player");
        player_2_label.setFont(new Font(Constants.FONT, 15));
        player_2_label.setTextFill(Color.WHITE);
        Label player_2_name_label = new Label(player_2.getName());
        player_2_name_label.setFont(new Font(Constants.FONT, 15));
        player_2_name_label.setTextFill(Color.WHITE);
        HBox player_2_name_box = new HBox(15);
        player_2_name_box.getChildren().addAll(player_2_label, player_2_name_label);
        player_2_name_box.setAlignment(Pos.CENTER);
        
        Label player_2_satz_label = new Label("Wins");
        player_2_satz_label.setFont(new Font(Constants.FONT, 15));
        player_2_satz_label.setTextFill(Color.WHITE);
        HBox player_2_satz_box = new HBox(15);
        player_2_satz_box.getChildren().addAll(player_2_satz_label, player_2_satz);
        player_2_satz_box.setAlignment(Pos.CENTER);
        
        VBox player_2_name_satz_actions_box = new VBox(5);
        player_2_name_satz_actions_box.setMaxSize(100, 110);
        player_2_name_satz_actions_box.getChildren().addAll(player_2_name_box, player_2_satz_box, player_2_actions_box);
        player_2_name_satz_actions_box.setAlignment(Pos.CENTER);
        
        the_game.getChildren().add(player_2_name_satz_actions_box);
 
        player_2_name_satz_actions_box.setTranslateX(Constants.GAME_FIELD_WIDTH - 90);
        player_2_name_satz_actions_box.setTranslateY(10);
    }
    
    /**
     *
     * @param the_game
     * @param push_enter_to_start
     * @param paused_message
     * @param who_wins_message
     */
    public static void build_other_components_on_screen(Group the_game, Label push_enter_to_start, Label paused_message,
                                                            Label who_wins_message){
        // "push_enter_to_start", "paused_message", "who_wins_message" von "Game_Graphic_Components"
        the_game.getChildren().add(push_enter_to_start);
        
        push_enter_to_start.setTranslateX(Constants.GAME_FIELD_WIDTH/2 - 100);
        push_enter_to_start.setTranslateY(Constants.GAME_FIELD_HEIGHT/2 - 100);
        
        the_game.getChildren().add(paused_message);
        
        paused_message.setTranslateX(Constants.GAME_FIELD_WIDTH/2 - 35);
        paused_message.setTranslateY(Constants.GAME_FIELD_HEIGHT/2 - 60);
        paused_message.setVisible(false);
        
        the_game.getChildren().add(who_wins_message);
        
        //Spielergebnis
        who_wins_message.setTranslateX(Constants.GAME_FIELD_WIDTH/2 - 95);
        who_wins_message.setTranslateY(Constants.GAME_FIELD_HEIGHT/2 - 180);
        who_wins_message.setVisible(false);   
    }
}
