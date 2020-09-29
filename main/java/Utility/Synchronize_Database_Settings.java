package Utility;

import Resources.Actions;
import Resources.Constants;
import Resources.Database;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;

/**
 * Zum Synchronisieren zwischen Einstellungen auf der UI-Szene und Einstellungen in Datenbank
 */
public class Synchronize_Database_Settings {
    /**
     *
     * @param database
     * @param ball_easy
     * @param ball_medium
     * @param ball_hard
     */
    
    public static void ball_speed(Database database, RadioMenuItem ball_easy, RadioMenuItem ball_medium, RadioMenuItem ball_hard){
        switch(database.getBall_speed()){
            case Constants.BALL_SLOW_SPEED :
                ball_easy.setSelected(true);
                break;
            case Constants.BALL_MEDIUM_SPEED :
                ball_medium.setSelected(true);
                break;
            case Constants.BALL_FAST_SPEED :
                ball_hard.setSelected(true);
                break;
        }
    }
    
    /**
     *
     * @param database
     * @param bot_easy
     * @param bot_medium
     * @param bot_hard
     */
    public static void bot_speed(Database database, RadioMenuItem bot_easy, RadioMenuItem bot_medium, RadioMenuItem bot_hard){
        double bot_diffi_ratio = database.getBot_diffi_ratio();
        if(bot_diffi_ratio == Constants.BOT_EASY_RATIO){
            bot_easy.setSelected(true);
        }
        if(bot_diffi_ratio == Constants.BOT_MEDIUM_RATIO){
            bot_medium.setSelected(true);
        }
        if(bot_diffi_ratio == Constants.BOT_HARD_RATIO){
            bot_hard.setSelected(true);
        }
    }
    
    /**
     *
     * @param database
     * @param accelerate_amount_0
     * @param accelerate_amount_1
     * @param accelerate_amount_2
     * @param accelerate_amount_3
     */
    public static void accelerate(Database database, RadioMenuItem accelerate_amount_0, RadioMenuItem accelerate_amount_1,
                                        RadioMenuItem accelerate_amount_2, RadioMenuItem accelerate_amount_3){
        int amount = 0;
        for(Actions action : database.getActions()){
            if(action == Actions.ACCELERATE){
                amount++;
            }
        }
        switch(amount){
            case 0:
                accelerate_amount_0.setSelected(true);
                break;
            case 1:
                accelerate_amount_1.setSelected(true);
                break;
            case 2:
                accelerate_amount_2.setSelected(true);
                break;
            case 3:
                accelerate_amount_3.setSelected(true);
                break;
        }
    }
    
    /**
     *
     * @param database
     * @param change_orbit_amount_0
     * @param change_orbit_amount_1
     * @param change_orbit_amount_2
     * @param change_orbit_amount_3
     */
    public static void change_orbit(Database database, RadioMenuItem change_orbit_amount_0, RadioMenuItem change_orbit_amount_1,
                                        RadioMenuItem change_orbit_amount_2, RadioMenuItem change_orbit_amount_3){
        int amount = 0;
        for(Actions action : database.getActions()){
            if(action == Actions.CHANGE_ORBIT){
                amount++;
            }
        }
        switch(amount){
            case 0:
                change_orbit_amount_0.setSelected(true);
                break;
            case 1:
                change_orbit_amount_1.setSelected(true);
                break;
            case 2:
                change_orbit_amount_2.setSelected(true);
                break;
            case 3:
                change_orbit_amount_3.setSelected(true);
                break;
        }
    }
    
    /**
     *
     * @param database
     * @param fireball_amount_0
     * @param fireball_amount_1
     */
    public static void fireball(Database database, RadioMenuItem fireball_amount_0, RadioMenuItem fireball_amount_1){
        int amount = 0;
        for(Actions action : database.getActions()){
            if(action == Actions.FIREBALL){
                amount++;
            }
        }
        switch(amount){
            case 0:
                fireball_amount_0.setSelected(true);
                break;
            case 1:
                fireball_amount_1.setSelected(true);
                break;
        }
    }
    
    /**
     *
     * @param database
     * @param ball_difficulty_toggle
     */
    public static void save_ball_speed(Database database, ToggleGroup ball_difficulty_toggle){
        switch(((RadioMenuItem) ball_difficulty_toggle.getSelectedToggle()).getText()){
            case "Slow":
                database.setBall_speed(Constants.BALL_SLOW_SPEED);
                break;
            case "Medium":
                database.setBall_speed(Constants.BALL_MEDIUM_SPEED);
                break;
            case "Fast":
                database.setBall_speed(Constants.BALL_FAST_SPEED);
                break;
        }
    }
    
    /**
     *
     * @param database
     * @param bot_difficulty_toggle
     */
    public static void save_bot_speed(Database database, ToggleGroup bot_difficulty_toggle){
        switch(((RadioMenuItem) bot_difficulty_toggle.getSelectedToggle()).getText()){
            case "Easy": 
                database.setBot_diffi_ratio(Constants.BOT_EASY_RATIO);
                break;
            case "Medium":
                database.setBot_diffi_ratio(Constants.BOT_MEDIUM_RATIO);
                break;
            case "Hard":
                database.setBot_diffi_ratio(Constants.BOT_HARD_RATIO);
                break;
        }
    }
    
    /**
     *
     * @param database
     * @param enter_up_key_field
     * @param enter_down_key_field
     * @param enter_action_key_field
     */
    public static void save_all_keys(Database database, TextField enter_up_key_field, 
                                    TextField enter_down_key_field, TextField enter_action_key_field){
        database.setMove_up_key(KeyCode.valueOf(enter_up_key_field.getText()));
        database.setMove_down_key(KeyCode.valueOf(enter_down_key_field.getText()));
        database.setAction_key(KeyCode.valueOf(enter_action_key_field.getText()));
    }
    
    /**
     *
     * @param database
     * @param accelerate_amount_toggle
     * @param change_orbit_amount_toggle
     * @param fireball_amount_toggle
     */
    public static void save_Actions(Database database, ToggleGroup accelerate_amount_toggle, 
                                    ToggleGroup change_orbit_amount_toggle, ToggleGroup fireball_amount_toggle){
        List<Actions> actions = new ArrayList<>();
        for(int i = 0; i < Integer.parseInt(((RadioMenuItem) accelerate_amount_toggle.getSelectedToggle()).getText()); i++){
            actions.add(Actions.ACCELERATE);
        }
        for(int k = 0; k < Integer.parseInt(((RadioMenuItem) change_orbit_amount_toggle.getSelectedToggle()).getText()); k++){
            actions.add(Actions.CHANGE_ORBIT);
        }
        for(int j = 0; j < Integer.parseInt(((RadioMenuItem) fireball_amount_toggle.getSelectedToggle()).getText()); j++){
            actions.add(Actions.FIREBALL);
        }
        database.setActions(actions);
    }
    
    /**
     * Derselbe Aufbau wie "save_Actions", diesmal aber in die "Actions_Server" Liste speichern
     * @param database
     * @param accelerate_amount_toggle
     * @param change_orbit_amount_toggle
     * @param fireball_amount_toggle
     */
    public static void save_Actions_Server(Database database, ToggleGroup accelerate_amount_toggle, 
                                    ToggleGroup change_orbit_amount_toggle, ToggleGroup fireball_amount_toggle){
        List<Actions> actions = new ArrayList<>();
        for(int i = 0; i < Integer.parseInt(((RadioMenuItem) accelerate_amount_toggle.getSelectedToggle()).getText()); i++){
            actions.add(Actions.ACCELERATE);
        }
        for(int k = 0; k < Integer.parseInt(((RadioMenuItem) change_orbit_amount_toggle.getSelectedToggle()).getText()); k++){
            actions.add(Actions.CHANGE_ORBIT);
        }
        for(int j = 0; j < Integer.parseInt(((RadioMenuItem) fireball_amount_toggle.getSelectedToggle()).getText()); j++){
            actions.add(Actions.FIREBALL);
        }
        database.setActions_server(actions);
    }
    
    /**
     * Derselbe Aufbau wie "save_Actions", diesmal aber in die "Actions_Client" Liste speichern
     * @param database
     * @param accelerate_amount_toggle
     * @param change_orbit_amount_toggle
     * @param fireball_amount_toggle
     */
    public static void save_Actions_Client(Database database, ToggleGroup accelerate_amount_toggle, 
                                    ToggleGroup change_orbit_amount_toggle, ToggleGroup fireball_amount_toggle){
        List<Actions> actions = new ArrayList<>();
        for(int i = 0; i < Integer.parseInt(((RadioMenuItem) accelerate_amount_toggle.getSelectedToggle()).getText()); i++){
            actions.add(Actions.ACCELERATE);
        }
        for(int k = 0; k < Integer.parseInt(((RadioMenuItem) change_orbit_amount_toggle.getSelectedToggle()).getText()); k++){
            actions.add(Actions.CHANGE_ORBIT);
        }
        for(int j = 0; j < Integer.parseInt(((RadioMenuItem) fireball_amount_toggle.getSelectedToggle()).getText()); j++){
            actions.add(Actions.FIREBALL);
        }
        database.setActions_client(actions);
    }
}
