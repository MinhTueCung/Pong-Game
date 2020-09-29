package org.openjfx.ea_aufgabe_2_pong;

import Model.Client;
import Model.Player;
import Resources.Packet_Types;

/**
 * Dieser Thread ist auch ein Tochterthread vom "Game_Thread_Client", analog zu "Send_Thread_Server". 
    Er übernimmt die Aufgabe: Daten vom Client (Player_2 Position, "ORDERS"-Befehle) im Laufe des Spiels an den Server zu verschicken.
 */
public class Send_Thread_Client extends Thread {
    private Client client;
    private Player player_2;
    /**
     * zur Kommunikation mit dem Parent_Thread "Game_Thread_Client"
     */
    private Game_Thread_Client mother_thread; 
    
    /**
     *
     * @param client
     * @param player_2
     * @param mother_thread
     */
    public Send_Thread_Client(Client client, Player player_2, Game_Thread_Client mother_thread){
        this.client = client;
        this.player_2 = player_2; 
        this.mother_thread = mother_thread;
        // Lasse den Thread das Stoppen der JVM nicht verhindern
        this.setDaemon(true);
    }
    
    /**
     * Dieser Thread läuft ohne Pause bis zum Spielende! Er übermittelt die Player_2 (Client) Position an den Server, 
        um das Spiel möglichst synchronisiert zu halten!
     */
    @Override
    public void run(){
        while(!mother_thread.isGame_ended()){
            String this_player_position = Packet_Types.CLIENT_PLAYER_POSITION.getPacket_id() + player_2.getPlayer_graphic().getY();
            // Zwischen den Sendungen kurze Pause machen (durch Thread.sleep(10)) Aus demselben Grund wie beim "Send_Thread_Server"
            client.send(this_player_position.getBytes());
            try {
                Thread.sleep(10);
            } 
            catch (InterruptedException ex) {

            }
            // Die "ORDERS"-Befehle im Stack von der Mutter "Game_Thread_Client" abarbeiten!
            if(!mother_thread.getOrders_stack().empty()){
                client.send(mother_thread.getOrders_stack().pop().getBytes());
            }
        }
    }
}
