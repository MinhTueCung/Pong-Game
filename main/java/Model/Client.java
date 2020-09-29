package Model;

import Resources.Constants;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * "Client" + "Server" Klassen sind im Prinzip nur 2 "Sockets" zum Senden + Empfangen von Dateien, an den anderen und von dem anderen!
 */
public class Client {
    private String player_name; // Dieser Client Spielername (Client = Spieler 2 = "Join" Modus)
    private DatagramSocket socket;
    private String server_name; // Server Spielername (Server = Spieler 1 = "Host" Modus)
    private InetAddress server_ip;
    
    /**
     * @param player_name
     */
    public Client(String player_name){
        this.player_name = player_name;
        try {
            socket = new DatagramSocket(Constants.THIS_APP_PORT);
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        /* "server_name" + "server_ip" noch unbekannt! Erst beim Empfangen einer Beta-Nachricht von dem Server (beim Build_Client_Thread)
           werden diese Infos bekannt!
        */
    }

    /**
     * @return
     */
    public DatagramSocket getSocket() {
        return socket;
    }

    /**
     * @return
     */
    public String getPlayer_name() {
        return player_name;
    }

    /**
     * @return
     */
    public String getServer_name() {
        return server_name;
    }

    /**
     * @param server_name
     */
    public void setServer_name(String server_name) {
        this.server_name = server_name;
    }
    
    /**
     * @param server_ip
     */
    public void setServer_ip(InetAddress server_ip) {
        this.server_ip = server_ip;
    }
    
    /**
     * @param content
     */
    public void send(byte[] content){
        DatagramPacket paket = new DatagramPacket(content, content.length, server_ip, Constants.THIS_APP_PORT);
        try {
            socket.send(paket);
        } catch (IOException ex) {
            System.out.println("Client can´t send a package: IO Exception!");
        }
    }
    
    /**
     * @return
     */
    public byte[] receive(){
        byte[] byte_message = new byte[1024];
        DatagramPacket paket = new DatagramPacket(byte_message, byte_message.length);
        try {
            socket.setSoTimeout(1000); //Standard: 1 s für das Empfangen! 
            socket.receive(paket);
            return paket.getData();
        } catch (IOException ex) {
            System.out.println("Client can´t receive a packet: IO Exception");
            return null;
        }
    }
}
