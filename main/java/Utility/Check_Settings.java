package Utility;

import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * Diese Klasse ist zum Überprüfen der Gültigkeit von Einstellungen gedacht, die vom User vorgenommen werden
 */
public class Check_Settings {
    /**
     *
     * @param accelerate_amount_toggle
     * @param change_orbit_amount_toggle
     * @param fireball_amount_toggle
     * @param enter_up_key_field
     * @param enter_down_key_field
     * @param enter_action_key_field
     * @return
     */
    public static boolean problems_in_settings(ToggleGroup accelerate_amount_toggle, ToggleGroup change_orbit_amount_toggle, 
        ToggleGroup fireball_amount_toggle, TextField enter_up_key_field, TextField enter_down_key_field, TextField enter_action_key_field){
        return total_number_actions_exceeds_3(accelerate_amount_toggle, change_orbit_amount_toggle, fireball_amount_toggle)
            || up_key_invalid(enter_up_key_field) || down_key_invalid(enter_down_key_field) || action_key_invalid(enter_action_key_field);
    }
    
    /**
     * prüft, ob die Gesamtanzahl von ausgewählten Actions > 3 (ungültig) ist
     * @param accelerate_amount_toggle
     * @param change_orbit_amount_toggle
     * @param fireball_amount_toggle
     * @return
     */
    public static boolean total_number_actions_exceeds_3(ToggleGroup accelerate_amount_toggle, 
                                                    ToggleGroup change_orbit_amount_toggle, ToggleGroup fireball_amount_toggle){
        int accelerate_amount = Integer.parseInt(((RadioMenuItem) accelerate_amount_toggle.getSelectedToggle()).getText());
        int change_orbit_amount = Integer.parseInt(((RadioMenuItem) change_orbit_amount_toggle.getSelectedToggle()).getText());
        int fireball_amount = Integer.parseInt(((RadioMenuItem) fireball_amount_toggle.getSelectedToggle()).getText());
        return accelerate_amount + change_orbit_amount + fireball_amount > 3;
    } 
     
    
    /*prüft, ob die Belegung der Tasten gültig ist (Ungültig, wenn es überhaupt keine Eingabe gibt oder die Eingabe
    besteht aus mehr als 1 "Char")     
    */

    /**
     *
     * @param enter_up_key_field
     * @return
     */

    public static boolean up_key_invalid(TextField enter_up_key_field){
        String input = enter_up_key_field.getText();
        return (input == null) || (input.length() != 1); 
    }
    
    /**
     *
     * @param enter_down_key_field
     * @return
     */
    public static boolean down_key_invalid(TextField enter_down_key_field){
        String input = enter_down_key_field.getText();
        return (input == null) || (input.length() != 1);
    }
    
    /**
     *
     * @param enter_action_key_field
     * @return
     */
    public static boolean action_key_invalid(TextField enter_action_key_field){
        String input = enter_action_key_field.getText();
        return (input == null) || (input.length() != 1);
    }
}
