package Resources;

/**
 *
 * @author Minh Tue
 */
public interface Constants {

    /**
     *
     */
    double GAME_FIELD_WIDTH = 1000;

    /**
     *
     */
    double GAME_FIELD_HEIGHT = 700;

    /**
     *
     */
    int DISTANCE_FROM_PADDLE_TO_EDGE = 40;

    /**
     *
     */
    int PADDLE_WIDTH = 8;

    /**
     *
     */
    double PADDLE_HEIGHT = 65;

    /**
     *
     */
    int PLAYER_PADDLE_SPEED = 40;

    /**
     *
     */
    double BALL_RADIUS = 10;

    /**
     *
     */
    double MIDDLE_LINE_WIDTH = 1;  

    /**
     *
     */
    int BALL_SLOW_SPEED = 8;

    /**
     *
     */
    int BALL_MEDIUM_SPEED = 13;

    /**
     *
     */
    int BALL_FAST_SPEED = 18;
    // Botgeschwindigkeit sollte relativ zur Ballgeschwindigkeit sein: Speed(Bot) = k * Speed(Ball). Bot "Easy" -> "k" am kleinsten, usw     

    /**
     *
     */
    double BOT_EASY_RATIO = 0.5;

    /**
     *
     */
    double BOT_MEDIUM_RATIO = 0.8;

    /**
     *
     */
    double BOT_HARD_RATIO = 1.2;    

    /**
     *
     */
    String BACKUP_FILE_FOR_HIGHSCORES = "Highscores.ser";

    /**
     *
     */
    int SPACING_BETWEEN_NODES_IN_START_MENU = 40;

    /**
     *
     */
    int COORD_X_OF_START_MENU_RELATIVE_TO_SCENE = 375;

    /**
     *
     */
    int COORD_Y_OF_START_MENU_RELATIVE_TO_SCENE = 80;

    /**
     *
     */
    int EXIT_CONFIRM_PANE_WIDTH = 390;

    /**
     *
     */
    int EXIT_CONFIRM_PANE_HEIGHT = 70;

    /**
     *
     */
    int SAVE_PROFILE_PANE_WIDTH = 270;

    /**
     *
     */
    int SAVE_PROFILE_PANE_HEIGHT = 120;

    /**
     *
     */
    int THIS_APP_PORT = 3000;

    /**
     *
     */
    String HIGHSCORES_RULES_NOTICE1 = "Player gets 2 points for every win, 0 for a lost.";

    /**
     *
     */
    String HIGHSCORES_RULES_NOTICE2 = "Player also gets Bonuspoints, even in case of a lost, if he/she plays against hard bots or with fast ball";

    /**
     *
     */
    String HIGHSCORES_RULES_NOTICE3 = "Bot hard -> +2 Points, Bot medium -> +1 Point, Bot easy -> +0 Point. The same applies for ball!";

    /**
     *
     */
    String REQUEST_FOR_NAME = "Please enter your name";

    /**
     *
     */
    String PLAYER_EXISTS_MESSAGE = "This player´s name already exists!" + "\n" + "If you are this player, click \"OK\", otherwise click \"Clear\"" 
                                    + "\n" + "and enter another name";

    /**
     *
     */
    String PUSH_ENTER_TO_START_MESSAGE = "Push \"ENTER\" to start!";

    /**
     *
     */
    String WAIT_FOR_PLAYER_1_MESSAGE = "Player 1 is not ready!";

    /**
     *
     */
    String WAIT_FOR_PLAYER_2_MESSAGE = "Player 2 is not ready!";

    /**
     *
     */
    String PAUSED_WORD = "PAUSED!";

    /**
     *
     */
    String DIFFICULTY_MESSAGE = "Difficulty";

    /**
     *
     */
    String SET_CONTROLS_MESSAGE = "Controls";

    /**
     *
     */
    String ENTER_UP_KEY_MESSAGE = "Enter 1 key (capital) for moving up: ";

    /**
     *
     */
    String ENTER_DOWN_KEY_MESSAGE = "Enter 1 key (capital) for moving down: ";

    /**
     *
     */
    String ENTER_ACTION_KEY_MESSAGE = "Enter 1 key (capital) for action-mode: ";

    /**
     *
     */
    String SET_ACTIONS_MESSAGE = "Actions: A total number of 3 Actions is allowed, with \"Fireball\" only 1 maximal";

    /**
     *
     */
    String ACCELERATE_NAME = "Accelerate";

    /**
     *
     */
    String CHANGE_ORBIT_NAME = "Change orbit";

    /**
     *
     */
    String FIREBALL_NAME = "Fireball";

    /**
     *
     */
    String EXIT_CONFIRM_MESS = "Do you want to exit the game? All progresses will not be saved!";

    /**
     *
     */
    String EXIT_MESS_MULTIPLAYER = "Click the button to exit!";

    /**
     *
     */
    String SAVE_CONFIRM_MESS = "Do you want to save your profile?" + "\n" + "Hint: Every loss will be saved in the system,"
                                + "\n" + "whether you like it or not ;)";

    /**
     *
     */
    int ACTIONS_SYMBOL_RADIUS = 6;

    /**
     *
     */
    String INVALID_NUMBER_OF_ACTIONS_WARNING = "The total number of actions choosed exceeds 3! Only 3 Actions are allowed!";

    /**
     *
     */
    String INVALID_KEY_INPUT_WARNING = "INVALID!";

    /**
     *
     */
    String FONT = "Calibri";

    /**
     *
     */
    int INSTRUCTIONS_LINE_HEADER_RADIUS = 5;

    /**
     *
     */
    String FIRST_INSTRUCTION = "Use the keys you chose to move, \"P\" to pause the game. You can also use the mouse to control the paddle.";

    /**
     *
     */
    String SECOND_INSTRUCTION = "Use the key you chose for action (special move). You only have 3 Actions at the most per round.";

    /**
     *
     */
    String THIRD_INSTRUCTION = "There are 3 kinds of \"Actions\": \"Accelerate\" speeds up the ball, \n"
                                + "\"Change Orbit\" changes the ball movement randomly,\n " 
                                + "\"Fireball\" goes through the paddle of your opponent, without any chance of defending.";

    /**
     *
     */
    String FOURTH_INSTRUCTION = "Score 15 points with a minimum distance of 2 points or just 21 points to win a round. \n"
                                + "Win 3 rounds to win the game!";

    /**
     *
     */
    String FIFTH_INSTRUCTION = "For \"Highscores\": Player gets 2 points for every win, 0 for a lost. \n"
                                + "Player also gets Bonuspoints, even in case of a lost, if he/she plays against hard bots or with fast ball. \n"
                                + "Bot hard -> +2 Points, Bot medium -> +1 Point, Bot easy -> +0 Point. The same applies for ball!";

    /**
     *
     */
    String PAUSE_ORDER = "P";

    /**
     *
     */
    String ACTION_ORDER = "A";

    /**
     *
     */
    String READY_ORDER = "R";
}
