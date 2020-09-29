package org.openjfx.ea_aufgabe_2_pong;

import Model.Ball;
import Model.Bot;
import Model.Player;
import Resources.Actions;
import Resources.Constants;
import Resources.Database;
import Resources.Packet_Types;
import Utility.Random_Angle;
import java.util.Stack;

/**
 *
 * @author Minh Tue
 */
public class Referee {
    private Ball ball;
    private Player player_1;
    private Player player_2;
    private Bot bot;
    private Database database;
    /**
     * Der "other_infos_stack" vom "Game_Thread_Server", zum Ablegen von Punkte-Farbenänderung-Signalen an den Client!
     */
    private Stack<String> other_infos_stack_server; 
    
    /**
     * Single Player
     * 
     * @param ball
     * @param player_1
     * @param bot
     * @param database
     */
    public Referee(Ball ball, Player player_1, Bot bot, Database database){
        this.ball = ball;
        this.player_1 = player_1;
        this.bot = bot;
        this.database = database;
    } 
    
    /**
     * Multiplayer
     * 
     * @param ball
     * @param player_1
     * @param player_2
     * @param database
     */
    public Referee(Ball ball, Player player_1, Player player_2, Database database){
        this.ball = ball;
        this.player_1 = player_1;
        this.player_2 = player_2;
        this.database = database;
    } 

    /**
     *
     * @param other_infos_stack_server
     */
    public void setOther_infos_stack_server(Stack<String> other_infos_stack_server) {
        this.other_infos_stack_server = other_infos_stack_server;
    }
    
    /**
     *
     */
    public void evaluate_in_Singleplayer(){
        if(ball_hit_edge()){
            ball.setDirection_einheit_Y(- ball.getDirection_einheit_Y());
            // Hier wird der Ball reflektiert -> velocity_einheit_X_coord bleibt dasselbe, Neuer Y_Coord aber = - (Alter Y_Coord)
        }
        if(goal_for_left_side()){
            player_1.setPoint(player_1.getPoint() + 1);
            ball.reset(database.getBall_speed());
        }
        if(goal_for_right_side()){
            bot.setPoint(bot.getPoint() + 1);
            ball.reset(database.getBall_speed());
        }
        if(ball_hit_Player_1()){
            if(player_1.getPlayer_graphic().getFill().equals(Actions.DEFAULT.getColor())){
                ball.setDirection_einheit_X(- ball.getDirection_einheit_X());
                // Der Ball wird nur reflektiert -> velocity_einheit_Y_coord bleibt dasselbe, Neuer X_Coord aber = - (Alter X_Coord)
            }
            else{
                if(player_1.getPlayer_graphic().getFill().equals(Actions.ACCELERATE.getColor())){
                    ball.setSpeed(ball.getSpeed() * 2);
                    ball.getBall_graphic().setFill(player_1.getPlayer_graphic().getFill());
                    player_1.getPlayer_graphic().setFill(Actions.DEFAULT.getColor());
                    ball.setDirection_einheit_X(- ball.getDirection_einheit_X());
                }
                if(player_1.getPlayer_graphic().getFill().equals(Actions.CHANGE_ORBIT.getColor())){
                    // Player_1 ist auf der linken Seite --> Random_Angle.between_minus_90_and_90
                    int random_angle = Random_Angle.between_minus_90_and_90_without_flat_angles();
                    double new_direction_einheit_X = Math.cos((Math.PI * random_angle) / 180);
                    ball.setDirection_einheit_X(new_direction_einheit_X);
                    // Minus "sin", weil die positive Y-Achse der UI-Szene die Gegenrichtung zu der herkömmlichen positiven Sinus-Richtung hat
                    double new_direction_einheit_Y = - Math.sin((Math.PI * random_angle) / 180);
                    ball.setDirection_einheit_Y(new_direction_einheit_Y);
                    player_1.getPlayer_graphic().setFill(Actions.DEFAULT.getColor());
                }
                if(player_1.getPlayer_graphic().getFill().equals(Actions.FIREBALL.getColor())){
                    ball.getBall_graphic().setFill(Actions.FIREBALL.getColor());
                    player_1.getPlayer_graphic().setFill(Actions.DEFAULT.getColor());
                    ball.setDirection_einheit_X(- ball.getDirection_einheit_X());
                }
            }
        }
        if(ball_hit_Bot()){
            if(!ball.getBall_graphic().getFill().equals(Actions.FIREBALL.getColor())){
                ball.setDirection_einheit_X(- ball.getDirection_einheit_X());
                //Feuerball schlägt durch -> Keine Reflektierung 
            }
        }
    }

    /**
     * Der "Referee" vom Server hat komplette Kontrolle auf das Spiel. Der vom Client muss gar nichts tun!
     * 
     * Der Referee von Seite Host hat volle Kontrolle auf den Ball! Im Laufe des Spiels teilt er ständig dem Client 
        die Infos (also Position, Bewegungsrichtung) vom Ball mit -> Synchronisation
     */
    public void evaluate_in_Multiplayer_host(){
        if(ball_hit_edge()){
            ball.setDirection_einheit_Y(- ball.getDirection_einheit_Y());
            // Hier wird der Ball reflektiert -> velocity_einheit_X_coord bleibt dasselbe, Neuer Y_Coord aber = - (Alter Y_Coord)
        }
        if(goal_for_left_side()){
            player_1.setPoint(player_1.getPoint() + 1);
            // dem Client aktuellen Punktanzahl vom Spieler 1 mitteilen --> Lege dies in den "other_infos_stack_server" ab!
            // Der Tochterthread "Send_Thread_Server" (vom "Game_Thread_Server") wird diesen Stack abarbeiten
            String player_1_latest_point_data = Packet_Types.SERVER_POINT.getPacket_id() + String.valueOf(player_1.getPoint());
            other_infos_stack_server.add(player_1_latest_point_data);
            // Reset den Ball + schicke dem Client die Ballfarbe "DEFAULT"!
            ball.reset(database.getBall_speed());
            String ball_reset_default_color_data = Packet_Types.BALL_COLOR.getPacket_id() + Character.toString(Actions.DEFAULT.getName_of_the_Color());
            other_infos_stack_server.add(ball_reset_default_color_data);
        }
        if(goal_for_right_side()){
            // Analog zu "goal_for_left_side()"
            player_2.setPoint(player_2.getPoint() + 1);
            String player_2_latest_point_data = Packet_Types.CLIENT_POINT.getPacket_id() + String.valueOf(player_2.getPoint());
            other_infos_stack_server.add(player_2_latest_point_data);
            
            ball.reset(database.getBall_speed());
            String ball_reset_default_color_data = Packet_Types.BALL_COLOR.getPacket_id() + Character.toString(Actions.DEFAULT.getName_of_the_Color());
            other_infos_stack_server.add(ball_reset_default_color_data);
        }
        // Feuerball schlägt durch -> Keine Aktion und keine Ausnahme!
        if(ball_hit_Player_1() && !ball.getBall_graphic().getFill().equals(Actions.FIREBALL.getColor())){
            if(player_1.getPlayer_graphic().getFill().equals(Actions.DEFAULT.getColor())){
                ball.setDirection_einheit_X(- ball.getDirection_einheit_X());
                // Der Ball wird nur reflektiert -> velocity_einheit_Y_coord bleibt dasselbe, Neuer X_Coord aber = - (Alter X_Coord)
            }
            else{
                if(player_1.getPlayer_graphic().getFill().equals(Actions.ACCELERATE.getColor())){
                    ball.setSpeed(ball.getSpeed() * 2);
                    ball.getBall_graphic().setFill(Actions.ACCELERATE.getColor());
                    // dem Client die Farbenänderung auch mitteilen!
                    String ball_new_color_data = Packet_Types.BALL_COLOR.getPacket_id() + Character.toString(Actions.ACCELERATE.getName_of_the_Color());
                    other_infos_stack_server.add(ball_new_color_data);
                    
                    player_1.getPlayer_graphic().setFill(Actions.DEFAULT.getColor());
                    // Farbenänderung von dem Spieler auch mitteilen!
                    String player_1_back_to_default_color_data = Packet_Types.SERVER_COLOR.getPacket_id() 
                                                                 + Character.toString(Actions.DEFAULT.getName_of_the_Color());
                    other_infos_stack_server.add(player_1_back_to_default_color_data);
                    
                    ball.setDirection_einheit_X(- ball.getDirection_einheit_X());
                }
                if(player_1.getPlayer_graphic().getFill().equals(Actions.CHANGE_ORBIT.getColor())){
                    int random_angle = Random_Angle.between_minus_90_and_90_without_flat_angles();
                    ball.setDirection_einheit_X(Math.cos((Math.PI * random_angle) / 180));
                    ball.setDirection_einheit_Y(- Math.sin((Math.PI * random_angle) / 180));
                    player_1.getPlayer_graphic().setFill(Actions.DEFAULT.getColor());
                    // Farbenänderung von dem Spieler auch mitteilen!
                    String player_1_back_to_default_color_data = Packet_Types.SERVER_COLOR.getPacket_id() 
                                                                 + Character.toString(Actions.DEFAULT.getName_of_the_Color());
                    other_infos_stack_server.add(player_1_back_to_default_color_data);
                }
                if(player_1.getPlayer_graphic().getFill().equals(Actions.FIREBALL.getColor())){
                    ball.getBall_graphic().setFill(Actions.FIREBALL.getColor());
                    // dem Client die Farbenänderung auch mitteilen!
                    String ball_new_color_data = Packet_Types.BALL_COLOR.getPacket_id() + Character.toString(Actions.FIREBALL.getName_of_the_Color());
                    other_infos_stack_server.add(ball_new_color_data);
                    
                    player_1.getPlayer_graphic().setFill(Actions.DEFAULT.getColor());
                    // Farbenänderung von dem Spieler auch mitteilen!
                    String player_1_back_to_default_color_data = Packet_Types.SERVER_COLOR.getPacket_id() 
                                                                 + Character.toString(Actions.DEFAULT.getName_of_the_Color());
                    other_infos_stack_server.add(player_1_back_to_default_color_data);
                    
                    ball.setDirection_einheit_X(- ball.getDirection_einheit_X());
                }
            }
        }
        if(ball_hit_Player_2() && !ball.getBall_graphic().getFill().equals(Actions.FIREBALL.getColor())){
            //Genauso wie bei Player_1
            if(player_2.getPlayer_graphic().getFill().equals(Actions.DEFAULT.getColor())){
                ball.setDirection_einheit_X(- ball.getDirection_einheit_X());
                // Der Ball wird nur reflektiert -> velocity_einheit_Y_coord bleibt dasselbe, Neuer X_Coord aber = - (Alter X_Coord)
            }
            else{
                if(player_2.getPlayer_graphic().getFill().equals(Actions.ACCELERATE.getColor())){
                    ball.setSpeed(ball.getSpeed() * 2);
                    ball.getBall_graphic().setFill(Actions.ACCELERATE.getColor());
                    String ball_new_color_data = Packet_Types.BALL_COLOR.getPacket_id() + Character.toString(Actions.ACCELERATE.getName_of_the_Color());
                    other_infos_stack_server.add(ball_new_color_data);
                    
                    player_2.getPlayer_graphic().setFill(Actions.DEFAULT.getColor());
                    String player_2_back_to_default_color_data = Packet_Types.CLIENT_COLOR.getPacket_id() 
                                                                 + Character.toString(Actions.DEFAULT.getName_of_the_Color());
                    other_infos_stack_server.add(player_2_back_to_default_color_data);
                    
                    ball.setDirection_einheit_X(- ball.getDirection_einheit_X());
                }
                if(player_2.getPlayer_graphic().getFill().equals(Actions.CHANGE_ORBIT.getColor())){
                    // Player_2 ist auf der rechten  Seite --> Random_Angle.between_90_and_270
                    int random_angle = Random_Angle.between_90_and_270_without_flat_angles();
                    ball.setDirection_einheit_X(Math.cos((Math.PI * random_angle) / 180));
                    // Minus Sin! Grund oben angegeben!
                    ball.setDirection_einheit_Y(- Math.sin((Math.PI * random_angle) / 180));
                    
                    player_2.getPlayer_graphic().setFill(Actions.DEFAULT.getColor());
                    String player_2_back_to_default_color_data = Packet_Types.CLIENT_COLOR.getPacket_id() 
                                                                 + Character.toString(Actions.DEFAULT.getName_of_the_Color());
                    other_infos_stack_server.add(player_2_back_to_default_color_data);
                }
                if(player_2.getPlayer_graphic().getFill().equals(Actions.FIREBALL.getColor())){
                    ball.getBall_graphic().setFill(Actions.FIREBALL.getColor());
                    String ball_new_color_data = Packet_Types.BALL_COLOR.getPacket_id() + Character.toString(Actions.FIREBALL.getName_of_the_Color());
                    other_infos_stack_server.add(ball_new_color_data);
                    
                    player_2.getPlayer_graphic().setFill(Actions.DEFAULT.getColor());
                    String player_2_back_to_default_color_data = Packet_Types.CLIENT_COLOR.getPacket_id() 
                                                                 + Character.toString(Actions.DEFAULT.getName_of_the_Color());
                    other_infos_stack_server.add(player_2_back_to_default_color_data);
                    
                    ball.setDirection_einheit_X(- ball.getDirection_einheit_X());
                }
            }
        }
    }
    
    /**
     * Eine Runde endet, wenn Player_1 die Runde gewinnt oder der Bot
     * 
     * Anfangs jeder Runde werden die Action-Listen neu erstellt, einerseits für die "Player.getActions()" im Hintergrund,
        andererseits für die "Actions-Box" vom "grap_viewer" auf der sichtbaren Szene (Vordergrund).
     *
     * Das Neu Erstellen im Hintergrund übernimmt der Controller sofort in dieser Methode, das andere übernimmt der "Game-Thread",
        weil es um Komponente der UI auf der Szene geht!
     * 
     * @return
     */
    public boolean a_round_ended_Singleplayer(){
        boolean b = false;
        if(player_1_wins_round_Singleplayer()){
            player_1.setSatz(player_1.getSatz() + 1);
            player_1.setPoint(0);
            if(player_1.getActions().size() < 3){
                player_1.setActions(database.getActions());
            }
            bot.setPoint(0);
            b = true;
        }
        if(bot_wins_round_Singleplayer()){
            bot.setSatz(bot.getSatz() + 1);
            bot.setPoint(0);
            player_1.setPoint(0);
            if(player_1.getActions().size() < 3){
                player_1.setActions(database.getActions());
            }
            b = true;
        }
        return b;
    }
    
    /**
     * Eine Runde endet, wenn Player_1 die Runde gewinnt oder Player_2
     * 
     * Anfangs jeder Runde werden die Action-Listen neu erstellt, einerseits für die "Player.getActions()" im Hintergrund,
        andererseits für die "Actions-Box" vom "grap_viewer" auf der sichtbaren Szene.
     *
     * Das Neu Erstellen im Hintergrund übernimmt der Controller sofort in dieser Methode, das andere übernimmt der "Game-Thread",
        weil es um Komponente der UI auf der Szene geht!
     * 
     * @return
     */
    public boolean a_round_ended_Multiplayer(){
        boolean b = false;
        if(player_1_wins_round_Multiplayer()){
            player_1.setSatz(player_1.getSatz() + 1);
            player_1.setPoint(0);
            if(player_1.getActions().size() < 3){
                player_1.setActions(database.getActions_server());
            }
            player_2.setPoint(0);
            if(player_2.getActions().size() < 3){
                player_2.setActions(database.getActions_client());
            }
            b = true;
        }
        if(player_2_wins_round_Multiplayer()){
            player_2.setSatz(player_2.getSatz() + 1);
            player_2.setPoint(0);
            if(player_2.getActions().size() < 3){
                player_2.setActions(database.getActions_client());
            }
            player_1.setPoint(0);
            if(player_1.getActions().size() < 3){
                player_1.setActions(database.getActions_server());
            }
            b = true;
        }
        return b;
    }
    
    /**
     *
     * @return
     */
    public boolean player_1_wins_the_game(){
        return player_1.getSatz() == 3;
    }
    
    /**
     *
     * @return
     */
    public boolean bot_wins_the_game(){
        return bot.getSatz() == 3;
    }
    
    private boolean player_1_wins_round_Singleplayer(){
        return ((player_1.getPoint() == 15) && (player_1.getPoint() - bot.getPoint() >= 2)) || player_1.getPoint() == 21;
    }
    
    private boolean bot_wins_round_Singleplayer(){
        return ((bot.getPoint() == 15) && (bot.getPoint() - player_1.getPoint() >= 2)) || bot.getPoint() == 21;
    }
    
    /**
     *
     * @return
     */
    public boolean player_2_wins_the_game(){
        return player_2.getSatz() == 3;
    }
    
    private boolean player_1_wins_round_Multiplayer(){
        return ((player_1.getPoint() == 15) && (player_1.getPoint() - player_2.getPoint() >= 2)) || player_1.getPoint() == 21;
    }
    
    private boolean player_2_wins_round_Multiplayer(){
        return ((player_2.getPoint() == 15) && (player_2.getPoint() - player_1.getPoint() >= 2)) || player_2.getPoint() == 21;
    } 
    
    private boolean ball_hit_edge(){
        return (ball.getBall_graphic().getCenterY() - Constants.BALL_RADIUS <= 0) 
                || (ball.getBall_graphic().getCenterY() + Constants.BALL_RADIUS >= Constants.GAME_FIELD_HEIGHT);
    }
    
    private boolean ball_hit_Player_1(){
        return player_1.getPlayer_graphic().getX() + Constants.PADDLE_WIDTH >= ball.getBall_graphic().getCenterX() - Constants.BALL_RADIUS
                && player_1.getPlayer_graphic().getX() + Constants.PADDLE_WIDTH <= ball.getBall_graphic().getCenterX() + Constants.BALL_RADIUS
                && ball.getBall_graphic().getCenterY() >= player_1.getPlayer_graphic().getY()
                && ball.getBall_graphic().getCenterY() <= player_1.getPlayer_graphic().getY() + Constants.PADDLE_HEIGHT;
    }
    
    private boolean ball_hit_Bot(){
        return bot.getBot_graphic().getX() <= ball.getBall_graphic().getCenterX() + Constants.BALL_RADIUS
                && bot.getBot_graphic().getX() >= ball.getBall_graphic().getCenterX() - Constants.BALL_RADIUS
                && ball.getBall_graphic().getCenterY() >= bot.getBot_graphic().getY()
                && ball.getBall_graphic().getCenterY() <= bot.getBot_graphic().getY() + Constants.PADDLE_HEIGHT;
    }
    
    private boolean ball_hit_Player_2(){
        return player_2.getPlayer_graphic().getX() <= ball.getBall_graphic().getCenterX() + Constants.BALL_RADIUS
                && player_2.getPlayer_graphic().getX() >= ball.getBall_graphic().getCenterX() - Constants.BALL_RADIUS
                && ball.getBall_graphic().getCenterY() >= player_2.getPlayer_graphic().getY()
                && ball.getBall_graphic().getCenterY() <= player_2.getPlayer_graphic().getY() + Constants.PADDLE_HEIGHT;
    } // Für "Multiplayer"
    
    private boolean goal_for_left_side(){
        return ball.getBall_graphic().getCenterX() >= Constants.GAME_FIELD_WIDTH;
    }
    
    private boolean goal_for_right_side(){
        return ball.getBall_graphic().getCenterX() <= 0;
    }
}
