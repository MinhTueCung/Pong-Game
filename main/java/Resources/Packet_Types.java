package Resources;
/**
 * Beim Paket Empfangen die Art des Pakets identifizieren, damit der Empfänger den Inhalt des Pakets versteht!
 * 
 *  Jedes Paket hat dann außer dem Byte-Inhalt noch eine ID (String) zum Identifizieren. Der Empfänger würde sofort erkennen,
    zum welchen Zweck bzw an welches Objekt die gesendeten Infos ausgerichtet sind!
 * 
 *  Strings lassen sich sehr einfach per Netzwerk versenden und empfangen 
    --> Alle Pakete zum Austauschen haben standardweise einen String-Inhalt (natürlich in Form von byte[])
 *   
    String-Inhalt = Packet_Types ID (2-stellig) + Infos
 *   
    Idee: Zur besseren Synchronisation: der "Referee" seitens Server hat volle Kontrolle auf das Spiel (Aktionen wie z.B:
    Richtung des Balls ändern beim Reflektieren, Punkte bei einem Tor vergeben, Farben der Paddles + des Balls wechseln bei "Actions",...)
 *  
 * --> Implementierung: 
 *  
 *  +) Der Client sollte nur seine momentane Paddle-Position an den Server schicken, und der Server verarbeitet diese Info im Laufe des Spiels
        (Packet_Types CLIENT_PLAYER_POSITION("01"))
 *  
 *  +) Der Server-"Referee" kontrolliert das Spiel komplett! Im Laufe des Spiels schickt der Server die Ball-Infos (BALL_POSITION_X_Y("00"))
        und seine eigene momentane Position (SERVER_PLAYER_POSITION("10")) an den Client, zum Zweck der Synchronisation. 
 *  
 *  +) Abgesehen von den Positionen des Balls + 2 Paddles, die ständig synchronisiert werden müssen, tauchen die "ORDERS" Signale sowie 
        Punkteänderungen + Farben (sowohl von Serverspieler als auch von Client- + Ball) nur vereinzelt auf
        ("ORDERS" nur wenn die andere Seite die "Actions", "Pause", "Continue" Tasten drückt / Punkteänderung nur bei einem Tor / Farben
        nur beim Kontakt zwischen Ball + Paddle im "Action" Modus). Diese Informationen sind auch zu synchronisieren, aber nicht ständig
        und zu jedem Moment.
 *  
 *  --> Implementierung: Solche vereinzelte Signale erstmal in den "other_infos_stack" vom "Game_Thread_Server" 
        (oder "orders_stack" vom "Game_Thread_Client") ablegen und getrennt von anderen Signalen versenden!
        "Packet_Types" dafür sind: SERVER_POINT("11"), CLIENT_POINT("12"), SERVER_COLOR("21"), CLIENT_COLOR("13"), ORDERS("31"), BALL_COLOR("14")
 */
public enum Packet_Types {
    /**
     *
     */
    BALL_POSITION_X_Y("00"), 

    /**
     *
     */
    CLIENT_PLAYER_POSITION("01"), 

    /**
     *
     */
    SERVER_PLAYER_POSITION("10"), 

    /**
     *
     */
    SERVER_POINT("11"), 

    /**
     *
     */
    CLIENT_POINT("12"), 

    /**
     *
     */
    SERVER_COLOR("21"),

    /**
     *
     */
    CLIENT_COLOR("13"),

    /**
     *
     */
    ORDERS("31"),

    /**
     *
     */
    BALL_COLOR("14");
    
    private final String packet_id;
    
    private Packet_Types(String packet_id){
        this.packet_id = packet_id;
    }

    /**
     * @return
     */
    public String getPacket_id() {
        return packet_id;
    }  
}
