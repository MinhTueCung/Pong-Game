package Utility;

import Resources.Actions;
import Resources.Constants;
import Resources.Database;
import View.Background;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import org.openjfx.ea_aufgabe_2_pong.App;

/**
 * Zum Bauen der "Einstellungen" Szene
 * 
 * "build_for_server" und "build_for_client" haben im Prinzip fast dieselbe Struktur wie "build" (Singleplayer)
 *
 * Es ist also bei "build_for_server" und "build_for_client" nur "Copy-Paste" und die nicht vorhandenen Schichten in der Struktur weglassen 
 */
public class Settings_Menu_Builder {
    /**
     * Bauen für SinglePlayer
     * @param database
     * @param default_scene
     * @param start_menu
     * @param settings_menu
     */
    public static void build(Database database, Scene default_scene, Group start_menu, Group settings_menu){
        //Hintergrund
        Background background = new Background();
        
        
        //Baue die "Difficulty Ball/Bot" Schicht
        Label difficulty_message = new Label(Constants.DIFFICULTY_MESSAGE);
        difficulty_message.setFont(new Font(Constants.FONT, 25));
        difficulty_message.setTextFill(Color.WHITE);
        MenuBar difficulty_menu = new MenuBar();
        Menu ball_difficulty = new Menu("Ball");
        Menu bot_difficulty = new Menu("Bot");
        ToggleGroup ball_difficulty_toggle = new ToggleGroup();
        ToggleGroup bot_difficulty_toggle = new ToggleGroup();
        
        //Für "Ball"      
        RadioMenuItem ball_easy = new RadioMenuItem("Slow");
        RadioMenuItem ball_medium = new RadioMenuItem("Medium");
        RadioMenuItem ball_hard = new RadioMenuItem("Fast");
        ball_easy.setToggleGroup(ball_difficulty_toggle);
        ball_medium.setToggleGroup(ball_difficulty_toggle);
        ball_hard.setToggleGroup(ball_difficulty_toggle);
        
        //Synchronisieren "Ball_speed" von Datenbank mit Wahlmenü ("Default" Modus anzeigen)
        Synchronize_Database_Settings.ball_speed(database, ball_easy, ball_medium, ball_hard);
        ball_difficulty.getItems().addAll(ball_easy, ball_medium, ball_hard);
        
        //Für "Bot"
        RadioMenuItem bot_easy = new RadioMenuItem("Easy");
        RadioMenuItem bot_medium = new RadioMenuItem("Medium");
        RadioMenuItem bot_hard = new RadioMenuItem("Hard");
        bot_easy.setToggleGroup(bot_difficulty_toggle);
        bot_medium.setToggleGroup(bot_difficulty_toggle);
        bot_hard.setToggleGroup(bot_difficulty_toggle);
        
        //Dasselbe Prinzip wie bei "Ball"
        Synchronize_Database_Settings.bot_speed(database, bot_easy, bot_medium, bot_hard);
        bot_difficulty.getItems().addAll(bot_easy, bot_medium, bot_hard);
        difficulty_menu.getMenus().addAll(ball_difficulty, bot_difficulty);
        HBox diffi_box = new HBox(100);
        diffi_box.getChildren().addAll(difficulty_message, difficulty_menu);
        diffi_box.setAlignment(Pos.CENTER);
        
        
        //Baue die "Belegung der Tasten" Schicht
        Label set_controls_message = new Label(Constants.SET_CONTROLS_MESSAGE);
        set_controls_message.setFont(new Font(Constants.FONT, 25));
        set_controls_message.setTextFill(Color.WHITE);
        
        //"Up"-Taste
        Label enter_up_key_message = new Label(Constants.ENTER_UP_KEY_MESSAGE);
        enter_up_key_message.setFont(new Font(Constants.FONT, 17));
        enter_up_key_message.setTextFill(Color.WHITE);
        TextField enter_up_key_field = new TextField(database.getMove_up_key().getName()); //Default
        HBox up_key_box = new HBox(100);
        up_key_box.getChildren().addAll(enter_up_key_message, enter_up_key_field);
        up_key_box.setAlignment(Pos.CENTER);
        
        //"Down"-Taste
        Label enter_down_key_message = new Label(Constants.ENTER_DOWN_KEY_MESSAGE);
        enter_down_key_message.setFont(new Font(Constants.FONT, 17));
        enter_down_key_message.setTextFill(Color.WHITE);
        TextField enter_down_key_field = new TextField(database.getMove_down_key().getName()); //Default
        HBox down_key_box = new HBox(100);
        down_key_box.getChildren().addAll(enter_down_key_message, enter_down_key_field);
        down_key_box.setAlignment(Pos.CENTER);
        
        //"Action"-Taste
        Label enter_action_key_message = new Label(Constants.ENTER_ACTION_KEY_MESSAGE);
        enter_action_key_message.setFont(new Font(Constants.FONT, 17));
        enter_action_key_message.setTextFill(Color.WHITE);
        TextField enter_action_key_field = new TextField(database.getAction_key().getName()); //Default
        HBox action_key_box = new HBox(100);
        action_key_box.getChildren().addAll(enter_action_key_message, enter_action_key_field);
        action_key_box.setAlignment(Pos.CENTER);
        
        
        //Baue die "Set Actions" Schicht
        Label set_actions_message = new Label(Constants.SET_ACTIONS_MESSAGE);
        set_actions_message.setFont(new Font(Constants.FONT, 25));
        set_actions_message.setTextFill(Color.WHITE);
        
        //"Accelerate" Action
        Label accelerate = new Label(Constants.ACCELERATE_NAME);
        accelerate.setFont(new Font(Constants.FONT, 17));
        accelerate.setTextFill(Color.WHITE);
        Circle accelerate_symbol = Actions.ACCELERATE.getGraphic();
        MenuBar accelerate_amount_choose_menu = new MenuBar();
        RadioMenuItem accelerate_amount_0 = new RadioMenuItem("0");
        RadioMenuItem accelerate_amount_1 = new RadioMenuItem("1");
        RadioMenuItem accelerate_amount_2 = new RadioMenuItem("2");
        RadioMenuItem accelerate_amount_3 = new RadioMenuItem("3");
        ToggleGroup accelerate_amount_toggle = new ToggleGroup();
        accelerate_amount_0.setToggleGroup(accelerate_amount_toggle);
        accelerate_amount_1.setToggleGroup(accelerate_amount_toggle);
        accelerate_amount_2.setToggleGroup(accelerate_amount_toggle);
        accelerate_amount_3.setToggleGroup(accelerate_amount_toggle);
        
        //Synchronisieren mit Datenbank (Default)
        Synchronize_Database_Settings.accelerate(database, accelerate_amount_0, accelerate_amount_1, accelerate_amount_2, accelerate_amount_3);
        Menu accelerate_amount_choose = new Menu("Amount");
        accelerate_amount_choose.getItems().addAll(accelerate_amount_0, accelerate_amount_1, accelerate_amount_2, accelerate_amount_3);
        accelerate_amount_choose_menu.getMenus().add(accelerate_amount_choose);
        HBox accelerate_action_setting = new HBox(100);
        accelerate_action_setting.getChildren().addAll(accelerate, accelerate_symbol, accelerate_amount_choose_menu);
        accelerate_action_setting.setAlignment(Pos.CENTER);
        
        //"Change Orbit" Action -> Der gleiche Aufbau wie von "Accelerate"
        Label change_orbit = new Label(Constants.CHANGE_ORBIT_NAME);
        change_orbit.setFont(new Font(Constants.FONT, 17));
        change_orbit.setTextFill(Color.WHITE);
        Circle change_orbit_symbol = Actions.CHANGE_ORBIT.getGraphic();
        MenuBar change_orbit_amount_choose_menu = new MenuBar();
        RadioMenuItem change_orbit_amount_0 = new RadioMenuItem("0");
        RadioMenuItem change_orbit_amount_1 = new RadioMenuItem("1");
        RadioMenuItem change_orbit_amount_2 = new RadioMenuItem("2");
        RadioMenuItem change_orbit_amount_3 = new RadioMenuItem("3");
        ToggleGroup change_orbit_amount_toggle = new ToggleGroup();
        change_orbit_amount_0.setToggleGroup(change_orbit_amount_toggle);
        change_orbit_amount_1.setToggleGroup(change_orbit_amount_toggle);
        change_orbit_amount_2.setToggleGroup(change_orbit_amount_toggle);
        change_orbit_amount_3.setToggleGroup(change_orbit_amount_toggle);
        
        //Synchro
        Synchronize_Database_Settings.change_orbit(database, change_orbit_amount_0, change_orbit_amount_1, change_orbit_amount_2, change_orbit_amount_3);
        Menu change_orbit_amount_choose = new Menu("Amount");
        change_orbit_amount_choose.getItems().addAll(change_orbit_amount_0, change_orbit_amount_1, change_orbit_amount_2, change_orbit_amount_3);
        change_orbit_amount_choose_menu.getMenus().add(change_orbit_amount_choose);
        HBox change_orbit_action_setting = new HBox(100);
        change_orbit_action_setting.getChildren().addAll(change_orbit, change_orbit_symbol, change_orbit_amount_choose_menu);
        change_orbit_action_setting.setAlignment(Pos.CENTER);
        
        //"Fireball" Action
        Label fireball = new Label(Constants.FIREBALL_NAME);
        fireball.setFont(new Font(Constants.FONT, 17));
        fireball.setTextFill(Color.WHITE);
        Circle fireball_symbol = Actions.FIREBALL.getGraphic();
        MenuBar fireball_amount_choose_menu = new MenuBar();
        RadioMenuItem fireball_amount_0 = new RadioMenuItem("0");
        RadioMenuItem fireball_amount_1 = new RadioMenuItem("1"); // Fireball maximal nur 1
        ToggleGroup fireball_amount_toggle = new ToggleGroup();
        fireball_amount_0.setToggleGroup(fireball_amount_toggle);
        fireball_amount_1.setToggleGroup(fireball_amount_toggle);
        
        //Synchro
        Synchronize_Database_Settings.fireball(database, fireball_amount_0, fireball_amount_1);
        Menu fireball_amount_choose = new Menu("Amount");
        fireball_amount_choose.getItems().addAll(fireball_amount_0, fireball_amount_1);
        fireball_amount_choose_menu.getMenus().add(fireball_amount_choose);
        HBox fireball_action_setting = new HBox(100);
        fireball_action_setting.getChildren().addAll(fireball, fireball_symbol, fireball_amount_choose_menu);
        fireball_action_setting.setAlignment(Pos.CENTER);
        
        
        
        //Baue die "Back" and "Save" Buttons Schicht
        Button back_button = new Button("Back");
        back_button.setMinSize(100, 40);
        back_button.setOnAction(event -> {
            //Reset (Daten in Datenbank finden) und zurück zum Startmenü
            Synchronize_Database_Settings.ball_speed(database, ball_easy, ball_medium, ball_hard);
            Synchronize_Database_Settings.bot_speed(database, bot_easy, bot_medium, bot_hard);
            enter_up_key_field.setText(database.getMove_up_key().getName());
            enter_down_key_field.setText(database.getMove_down_key().getName());
            enter_action_key_field.setText(database.getAction_key().getName());
            set_actions_message.setText(Constants.SET_ACTIONS_MESSAGE);
            set_actions_message.setTextFill(Color.WHITE);
            Synchronize_Database_Settings.accelerate(database, accelerate_amount_0, accelerate_amount_1, accelerate_amount_2, accelerate_amount_3);
            Synchronize_Database_Settings.change_orbit(database, change_orbit_amount_0, change_orbit_amount_1, change_orbit_amount_2, change_orbit_amount_3);
            Synchronize_Database_Settings.fireball(database, fireball_amount_0, fireball_amount_1);
            default_scene.setRoot(start_menu);
        });
        Button save_button = new Button("Save");
        save_button.setMinSize(100, 40);
        save_button.setOnAction(eve -> {
            if(Check_Settings.problems_in_settings(accelerate_amount_toggle, change_orbit_amount_toggle, fireball_amount_toggle, 
                                                    enter_up_key_field, enter_down_key_field, enter_action_key_field)){
                // Wenn die vorgenommenen Einstellungen nicht gültig sind
                if(Check_Settings.total_number_actions_exceeds_3(accelerate_amount_toggle, change_orbit_amount_toggle, 
                                                                fireball_amount_toggle)){
                    set_actions_message.setText(Constants.INVALID_NUMBER_OF_ACTIONS_WARNING);
                    set_actions_message.setTextFill(Color.RED);
                }
                if(Check_Settings.up_key_invalid(enter_up_key_field)){
                    enter_up_key_field.setText(Constants.INVALID_KEY_INPUT_WARNING);
                }
                if(Check_Settings.down_key_invalid(enter_down_key_field)){
                    enter_down_key_field.setText(Constants.INVALID_KEY_INPUT_WARNING);
                }
                if(Check_Settings.action_key_invalid(enter_action_key_field)){
                    enter_action_key_field.setText(Constants.INVALID_KEY_INPUT_WARNING);
                }
            }
            else{
                // Alles ok! -> Alle Daten in Datenbank speichern und zum Schluss zurück zum Startmenü
                //"Difficulty Ball/Bot" speichern
                Synchronize_Database_Settings.save_ball_speed(database, ball_difficulty_toggle);
                Synchronize_Database_Settings.save_bot_speed(database, bot_difficulty_toggle);
                //die Tasten speichern
                Synchronize_Database_Settings.save_all_keys(database, enter_up_key_field, enter_down_key_field, enter_action_key_field);
                //die Actions speichern
                Synchronize_Database_Settings.save_Actions(database, accelerate_amount_toggle, 
                                                        change_orbit_amount_toggle, fireball_amount_toggle);
                //Warnung löschen
                set_actions_message.setText(Constants.SET_ACTIONS_MESSAGE);
                set_actions_message.setTextFill(Color.WHITE);
                //zurück zum Startmenü
                default_scene.setRoot(start_menu);
            }          
        });
        HBox buttons_box = new HBox(600);
        buttons_box.getChildren().addAll(back_button, save_button);
        buttons_box.setAlignment(Pos.CENTER);
        
        
        
        //Füge alle Schichten in "content" hinzu
        VBox content = new VBox(40);
        content.getChildren().addAll(diffi_box, set_controls_message, up_key_box, down_key_box, action_key_box, 
                                        set_actions_message, accelerate_action_setting, change_orbit_action_setting,
                                        fireball_action_setting, buttons_box);
        content.setAlignment(Pos.CENTER);
        content.setTranslateX(92);
        content.setTranslateY(25);
        
        
        
        settings_menu.getChildren().addAll(background, content);
    }
    
    /**
     * Bauen für MultiPlayer
     * 
     * Im Prinzip fast genauso wie für Singleplayer-Settingsmenü
     * 
     * Der Server Host kann noch extra die Ballgeschwindigkeit einstellen, Client kann dagegen nicht! Und der Server hat volle 
        Kontrolle auf den Ball im Spielverlauf
     * @param main_app
     * @param database
     * @param settings_menu_for_server
     */
    public static void build_for_server(App main_app, Database database, Group settings_menu_for_server){
        //Hintergrund
        Background background = new Background();
        
        
        
        //Baue die "Difficulty Ball" Schicht
        Label ball_diffi_message = new Label(Constants.DIFFICULTY_MESSAGE);
        ball_diffi_message.setFont(new Font(Constants.FONT, 25));
        ball_diffi_message.setTextFill(Color.WHITE);
        MenuBar ball_diffi_menu = new MenuBar();
        Menu ball_diffi = new Menu("Ball");
        ToggleGroup ball_diffi_toggle = new ToggleGroup();
        
        RadioMenuItem ball_easy = new RadioMenuItem("Slow");
        RadioMenuItem ball_medium = new RadioMenuItem("Medium");
        RadioMenuItem ball_hard = new RadioMenuItem("Fast");
        ball_easy.setToggleGroup(ball_diffi_toggle);
        ball_medium.setToggleGroup(ball_diffi_toggle);
        ball_hard.setToggleGroup(ball_diffi_toggle);
        
        //Synchronisieren "Ball_speed" von Datenbank mit Wahlmenü ("Default" Modus anzeigen)
        Synchronize_Database_Settings.ball_speed(database, ball_easy, ball_medium, ball_hard);
        ball_diffi.getItems().addAll(ball_easy, ball_medium, ball_hard);
        
        ball_diffi_menu.getMenus().add(ball_diffi);
        
        HBox diffi_box = new HBox(100);
        diffi_box.getChildren().addAll(ball_diffi_message, ball_diffi_menu);
        diffi_box.setAlignment(Pos.CENTER);
        
        
        
        //Baue die "Belegung der Tasten" Schicht
        Label set_controls_message = new Label(Constants.SET_CONTROLS_MESSAGE);
        set_controls_message.setFont(new Font(Constants.FONT, 25));
        set_controls_message.setTextFill(Color.WHITE);
        
        //"Up"-Taste
        Label enter_up_key_message = new Label(Constants.ENTER_UP_KEY_MESSAGE);
        enter_up_key_message.setFont(new Font(Constants.FONT, 17));
        enter_up_key_message.setTextFill(Color.WHITE);
        TextField enter_up_key_field = new TextField(database.getMove_up_key().getName()); //Default
        HBox up_key_box = new HBox(100);
        up_key_box.getChildren().addAll(enter_up_key_message, enter_up_key_field);
        up_key_box.setAlignment(Pos.CENTER);
        
        //"Down"-Taste
        Label enter_down_key_message = new Label(Constants.ENTER_DOWN_KEY_MESSAGE);
        enter_down_key_message.setFont(new Font(Constants.FONT, 17));
        enter_down_key_message.setTextFill(Color.WHITE);
        TextField enter_down_key_field = new TextField(database.getMove_down_key().getName()); //Default
        HBox down_key_box = new HBox(100);
        down_key_box.getChildren().addAll(enter_down_key_message, enter_down_key_field);
        down_key_box.setAlignment(Pos.CENTER);
        
        //"Action"-Taste
        Label enter_action_key_message = new Label(Constants.ENTER_ACTION_KEY_MESSAGE);
        enter_action_key_message.setFont(new Font(Constants.FONT, 17));
        enter_action_key_message.setTextFill(Color.WHITE);
        TextField enter_action_key_field = new TextField(database.getAction_key().getName()); //Default
        HBox action_key_box = new HBox(100);
        action_key_box.getChildren().addAll(enter_action_key_message, enter_action_key_field);
        action_key_box.setAlignment(Pos.CENTER);
        
        
        
        //Baue die "Set Actions" Schicht
        Label set_actions_message = new Label(Constants.SET_ACTIONS_MESSAGE);
        set_actions_message.setFont(new Font(Constants.FONT, 25));
        set_actions_message.setTextFill(Color.WHITE);
        
        //"Accelerate" Action
        Label accelerate = new Label(Constants.ACCELERATE_NAME);
        accelerate.setFont(new Font(Constants.FONT, 17));
        accelerate.setTextFill(Color.WHITE);
        Circle accelerate_symbol = Actions.ACCELERATE.getGraphic();
        MenuBar accelerate_amount_choose_menu = new MenuBar();
        RadioMenuItem accelerate_amount_0 = new RadioMenuItem("0");
        RadioMenuItem accelerate_amount_1 = new RadioMenuItem("1");
        RadioMenuItem accelerate_amount_2 = new RadioMenuItem("2");
        RadioMenuItem accelerate_amount_3 = new RadioMenuItem("3");
        ToggleGroup accelerate_amount_toggle = new ToggleGroup();
        accelerate_amount_0.setToggleGroup(accelerate_amount_toggle);
        accelerate_amount_1.setToggleGroup(accelerate_amount_toggle);
        accelerate_amount_2.setToggleGroup(accelerate_amount_toggle);
        accelerate_amount_3.setToggleGroup(accelerate_amount_toggle);
        
        accelerate_amount_1.setSelected(true); //Default
        Menu accelerate_amount_choose = new Menu("Amount");
        accelerate_amount_choose.getItems().addAll(accelerate_amount_0, accelerate_amount_1, accelerate_amount_2, accelerate_amount_3);
        accelerate_amount_choose_menu.getMenus().add(accelerate_amount_choose);
        HBox accelerate_action_setting = new HBox(100);
        accelerate_action_setting.getChildren().addAll(accelerate, accelerate_symbol, accelerate_amount_choose_menu);
        accelerate_action_setting.setAlignment(Pos.CENTER);
        
        //"Change Orbit" Action -> Der gleiche Aufbau wie von "Accelerate"
        Label change_orbit = new Label(Constants.CHANGE_ORBIT_NAME);
        change_orbit.setFont(new Font(Constants.FONT, 17));
        change_orbit.setTextFill(Color.WHITE);
        Circle change_orbit_symbol = Actions.CHANGE_ORBIT.getGraphic();
        MenuBar change_orbit_amount_choose_menu = new MenuBar();
        RadioMenuItem change_orbit_amount_0 = new RadioMenuItem("0");
        RadioMenuItem change_orbit_amount_1 = new RadioMenuItem("1");
        RadioMenuItem change_orbit_amount_2 = new RadioMenuItem("2");
        RadioMenuItem change_orbit_amount_3 = new RadioMenuItem("3");
        ToggleGroup change_orbit_amount_toggle = new ToggleGroup();
        change_orbit_amount_0.setToggleGroup(change_orbit_amount_toggle);
        change_orbit_amount_1.setToggleGroup(change_orbit_amount_toggle);
        change_orbit_amount_2.setToggleGroup(change_orbit_amount_toggle);
        change_orbit_amount_3.setToggleGroup(change_orbit_amount_toggle);
        
        change_orbit_amount_1.setSelected(true); //Default
        Menu change_orbit_amount_choose = new Menu("Amount");
        change_orbit_amount_choose.getItems().addAll(change_orbit_amount_0, change_orbit_amount_1, change_orbit_amount_2, change_orbit_amount_3);
        change_orbit_amount_choose_menu.getMenus().add(change_orbit_amount_choose);
        HBox change_orbit_action_setting = new HBox(100);
        change_orbit_action_setting.getChildren().addAll(change_orbit, change_orbit_symbol, change_orbit_amount_choose_menu);
        change_orbit_action_setting.setAlignment(Pos.CENTER);
        
        //"Fireball" Action
        Label fireball = new Label(Constants.FIREBALL_NAME);
        fireball.setFont(new Font(Constants.FONT, 17));
        fireball.setTextFill(Color.WHITE);
        Circle fireball_symbol = Actions.FIREBALL.getGraphic();
        MenuBar fireball_amount_choose_menu = new MenuBar();
        RadioMenuItem fireball_amount_0 = new RadioMenuItem("0");
        RadioMenuItem fireball_amount_1 = new RadioMenuItem("1"); // Fireball maximal nur 1
        ToggleGroup fireball_amount_toggle = new ToggleGroup();
        fireball_amount_0.setToggleGroup(fireball_amount_toggle);
        fireball_amount_1.setToggleGroup(fireball_amount_toggle);
        
        fireball_amount_1.setSelected(true); //Default
        Menu fireball_amount_choose = new Menu("Amount");
        fireball_amount_choose.getItems().addAll(fireball_amount_0, fireball_amount_1);
        fireball_amount_choose_menu.getMenus().add(fireball_amount_choose);
        HBox fireball_action_setting = new HBox(100);
        fireball_action_setting.getChildren().addAll(fireball, fireball_symbol, fireball_amount_choose_menu);
        fireball_action_setting.setAlignment(Pos.CENTER);
        
        
        
        //Baue die "Save" Button Schicht
        Button save_button = new Button("Save");
        save_button.setMinSize(100, 40);
        save_button.setOnAction(eve -> {
            if(Check_Settings.problems_in_settings(accelerate_amount_toggle, change_orbit_amount_toggle, fireball_amount_toggle, 
                                                    enter_up_key_field, enter_down_key_field, enter_action_key_field)){
                if(Check_Settings.total_number_actions_exceeds_3(accelerate_amount_toggle, change_orbit_amount_toggle, 
                                                                fireball_amount_toggle)){
                    set_actions_message.setText(Constants.INVALID_NUMBER_OF_ACTIONS_WARNING);
                    set_actions_message.setTextFill(Color.RED);
                }
                if(Check_Settings.up_key_invalid(enter_up_key_field)){
                    enter_up_key_field.setText(Constants.INVALID_KEY_INPUT_WARNING);
                }
                if(Check_Settings.down_key_invalid(enter_down_key_field)){
                    enter_down_key_field.setText(Constants.INVALID_KEY_INPUT_WARNING);
                }
                if(Check_Settings.action_key_invalid(enter_action_key_field)){
                    enter_action_key_field.setText(Constants.INVALID_KEY_INPUT_WARNING);
                }
            }
            else{
                //Alle Daten in Datenbank speichern und zum Schluss das Spiel bauen
                //"Difficulty Ball" speichern
                Synchronize_Database_Settings.save_ball_speed(database, ball_diffi_toggle);
                //die Tasten speichern
                Synchronize_Database_Settings.save_all_keys(database, enter_up_key_field, enter_down_key_field, enter_action_key_field);
                //die Actions speichern
                Synchronize_Database_Settings.save_Actions_Server(database, accelerate_amount_toggle, 
                                                        change_orbit_amount_toggle, fireball_amount_toggle);
                //Warnung löschen
                set_actions_message.setText(Constants.SET_ACTIONS_MESSAGE);
                set_actions_message.setTextFill(Color.WHITE);
                
                //Baue das Spiel
                main_app.build_the_game_multiplayer_host();
            }          
        });
        
        
        
        //Füge alle Schichten in "content" hinzu
        VBox content = new VBox(40);
        content.getChildren().addAll(diffi_box, set_controls_message, up_key_box, down_key_box, action_key_box, 
                                        set_actions_message, accelerate_action_setting, change_orbit_action_setting,
                                        fireball_action_setting, save_button);
        content.setAlignment(Pos.CENTER);
        content.setTranslateX(92);
        content.setTranslateY(25);
        
        
        
        settings_menu_for_server.getChildren().addAll(background, content);
    }
    
    /**
     * Im Prinzip fast genauso wie für Singleplayer-Settingsmenü
       Client kann die Ballgeschwindigkeit nicht einstellen! -> keine "Ball" Schicht auf der Einstellungsszene
     * @param main_app
     * @param database
     * @param settings_menu_for_client
     */
    public static void build_for_client(App main_app, Database database, Group settings_menu_for_client){
        //Hintergrund
        Background background = new Background();
        
        
        
        //Baue die "Belegung der Tasten" Schicht
        Label set_controls_message = new Label(Constants.SET_CONTROLS_MESSAGE);
        set_controls_message.setFont(new Font(Constants.FONT, 25));
        set_controls_message.setTextFill(Color.WHITE);
        
        //"Up"-Taste
        Label enter_up_key_message = new Label(Constants.ENTER_UP_KEY_MESSAGE);
        enter_up_key_message.setFont(new Font(Constants.FONT, 17));
        enter_up_key_message.setTextFill(Color.WHITE);
        TextField enter_up_key_field = new TextField(database.getMove_up_key().getName()); //Default
        HBox up_key_box = new HBox(100);
        up_key_box.getChildren().addAll(enter_up_key_message, enter_up_key_field);
        up_key_box.setAlignment(Pos.CENTER);
        
        //"Down"-Taste
        Label enter_down_key_message = new Label(Constants.ENTER_DOWN_KEY_MESSAGE);
        enter_down_key_message.setFont(new Font(Constants.FONT, 17));
        enter_down_key_message.setTextFill(Color.WHITE);
        TextField enter_down_key_field = new TextField(database.getMove_down_key().getName()); //Default
        HBox down_key_box = new HBox(100);
        down_key_box.getChildren().addAll(enter_down_key_message, enter_down_key_field);
        down_key_box.setAlignment(Pos.CENTER);
        
        //"Action"-Taste
        Label enter_action_key_message = new Label(Constants.ENTER_ACTION_KEY_MESSAGE);
        enter_action_key_message.setFont(new Font(Constants.FONT, 17));
        enter_action_key_message.setTextFill(Color.WHITE);
        TextField enter_action_key_field = new TextField(database.getAction_key().getName()); //Default
        HBox action_key_box = new HBox(100);
        action_key_box.getChildren().addAll(enter_action_key_message, enter_action_key_field);
        action_key_box.setAlignment(Pos.CENTER);
        
        
        
        //Baue die "Set Actions" Schicht
        Label set_actions_message = new Label(Constants.SET_ACTIONS_MESSAGE);
        set_actions_message.setFont(new Font(Constants.FONT, 25));
        set_actions_message.setTextFill(Color.WHITE);
        
        //"Accelerate" Action
        Label accelerate = new Label(Constants.ACCELERATE_NAME);
        accelerate.setFont(new Font(Constants.FONT, 17));
        accelerate.setTextFill(Color.WHITE);
        Circle accelerate_symbol = Actions.ACCELERATE.getGraphic();
        MenuBar accelerate_amount_choose_menu = new MenuBar();
        RadioMenuItem accelerate_amount_0 = new RadioMenuItem("0");
        RadioMenuItem accelerate_amount_1 = new RadioMenuItem("1");
        RadioMenuItem accelerate_amount_2 = new RadioMenuItem("2");
        RadioMenuItem accelerate_amount_3 = new RadioMenuItem("3");
        ToggleGroup accelerate_amount_toggle = new ToggleGroup();
        accelerate_amount_0.setToggleGroup(accelerate_amount_toggle);
        accelerate_amount_1.setToggleGroup(accelerate_amount_toggle);
        accelerate_amount_2.setToggleGroup(accelerate_amount_toggle);
        accelerate_amount_3.setToggleGroup(accelerate_amount_toggle);
        
        accelerate_amount_1.setSelected(true); //Default
        Menu accelerate_amount_choose = new Menu("Amount");
        accelerate_amount_choose.getItems().addAll(accelerate_amount_0, accelerate_amount_1, accelerate_amount_2, accelerate_amount_3);
        accelerate_amount_choose_menu.getMenus().add(accelerate_amount_choose);
        HBox accelerate_action_setting = new HBox(100);
        accelerate_action_setting.getChildren().addAll(accelerate, accelerate_symbol, accelerate_amount_choose_menu);
        accelerate_action_setting.setAlignment(Pos.CENTER);
        
        //"Change Orbit" Action -> Der gleiche Aufbau wie von "Accelerate"
        Label change_orbit = new Label(Constants.CHANGE_ORBIT_NAME);
        change_orbit.setFont(new Font(Constants.FONT, 17));
        change_orbit.setTextFill(Color.WHITE);
        Circle change_orbit_symbol = Actions.CHANGE_ORBIT.getGraphic();
        MenuBar change_orbit_amount_choose_menu = new MenuBar();
        RadioMenuItem change_orbit_amount_0 = new RadioMenuItem("0");
        RadioMenuItem change_orbit_amount_1 = new RadioMenuItem("1");
        RadioMenuItem change_orbit_amount_2 = new RadioMenuItem("2");
        RadioMenuItem change_orbit_amount_3 = new RadioMenuItem("3");
        ToggleGroup change_orbit_amount_toggle = new ToggleGroup();
        change_orbit_amount_0.setToggleGroup(change_orbit_amount_toggle);
        change_orbit_amount_1.setToggleGroup(change_orbit_amount_toggle);
        change_orbit_amount_2.setToggleGroup(change_orbit_amount_toggle);
        change_orbit_amount_3.setToggleGroup(change_orbit_amount_toggle);
        
        change_orbit_amount_1.setSelected(true); //Default
        Menu change_orbit_amount_choose = new Menu("Amount");
        change_orbit_amount_choose.getItems().addAll(change_orbit_amount_0, change_orbit_amount_1, change_orbit_amount_2, change_orbit_amount_3);
        change_orbit_amount_choose_menu.getMenus().add(change_orbit_amount_choose);
        HBox change_orbit_action_setting = new HBox(100);
        change_orbit_action_setting.getChildren().addAll(change_orbit, change_orbit_symbol, change_orbit_amount_choose_menu);
        change_orbit_action_setting.setAlignment(Pos.CENTER);
        
        //"Fireball" Action
        Label fireball = new Label(Constants.FIREBALL_NAME);
        fireball.setFont(new Font(Constants.FONT, 17));
        fireball.setTextFill(Color.WHITE);
        Circle fireball_symbol = Actions.FIREBALL.getGraphic();
        MenuBar fireball_amount_choose_menu = new MenuBar();
        RadioMenuItem fireball_amount_0 = new RadioMenuItem("0");
        RadioMenuItem fireball_amount_1 = new RadioMenuItem("1"); // Fireball maximal nur 1
        ToggleGroup fireball_amount_toggle = new ToggleGroup();
        fireball_amount_0.setToggleGroup(fireball_amount_toggle);
        fireball_amount_1.setToggleGroup(fireball_amount_toggle);
        
        fireball_amount_1.setSelected(true); //Default
        Menu fireball_amount_choose = new Menu("Amount");
        fireball_amount_choose.getItems().addAll(fireball_amount_0, fireball_amount_1);
        fireball_amount_choose_menu.getMenus().add(fireball_amount_choose);
        HBox fireball_action_setting = new HBox(100);
        fireball_action_setting.getChildren().addAll(fireball, fireball_symbol, fireball_amount_choose_menu);
        fireball_action_setting.setAlignment(Pos.CENTER);
        
        
        
        //Baue die "Save" Button Schicht
        Button save_button = new Button("Save");
        save_button.setMinSize(100, 40);
        save_button.setOnAction(eve -> {
            if(Check_Settings.problems_in_settings(accelerate_amount_toggle, change_orbit_amount_toggle, fireball_amount_toggle, 
                                                    enter_up_key_field, enter_down_key_field, enter_action_key_field)){
                if(Check_Settings.total_number_actions_exceeds_3(accelerate_amount_toggle, change_orbit_amount_toggle, 
                                                                fireball_amount_toggle)){
                    set_actions_message.setText(Constants.INVALID_NUMBER_OF_ACTIONS_WARNING);
                    set_actions_message.setTextFill(Color.RED);
                }
                if(Check_Settings.up_key_invalid(enter_up_key_field)){
                    enter_up_key_field.setText(Constants.INVALID_KEY_INPUT_WARNING);
                }
                if(Check_Settings.down_key_invalid(enter_down_key_field)){
                    enter_down_key_field.setText(Constants.INVALID_KEY_INPUT_WARNING);
                }
                if(Check_Settings.action_key_invalid(enter_action_key_field)){
                    enter_action_key_field.setText(Constants.INVALID_KEY_INPUT_WARNING);
                }
            }
            else{
                //Alle Daten in Datenbank speichern und zum Schluss das Spiel bauen
                //die Tasten speichern
                Synchronize_Database_Settings.save_all_keys(database, enter_up_key_field, enter_down_key_field, enter_action_key_field);
                //die Actions speichern
                Synchronize_Database_Settings.save_Actions_Client(database, accelerate_amount_toggle, 
                                                        change_orbit_amount_toggle, fireball_amount_toggle);
                //Warnung löschen
                set_actions_message.setText(Constants.SET_ACTIONS_MESSAGE);
                set_actions_message.setTextFill(Color.WHITE);
                
                //Baue das Spiel
                main_app.build_the_game_multiplayer_join();
            }          
        });
        
        
        
        //Füge alle Schichten in "content" hinzu
        VBox content = new VBox(45);
        content.getChildren().addAll(set_controls_message, up_key_box, down_key_box, action_key_box, 
                                        set_actions_message, accelerate_action_setting, change_orbit_action_setting,
                                        fireball_action_setting, save_button);
        content.setAlignment(Pos.CENTER);
        content.setTranslateX(92);
        content.setTranslateY(25);
        
        
        
        settings_menu_for_client.getChildren().addAll(background, content);
    }
    
   
}
