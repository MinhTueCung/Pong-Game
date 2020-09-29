package Utility;

import Model.Client;
import Model.Player;
import Model.Server;
import Resources.Actions;
import Resources.Database;
import View.Game_Graphic_Components;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import org.openjfx.ea_aufgabe_2_pong.App;
import org.openjfx.ea_aufgabe_2_pong.Game_Thread_Client;
import org.openjfx.ea_aufgabe_2_pong.Game_Thread_Server;

/**
 * Dieser Thread ist zum Austauschen der "Actions"-Listen + Bauen der Komponente des Spiels auf der UI gedacht (einmalige Nutzung) 
 * 
 * Für beide Server + Client entworfen
 */
public class Build_Game_Thread extends Thread {
    private Server server;
    private Client client;
    private Player player_1;
    private Player player_2;
    private Database database;
    // Sobald alle Actions-Listen vorhanden sind, sollte das Spiel gestartet werden bzw angezeigt werden --> deswegen diese UI-Komponente
    /**
     * Die Game-Szene von "App.java"
     */
    private Scene game_scene; 
    /**
     * Für das Spiel (siehe "App.java")
     */
    private Group the_game; 
    /** 
     * Die Actions-Listen auf der UI-Szene auch bereitstellen, durch "grap_viewer", sobald alle Actions-Listen vorhanden sind!
     */
    private Game_Graphic_Components grap_viewer;
    /**
     * Der "Build_Game_Thread" entscheidet, wann der "Game_Thread" (für Server oder Client) gestartet werden soll (also gleich nachdem
       dieser Thread abgelaufen ist, die Actions-Listen von beiden Seiten schon da sind und das Spiel fertig aufgebaut ist)
     * 
     * --> daher diese Attribute, um die Kontrolle auf "Game_Thread" zu erlangen!
    */
    private Game_Thread_Client game_thread_client;
    private Game_Thread_Server game_thread_server;
    /** 
     * Auch zum Aufrufen von ein paar Methoden von "App.java"
     */
    private App main_app;
    
    /**
     * @param server
     * @param player_1
     * @param player_2
     * @param database
     * @param game_scene
     * @param the_game
     * @param grap_viewer
     * @param game_thread_server
     * @param main_app
     */
    public Build_Game_Thread(Server server, Player player_1, Player player_2, Database database, Scene game_scene, Group the_game, 
                                Game_Graphic_Components grap_viewer, Game_Thread_Server game_thread_server, App main_app){
        this.server = server;
        client = null;
        this.player_1 = player_1;
        this.player_2 = player_2;
        this.database = database;
        this.game_scene = game_scene;
        this.the_game = the_game;
        this.grap_viewer = grap_viewer;
        //Lasse den Thread das Stoppen der JVM nicht verhindern
        this.setDaemon(true);
        
        this.game_thread_server = game_thread_server;
        
        this.main_app = main_app;
    } // Für den Host (Server)
    
    /**
     * @param client
     * @param player_1
     * @param player_2
     * @param database
     * @param game_scene
     * @param the_game
     * @param grap_viewer
     * @param game_thread_client
     * @param main_app
     */
    public Build_Game_Thread(Client client, Player player_1, Player player_2, Database database, Scene game_scene, Group the_game,
                                Game_Graphic_Components grap_viewer, Game_Thread_Client game_thread_client, App main_app){
        this.client = client;
        server = null;
        this.player_1 = player_1;
        this.player_2 = player_2;
        this.database = database;
        this.game_scene = game_scene;
        this.the_game = the_game;
        this.grap_viewer = grap_viewer;
        this.setDaemon(true);
        
        this.game_thread_client = game_thread_client;
        
        this.main_app = main_app;
    } // Für den Join (Client)
    
    @Override
    public void run(){
        if(client == null){
            // Run für Server
            
            while(player_2.getActions() == null){
                // Laufen, bis der Player_2 die "Actions"-Liste vom Client bekommen hat!
                send_actions_list_String_to_opponent();
                receive_actions_list_String_from_opponent();
                if(player_2.getActions() != null){
                    // Alle Actions-Listen sind vorhanden -> Baue die Komponente (Punkte, Actions,...) auf der UI-Szene + Zeige das Spiel an!
                    grap_viewer.setup_the_actions_boxes_MultiPlayer();
                    
                    // Das Spiel "the_game" wird im Hintergrund gebaut -> Platform.runLater() hier nicht nötig
                    Game_Builder.build_player_1_infos_on_screen(the_game, player_1, grap_viewer.getPlayer_1_point(),
                                grap_viewer.getPlayer_1_satz(), grap_viewer.getPlayer_1_actions_box());
                    
                    Game_Builder.build_player_2_infos_on_screen(the_game, player_2, grap_viewer.getPlayer_2_point(),
                                grap_viewer.getPlayer_2_satz(), grap_viewer.getPlayer_2_actions_box());
                    
                    Game_Builder.build_other_components_on_screen(the_game, grap_viewer.getPush_enter_to_start(),
                                grap_viewer.getPaused_message(),grap_viewer.getWho_wins_message());
                        
                    /*  Das Spiel fertig gebaut -> Anzeigen auf der UI. Hier muss Platform.runLater() benutzt werden, 
                        weil die "game_scene" mit einem "Waiting_Lounge" gerade auf der interkativen UI-Szene angezeigt wird. 
                        "Build_Game_Thread" ist ein Thread, der das Spiel im Hintergrund baut
                    */
                    Platform.runLater(() -> {
                        game_scene.setRoot(the_game);
                    });
                    
                    // Starten den Game_Thread
                    game_thread_server.start();
                    // Der UI-Szene Funktionalitäten geben (Bewegung der Paddles durch Maus, Tasten, Actions-Tasten,...)
                    main_app.provide_functionalities_to_game_scene_host_mode();
                }
            }
        }
        else{
            // Run für Client
            
            boolean client_build_game_thread_running = true;
            int counter = 0;
            // Der Client sollte erstmal nur auf die Zusendung der Actions-Liste vom Server warten, zur besseren Synchronisation.
            // Nur erst nachdem er sie bekommen hat, schickt er seine eigene Client Actions-Liste an den Server
            // und zwar so lange, bis der "counter" Variable den Wert 30 erreicht!
            while(client_build_game_thread_running){
                if(player_1.getActions() == null){
                    receive_actions_list_String_from_opponent();
                }
                else{
                    // Actions-List für den Server vom Server bekommen -> Schick die Client-Liste an den Server
                    send_actions_list_String_to_opponent();
                    counter++;
                    if(counter == 30){
                        // "counter" hat den Wert 30 erreicht -> Beende diesen "Build_Game_Thread" und baue das Spiel!
                        client_build_game_thread_running = false;
                        // "grap_viewer" set up UI-Komponent für die 2 Actions-Listen
                        grap_viewer.setup_the_actions_boxes_MultiPlayer();
                        
                        // Baue die UI-Komponente auf der Szene und zeige das Spiel an!
                        Game_Builder.build_player_1_infos_on_screen(the_game, player_1, grap_viewer.getPlayer_1_point(), 
                                                        grap_viewer.getPlayer_1_satz(), grap_viewer.getPlayer_1_actions_box());
                        
                        Game_Builder.build_player_2_infos_on_screen(the_game, player_2, grap_viewer.getPlayer_2_point(), 
                                                        grap_viewer.getPlayer_2_satz(), grap_viewer.getPlayer_2_actions_box());

                        Game_Builder.build_other_components_on_screen(the_game, grap_viewer.getPush_enter_to_start(), 
                                                        grap_viewer.getPaused_message(),grap_viewer.getWho_wins_message());
                        
                        // Ziemlich analog zu dem Ablauf für den Server!
                        Platform.runLater(() -> {
                            game_scene.setRoot(the_game);
                        });
                           
                        // Starten den Game_Thread
                        game_thread_client.start();
                        
                        main_app.provide_functionalities_to_game_scene_join_mode();
                    }
                }
            }
        }
    }
    
    /**
     * die Actions-Liste in Form von String (eine Kette von Farbennamen der Actions) an den Gegener schicken!
     */
    private void send_actions_list_String_to_opponent(){
        if(client == null){
            // Für den Server
            List<Actions> player_1_server_Actions = player_1.getActions();
            StringBuilder player_1_actions_list_String = new StringBuilder();
            
            /* Das einzigartige Zeichen '&' anhängen als ID zum Unterscheiden der Actions-Liste String von anderen String-Paketen
               (Beta-Nachricht (Build_Server/Client_Thread), ...)
            */ 
            player_1_actions_list_String.append('&');
                    
            for(Actions an_action : player_1_server_Actions){
                player_1_actions_list_String.append(an_action.getName_of_the_Color());
            }
            server.send(player_1_actions_list_String.toString().getBytes());
        }
        else{
            // Für den Client, ganz ähnlich 
            List<Actions> player_2_client_Actions = player_2.getActions();
            StringBuilder player_2_actions_list_String = new StringBuilder();
            
            player_2_actions_list_String.append('&');
                          
            for(Actions an_action : player_2_client_Actions){
                player_2_actions_list_String.append(an_action.getName_of_the_Color());
            }
            client.send(player_2_actions_list_String.toString().getBytes());
        }
    }
    
    /**
     * Empfangen und Entschlüsseln den String von Seite des Gegners in eine Actions-Liste!
     * 
     * Client empfängt Liste vom Server, für den Server auf Client-Seite, und umgekehrt -> Synchronisation der 2 Actions-Listen
     */
    private void receive_actions_list_String_from_opponent(){
        if(client == null){
            // Für den Server
            byte[] data_from_opponent = server.receive();
            if(data_from_opponent != null){
                // trim() zum Entfernen von Leerzeichen!
                String actions_list_data_from_opponent = new String(data_from_opponent).trim();
                System.out.println("Receive Actions List String from Client: " + actions_list_data_from_opponent);
                
                /* Das einzigartige Zeichen '&' identifizieren
                   +) Wenn ja -> ist die Actions-Liste vom Gegener -> Entschlüsseln
                   +) Wenn nein -> Andere Info-Pakete -> einfach ignorieren
                */
                if(actions_list_data_from_opponent.charAt(0) == '&'){
                    // den String entschlüsseln und 2 identische Actions-Listen daraus konstruieren + speichern
                    // Eine Liste ist für den Spieler selbst "player_2.setActions()"
                    // Eine Liste ist zum Einspeichern in die Datenbank, für spätere Referenzen
                    /* Es sollen 2 verschiedene Listen sein (1 für den Spieler, 1 in Datenbank). Wenn es nur eine einzige Liste gäbe, 
                       und der Spieler kriegt sie, würde der Spieler sie im Laufe des Spiels verändern (durch "Actions" Modus)
                       --> Die eine Liste wird verändert, sogar wenn sie sich schon "sicher" in der Datenbank befindet (Call-by-Reference)
                       --> 2 identische Listen sind hier die Lösung
                       Es sollte kein Problem geben beim Abrufen der Liste von Datenbank durch "getActions_server()" / "getActions_client()"
                       da die 2 Methoden nur eine Kopie von der gespeicherten Liste ausspucken (siehe "Database" Klasse)
                    */
                    List<Actions> actions_list_opponent = new ArrayList<>();
                    List<Actions> actions_list_opponent_database = new ArrayList<>();
                    
                    for(int i = 1; i < actions_list_data_from_opponent.length(); i++){
                        switch(actions_list_data_from_opponent.charAt(i)){
                            case 'w': // "DEFAULT"-Actions (siehe "Actions" Klasse)
                                // Die 2 Listen sind inhaltlich komplett identisch!
                                actions_list_opponent.add(Actions.DEFAULT);
                                actions_list_opponent_database.add(Actions.DEFAULT);
                                break;
                            case 'b':
                                actions_list_opponent.add(Actions.ACCELERATE);
                                actions_list_opponent_database.add(Actions.ACCELERATE);
                                break;
                            case 'y':
                                actions_list_opponent.add(Actions.CHANGE_ORBIT);
                                actions_list_opponent_database.add(Actions.CHANGE_ORBIT);
                                break;
                            case 'f':
                                actions_list_opponent.add(Actions.FIREBALL);
                                actions_list_opponent_database.add(Actions.FIREBALL);
                                break;
                        }
                    }
                    // Speichern, für den Client-Spieler + in die Datenbank (für spätere Referenzen)
                    player_2.setActions(actions_list_opponent);
                    database.setActions_client(actions_list_opponent_database);
                } 
            }
        }
        else{
            // Für den Client
            // Ganz ähnlich zu für den Server
            byte[] data_from_opponent = client.receive();
            if(data_from_opponent != null){
                String actions_list_data_from_opponent = new String(data_from_opponent).trim();
                
                if(actions_list_data_from_opponent.charAt(0) == '&'){
                    // den String entschlüsseln und eine Actions-Liste daraus konstruieren + speichern
                    // Analog zu der Methode für den Server-Spieler
                    List<Actions> actions_list_opponent = new ArrayList<>();
                    List<Actions> actions_list_opponent_database = new ArrayList<>();
                    
                    for(int i = 1; i < actions_list_data_from_opponent.length(); i++){
                        switch(actions_list_data_from_opponent.charAt(i)){
                            case 'w':
                                actions_list_opponent.add(Actions.DEFAULT);
                                actions_list_opponent_database.add(Actions.DEFAULT);
                                break;
                            case 'b':
                                actions_list_opponent.add(Actions.ACCELERATE);
                                actions_list_opponent_database.add(Actions.ACCELERATE);
                                break;
                            case 'y':
                                actions_list_opponent.add(Actions.CHANGE_ORBIT);
                                actions_list_opponent_database.add(Actions.CHANGE_ORBIT);
                                break;
                            case 'f':
                                actions_list_opponent.add(Actions.FIREBALL);
                                actions_list_opponent_database.add(Actions.FIREBALL);
                                break;
                        }
                    }
                    // Speichern, für den Server + in die Datenbank (für spätere Referenzen)
                    player_1.setActions(actions_list_opponent);     
                    database.setActions_server(actions_list_opponent_database);
                }
            }
        }
    }
}
