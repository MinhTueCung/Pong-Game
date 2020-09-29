package View;

import Resources.Constants;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Das "Wartezimmer" (eine Szene "Loading"), die angezeigt wird, während das Spiel gebaut wird (Multiplayer).
 */
public class Waiting_Lounge extends Group {
    /**
     *
     */
    public Waiting_Lounge(){
        //Hintergrund
        Background background = new Background();
        
        //Nachricht
        Label message = new Label("Loading...");
        message.setFont(new Font(Constants.FONT, 30));
        message.setTextFill(Color.WHITE);
        //ProgressIndicator
        ProgressIndicator pi = new ProgressIndicator(-1.0f);
        
        VBox content = new VBox(80);
        content.getChildren().addAll(message, pi);
        content.setAlignment(Pos.CENTER);
        content.setTranslateX(Constants.GAME_FIELD_WIDTH/2 - 80);
        content.setTranslateY(Constants.GAME_FIELD_HEIGHT/2 - 100);
        
        this.getChildren().addAll(background, content);
    }
}
