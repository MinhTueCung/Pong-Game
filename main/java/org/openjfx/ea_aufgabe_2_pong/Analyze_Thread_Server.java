package org.openjfx.ea_aufgabe_2_pong;

import Model.Player;
import Resources.Constants;
import Resources.Packet_Types;
import View.Game_Graphic_Components;
import javafx.application.Platform;

/**
 * Dieser Thread ist quasi ein Tochterthread vom "Game_Thread_Server". Er übernimmt die Aufgabe: Daten-Pakete vom Client zu entschlüsseln
    und verarbeiten im Laufe des Spiels.
 */
public class Analyze_Thread_Server extends Thread {
    private Player player_2;
    private Game_Graphic_Components grap_viewer;
    /**
     * zur Kommunikation mit dem Mutterthread "Game_Thread_Server"
     */
    private Game_Thread_Server mother_thread; 
    
    /**
     *
     * @param player_2
     * @param grap_viewer
     * @param mother_thread
     */
    public Analyze_Thread_Server(Player player_2, Game_Graphic_Components grap_viewer, Game_Thread_Server mother_thread){
        this.player_2 = player_2;
        this.grap_viewer = grap_viewer;
        this.mother_thread = mother_thread;
        // Lasse den Thread das Stoppen der JVM nicht verhindern
        this.setDaemon(true);
    }
    
    /**
     * Dieser Thread läuft ständig, also verarbeitet Daten vom Client pausenlos, bis zum Spielende, um auf jedes Befehl 
        vom Client zu reagieren (z.B: "Fortsetzen", "Pause", ...). Daten werden vom "messages_from_client_stack" der Mutter 
        "Game_Thread_Server" rausgenommen!
     */
    @Override
    public void run(){
        while(!mother_thread.isGame_ended()){
            if(!mother_thread.getMessages_from_client_stack().empty()){
                analyze(mother_thread.getMessages_from_client_stack().pop());
            }
        }
    }
    
    /**
     * Alle Daten zum Austauschen zwischen Server und Client sind standardweise Strings, aber in Form von byte[]!
     * 
     * Daten = Strings = Packet_Types IP (2 erste Ziffern) + Infos
     * 
     * Der Client sollte nur seine "Player_2" Position + "ORDERS" Befehle (Pause, Actions,...) im Laufe des Spiels zur 
        Synchronisation an den Server schicken! (siehe mehr von "Packet_Types" Klasse)
     *
     * Das Spiel selbst kontrolliert der Server (Ball, Punktevergabe,...)
     * @param byte_data 
     */
    private void analyze(byte[] byte_data){
        String data = new String(byte_data).trim();
        String data_ID = data.substring(0, 2);
        
        if(data_ID.equals(Packet_Types.CLIENT_PLAYER_POSITION.getPacket_id())){
            double oppo_position_Y = Double.parseDouble(data.substring(2));
            // Platform.runLater(), weil player_2 Grafik auf der UI-Szene ist
            Platform.runLater(() -> {
                player_2.getPlayer_graphic().setY(oppo_position_Y);
            });
        }
        if(data_ID.equals(Packet_Types.ORDERS.getPacket_id())){
            switch(data.substring(2)){
                case Constants.PAUSE_ORDER:
                    // Wenn der Client "ready" ist -> "Pause" vom Client pausiert das Spiel, ansonsten setzt das Spiel fort!
                    if(mother_thread.isClient_ready()){
                        // Auf der UI-anzeigen
                        Platform.runLater(() -> {
                            grap_viewer.getPaused_message().setVisible(true);
                        });
                        //dem Mutterthread "Game_Thread_Server" das Signal mitteilen!
                        mother_thread.setClient_ready(false);
                    }
                    else{
                        Platform.runLater(() -> {
                            grap_viewer.getPaused_message().setVisible(false);
                        });
                        mother_thread.setClient_ready(true);
                    }
                    break;
                case Constants.ACTION_ORDER:
                    // "Action"-Befehl vom Client
                    if(!player_2.getActions().isEmpty()){
                        Platform.runLater(() -> {
                            player_2.getPlayer_graphic().setFill(player_2.getActions().remove(0).getColor());
                            grap_viewer.getPlayer_2_actions_box().getChildren().remove(0);
                        });
                    }
                    break;
                case Constants.READY_ORDER: //"Enter" zum Starten Befehl
                    mother_thread.setClient_ready(true);
                    /* Wenn der Server selbst auch ready ist -> die "Press Enter to start" Nachricht verschwinden 
                    und das Spiel laufen lassen! Ansonsten die Nachricht zum "Player_1 not ready" überschreiben!
                    */
                    if(mother_thread.isServer_ready()){
                        Platform.runLater(() -> {
                            grap_viewer.getPush_enter_to_start().setText(Constants.PUSH_ENTER_TO_START_MESSAGE);
                            grap_viewer.getPush_enter_to_start().setVisible(false);
                        });
                    }
                    else{
                        Platform.runLater(() -> {
                            grap_viewer.getPush_enter_to_start().setText(Constants.WAIT_FOR_PLAYER_1_MESSAGE);
                        });
                    }
                    break;
            }
        }
    }
}
