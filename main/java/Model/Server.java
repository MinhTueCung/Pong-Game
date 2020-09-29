package Model;

import Resources.Constants;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * analog zur Klasse "Client"
 */
public class Server {
    private String player_name;
    private DatagramSocket socket;
    private String client_name;
    private InetAddress client_ip;
    
    /**
     * @param player_name
     */
    public Server(String player_name){
        this.player_name = player_name;
        try {
            socket = new DatagramSocket(Constants.THIS_APP_PORT);
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        /* "client_name" + "client_ip" noch unbekannt! Erst beim Empfangen einer Beta-Nachricht von dem Server (beim Build_Server_Thread)
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
    public String getClient_name() {
        return client_name;
    }

    /**
     * @param client_name
     */
    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }
    
    /**
     * @param client_ip
     */
    public void setClient_ip(InetAddress client_ip) {
        this.client_ip = client_ip;
    }

    /**
     * @return
     */
    public InetAddress getClient_ip() {
        return client_ip;
    }

    /**
     * @return
     */
    public String getPlayer_name() {
        return player_name;
    }
    
    /**
     * @param content
     */
    public void send(byte[] content){
        DatagramPacket paket = new DatagramPacket(content, content.length, client_ip, Constants.THIS_APP_PORT); 
        try {
            socket.send(paket);
        } catch (IOException ex) {
            System.out.println("Server can´t send a package: IO Exception!");
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
            System.out.println("Server can´t receive a packet: IO Exception");
            return null;
        }
    }
}
