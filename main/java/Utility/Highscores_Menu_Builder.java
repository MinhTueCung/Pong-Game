package Utility;

import Model.Player_Infos;
import Resources.Constants;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * zum Bauen der Highscores-Tabelle auf der UI-Szene
 */
public class Highscores_Menu_Builder {
    /**
     *
     * @param highscores_table
     * @param highscores_menu
     * @param default_scene
     * @param start_menu
     */
    public static void build(TableView<Player_Infos> highscores_table, VBox highscores_menu, Scene default_scene, Group start_menu){
        //Titel
        Label title = new Label("Highscores");
        title.setFont(new Font(Constants.FONT, 40));
        
        
        //Die Spalten bauen
        TableColumn<Player_Infos, String> name_column = new TableColumn<>("Name");
        name_column.setMinWidth(240);
        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Player_Infos, Integer> wins_column = new TableColumn<>("Wins");
        wins_column.setMinWidth(240);
        wins_column.setCellValueFactory(new PropertyValueFactory<>("wins"));
        TableColumn<Player_Infos, Integer> losts_column = new TableColumn<>("Losts");
        losts_column.setMinWidth(240);
        losts_column.setCellValueFactory(new PropertyValueFactory<>("losts"));
        TableColumn<Player_Infos, Integer> point_column = new TableColumn<>("Point");
        point_column.setMinWidth(240);
        point_column.setCellValueFactory(new PropertyValueFactory<>("point"));
        highscores_table.getColumns().addAll(name_column, wins_column, losts_column, point_column);
        highscores_table.setEditable(false);
        
        
        //Regeln zur Punktevergabe
        Label notice1 = new Label(Constants.HIGHSCORES_RULES_NOTICE1);
        notice1.setFont(new Font(Constants.FONT, 20));
        Label notice2 = new Label(Constants.HIGHSCORES_RULES_NOTICE2);
        notice1.setFont(new Font(Constants.FONT, 20));
        Label notice3 = new Label(Constants.HIGHSCORES_RULES_NOTICE3);
        notice1.setFont(new Font(Constants.FONT, 20));
        
        
        //Zurück-Button
        Button back_button = new Button("Back");
        back_button.setMinSize(120, 60);
        back_button.setOnAction(event -> {
            default_scene.setRoot(start_menu);
        });
        
        
        //Füge alle hinzu
        highscores_menu.getChildren().addAll(title, highscores_table, notice1, notice2, notice3, back_button);
        back_button.setTranslateX(750);
        back_button.setTranslateY(-120);
    }
}
