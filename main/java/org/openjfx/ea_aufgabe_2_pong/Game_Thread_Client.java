package org.openjfx.ea_aufgabe_2_pong;

import Model.Ball;
import Model.Client;
import Model.Player;
import Resources.Actions;
import View.Game_Graphic_Components;
import java.util.Stack;
import javafx.application.Platform;
import javafx.scene.Group;

/**
 * funktioniert ziemlich analog zu "Game_Thread_Server"
 * 
 * Ein paar Unterschiede: 
 * 
 * 1. Der Client schickt außer seiner Spielerposition nur die "ORDERS" Befehle ("Pause", "Action"), gelagert in "orders_stack"
 * 
 * 2. Referee Client muss nichts tun, alles wird vom Server synchronisiert --> Game_Thread_Client muss auch kaum was tun!
 */
public class Game_Thread_Client extends Thread {
    private Ball ball;
    private Player player_1;
    private Player player_2;
    private Client client;
    private Referee controller;
    /**
     * Beim Multiplayer-Spielende diese Anzeige zum Beenden des Spiels
     */
    private Group exit_pane_multiplayer; 
    private Game_Graphic_Components grap_viewer;
    /**
     * Tochterthread
     */
    private Receive_Thread_Client receive_thread; 
    /**
     * Tochterthread
     */
    private Send_Thread_Client send_thread; 
    /**
     * Tochterthread
     */
    private Analyze_Thread_Client analyze_thread;
    /**
     * Dieser Stack lagert die "ORDERS"-Signale zum Senden an den Server ein, ziemlich ähnlich zu dem 
    vom "Game_Thread_Server"
     */
    private Stack<String> orders_stack;
    /**
     * Dieser Stack lagert die vom Server durch "Receive_Thread_Client" empfangenen 
    Nachrichten ein! Er wird vom "Analyze_Thread_Client" im Laufe des Spiels abgearbeitet!
     */
    private Stack<byte[]> messages_from_server_stack; 
    private boolean game_ended;
    /**
     * Das Spiel läuft, solange beide Seiten "bereit" sind
     */
    private boolean server_ready; 
    /**
     * "Nicht bereit" Zustand könnte Pause-Zustand (von einer oder beiden Seiten), 
        oder wenn eine Runde zu Ende kommt
     */
    private boolean client_ready; 
    
    /**
     *
     * @param ball
     * @param player_1
     * @param player_2
     * @param client
     * @param controller
     * @param grap_viewer
     * @param exit_pane_multiplayer
     */
    public Game_Thread_Client(Ball ball, Player player_1, Player player_2, Client client, Referee controller, 
                                Game_Graphic_Components grap_viewer, Group exit_pane_multiplayer){
        this.ball = ball;
        this.player_1 = player_1;
        this.player_2 = player_2;
        this.client = client;
        this.controller = controller;
        this.grap_viewer = grap_viewer;
        this.exit_pane_multiplayer = exit_pane_multiplayer;
        // Komposition: Wenn dieser Mutterthread stirbt, sterben auch die Töchter "receive_thread" + "send_thread" + "analyze_thread"
        receive_thread = new Receive_Thread_Client(client, this);
        send_thread = new Send_Thread_Client(client, player_2, this);
        analyze_thread = new Analyze_Thread_Client(ball, player_1, player_2, grap_viewer, this);
        orders_stack = new Stack<>();
        messages_from_server_stack = new Stack<>();
        // Lasse den Thread das Stoppen der JVM nicht verhindern
        this.setDaemon(true);
        game_ended = false;
        server_ready = false;
        client_ready = false;
    }

    /**
     *
     * @param game_ended
     */
    public void setGame_ended(boolean game_ended) {
        this.game_ended = game_ended;
    }

    /**
     *
     * @param client_ready
     */
    public void setClient_ready(boolean client_ready) {
        this.client_ready = client_ready;
    }

    /**
     *
     * @param server_ready
     */
    public void setServer_ready(boolean server_ready) {
        this.server_ready = server_ready;
    }
    
    /**
     *
     * @return
     */
    public boolean isClient_ready() {
        return client_ready;
    }

    /**
     *
     * @return
     */
    public boolean isServer_ready() {
        return server_ready;
    }

    /**
     *
     * @return
     */
    public boolean isGame_ended() {
        return game_ended;
    }

    /**
     *
     * @return
     */
    public Stack<String> getOrders_stack() {
        return orders_stack;
    }

    /**
     *
     * @return
     */
    public Stack<byte[]> getMessages_from_server_stack() {
        return messages_from_server_stack;
    }
    
    /** 
     * Hier läuft der Game_Thread_Client! Der Thread empfängt einerseits ständig die Nachrichten vom Server
    (durch Tochter "Receive_Thread_Client"), steckt diese Nachrichten in den Stack "messages_from_server_stack" ein, und verarbeitet
    sie durch "Analyze_Thread_Client". 
     * 
     * Andererseits schickt er auch die wichtigsten Infos an den Server (durch Tochter 
    "Send_Thread_Client")! 
     * 
     * Game_Thread_Client muss kaum was tun! Fast alles wird vom Game_Thread_Server erledigt!
    */
    @Override
    public void run(){
        // Die Tochterthreads auch mitstarten!
        receive_thread.start();
        send_thread.start();
        analyze_thread.start();
        
        while(!game_ended){
            try {
                Thread.sleep(25);
            }
            catch (InterruptedException ex) {

            }
            // Dieser "If" läuft solange bei jedem Durchlauf, bis ne neue Runde anfängt! Oder bis das Spiel pausiert wird
            if(server_ready && client_ready){
                // Game_Thread_Client muss kaum was tun! Fast alles wird vom Game_Thread_Server erledigt!
                /* Eine Runde endet -> Reset die Actions-Listen
                   1. Actions-Listen als Attribut von den Spielern im Hintergrund werdrn durch "controller.a_round_ended_Multiplayer()" 
                      gereset (siehe "Referee" Klasse)
                   2. Actions-Listen als Labels auf der UI (Vordergrund) werden hier gereset
                */  
                if(controller.a_round_ended_Multiplayer()){
                    Platform.runLater(() -> {
                        // Reset die Actions-Listen auf der Szene
                        if(grap_viewer.getPlayer_1_actions_box().getChildren().size() < 3){
                            grap_viewer.getPlayer_1_actions_box().getChildren().remove(0, grap_viewer.getPlayer_1_actions_box().getChildren().size());
                            for(Actions action : player_1.getActions()){
                                grap_viewer.getPlayer_1_actions_box().getChildren().add(action.getGraphic());
                            }
                        }
                        if(grap_viewer.getPlayer_2_actions_box().getChildren().size() < 3){
                            grap_viewer.getPlayer_2_actions_box().getChildren().remove(0, grap_viewer.getPlayer_2_actions_box().getChildren().size());
                            for(Actions action : player_2.getActions()){
                                grap_viewer.getPlayer_2_actions_box().getChildren().add(action.getGraphic());
                            }
                        }
                        grap_viewer.getPush_enter_to_start().setVisible(true);
                    });
                    server_ready = false;
                    client_ready = false;
                }
                if(controller.player_1_wins_the_game() || controller.player_2_wins_the_game()){
                    // Spiel zu Ende -> Töte den "Game_thread"
                    server_ready = false;
                    client_ready = false;
                    game_ended = true;
                    // Zeige die Siegnachricht an + Exit-Pane zum Beenden!
                    Platform.runLater(() -> {
                        grap_viewer.getPush_enter_to_start().setVisible(false);
                        exit_pane_multiplayer.setVisible(true);
                        if(controller.player_1_wins_the_game()){
                            grap_viewer.getWho_wins_message().setText("Player 1 wins");
                            grap_viewer.getWho_wins_message().setVisible(true);
                        }
                        if(controller.player_2_wins_the_game()){
                            grap_viewer.getWho_wins_message().setText("Player 2 wins");
                            grap_viewer.getWho_wins_message().setVisible(true);
                        }
                    });
                }
                Platform.runLater(() -> {
                    grap_viewer.update_point_satz_Multiplayer();
                });   
            }
        }
    }
}
