import AdventureModel.AdventureGame;
import javafx.application.Application;
import javafx.stage.Stage;
import views.AdventureGameView;

import java.io.IOException;

/**
 * Class AdventureGameApp.
 */
public class AdventureGameApp extends  Application {
    /**
     * The game that is being played.
     */
    AdventureGame model;

    /**
     * The visualization of the game.
     */
    AdventureGameView view;

    /**
     * The main method. Runs the game.
     *
     * @param args The command line.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method. Starts the game and its visualization.
     *
     * @param primaryStage The stage the visualization takes place.
     * @throws IOException in case file causes an I/O error.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.model = new AdventureGame("TinyGame"); //change the name of the game if you want to try something bigger!
        this.view = new AdventureGameView(model, primaryStage);
    }

}
