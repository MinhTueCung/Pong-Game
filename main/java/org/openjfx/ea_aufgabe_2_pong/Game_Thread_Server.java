package org.openjfx.ea_aufgabe_2_pong;

import Model.Ball;
import Model.Player;
import Model.Server;
import Resources.Actions;
import View.Game_Graphic_Components;
import java.util.Stack;
import javafx.application.Platform;
import javafx.scene.Group;

/**
 * Dieser Thread steuert das Spiel seitens Server an!
 * 
 * Seine Soll-Aufgabe: das Spiel ansteuern + währenddessen ständig die Infos, Zustände des Spiels an den Client senden und vom Client
    empfangen und verarbeiten (Also: Ansteuern + Synchronisieren gleichzeitig)
 *
 * Dies macht er mit Hilfe von 3 Tochterthreads: "Receive_Thread_Server", "Send_Thread_Server" und "Analyze_Thread_Server"
 * 
 * 1. Game_Thread_Server selber: Spiel ansteuern
 * 
 * 2. 3 Tochterthreads: Synchronisieren, und zwar
 * 
 * +) Send_Thread_Server: Wichtigste Daten an den Client schicken
 * 
 * +) Receive_Thread_Server: Daten vom Client empfangen
 * 
 * +) Analyze_Thread_Server: Die empfangenen Daten vom Client ab-und verarbeiten 
 * 
 * Der Ablauf sollte so aussehen:
 * 
 * 1. Game_Thread_Server steuert das Spiel an
 * 
 * 2. Send_Thread_Server schickt Daten im Laufe des Spiels pausenlos an den Client, unter anderem auch die "ORDERS" Befehle + andere 
       Signale: diese sind im "other_infos_stack" gelagert!
 *
 * 3. Receive_Thread_Server empfängt ständig, ohne Pause, Daten vom Client, verarbeitet diese aber noch nicht, sondern legt sie in den 
       "messages_from_client_stack" ab, für "Analyze_Thread_Server"
 *
 * 4. Analyze_Thread_Server arbeitet den "messages_from_client_stack" ab!
 * @author Minh Tue
 */
public class Game_Thread_Server extends Thread {
    private Ball ball;
    private Player player_1;
    private Player player_2;
    private Server server;
    private Referee controller;
    /**
     * Beim Multiplayer-Spielende diese Anzeige zum Beenden des Spiels
     */
    private Group exit_pane_multiplayer; 
    private Game_Graphic_Components grap_viewer;
    /**
     * Tochterthread
     */
    private Receive_Thread_Server receive_thread;
    /**
     * Tochterthread
     */
    private Send_Thread_Server send_thread; 
    /**
     * Tochterthread
     */
    private Analyze_Thread_Server analyze_thread; 
    /**
     * Dieser Stack lagert die "ORDERS" + Farben- Punktenänderung Signale zum Senden an den Client ein!
     * 
     * Grund: Anders als die "BALL_POSITION_X_Y" und die "SERVER_PLAYER_POSITION", die ständig im Laufe des Spiels zur Synchronisation an den Client
    gesendet werden müssen (und daher der "Send_Thread_Server"), kommen solche Signale eher spontan und vereinzelt vor (wenn der Server-Spieler 
    auf "PAUSE" drückt, "ENTER" zum Anstarten, oder bei nem Tor, "Actions",..) 
     *
     * Außerdem können diese Signale nicht von dem UI-Thread durch "server.send(byte[] byte_data)" geschickt werden, 
    da "Send_Thread_Server" die Methode nicht erst nach dem Ende des Spiels freigibt!
     *
     * --> "server.send(byte[] byte_data)"  =  kritische Stelle, Konkurrenz zwischen UI-Thread und "Send_Thread_Server"
     * 
     * --> Lösung: Die Signale erstmal in diesen "other_infos_stack" lagern, und dieser "Send_Thread_Server" arbeitet den Stack ab, 
    nachdem er in einem Durchlauf schon die "BALL_POSITION_X_Y" + "SERVER_PLAYER_POSITION" an den Client geschickt hat!
     */
    private Stack<String> other_infos_stack;
    /**
     * Dieser Stack lagert die vom Client durch "Receive_Thread_Server" empfangenen 
    Nachrichten ein! Er wird vom "Analyze_Thread_Server" im Laufe des Spiels abgearbeitet!
     */
    private Stack<byte[]> messages_from_client_stack; 
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
     * @param server
     * @param controller
     * @param grap_viewer
     * @param exit_pane_multiplayer
     */
    public Game_Thread_Server(Ball ball, Player player_1, Player player_2, Server server, Referee controller,
                                Game_Graphic_Components grap_viewer, Group exit_pane_multiplayer){
        this.ball = ball;
        this.player_1 = player_1;
        this.player_2 = player_2;
        this.server = server;
        this.controller = controller;
        this.grap_viewer = grap_viewer;
        this.exit_pane_multiplayer = exit_pane_multiplayer;
        // Komposition: Wenn dieser Mutterthread stirbt, sterben auch die Töchter "receive_thread" + "send_thread" + "analyze_thread"
        this.receive_thread = new Receive_Thread_Server(server, this);
        this.send_thread = new Send_Thread_Server(server, ball, player_1, this);
        this.analyze_thread = new Analyze_Thread_Server(player_2, grap_viewer, this);
        other_infos_stack = new Stack<>();
        messages_from_client_stack = new Stack<>();
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
     * @param server_ready
     */
    public void setServer_ready(boolean server_ready) {
        this.server_ready = server_ready;
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
     * @return
     */
    public boolean isServer_ready() {
        return server_ready;
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
    public boolean isGame_ended() {
        return game_ended;
    }
    
    /**
     *
     * @return
     */
    public Stack<String> getOther_infos_stack() {
        return other_infos_stack;
    }

    /**
     *
     * @return
     */
    public Stack<byte[]> getMessages_from_client_stack() {
        return messages_from_client_stack;
    }
    
    /** 
     * Hier läuft der Game_Thread_Server! Der Thread empfängt einerseits ständig die Nachrichten vom Client
    (durch Tochter "Receive_Thread_Server"), steckt diese Nachrichten in den Stack "messages_from_client_stack" ein, und verarbeitet
    sie durch Tochterthread "Analyze_Thread_Server". 
     * 
     * Andererseits schickt er auch die wichtigsten Infos an den Client (durch Tochter 
    "Send_Thread_Server") und steuert das Spiel an! 
    */
    @Override
    public void run(){
        // Die Tochterthreads auch mitstarten!
        receive_thread.start();
        send_thread.start();
        analyze_thread.start();
        
        while(!game_ended){
            /* 4 Threads (dieser + 3 Töchter) laufen gleichzeitig, um das Spiel anzutreiben 
               --> Thread.sleep(25), um Überlastung an dem CPU zu vermeiden!
            */
            try {
                Thread.sleep(25);
            }
            catch (InterruptedException ex) {

            }
            // Dieser "If" läuft solange bei jedem Durchlauf, bis ne neue Runde anfängt! Oder bis das Spiel pausiert wird
            if(server_ready && client_ready){
                // Der Server Referee hat volle Kontrolle auf den Ball!
                // Platform.runLater(), weil es um Veränderung auf der UI (UI-Thread) geht!
                Platform.runLater(() -> {
                    ball.move();
                    controller.evaluate_in_Multiplayer_host();        
                });              
                if(controller.a_round_ended_Multiplayer()){
                    // Der Ball wird vom "Controller Host" bei jedem "Tor" zurückgestellt -> kein Reset nötig!
                    Platform.runLater(() -> {
                        //Reset die "Actions-Listen" auf der UI
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
                    // Spiel zu Ende -> Tötet den "Game_thread"
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
                // Die Punkt-, Satzlabels auf der UI immer auf dem neuesten Stand halten
                Platform.runLater(() -> {
                    grap_viewer.update_point_satz_Multiplayer();
                });  
            }
        }
    }   
}
