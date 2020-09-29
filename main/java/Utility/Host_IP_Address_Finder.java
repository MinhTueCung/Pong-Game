package Utility;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Diese Klasse ist zum Identifizieren der IP-Adresse des Hosts (Server im "Multiplayer") gedacht, durch NetworkInterface
 */
public class Host_IP_Address_Finder {
    /**
     *
     * @return
     */
    public static String ipv4_lan(){
        String ip = "Unknown IP";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while(interfaces.hasMoreElements()){
                NetworkInterface iface = interfaces.nextElement();
                //"home" 127.0.0.1 (Localhost) und inaktive Interfaces ausfiltern!
                if (iface.isLoopback() || !iface.isUp())
                    continue;
            
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    //IPv6 auch ausfiltern! Hier wird nur IPv4 (LAN) gesucht!
                    if (addr instanceof Inet6Address) 
                        continue;
            
                    ip = addr.getHostAddress();
                }
            }
        } 
        catch (SocketException ex) {

        }
        return ip;
    }
}
