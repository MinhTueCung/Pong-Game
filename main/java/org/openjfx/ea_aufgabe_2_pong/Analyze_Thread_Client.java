package org.openjfx.ea_aufgabe_2_pong;

import Model.Ball;
import Model.Player;
import Resources.Actions;
import Resources.Constants;
import Resources.Packet_Types;
import View.Game_Graphic_Components;
import javafx.application.Platform;

/**
 * Dieser Thread funktioniert fast analog zum "Analyze_Thread_Server": Er verarbeitet Daten vom Server
 */
public class Analyze_Thread_Client extends Thread {
    /**
     * um mit dem Ball von Seite Host (Server) zu synchronisieren!
     */
    private Ball ball; 
    private Player player_1;
    private Player player_2;
    private Game_Graphic_Components grap_viewer;
    /**
     * zur Kommunikation mit dem Mutterthread "Game_Thread_Client"
     */
    private Game_Thread_Client mother_thread; 
    
    /**
     *
     * @param ball
     * @param player_1
     * @param player_2
     * @param grap_viewer
     * @param mother_thread
     */
    public Analyze_Thread_Client(Ball ball, Player player_1, Player player_2, Game_Graphic_Components grap_viewer, Game_Thread_Client mother_thread){
        this.ball = ball;
        this.player_1 = player_1;
        this.player_2 = player_2;
        this.grap_viewer = grap_viewer;
        this.mother_thread = mother_thread;
        // Lasse den Thread das Stoppen der JVM nicht verhindern
        this.setDaemon(true);
    }
    
    /**
     * Dieser Thread läuft ständig, also verarbeitet Daten vom Server pausenlos, bis zum Spielende, um auf jedes Befehl 
        vom Server zu reagieren (z.B: "Fortsetzen", "Pause", ...). Daten werden vom "messages_from_server_stack" der Mutter 
        "Game_Thread_Client" rausgenommen!
     */
    @Override
    public void run(){
        while(!mother_thread.isGame_ended()){
            if(!mother_thread.getMessages_from_server_stack().empty()){
                analyze(mother_thread.getMessages_from_server_stack().pop());
            }
        }
    }
    
    /**
     * Alle Daten zum Austauschen zwischen Server und Client sind standardweise Strings, aber in Form von byte[]!
     * 
     * Daten = Strings = Packet_Types IP (2 erste Ziffern) + Infos
     * 
     * Der "Referee" vom Server hat volle Kontrolle auf das Spiel. Der Client muss nur die Daten vom Server verarbeiten zum
        Synchronisieren!
     * 
     * Solche Daten sind: Ballposition, Player_1 (Server) Position, "ORDERS" Befehle, Punkte, Farben des Balls + der Paddles
        (siehe "Packet_Types" Klasse)
     * @param byte_data 
     */
    private void analyze(byte[] byte_data){
        String data = new String(byte_data).trim();
        String data_ID = data.substring(0, 2);
        
        if(data_ID.equals(Packet_Types.BALL_POSITION_X_Y.getPacket_id())){
            String data_content = data.substring(2);
            // Finden den Index des Trennungszeichens "|" zwischen Koordinate_X und Y (siehe "Send_Thread_Server" Klasse)
            int i = 0;
            while(i < data_content.length()){
                if(data_content.charAt(i) == '|'){
                    break;
                }
                else{
                    i++;
                }
            }
            // Die 2 X- und Y-Koordinaten extrahieren!
            double ball_position_X = Double.parseDouble(data_content.substring(0, i));
            double ball_position_Y = Double.parseDouble(data_content.substring(i + 1));
            
            // Ball ist auf der UI --> Platform.runLater()
            Platform.runLater(() -> {
                ball.getBall_graphic().setCenterX(ball_position_X);
                ball.getBall_graphic().setCenterY(ball_position_Y);
            });
        }
        if(data_ID.equals(Packet_Types.SERVER_PLAYER_POSITION.getPacket_id())){
            double oppo_position_Y = Double.parseDouble(data.substring(2));
            Platform.runLater(() -> {
                player_1.getPlayer_graphic().setY(oppo_position_Y);
            });
        }
        if(data_ID.equals(Packet_Types.ORDERS.getPacket_id())){
            switch(data.substring(2)){
                case Constants.PAUSE_ORDER:
                    // Wenn der Server "ready" ist -> "Pause" vom Server pausiert das Spiel, ansonsten setzt das Spiel fort!
                    if(mother_thread.isServer_ready()){
                        // Auf der UI anzeigen
                        Platform.runLater(() -> {
                            grap_viewer.getPaused_message().setVisible(true);
                        });
                        //dem Mutterthread "Game_Thread_Client" das Signal mitteilen!
                        mother_thread.setServer_ready(false);
                    }
                    else{
                        Platform.runLater(() -> {
                            grap_viewer.getPaused_message().setVisible(false);
                        });
                        mother_thread.setServer_ready(true);
                    }
                    break;
                case Constants.ACTION_ORDER:
                    // "Action"-Befehl vom Server
                    if(!player_1.getActions().isEmpty()){
                        Platform.runLater(() -> {
                            player_1.getPlayer_graphic().setFill(player_1.getActions().remove(0).getColor());
                            grap_viewer.getPlayer_1_actions_box().getChildren().remove(0);
                        });
                    }
                    break;
                case Constants.READY_ORDER: //"Enter" zum Starten Befehl
                    mother_thread.setServer_ready(true);
                    /* Wenn der Client selbst auch ready ist -> die "Press Enter to start" Nachricht verschwinden 
                    und das Spiel laufen lassen! Ansonsten die Nachricht zum "Player_2 not ready" überschreiben!
                    */
                    if(mother_thread.isClient_ready()){
                        Platform.runLater(() -> {
                            grap_viewer.getPush_enter_to_start().setText(Constants.PUSH_ENTER_TO_START_MESSAGE);
                            grap_viewer.getPush_enter_to_start().setVisible(false);
                        });
                    }
                    else{
                        Platform.runLater(() -> {
                            grap_viewer.getPush_enter_to_start().setText(Constants.WAIT_FOR_PLAYER_2_MESSAGE);
                        });
                    }
                    break;
            }
        }
        if(data_ID.equals(Packet_Types.SERVER_POINT.getPacket_id())){
            int server_point = Integer.parseInt(data.substring(2));
            /* Punkte vom Spieler 1 aktualisieren! Punktzahl befindet sich an 2 Orten: 
               +) Vom Spieler selbst als Attribut "player_1.getPoint()" (Hintergrund)
               +) Als ein Label auf der UI-Szene. "grap_viewer" ("Game_Graphic_Components") hat Zugriff darauf (Vordergrund)
               --> Aktualisiere den 1. (Hintergrund)
               --> Auf der UI-Szene (Vordergrund) wird "Point" durch "grap_viewer.update_point_satz_Multiplayer()" 
               aktualisiert (siehe "Game_Thread_Client")
            */
            player_1.setPoint(server_point);
        }
        if(data_ID.equals(Packet_Types.CLIENT_POINT.getPacket_id())){
            // Analog zu "SERVER_POINT"
            int client_point = Integer.parseInt(data.substring(2));
            player_2.setPoint(client_point);
        }
        if(data_ID.equals(Packet_Types.SERVER_COLOR.getPacket_id())){
            /* Das dritte "Char" von dem String "data" ist der Name der Farbe ('w': Weiß, DEFAULT / 'b': Blau, ACCELERATE /
               'y': Gelb, CHANGE_ORBIT / 'f'; FEUERROT, FIREBALL (siehe "Actions" Klasse))
            */
            // Immer "Platform.runLater()", wenn es um Veränderungen an Komponenten auf der UI geht!
            char color_name = data.charAt(2);
            switch(color_name){
                case 'w':
                    Platform.runLater(() -> {
                        player_1.getPlayer_graphic().setFill(Actions.DEFAULT.getColor());
                    });
                    break;
                case 'b':
                    Platform.runLater(() -> {
                        player_1.getPlayer_graphic().setFill(Actions.ACCELERATE.getColor());
                    });
                    break;
                case 'y':
                    Platform.runLater(() -> {
                        player_1.getPlayer_graphic().setFill(Actions.CHANGE_ORBIT.getColor());
                    });
                    break;
                case 'f':
                    Platform.runLater(() -> {
                        player_1.getPlayer_graphic().setFill(Actions.FIREBALL.getColor());
                    });
                    break;
            }
        }
        if(data_ID.equals(Packet_Types.CLIENT_COLOR.getPacket_id())){
            // Analog zu "SERVER_COLOR"
            char color_name = data.charAt(2);
            switch(color_name){
                case 'w':
                    Platform.runLater(() -> {
                        player_2.getPlayer_graphic().setFill(Actions.DEFAULT.getColor());
                    });
                    break;
                case 'b':
                    Platform.runLater(() -> {
                        player_2.getPlayer_graphic().setFill(Actions.ACCELERATE.getColor());
                    });
                    break;
                case 'y':
                    Platform.runLater(() -> {
                        player_2.getPlayer_graphic().setFill(Actions.CHANGE_ORBIT.getColor());
                    });
                    break;
                case 'f':
                    Platform.runLater(() -> {
                        player_2.getPlayer_graphic().setFill(Actions.FIREBALL.getColor());
                    });
                    break;
            }
        }
        if(data_ID.equals(Packet_Types.BALL_COLOR.getPacket_id())){
            char color_name = data.charAt(2);
            switch(color_name){
                case 'w':
                    Platform.runLater(() -> {
                        ball.getBall_graphic().setFill(Actions.DEFAULT.getColor());
                    });
                    break;
                case 'b':
                    Platform.runLater(() -> {
                        ball.getBall_graphic().setFill(Actions.ACCELERATE.getColor());
                    });
                    break;
                case 'y':
                    Platform.runLater(() -> {
                        ball.getBall_graphic().setFill(Actions.CHANGE_ORBIT.getColor());
                    });
                    break;
                case 'f':
                    Platform.runLater(() -> {
                        ball.getBall_graphic().setFill(Actions.FIREBALL.getColor());
                    });
                    break;
            }
        }
    }
}

