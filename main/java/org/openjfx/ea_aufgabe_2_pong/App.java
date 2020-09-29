package org.openjfx.ea_aufgabe_2_pong;

import Model.Ball;
import Model.Bot;
import Model.Client;
import Model.Server;
import Model.Player;
import Model.Player_Infos;
import Resources.Constants;
import Resources.Database;
import Resources.Packet_Types;
import Utility.Build_Client_Thread;
import Utility.Build_Game_Thread;
import Utility.Build_Server_Thread;
import Utility.Game_Builder;
import Utility.Highscores_Menu_Builder;
import Utility.Host_IP_Address_Finder;
import Utility.InStream_and_OutStream;
import Utility.Instructions_Set_Builder;
import Utility.Settings_Menu_Builder;
import View.Background;
import View.Game_Graphic_Components;
import View.Waiting_Lounge;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Die Main-App
 */
public class App extends Application {
    
    private Stage window;
    /**
     * Die Datenbank
     */
    private Database database; 
    /**
     * Die Szene, nur für das Spiel reserviert! Diese erhält Kontrollfunktionen (Mausbewegung,
    Tasten drücken,...), damit der Benutzer das Spiel kontrollieren kann
     */
    private Scene game_scene; 
    /**
     * Eine andere Szene, für anderen Kram!
     */
    private Scene default_scene; 
    /**
     * Für das Spiel
     */
    private Group the_game; 
    /**
     * Game Thread SinglePlayer
     */
    private Game_Thread game_thread; 
    private Game_Thread_Server game_thread_server;
    private Game_Thread_Client game_thread_client;
    /**
     * hat Zugriff auf die UI-Komponente
     */
    private Game_Graphic_Components grap_viewer;
    /**
     * Beim Singleplayer-Spielende diese Anzeige zum Beenden + Speichern Spieler-Profil 
     */
    private Group save_profile_pane; 
    /**
     * Beim Multiplayer-Spielende diese Anzeige zum Beenden des Spiels
     */
    private Group exit_pane_multiplayer;
    
    private Ball ball;
    private Bot bot;
    private Player player_1;
    private Player player_2;
    private Referee controller;
    private Group start_menu;
    
    private VBox player_name_input;
    /**
     * Zum Anzeigen des Multiplayer Moduses
     */
    private Group multiplayer_mode; 
    /**
     * Zum Anzeigen von Multiplayer Host Modus oder Join - 2 in 1
     */
    private Group multiplayer_host_or_join_mode; 
    private Server server;
    private Client client;
    /**
     * Settings für Multiplayer-Modus, für beide Server + Client
     */
    private Group settings_menu_server_or_client; 

    private VBox instructions_set;
    private Group settings_menu;
    private VBox highscores_menu;
    /**
     * Die "Highscores" Tabelle, zum Anzeigen auf der UI
     */
    private TableView<Player_Infos> highscores_table;
    
    /**
     *
     */
    @Override
    public void init(){
        create_Database();
        instantiate();
        Highscores_Menu_Builder.build(highscores_table, highscores_menu, default_scene, start_menu);
        Settings_Menu_Builder.build(database, default_scene, start_menu, settings_menu);
        Instructions_Set_Builder.build(default_scene, start_menu, instructions_set);
        build_multiplayer_mode();
        build_player_name_input();
        build_start_menu();
    }
    
    /**
     *
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        window = stage;
        window.setResizable(false);
        window.setScene(default_scene);
        window.show();
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }
    
    private void create_Database(){
        database = new Database(InStream_and_OutStream.read_Highscores_from_Backup_file());
    }
    
    private void build_start_menu(){
        //Zeichne Hintergrund
        start_menu.getChildren().add(new Background());
        
        
        //Baue das Menü
        //Der Name
        Label the_name = new Label("P.O.N.G");
        the_name.setTextFill(Color.ORANGE);
        the_name.setFont(new Font(Constants.FONT, 70));
        //"Single Player" Button
        Button single_player_button = new Button("Single Player");
        single_player_button.setMinSize(150, 60);
        single_player_button.setOnAction(eve -> {
            default_scene.setRoot(player_name_input);
        });
        //"Multiplayer" Button
        Button multiplayer_button = new Button("Multiplayer");
        multiplayer_button.setMinSize(150, 60);
        multiplayer_button.setOnAction(e -> {
            default_scene.setRoot(multiplayer_mode);
            
        });
        //"Instructions" Button
        Button instructions_button = new Button("Instructions");
        instructions_button.setMinSize(150, 60);
        instructions_button.setOnAction(an_event -> {
            default_scene.setRoot(instructions_set);
        });
        //"Settings" Button
        Button settings_button = new Button("Settings");
        settings_button.setMinSize(150, 60);
        settings_button.setOnAction(even -> {
            default_scene.setRoot(settings_menu);
        });
        //"Highscores" Button
        Button highscores_button = new Button("Highscores");
        highscores_button.setMinSize(150, 60);
        highscores_button.setOnAction(event -> {
            highscores_table.setItems(FXCollections.observableArrayList(database.getHighscores()));
            default_scene.setRoot(highscores_menu);
        });
        //Füge alle in eine Menübox hinzu
        VBox menu = new VBox(Constants.SPACING_BETWEEN_NODES_IN_START_MENU);
        menu.getChildren().addAll(the_name, single_player_button, multiplayer_button, instructions_button, settings_button, highscores_button);
        menu.setAlignment(Pos.CENTER);
        //Menübox -> das Ganze
        start_menu.getChildren().add(menu);
        
        
        //Bewege das Menübox in die Mitte
        menu.setLayoutX(Constants.COORD_X_OF_START_MENU_RELATIVE_TO_SCENE);
        menu.setLayoutY(Constants.COORD_Y_OF_START_MENU_RELATIVE_TO_SCENE);
    }
    
    private void instantiate(){
        start_menu = new Group();
        default_scene = new Scene(start_menu, Constants.GAME_FIELD_WIDTH, Constants.GAME_FIELD_HEIGHT); //
        player_name_input = new VBox(180);
        player_name_input.setPadding(new Insets(20, 20, 20, 20));
        multiplayer_mode = new Group();
        instructions_set = new VBox(70);
        instructions_set.setPadding(new Insets(20, 20, 20, 20));
        settings_menu = new Group();
        highscores_menu = new VBox(40);
        highscores_menu.setPadding(new Insets(20, 20, 20, 20));
        highscores_table = new TableView<>();
    }
    
    private void build_multiplayer_mode(){
        //Hintergrund
        Background background = new Background();
        
        Label enter_your_name = new Label(Constants.REQUEST_FOR_NAME);
        enter_your_name.setTextFill(Color.WHITE);
        enter_your_name.setFont(new Font(Constants.FONT, 30));
        
        //Name-Input Eingabefeld, hier den Spielername zum Bauen des Spiels entnehmen
        TextField name_input_multiplayer = new TextField();
        name_input_multiplayer.setMaxWidth(500);
        name_input_multiplayer.setMinHeight(40);
        name_input_multiplayer.setPromptText("Your name");
        
        Button host_button = new Button("Host");
        host_button.setMinSize(100, 50);
        host_button.setOnAction(event -> {
            //Der Button reagiert nur, wenn ein Name schon angegeben ist
            if(name_input_multiplayer.getText().length() > 0){
                build_multiplayer_host_mode();
                default_scene.setRoot(multiplayer_host_or_join_mode);
                server = new Server(name_input_multiplayer.getText());
                //
            }
        });
        
        Button join_button = new Button("Join");
        join_button.setMinSize(100, 50);
        join_button.setOnAction(ev -> {
            if(name_input_multiplayer.getText().length() > 0){
                build_multiplayer_join_mode();
                default_scene.setRoot(multiplayer_host_or_join_mode);
                client = new Client(name_input_multiplayer.getText());
                //
            }
        });
        
        Button back_button = new Button("Back");
        back_button.setMinSize(100, 50);
        back_button.setOnAction(eve -> {
            name_input_multiplayer.clear();
            default_scene.setRoot(start_menu);
        });
        
        VBox content = new VBox(70);
        content.getChildren().addAll(enter_your_name, name_input_multiplayer, host_button, join_button, back_button);
        content.setAlignment(Pos.CENTER);
        content.setTranslateX(Constants.GAME_FIELD_WIDTH/2 - 145);
        content.setTranslateY(100);
        
        multiplayer_mode.getChildren().addAll(background, content);
    }
    
    private void build_multiplayer_host_mode(){
        multiplayer_host_or_join_mode = new Group();
        
        //Hintergrund
        Background background = new Background();
        
        Label multiplayer_host_message = new Label();
        multiplayer_host_message.setFont(new Font(Constants.FONT, 20));
        multiplayer_host_message.setTextFill(Color.WHITE);
        multiplayer_host_message.setTranslateY(80);
        multiplayer_host_message.setTranslateX(Constants.GAME_FIELD_WIDTH/2 - 120);

            
        multiplayer_host_message.setText("Host IP is: " + Host_IP_Address_Finder.ipv4_lan());
        
        ProgressIndicator pi = new ProgressIndicator(-1.0f);
        pi.setVisible(false);

        Button host_button = new Button("Host");
        host_button.setMinSize(100, 50);
        Button ok_or_try_again_button = new Button("OK");
        ok_or_try_again_button.setMinSize(100, 50);
        ok_or_try_again_button.setVisible(false);
        Button back_button = new Button("Back");
        back_button.setMinSize(100, 50);
        
        host_button.setOnAction(event -> {
            // Button, anklicken zum Hosten
            // Prozess anzeigen
            pi.setVisible(true);
            host_button.setVisible(false);
            back_button.setVisible(false);
            // Hosten, durch "Build_Server_Thread" (Senden + Empfangen einer Beta Nachricht)
            Build_Server_Thread server_build_thread = new Build_Server_Thread(server, multiplayer_host_message, 
                                                            pi, ok_or_try_again_button, back_button);
            server_build_thread.start();
            
        });

        
        ok_or_try_again_button.setOnAction(eve -> {
            if(ok_or_try_again_button.getText().equals("OK")){
                // Verbindung wurde hergestellt! -> Zeige die "Einstellung" Szene für das gemeinsame Spiel
                settings_menu_server_or_client = new Group();
                Settings_Menu_Builder.build_for_server(this, database, settings_menu_server_or_client);
                default_scene.setRoot(settings_menu_server_or_client);
            }
            if(ok_or_try_again_button.getText().equals("Try again")){
                multiplayer_host_message.setText("Host IP is: " + Host_IP_Address_Finder.ipv4_lan());
                
                multiplayer_host_message.setTextFill(Color.WHITE);
                pi.setVisible(true);
                ok_or_try_again_button.setVisible(false);
                back_button.setVisible(false);
                // den Build_Server_Thread neu erstellen und laufen lassen
                Build_Server_Thread server_build_thread = new Build_Server_Thread(server, multiplayer_host_message, 
                                                            pi, ok_or_try_again_button, back_button);
                server_build_thread.start();
            }
        });
        back_button.setOnAction(e -> {
            default_scene.setRoot(multiplayer_mode);
            multiplayer_host_or_join_mode = null;
            server = null;
        });
        
        VBox content = new VBox(50);
        content.getChildren().addAll(pi, host_button, ok_or_try_again_button, back_button);
        content.setAlignment(Pos.CENTER);
        content.setTranslateX(Constants.GAME_FIELD_WIDTH/2 - 50);
        content.setTranslateY(Constants.GAME_FIELD_HEIGHT/2 - 70);
        
        multiplayer_host_or_join_mode.getChildren().addAll(background, multiplayer_host_message, content);
    }
    
    private void build_multiplayer_join_mode(){
        // Fast aus demselben Prinzip wie "build_multiplayer_host_mode"
        multiplayer_host_or_join_mode = new Group();
        
        //Hintergrund
        Background background = new Background();
        
        Label multiplayer_join_message = new Label("Enter the host IP you want to connect with");
        multiplayer_join_message.setFont(new Font(Constants.FONT, 20));
        multiplayer_join_message.setTextFill(Color.WHITE);
        multiplayer_join_message.setTranslateY(80);
        multiplayer_join_message.setTranslateX(Constants.GAME_FIELD_WIDTH/2 - 170);
        
        // Für die Eingabe vom Server-IP
        TextField ip_input = new TextField();
        ip_input.setMinWidth(300);
        ip_input.setMinHeight(40);
        ip_input.setPromptText("IP Host");
        ip_input.setTranslateX(Constants.GAME_FIELD_WIDTH/2 - 150);
        ip_input.setTranslateY(150);
        
        ProgressIndicator pi = new ProgressIndicator(-1.0f);
        pi.setVisible(false);
        
        Button join_button = new Button("Join");
        join_button.setMinSize(100, 50);
        Button ok_or_try_again_button = new Button("OK");
        ok_or_try_again_button.setMinSize(100, 50);
        ok_or_try_again_button.setVisible(false);
        Button back_button = new Button("Back");
        back_button.setMinSize(100, 50);
        
        join_button.setOnAction(event -> {
            //Reagiert nur, wenn etwas schon da als "Server-IP" steht
            if(ip_input.getText().length() > 0){
                try {
                    // Versuche, den IP von dem Eingabefeld als "Server IP" zu setzen! 
                    // 1. Wenn IP gültig ist -> client.serServer_ip sofort für spätere Verwendungen
                    /* 2. Wenn nein -> InetAddress.getByName(ip_input.getText()) wirft "UnknownHostException" -> Dieser Block Code 
                          wird unterbrochen und springt sofort zum "catch(UnknownHostException ex)"
                    */
                    client.setServer_ip(InetAddress.getByName(ip_input.getText()));
                    ip_input.setVisible(false);
                    join_button.setVisible(false);
                    back_button.setVisible(false);
                    pi.setVisible(true);
                    Build_Client_Thread client_build_thread = new Build_Client_Thread(client, multiplayer_join_message, pi, 
                                                                        ok_or_try_again_button, back_button);
                    client_build_thread.start();
                    
                } catch (UnknownHostException ex) {
                    multiplayer_join_message.setText("IP not found or invalid!");
                    multiplayer_join_message.setTextFill(Color.RED);
                }
            }   
        });
        ok_or_try_again_button.setOnAction(eve -> {
            if(ok_or_try_again_button.getText().equals("OK")){
                settings_menu_server_or_client = new Group();
                Settings_Menu_Builder.build_for_client(this, database, settings_menu_server_or_client);
                default_scene.setRoot(settings_menu_server_or_client);
            }
            if(ok_or_try_again_button.getText().equals("Try again")){
                multiplayer_join_message.setText("Enter the host IP you want to connect with");
                multiplayer_join_message.setTextFill(Color.WHITE);
                ip_input.clear();
                ip_input.setVisible(true);
                join_button.setVisible(true);
                ok_or_try_again_button.setVisible(false);
            }
        });
        back_button.setOnAction(e -> {
            default_scene.setRoot(multiplayer_mode);
            multiplayer_host_or_join_mode = null;
            client = null;
        });
        
        VBox content = new VBox(50);
        content.getChildren().addAll(pi, join_button, ok_or_try_again_button, back_button);
        content.setAlignment(Pos.CENTER);
        content.setTranslateX(Constants.GAME_FIELD_WIDTH/2 - 50);
        content.setTranslateY(Constants.GAME_FIELD_HEIGHT/2 - 70);
        
        multiplayer_host_or_join_mode.getChildren().addAll(background, multiplayer_join_message, ip_input, content);
    }
    
    /**
     * Spielernameeingabe für SinglePlayer, vor dem Spielantritt
     */
    private void build_player_name_input(){
        Label request_for_name = new Label(Constants.REQUEST_FOR_NAME);
        request_for_name.setFont(new Font(Constants.FONT, 32));
        
        
        TextField name_input_field = new TextField();
        name_input_field.setMaxWidth(700);
        name_input_field.setMinHeight(50);
        name_input_field.setPromptText("Your name");
        
        
        Button submit_button = new Button("Submit");
        submit_button.setMinSize(100, 60);
        Button cancel_button = new Button("Cancel");
        cancel_button.setMinSize(100, 60);
        Button clear_button = new Button("Clear");
        clear_button.setMinSize(100, 60);
        cancel_button.setOnAction(event -> {
            request_for_name.setText(Constants.REQUEST_FOR_NAME);
            request_for_name.setTextFill(Color.BLACK);
            name_input_field.clear();
            submit_button.setText("Submit");
            default_scene.setRoot(start_menu);
        });
        clear_button.setOnAction(ev -> {
            request_for_name.setText(Constants.REQUEST_FOR_NAME);
            request_for_name.setTextFill(Color.BLACK);
            name_input_field.clear();
            submit_button.setText("Submit");
        });
        submit_button.setOnAction(e -> {
            // Der Button reagiert nur, wenn etwas schon da als "Spielername" steht
            if(name_input_field.getText().length() > 0){
                // Wenn der Button den Namen "Submit", nicht "OK" trägt und der Spielername schon in Datenbank registriert ist 
                if(submit_button.getText().equals("Submit") && database.player_already_exists(name_input_field.getText())){
                    request_for_name.setText(Constants.PLAYER_EXISTS_MESSAGE);
                    request_for_name.setTextFill(Color.RED);
                    submit_button.setText("OK");
                }
                /* "OK" wird angezeigt, wenn der Spielername schon registriert ist und der User möchte sich trotzdem mit diesem Namen 
                   zum Spiel anmelden (siehe "Constants.PLAYER_EXISTS_MESSAGE")
                */
                else{
                    // Alles erledigt! -> Das Spiel bauen!
                    build_the_game_single_player(name_input_field.getText());
                    game_scene = new Scene(the_game);
                    window.setScene(game_scene);
                    provide_functionalities_to_game_scene();
                    default_scene.setRoot(start_menu);
                    //
                }
            }
        });
        HBox buttons = new HBox(300);
        buttons.getChildren().addAll(cancel_button, submit_button, clear_button);
        buttons.setAlignment(Pos.CENTER);
        
        
        player_name_input.getChildren().addAll(request_for_name, name_input_field, buttons);
        player_name_input.setAlignment(Pos.CENTER);
    }
    
    private void build_the_game_single_player(String player_name){
        the_game = new Group();
         
        Background game_field = new Background(Constants.MIDDLE_LINE_WIDTH);
        the_game.getChildren().add(game_field);
           
        //Komponente des Spiels instanziieren: Ball, Player, Bot,...
        ball = new Ball(database.getBall_speed());
        bot = new Bot(database.getBot_diffi_ratio() * database.getBall_speed()); //V(Bot) = k * V(Ball)
        player_1 = new Player(Constants.DISTANCE_FROM_PADDLE_TO_EDGE, player_name, database.getActions());
        controller = new Referee(ball, player_1, bot, database);
        
        grap_viewer = new Game_Graphic_Components(player_1, bot);
        grap_viewer.setup_the_actions_box_SinglePlayer();
        
        Game_Builder.build_player_1_infos_on_screen(the_game, player_1, grap_viewer.getPlayer_1_point(), 
                                                        grap_viewer.getPlayer_1_satz(), grap_viewer.getPlayer_1_actions_box());
        Game_Builder.build_bot_infos_on_screen(the_game, grap_viewer.getBot_point(), grap_viewer.getBot_satz());
        Game_Builder.build_other_components_on_screen(the_game, grap_viewer.getPush_enter_to_start(), grap_viewer.getPaused_message(),
                                                        grap_viewer.getWho_wins_message());
        
        //Exit-Pane (wenn man auf "Option"-Menü drückt)
        Group exit_confirm_pane = new Group();
        Background exit_confirm_pane_background = new Background(Constants.EXIT_CONFIRM_PANE_WIDTH, Constants.EXIT_CONFIRM_PANE_HEIGHT,
                                                                    Color.WHITE);
        Label exit_confirm_mess = new Label(Constants.EXIT_CONFIRM_MESS);
        exit_confirm_mess.setFont(new Font(Constants.FONT, 15));
        Button exit_confirm_yes = new Button("Yes");
        exit_confirm_yes.setMinSize(50, 25);
        exit_confirm_yes.setOnAction(eve -> {
            game_thread.setGame_ended(true);
            game_thread.setPaused(false);
            window.setScene(default_scene);
            game_scene = null;
            the_game = null;
        });
        Button exit_confirm_no = new Button("No");
        exit_confirm_no.setMinSize(50, 25);
        exit_confirm_no.setOnAction(ev -> {
            exit_confirm_pane.setVisible(false);
            grap_viewer.getPaused_message().setVisible(true);
            grap_viewer.getPush_enter_to_start().setVisible(false);
        });
        HBox buttons = new HBox(40);
        buttons.getChildren().addAll(exit_confirm_yes, exit_confirm_no);
        buttons.setAlignment(Pos.CENTER);
        VBox exit_confirm = new VBox(20);
        exit_confirm.getChildren().addAll(exit_confirm_mess, buttons);
        exit_confirm.setAlignment(Pos.CENTER);
        exit_confirm_pane.getChildren().addAll(exit_confirm_pane_background, exit_confirm);
        exit_confirm_pane.setTranslateX(Constants.GAME_FIELD_WIDTH/2 - 195);
        exit_confirm_pane.setTranslateY(Constants.GAME_FIELD_HEIGHT/2 - 80);
        exit_confirm_pane.setVisible(false);
        
        //"Option" Menü
        Menu options_box = new Menu("Option");
        MenuItem exit_option = new MenuItem("Exit");
        exit_option.setOnAction(event -> {
            game_thread.setGame_running(false);
            game_thread.setPaused(true);
            exit_confirm_pane.setVisible(true);     
        });
        options_box.getItems().add(exit_option);
        MenuBar menu_bar = new MenuBar();
        menu_bar.getMenus().add(options_box);
        menu_bar.setMaxSize(70, 20);
        menu_bar.setTranslateX(Constants.GAME_FIELD_WIDTH/2 - 34);
        menu_bar.setTranslateY(Constants.GAME_FIELD_HEIGHT/2 - 310);
        
        //Spielergebnis + Profil speichern am Ende des Spiels
        //Spielergebnis

        //Profil speichern 
        save_profile_pane = new Group();
        Background save_profile_pane_background = new Background(Constants.SAVE_PROFILE_PANE_WIDTH, Constants.SAVE_PROFILE_PANE_HEIGHT,
                                                                    Color.WHITE);
        Label save_confirm_mess = new Label(Constants.SAVE_CONFIRM_MESS);
        save_confirm_mess.setFont(new Font(Constants.FONT, 15));
        Button save_confirm_yes = new Button("Yes");
        save_confirm_yes.setMinSize(50, 25);
        save_confirm_yes.setOnAction(e -> {
            if(controller.bot_wins_the_game()){
                //Eine Niederlage wird immer gespeichert
                database.update_and_save_player_infos(player_1.getName(), false);
            }
            if(controller.player_1_wins_the_game()){
                database.update_and_save_player_infos(player_1.getName(), true);
            }
            save_profile_pane.setVisible(false);
            window.setScene(default_scene);
            game_scene = null;
            the_game = null;
        });
        Button save_confirm_no = new Button("No");
        save_confirm_no.setMinSize(50, 25);
        save_confirm_no.setOnAction(anevent -> {
            if(controller.bot_wins_the_game()){
                //Eine Niederlage wird immer gespeichert
                database.update_and_save_player_infos(player_1.getName(), false);
            }
            save_profile_pane.setVisible(false);
            window.setScene(default_scene);
            game_scene = null;
            the_game = null;      
        });
        HBox yes_no_buttons = new HBox(40);
        yes_no_buttons.getChildren().addAll(save_confirm_yes, save_confirm_no);
        yes_no_buttons.setAlignment(Pos.CENTER);
        VBox save_confirm = new VBox(20);
        save_confirm.getChildren().addAll(save_confirm_mess, yes_no_buttons);
        save_confirm.setAlignment(Pos.CENTER);
        save_profile_pane.getChildren().addAll(save_profile_pane_background, save_confirm);
        save_profile_pane.setTranslateX(Constants.GAME_FIELD_WIDTH/2 - 135);
        save_profile_pane.setTranslateY(Constants.GAME_FIELD_HEIGHT/2 - 100);
        save_profile_pane.setVisible(false);
              
        //Alle ins Spiel hinzufügen
        the_game.getChildren().addAll(ball.getBall_graphic(), bot.getBot_graphic(), player_1.getPlayer_graphic()
                                    , exit_confirm_pane, save_profile_pane, menu_bar);
        
        
        //Erzeuge den Hintergrundsthread "game_thread" als Hintergrundsplattform fürs Spiel und starte ihn an
        game_thread = new Game_Thread(ball, bot, controller, grap_viewer, player_1, save_profile_pane);
        game_thread.start();       
    } 
    
    /**
     * Hier werden erstmal nur verschiedene Komponente des Spiels initialisiert (Player, Ball, Controller,...)!
     * 
     * Das Bauen der sichtbaren UI des Spiels auf der Szene sowie die Übermittlung der Actions-Listen übernimmt ein externer Thread "Build_Game_Thread"
     */
    public void build_the_game_multiplayer_host(){
        // Baue erstmal das Wartezimmer (eine Szene) und dies anzeigen, während das Spiel gebaut wird
        Waiting_Lounge waiting_lounge = new Waiting_Lounge();
        
        //Dies anzeigen
        game_scene = new Scene(waiting_lounge);
        window.setScene(game_scene);
        default_scene.setRoot(start_menu);
        
        
        //Baue das Spiel im Hintergrund 
        the_game = new Group();
        
        Background game_field = new Background(Constants.MIDDLE_LINE_WIDTH);
        the_game.getChildren().add(game_field);
        
        ball = new Ball(database.getBall_speed());
        player_1 = new Player(Constants.DISTANCE_FROM_PADDLE_TO_EDGE, server.getPlayer_name(), database.getActions_server());
        // Um Player_2 (Client) zu erstellen, brauchen wir die Actions-Liste, gewählt von der Seite Client
        // Lösung: Erstell erstmal grob Player_2 mit Actions-Liste "null", dann aktualisiere diese Info durch Austauschen
        // zwischen Server und Client (Build_Game_Thread)
        player_2 = new Player(Constants.GAME_FIELD_WIDTH - Constants.DISTANCE_FROM_PADDLE_TO_EDGE - Constants.PADDLE_WIDTH, 
                                server.getClient_name(), null);
        controller = new Referee(ball, player_1, player_2, database);
        
        grap_viewer = new Game_Graphic_Components(player_1, player_2);
        // Noch nicht "setup" wie in "build_the_game_Singleplayer", weil player_2 Actions-Liste fehlt!
        
        // Baue auch den "Exit-Pane": zum Beenden des Spiels und zurück zum Startmenü
        exit_pane_multiplayer = new Group();
        Background exit_pane_multiplayer_background = new Background(Constants.SAVE_PROFILE_PANE_WIDTH, Constants.SAVE_PROFILE_PANE_HEIGHT,
                                                                        Color.WHITE);
        Label exit_mess = new Label(Constants.EXIT_MESS_MULTIPLAYER);
        exit_mess.setFont(new Font(Constants.FONT, 15));
        Button exit_button = new Button("Exit");
        exit_button.setMinSize(50, 25);
        exit_button.setOnAction(e -> {
            exit_pane_multiplayer.setVisible(false);
            window.setScene(default_scene);
            game_scene = null;
            the_game = null;
        });
        VBox exit_confirm = new VBox(20);
        exit_confirm.getChildren().addAll(exit_mess, exit_button);
        exit_confirm.setAlignment(Pos.CENTER);
        exit_pane_multiplayer.getChildren().addAll(exit_pane_multiplayer_background, exit_confirm);
        exit_pane_multiplayer.setTranslateX(Constants.GAME_FIELD_WIDTH/2 - 135);
        exit_pane_multiplayer.setTranslateY(Constants.GAME_FIELD_HEIGHT/2 - 100);
        exit_pane_multiplayer.setVisible(false);
        
        
        //Alle ins Spiel hinzufügen 
        the_game.getChildren().addAll(ball.getBall_graphic(), player_1.getPlayer_graphic(), player_2.getPlayer_graphic(), exit_pane_multiplayer);

        /* Erzeuge den Game_Thread, starten ihn aber erst, wenn alle Actions-Listen schon da sind  
           --> der "Build_Game_Thread" sollte entscheiden, wann der Game_Thread gestartet werden soll 
           (also gleich nachdem der "Build_Game_Thread" abgelaufen ist und die Listen da sind)
        */
        game_thread_server = new Game_Thread_Server(ball, player_1, player_2, server, controller, grap_viewer, exit_pane_multiplayer);
        /* Verbinde den Server-"Referee" mit dem "other_infos_stack" (game_thread_server), damit der Referee die Punkteänderung, Farben
           an den Client zwecks der Synchronisierung schicken kann!
        */
        controller.setOther_infos_stack_server(game_thread_server.getOther_infos_stack());
        
        // Synchron: Player_1 Actions-Liste an den Client schicken und Player_2 Actions-Liste vom Client bekommen!
        // Weil die Methode "Austauschen der Actions-Liste" solange laufen würde, bis die Listen synchronisiert wurden 
        // -> Wir brauchen nen externen Thread, um diese auszuführen, sonst blockiert die Methode den UI-Thread
        Build_Game_Thread thread = new Build_Game_Thread(server, player_1, player_2, database, game_scene, 
                                                        the_game, grap_viewer, game_thread_server, this);
        thread.start();
        
        // Der "game_scene" Funktionalitäten geben (UI-Kontrolle)
        // Guck in "Build_Game_Thread" nach
    }
    
    /**
     * Hier werden erstmal nur verschiedene Komponente des Spiels initialisiert (Player, Ball, Controller,...)!
     * 
     * Das Bauen der sichtbaren UI des Spiels auf der Szene sowie die Übermittlung der Actions-Listen übernimmt ein externer Thread "Build_Game_Thread"
     */
    public void build_the_game_multiplayer_join(){
        // Baue erstmal das Wartezimmer (eine Szene) und dies anzeigen, während das Spiel gebaut wird
        Waiting_Lounge waiting_lounge = new Waiting_Lounge();
        
        //Dies anzeigen
        game_scene = new Scene(waiting_lounge);
        window.setScene(game_scene);
        default_scene.setRoot(start_menu);
        
        
        //Baue das Spiel im Hintergrund 
        the_game = new Group();
        
        Background game_field = new Background(Constants.MIDDLE_LINE_WIDTH);
        the_game.getChildren().add(game_field);
        
        ball = new Ball(database.getBall_speed()); //Einfach einen Ball erzeugen, der Ball des Clients wird eigentlich vom Server kontrolliert
        // Um Player_1 (Server) zu erstellen, brauchen wir die Actions-Liste, gewählt von der Seite Server
        // Lösung: Erstell erstmal Player_1 mit Actions-Liste "null" (Grobstruktur), dann aktualisiere diese Info durch Austauschen
        // zwischen Server und Client
        player_1 = new Player(Constants.DISTANCE_FROM_PADDLE_TO_EDGE, client.getServer_name(), null);
        player_2 = new Player(Constants.GAME_FIELD_WIDTH - Constants.DISTANCE_FROM_PADDLE_TO_EDGE - Constants.PADDLE_WIDTH, 
                                client.getPlayer_name(), database.getActions_client());
        controller = new Referee(ball, player_1, player_2, database);
        
        grap_viewer = new Game_Graphic_Components(player_1, player_2);
        // Noch nicht "setup" wie in "build_the_game_Singleplayer", weil player_1 Actions-Liste fehlt!

        exit_pane_multiplayer = new Group();
        Background exit_pane_multiplayer_background = new Background(Constants.SAVE_PROFILE_PANE_WIDTH, Constants.SAVE_PROFILE_PANE_HEIGHT,
                                                                        Color.WHITE);
        Label exit_mess = new Label(Constants.EXIT_MESS_MULTIPLAYER);
        exit_mess.setFont(new Font(Constants.FONT, 15));
        Button exit_button = new Button("Exit");
        exit_button.setMinSize(50, 25);
        exit_button.setOnAction(e -> {
            exit_pane_multiplayer.setVisible(false);
            window.setScene(default_scene);
            game_scene = null;
            the_game = null;
        });
        VBox exit_confirm = new VBox(20);
        exit_confirm.getChildren().addAll(exit_mess, exit_button);
        exit_confirm.setAlignment(Pos.CENTER);
        exit_pane_multiplayer.getChildren().addAll(exit_pane_multiplayer_background, exit_confirm);
        
        exit_confirm.setTranslateX(100);
        
        exit_pane_multiplayer.setTranslateX(Constants.GAME_FIELD_WIDTH/2 - 135);
        exit_pane_multiplayer.setTranslateY(Constants.GAME_FIELD_HEIGHT/2 - 100);
        exit_pane_multiplayer.setVisible(false);
        
        
        //Alle ins Spiel hinzufügen 
        the_game.getChildren().addAll(ball.getBall_graphic(), player_1.getPlayer_graphic(), player_2.getPlayer_graphic(), exit_pane_multiplayer);

        /* Erzeuge den Game_Thread, starten ihn aber erst, wenn alle Actions-Listen schon da sind  
           --> der "Build_Game_Thread" sollte entscheiden, wann der Game_Thread gestartet werden soll 
           (also gleich nachdem der "Build_Game_Thread" abgelaufen ist und die Listen da sind)
        */
        game_thread_client = new Game_Thread_Client(ball, player_1, player_2, client, controller, grap_viewer, exit_pane_multiplayer);
        // Synchron: Player_2 Actions-Liste an den Server schicken und Player_1 Actions-Liste vom Server bekommen!
        // Weil die Methode "Austauschen der Actions-Liste" solange laufen würde, bis die Listen synchronisiert wurden 
        // -> Wir brauchen nen externen Thread, um diese auszuführen, sonst blockiert die Methode den UI-Thread
        Build_Game_Thread thread = new Build_Game_Thread(client, player_1, player_2, database, game_scene, 
                                                        the_game, grap_viewer, game_thread_client, this);
        thread.start();
        
        // Der "game_scene" Funktionalitäten geben (UI-Kontrolle)
        
    }
    
    private void provide_functionalities_to_game_scene(){
        game_scene.setOnKeyPressed(keyevent -> {
            // Auf "Move-Up" Taste reagieren 
            if(keyevent.getCode() == database.getMove_up_key() && player_1.getPlayer_graphic().getY() > 0){
                player_1.getPlayer_graphic().setY(player_1.getPlayer_graphic().getY() - Constants.PLAYER_PADDLE_SPEED);
            }
            // Auf "Move-Down"
            if(keyevent.getCode() == database.getMove_down_key() && player_1.getPlayer_graphic().getY() < Constants.GAME_FIELD_HEIGHT - Constants.PADDLE_HEIGHT){
                player_1.getPlayer_graphic().setY(player_1.getPlayer_graphic().getY() + Constants.PLAYER_PADDLE_SPEED);
            }
            // Auf "Pause"
            if(keyevent.getCode() == KeyCode.P){
                if(game_thread.isPaused()){
                    /*Boolean "paused" vom "game_thread" funktioniert hier wie ein Zähler. Ungerade Drückzahl von "P" 
                    pausiert das Spiel, gerade Drückzahl setzt das Spiel fort
                    */
                    game_thread.setGame_running(true);
                    game_thread.setPaused(false);
                    grap_viewer.getPaused_message().setVisible(false);
                }
                else{
                    game_thread.setGame_running(false);
                    game_thread.setPaused(true);
                    grap_viewer.getPaused_message().setVisible(true);
                }
            }
            // Auf "Action"
            if(keyevent.getCode() == database.getAction_key() && !(player_1.getActions().isEmpty())){
                player_1.getPlayer_graphic().setFill(player_1.getActions().remove(0).getColor());
                grap_viewer.getPlayer_1_actions_box().getChildren().remove(0);
            }
            // Auf "Enter" zum Starten
            if(keyevent.getCode() == KeyCode.ENTER){
                game_thread.setGame_running(true);
                grap_viewer.getPush_enter_to_start().setVisible(false);      
            }
        });
        game_scene.setOnMouseMoved(mouseevent -> {
            // Beweg die Paddle mit der Maus
            if(mouseevent.getY() <= Constants.GAME_FIELD_HEIGHT - Constants.PADDLE_HEIGHT){
                player_1.getPlayer_graphic().setY(mouseevent.getY());
            }
        });
    }
    
    /**
     *
     */
    public void provide_functionalities_to_game_scene_host_mode(){
        game_scene.setOnKeyPressed(keyevent -> {
            // Auf "Move-Up" Taste reagieren 
            if(keyevent.getCode() == database.getMove_up_key() && player_1.getPlayer_graphic().getY() > 0){
                player_1.getPlayer_graphic().setY(player_1.getPlayer_graphic().getY() - Constants.PLAYER_PADDLE_SPEED);
            }
            // Auf "Move-Down"
            if(keyevent.getCode() == database.getMove_down_key() && player_1.getPlayer_graphic().getY() < Constants.GAME_FIELD_HEIGHT - Constants.PADDLE_HEIGHT){
                player_1.getPlayer_graphic().setY(player_1.getPlayer_graphic().getY() + Constants.PLAYER_PADDLE_SPEED);
            }
            // Auf "Pause"
            if(keyevent.getCode() == KeyCode.P){
                if(game_thread_server.isServer_ready()){
                    //Server ist ready -> "P" pausiert das Spiel. Anschließend den "Pause"-Befehl an den Client schicken
                    game_thread_server.setServer_ready(false);
                    String pause_order = Packet_Types.ORDERS.getPacket_id() + Constants.PAUSE_ORDER;
                    // Schmeiß den "ORDERS"-Befehl erstmal in den "orders_stack" vom "Game_Thread_Server" ein
                    game_thread_server.getOther_infos_stack().add(pause_order);
                    grap_viewer.getPaused_message().setVisible(true);
                }
                else{
                    game_thread_server.setServer_ready(true);
                    // pause_order bedeutet diesmal "Fortsetzen". Dies wird vom Game_Thread seitens Client interpretiert!
                    String pause_order = Packet_Types.ORDERS.getPacket_id() + Constants.PAUSE_ORDER;
                    game_thread_server.getOther_infos_stack().add(pause_order);
                    grap_viewer.getPaused_message().setVisible(false);
                }
            }
            // Auf "Action"
            if(keyevent.getCode() == database.getAction_key() && !(player_1.getActions().isEmpty())){
                // "Action" ausführen und anschließend den "Action"-Befehl an den Client schicken!
                player_1.getPlayer_graphic().setFill(player_1.getActions().remove(0).getColor());
                grap_viewer.getPlayer_1_actions_box().getChildren().remove(0);
                String action_order = Packet_Types.ORDERS.getPacket_id() + Constants.ACTION_ORDER;
                game_thread_server.getOther_infos_stack().add(action_order);
            }
            // Auf "Enter" zum Starten
            if(keyevent.getCode() == KeyCode.ENTER){
                /* Den "game_thread_server" auf "server_ready" schalten, das Signal an den Client schicken, 
                und die Nachricht "Push Enter to start" auf der UI passend überschreiben oder verschwinden lassen  
                */
                game_thread_server.setServer_ready(true);
                String ready_order = Packet_Types.ORDERS.getPacket_id() + Constants.READY_ORDER;
                game_thread_server.getOther_infos_stack().add(ready_order);
                if(game_thread_server.isClient_ready()){
                    //Client ist auch bereit -> die Nachricht auf der UI verschwinden lassen 
                    grap_viewer.getPush_enter_to_start().setText(Constants.PUSH_ENTER_TO_START_MESSAGE);
                    grap_viewer.getPush_enter_to_start().setVisible(false);
                }
                else{
                    //Client ist noch nicht bereit -> die Nachricht auf der UI überschreiben 
                    grap_viewer.getPush_enter_to_start().setText(Constants.WAIT_FOR_PLAYER_2_MESSAGE);
                }
            }
        });
        game_scene.setOnMouseMoved(mouseevent -> {
            // Beweg die Paddle mit der Maus
            if(mouseevent.getY() <= Constants.GAME_FIELD_HEIGHT - Constants.PADDLE_HEIGHT){
                player_1.getPlayer_graphic().setY(mouseevent.getY());
            }
        });     
    }
    
    /**
     *
     */
    public void provide_functionalities_to_game_scene_join_mode(){
        game_scene.setOnKeyPressed(keyevent -> {
            // Auf "Move-Up" Taste reagieren 
            if(keyevent.getCode() == database.getMove_up_key() && player_2.getPlayer_graphic().getY() > 0){
                player_2.getPlayer_graphic().setY(player_2.getPlayer_graphic().getY() - Constants.PLAYER_PADDLE_SPEED);
            }
            // Auf "Move-Down"
            if(keyevent.getCode() == database.getMove_down_key() && player_2.getPlayer_graphic().getY() < Constants.GAME_FIELD_HEIGHT - Constants.PADDLE_HEIGHT){
                player_2.getPlayer_graphic().setY(player_2.getPlayer_graphic().getY() + Constants.PLAYER_PADDLE_SPEED);
            }
            // Auf "Pause"
            if(keyevent.getCode() == KeyCode.P){
                if(game_thread_client.isClient_ready()){
                    //Client ist ready -> "P" pausiert das Spiel. Anschließend den "Pause"-Befehl an den Server schicken
                    game_thread_client.setClient_ready(false);
                    String pause_order = Packet_Types.ORDERS.getPacket_id() + Constants.PAUSE_ORDER;
                    // Schmeiß den "ORDERS"-Befehl erstmal in den "orders_stack" vom "Game_Thread_Client" ein
                    game_thread_client.getOrders_stack().add(pause_order);
                    grap_viewer.getPaused_message().setVisible(true);
                }
                else{
                    game_thread_client.setClient_ready(true);
                    // pause_order bedeutet diesmal "Fortsetzen". Dies wird vom Game_Thread seitens Server interpretiert!
                    String pause_order = Packet_Types.ORDERS.getPacket_id() + Constants.PAUSE_ORDER;
                    game_thread_client.getOrders_stack().add(pause_order);
                    grap_viewer.getPaused_message().setVisible(false);
                }
            }
            // Auf "Action"
            if(keyevent.getCode() == database.getAction_key() && !(player_2.getActions().isEmpty())){
                // "Action" ausführen und anschließend den "Action"-Befehl an den Server schicken!
                player_2.getPlayer_graphic().setFill(player_2.getActions().remove(0).getColor());
                grap_viewer.getPlayer_2_actions_box().getChildren().remove(0);
                String action_order = Packet_Types.ORDERS.getPacket_id() + Constants.ACTION_ORDER;
                game_thread_client.getOrders_stack().add(action_order);
            }
            // Auf "Enter" zum Starten
            if(keyevent.getCode() == KeyCode.ENTER){
                /* Den "game_thread_client" auf "client_ready" schalten, das Signal an den Server schicken, 
                und die Nachricht "Push Enter to start" auf der UI passend überschreiben oder verschwinden lassen  
                */
                game_thread_client.setClient_ready(true);
                String ready_order = Packet_Types.ORDERS.getPacket_id() + Constants.READY_ORDER;
                game_thread_client.getOrders_stack().add(ready_order);
                if(game_thread_client.isServer_ready()){
                    //Server ist auch bereit -> die Nachricht auf der UI verschwinden lassen 
                    grap_viewer.getPush_enter_to_start().setText(Constants.PUSH_ENTER_TO_START_MESSAGE);
                    grap_viewer.getPush_enter_to_start().setVisible(false);
                }
                else{
                    //Server ist noch nicht bereit -> die Nachricht auf der UI überschreiben 
                    grap_viewer.getPush_enter_to_start().setText(Constants.WAIT_FOR_PLAYER_1_MESSAGE);
                }
            }
        });
        game_scene.setOnMouseMoved(mouseevent -> {
            // Beweg die Paddle mit der Maus
            if(mouseevent.getY() <= Constants.GAME_FIELD_HEIGHT - Constants.PADDLE_HEIGHT){
                player_2.getPlayer_graphic().setY(mouseevent.getY());
            }
        });
    }    
}