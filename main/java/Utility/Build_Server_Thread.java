package Utility;

import Model.Server;
import java.io.IOException;
import java.net.DatagramPacket;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.paint.Color;

/**
 * Ganz analog zum "Build_Client_Thread"
 */
public class Build_Server_Thread extends Thread {
    private Server server;
    //Der "Build_Server_Thread" hat Zugriff auf die UI-Komponente, um direkt Fehlermeldung oder ähnliches auf der UI anzeigen zu können
    private Label multiplayer_host_message;
    private ProgressIndicator pi;
    private Button ok_or_try_again_button;
    private Button back_button;
    
    /**
     * @param server
     * @param multiplayer_host_message
     * @param pi
     * @param ok_or_try_again_button
     * @param back_button
     */
    public Build_Server_Thread(Server server, Label multiplayer_host_message, ProgressIndicator pi,
                                Button ok_or_try_again_button, Button back_button){
        this.server = server;
        this.multiplayer_host_message = multiplayer_host_message;
        this.pi = pi;
        this.ok_or_try_again_button = ok_or_try_again_button;
        this.back_button = back_button;
        this.setDaemon(true);
    }
    
    /**
     * Die Seite Server sollte zuerst nur auf die Beta-Nachricht vom Client warten (15 s)
     */
    @Override
    public void run(){
        receive_beta_message_from_client();
        if(server.getClient_ip() != null){
            // Nur wenn der Server das Beta-Paket mit Client-IP vom Client bekommen hat -> schickt er auch eine Beta-Nachricht an den Client
            String server_player_name = server.getPlayer_name();
            //Schicke den Namen des Clients, und zwar so lange, bis der Zähler den Wert 30 erreicht hat
            for(int zaehler = 0; zaehler < 30; zaehler++){
                server.send(server_player_name.getBytes());
            }
        }
    }
    
    private void receive_beta_message_from_client(){
        byte[] message_from_client = new byte[80];
        DatagramPacket packet = new DatagramPacket(message_from_client, message_from_client.length);
        try{
            //Warten maximal 15s auf das Paket vom Client
            server.getSocket().setSoTimeout(15000); 
            server.getSocket().receive(packet);
            String client_name = new String(packet.getData()).trim();
            Platform.runLater(() -> {
                multiplayer_host_message.setText("Connected with player: " + client_name);
                pi.setProgress(1.0f);
                ok_or_try_again_button.setText("OK");
                ok_or_try_again_button.setVisible(true);
            });
            //Speichere sofort die IP + Namen vom Client für spätere Verwendung
            server.setClient_ip(packet.getAddress());
            server.setClient_name(client_name);
        }
        catch (IOException ex) {
            System.out.println("Cannot receive packet from client: IO-Failure!");
            Platform.runLater(() -> {
                multiplayer_host_message.setText("Connection failed!");
                multiplayer_host_message.setTextFill(Color.RED);
                pi.setVisible(false);
                ok_or_try_again_button.setText("Try again");
                ok_or_try_again_button.setVisible(true);
                back_button.setVisible(true);
            });
        }
    }
}
