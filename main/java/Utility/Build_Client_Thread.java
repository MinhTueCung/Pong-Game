package Utility;

import Model.Client;
import java.io.IOException;
import java.net.DatagramPacket;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.paint.Color;

/**
 *  Dieser Thread ist nur zur einmaligen Nutzung. Seine Aufgabe: Neben dem Main UI-Thread, im Hintergrund die Existenz einer 
    "Client-Server" Verbindung prüfen. Er macht dies durch das Senden einer "Beta"-Nachricht (in diesem Fall seines eigenen 
    Client-Spielername) an den Server + Empfangen die selbe Art von "Beta"-Nachricht (Server-Spielername) von dem Server. Wenn eine
    UDP-Verbindung zwischen Server und Client besteht (korrekte IP-Adresse + die andere Seite ist erreichbar), bekommt er diese Beta-Nachricht
    vom Server und umgekehrt. Wenn nein, bekommt er gar nichts.
 *
 *  Beta-Nachricht (Server Spielername) empfangen -> sofort anzeigen auf der Main-UI Szene
    Nichts empfangen -> auch anzeigen "Connection Failed" + "Try Again"
 */
public class Build_Client_Thread extends Thread {
    private Client client;
    /*Der "Build_Client_Thread" hat Zugriff auf die UI-Komponente, um direkt Fehlermeldung oder ähnliches auf der UI-Szene
      anzeigen zu können (Aggregation)
    */
    private Label multiplayer_join_message;
    private ProgressIndicator pi;
    private Button ok_or_try_again_button;
    private Button back_button;
    
    /**
     * @param client
     * @param multiplayer_join_message
     * @param pi
     * @param ok_or_try_again_button
     * @param back_button
     */
    public Build_Client_Thread(Client client, Label multiplayer_join_message, ProgressIndicator pi,
                                Button ok_or_try_again_button, Button back_button){
        this.client = client;
        this.multiplayer_join_message = multiplayer_join_message;
        this.pi = pi;
        this.ok_or_try_again_button = ok_or_try_again_button;
        this.back_button = back_button;
        // Lass den Thread das Stoppen der JVM nicht verhindern (wenn etwas schiefläuft -> der Benutzer drückt "Exit")
        this.setDaemon(true);
    }
    
    /**
     * Die Seite Client schickt die Beta-Nachricht an den Server zuerst, dann fängt sie an, auch die Nachricht vom Server zu empfangen
     */
    @Override
    public void run(){
        for(int zaehler = 0; zaehler < 30; zaehler++){
            //Schicke den Namen des Clients, und zwar so lange, bis der Zähler den Wert 30 erreicht hat (30 mal)
            client.send(client.getPlayer_name().getBytes());
        }
        receive_beta_message_from_server();
    }
    
    private void receive_beta_message_from_server(){
        byte[] message_from_server = new byte[80];
        DatagramPacket packet = new DatagramPacket(message_from_server, message_from_server.length);
        try{
            //Warten 15 s maximal auf das Paket. Wenn die Zeit abläuft -> IO Exception geworfen -> "Connection Failed" und dies auf der UI anzeigen 
            client.getSocket().setSoTimeout(15000); 
            client.getSocket().receive(packet);
            // "trim()" zum Entfernen der Leerzeichen (leere Bytes im byte[] Inhalt)
            String server_name = new String(packet.getData()).trim();
            // Verbindung besteht -> auf der UI anzeigen 
            /* Immer "Platform.runLater()", wenn es um Komponente auf der UI-Szene geht
               Grund: Die interaktive UI-Szene gehört zum Main UI-Thread des Programms. Dieser Thread läuft ununterbrechlich, um die Szene
               interaktiv (auf Benutzer-Inputs reagieren) zu halten
               --> andere Threads im Hintergrund könnten diesen und damit das ganze Programm stören, wenn sie versuchen, die interkativen 
               UI-Komponente zu verändern. Dies muss ausschließlich durch den Main UI-Thread erfolgen
            */
            Platform.runLater(() -> {
                multiplayer_join_message.setTextFill(Color.WHITE);
                multiplayer_join_message.setText("Connected with player: " + server_name);
                pi.setProgress(1.0f);
                ok_or_try_again_button.setText("OK");
                ok_or_try_again_button.setVisible(true);
            });
            // Der Client registriert den Namen des Gegeners sofort
            client.setServer_name(server_name);
            // Der Server-IP ist bereits eingegeben!
        }
        catch(IOException ex){
            // Connection Failed -> auch anzeigen
            Platform.runLater(() -> {
                multiplayer_join_message.setText("Connection failed!");
                multiplayer_join_message.setTextFill(Color.RED);
                pi.setVisible(false);
                ok_or_try_again_button.setText("Try again");
                ok_or_try_again_button.setVisible(true);
                back_button.setVisible(true);
            });
        }
    }
}
