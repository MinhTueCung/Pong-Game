package org.openjfx.ea_aufgabe_2_pong;

import Model.Ball;
import Model.Player;
import Model.Server;
import Resources.Packet_Types;

/**
 * Dieser Thread ist ein Tochterthread vom "Game_Thread_Server". Er übernimmt die Aufgabe: Daten vom Server (SERVER_PLAYER_POSITION,
    BALL_POSITION_X_Y und andere Signale) im Laufe des Spiels an den Client zu verschicken.
 */
public class Send_Thread_Server extends Thread {
    private Server server;
    private Ball ball;
    private Player player_1;
    /**
     * zur Kommunikation mit dem Parent_Thread "Game_Thread_Server"
     */
    private Game_Thread_Server mother_thread; 
    
    /**
     *
     * @param server
     * @param ball
     * @param player_1
     * @param mother_thread
     */
    public Send_Thread_Server(Server server, Ball ball, Player player_1, Game_Thread_Server mother_thread){
        this.server = server;
        this.ball = ball;
        this.player_1 = player_1;
        this.mother_thread = mother_thread;
        // Lasse den Thread das Stoppen der JVM nicht verhindern
        this.setDaemon(true);
    }
    
    /**
     * Dieser Thread läuft ohne Pause bis zum Spielende! Er übermittelt die Informationen (oben genannt) an den Client, 
        um das Spiel möglichst synchronisiert zu halten!
     */
    @Override
    public void run(){
        while(!mother_thread.isGame_ended()){        
            String ball_position_X_Y_data = Packet_Types.BALL_POSITION_X_Y.getPacket_id() + ball.getBall_graphic().getCenterX() + 
                                        '|' + ball.getBall_graphic().getCenterY(); // '|' ist zum Trennen der X-Position und Y-Position gedacht
            String server_player_position = Packet_Types.SERVER_PLAYER_POSITION.getPacket_id() + player_1.getPlayer_graphic().getY();
            
            /* Zwischen den Sendungen kurze Pause machen (durch Thread.sleep(10)), weil sonst könnte der "Receive_Thread_Client" vom Client
               nicht mithalten (also dass er sicher alle gesendeten Pakete auch bekommt) -> Daten würden verloren gehen -> Nicht gut für die Synchro!
            */
            server.send(ball_position_X_Y_data.getBytes());
            try {
                Thread.sleep(10);
            } 
            catch (InterruptedException ex) {

            }
            server.send(server_player_position.getBytes());
            try {
                Thread.sleep(10);
            } 
            catch (InterruptedException ex) {

            }
            // Die anderen Signale ("ORDERS", Punkte, Farben) im "other_infos_stack" von der Mutter "Game_Thread_Server" abarbeiten!
            if(!mother_thread.getOther_infos_stack().empty()){
                server.send(mother_thread.getOther_infos_stack().pop().getBytes());
            }
        }
    }
}
