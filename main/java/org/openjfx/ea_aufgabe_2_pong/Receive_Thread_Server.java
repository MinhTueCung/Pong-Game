package org.openjfx.ea_aufgabe_2_pong;

import Model.Server;

/**
 * Dieser Thread ist quasi ein Tochterthread vom "Game_Thread_Server". Er übernimmt die Aufgabe: Daten vom Client zu empfangen
    und sie in den "messages_from_client_stack" vom Mutterthread abzulegen, für spätere Verarbeitung vom "Analyze_Thread_Server".
 */
public class Receive_Thread_Server extends Thread {
    private Server server;
    /**
     * zur Kommunikation mit dem Parent_Thread "Game_Thread_Server"
     */
    private Game_Thread_Server mother_thread; 
    
    /**
     *
     * @param server
     * @param mother_thread
     */
    public Receive_Thread_Server(Server server, Game_Thread_Server mother_thread){
        this.server = server;
        this.mother_thread = mother_thread;
        // Lasse den Thread das Stoppen der JVM nicht verhindern
        this.setDaemon(true);
    }
    
    /**
     * Dieser Thread läuft ständig bis zum Spielende, also empfängt Daten vom Client pausenlos, um den Datenverlust so klein
        wie möglich zu halten
     */
    @Override
    public void run(){
        while(!mother_thread.isGame_ended()){
            byte[] byte_data = server.receive();
            if(byte_data != null){
                mother_thread.getMessages_from_client_stack().add(byte_data);
            }
        }
    }
}
