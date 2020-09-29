package org.openjfx.ea_aufgabe_2_pong;

import Model.Client;

/**
 * Dieser Thread ist ein Tochterthread vom "Game_Thread_Client", analog zu "Receive_Thread_Server". 
    Er übernimmt die Aufgabe: Daten vom Server zu empfangen und sie in den "message_from_server_stack" vom Mutterthread abzulegen,
    für spätere Verarbeitung vom "Analyze_Thread_Client".
 */
public class Receive_Thread_Client extends Thread {
    private Client client;
    /**
     * zur Kommunikation mit dem Parent_Thread "Game_Thread_Client"
     */
    private Game_Thread_Client mother_thread; 
    
    /**
     *
     * @param client
     * @param mother_thread
     */
    public Receive_Thread_Client(Client client, Game_Thread_Client mother_thread){
        this.client = client;
        this.mother_thread = mother_thread;
        // Lasse den Thread das Stoppen der JVM nicht verhindern
        this.setDaemon(true);
    }
    
    /**
     * Dieser Thread läuft ständig bis zum Spielende, also empfängt Daten vom Server pausenlos!
     */
    @Override
    public void run(){
        while(!mother_thread.isGame_ended()){
            byte[] byte_data = client.receive();
            if(byte_data != null){
                mother_thread.getMessages_from_server_stack().add(byte_data);
            }
        }
    }
}
