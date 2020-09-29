package Utility;

import Resources.Constants;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

/**
 * Zum Bauen der Spielanleitung auf der UI-Szene
 */
public class Instructions_Set_Builder {
    /**
     * "default_scene", "start_menu" von "App" Klasse zu nehmen
     * @param default_scene
     * @param start_menu
     * @param instructions_set
     */
    public static void build(Scene default_scene, Group start_menu, VBox instructions_set){
        //"Anweisungen-Set" besteht aus Zeilen, jede Zeile hat den Standardsaufbau: ein "Circle" als Zeilenkopf + ein "Label" (Text)
        //Erste Zeile
        Circle first_header = new Circle(Constants.INSTRUCTIONS_LINE_HEADER_RADIUS, Color.BLACK);
        Label first_content = new Label(Constants.FIRST_INSTRUCTION);
        first_content.setFont(new Font(Constants.FONT, 20));
        HBox first_instr = new HBox(20);
        first_instr.getChildren().addAll(first_header, first_content);
        
        //Zweite Zeile (das gleiche Prinzip)
        Circle second_header = new Circle(Constants.INSTRUCTIONS_LINE_HEADER_RADIUS, Color.BLACK);
        Label second_content = new Label(Constants.SECOND_INSTRUCTION);
        second_content.setFont(new Font(Constants.FONT, 20));
        HBox second_instr = new HBox(20);
        second_instr.getChildren().addAll(second_header, second_content);
        
        //3.Zeile
        Circle third_header = new Circle(Constants.INSTRUCTIONS_LINE_HEADER_RADIUS, Color.BLACK);
        Label third_content = new Label(Constants.THIRD_INSTRUCTION);
        third_content.setFont(new Font(Constants.FONT, 20));
        HBox third_instr = new HBox(20);
        third_instr.getChildren().addAll(third_header, third_content);
        
        //4.Zeile
        Circle fourth_header = new Circle(Constants.INSTRUCTIONS_LINE_HEADER_RADIUS, Color.BLACK);
        Label fourth_content = new Label(Constants.FOURTH_INSTRUCTION);
        fourth_content.setFont(new Font(Constants.FONT, 20));
        HBox fourth_instr = new HBox(20);
        fourth_instr.getChildren().addAll(fourth_header, fourth_content);
        
        //5.Zeile
        Circle fifth_header = new Circle(Constants.INSTRUCTIONS_LINE_HEADER_RADIUS, Color.BLACK);
        Label fifth_content = new Label(Constants.FIFTH_INSTRUCTION);
        fifth_content.setFont(new Font(Constants.FONT, 20));
        HBox fifth_instr = new HBox(20);
        fifth_instr.getChildren().addAll(fifth_header, fifth_content);
        
        //"Back" Button
        Button back_button = new Button("Back");
        back_button.setMinSize(100, 30);
        back_button.setOnAction(event -> {
            default_scene.setRoot(start_menu);
        });
        
        //Alle ins "Instructions-Set" hinzufügen 
        instructions_set.getChildren().addAll(first_instr, second_instr, third_instr, fourth_instr, fifth_instr, back_button);
    }
}
