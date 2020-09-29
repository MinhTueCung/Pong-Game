package org.openjfx.ea_aufgabe_2_pong;

import Model.Ball;
import Model.Bot;
import Model.Player;
import Resources.Actions;
import View.Game_Graphic_Components;
import javafx.application.Platform;
import javafx.scene.Group;

/**
 * Single Player Game Thread! Dieser Thread steuert das Singleplayer-Spiel an!
 */
public class Game_Thread extends Thread {
    private Ball ball;
    private Bot bot;
    private Referee controller;
    private Game_Graphic_Components grap_viewer;
    private Player player_1;
    /**
     * Beim Singleplayer-Spielende diese Anzeige zum Beenden + Speichern Spieler-Profil
     */
    private Group save_profile_pane; 
    private boolean game_running;
    private boolean paused;
    private boolean game_ended;
    
    /**
     *
     * @param ball
     * @param bot
     * @param controller
     * @param grap_viewer
     * @param player_1
     * @param save_profile_pane
     */
    public Game_Thread(Ball ball, Bot bot, Referee controller, Game_Graphic_Components grap_viewer, Player player_1,
                        Group save_profile_pane){
        this.ball = ball;
        this.bot = bot;
        this.controller = controller;
        this.grap_viewer = grap_viewer;
        this.player_1 = player_1;
        this.save_profile_pane = save_profile_pane;
        game_ended = false;
        game_running = false;
        paused = false;
        // Lasse den Thread das Stoppen der JVM nicht verhindern!
        this.setDaemon(true);
    }

    /**
     *
     * @return
     */
    public boolean isPaused() {
        return paused;
    }
    
    /**
     *
     * @param game_ended
     */
    public void setGame_ended(boolean game_ended) {
        this.game_ended = game_ended;
    }

    /**
     *
     * @param paused
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     *
     * @param game_running
     */
    public void setGame_running(boolean game_running) {
        this.game_running = game_running;
    }
    
    @Override
    public void run(){
        while(!game_ended){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {

            }
            while(game_running){
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {

                }
                // Benutze Platform.runLater(), weil es um Veränderungen auf der UI-Szene (UI-Thread) geht!
                Platform.runLater(() -> {
                    ball.move();
                    bot.move(ball);
                    controller.evaluate_in_Singleplayer();
                    if(controller.a_round_ended_Singleplayer()){
                        if(grap_viewer.getPlayer_1_actions_box().getChildren().size() < 3){
                            // Reset die Actions-Liste, wenn sie nicht voll ist
                            grap_viewer.getPlayer_1_actions_box().getChildren().remove(0, grap_viewer.getPlayer_1_actions_box().getChildren().size());
                            for(Actions action : player_1.getActions()){
                                grap_viewer.getPlayer_1_actions_box().getChildren().add(action.getGraphic());
                            }
                        }
                        game_running = false;
                        grap_viewer.getPush_enter_to_start().setVisible(true);
                    }
                    if(controller.bot_wins_the_game() || controller.player_1_wins_the_game()){
                        //Spiel zu Ende -> Töte den "Game_thread"
                        game_running = false;
                        game_ended = true;
                        paused = false;
                        grap_viewer.getPush_enter_to_start().setVisible(false);
                        //Zeige die Speichernachricht an
                        if(controller.player_1_wins_the_game()){
                            grap_viewer.getWho_wins_message().setVisible(true);
                            save_profile_pane.setVisible(true);
                        }
                        if(controller.bot_wins_the_game()){
                            grap_viewer.getWho_wins_message().setText("Bot wins!");
                            grap_viewer.getWho_wins_message().setVisible(true);
                            save_profile_pane.setVisible(true);
                        }
                    }
                    grap_viewer.update_point_satz_SinglePlayer();
                }); 
            }
        }
    }
}
