package views;

import AdventureModel.AdventureGame;
import AdventureModel.AdventureLoader;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Class LoadView.
 *
 * Loads Serialized adventure games.
 */
public class LoadView {
    /**
     * The visualization of the game.
     */
    private AdventureGameView adventureGameView;

    /**
     * Label for the selected game.
     */
    private Label selectGameLabel;

    /**
     * The select game button.
     */
    private Button selectGameButton;

    /**
     * The button to close the window.
     */
    private Button closeWindowButton;

    /**
     * List of game files saved.
     */
    private ListView<String> GameList;

    /**
     * The name of file selected.
     */
    private String filename = null;

    /**
     * LoadView Constructor.
     *
     * @param adventureGameView The visualization of the game.
     */
    public LoadView(AdventureGameView adventureGameView){

        //note that the buttons in this view are not accessible!!
        this.adventureGameView = adventureGameView;
        selectGameLabel = new Label(String.format(""));

        GameList = new ListView<>(); //to hold all the file names
        GameList.setStyle("-fx-font-size:" + adventureGameView.textFont.getSize());

        final Stage dialog = new Stage(); //dialogue box
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);

        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: " + adventureGameView.subBgColor + ";");
        selectGameLabel.setId("CurrentGame"); // DO NOT MODIFY ID
        GameList.setId("GameList");  // DO NOT MODIFY ID
        GameList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        getFiles(GameList); //get files for file selector
        selectGameButton = new Button("Change Game");
        selectGameButton.setId("ChangeGame"); // DO NOT MODIFY ID
        AdventureGameView.makeButtonAccessible(selectGameButton, "select game", "This is the button to select a game", "Use this button to indicate a game file you would like to load.");

        closeWindowButton = new Button("Close Window");
        closeWindowButton.setId("closeWindowButton"); // DO NOT MODIFY ID
        closeWindowButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        closeWindowButton.setPrefSize(200, 50);
        closeWindowButton.setFont(new Font(16));
        closeWindowButton.setEffect(adventureGameView.contrast);
        closeWindowButton.setOnAction(e -> {
            adventureGameView.getButtonSound().play();
            dialog.close();
        });
        AdventureGameView.makeButtonAccessible(closeWindowButton, "close window", "This is a button to close the load game window", "Use this button to close the load game window.");

        //on selection, do something
        selectGameButton.setOnAction(e -> {
            adventureGameView.getButtonSound().play();
            try {
                selectGame(selectGameLabel, GameList);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        VBox selectGameBox = new VBox(10, selectGameLabel, GameList, selectGameButton);

        // Default styles which can be modified
        GameList.setPrefHeight(100);
        GameList.setOpacity(adventureGameView.textOpacity);
        selectGameLabel.setStyle(adventureGameView.textColor);
        selectGameLabel.setFont(adventureGameView.textFont);
        selectGameLabel.setOpacity(adventureGameView.textOpacity);
        selectGameLabel.setWrapText(true);
        selectGameButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        selectGameButton.setPrefSize(200, 50);
        selectGameButton.setFont(adventureGameView.textFont);
        selectGameButton.setEffect(adventureGameView.contrast);
        selectGameLabel.setOpacity(adventureGameView.textOpacity);
        selectGameBox.setAlignment(Pos.CENTER);
        dialogVbox.getChildren().add(selectGameBox);
        Scene dialogScene = new Scene(dialogVbox, 400, 400);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Get Files to display in the on screen ListView
     * Populate the listView attribute with .ser file names
     * Files will be located in the Games/Saved directory
     *
     * @param listView the ListView containing all the .ser files in the Games/Saved directory.
     */
    private void getFiles(ListView<String> listView) {
        File savedGames = new File("Games/Saved");
        File[] files = savedGames.listFiles();       // Get list of all files in Games/Saved directory
        ObservableList<String> serFiles = FXCollections.observableArrayList();

        assert files != null;

        for (File f: files) {       // Collect all files ending with .ser
            if (f.getName().endsWith(".ser")) {serFiles.add(f.getName());}
        }
        listView.setItems(serFiles);        // Update listView with .ser files
    }

    /**
     * Select the Game
     * Try to load a game from the Games/Saved
     * If successful, stop any articulation and put the name of the loaded file in the selectGameLabel.
     * If unsuccessful, stop any articulation and start an entirely new game from scratch.
     * In this case, change the selectGameLabel to indicate a new game has been loaded.
     *
     * @param selectGameLabel the label to use to print errors and or successes to the user.
     * @param GameList the ListView to populate
     */
    private void selectGame(Label selectGameLabel, ListView<String> GameList) throws IOException {
        adventureGameView.stopArticulation();
        getFiles(GameList);
        filename = GameList.getSelectionModel().getSelectedItem();
        try {       // Attempt to load selected game
            adventureGameView.model = loadGame("Games/Saved/" + filename);
            selectGameLabel.setText("Selected Game: " + filename);
        }
        catch (FileNotFoundException | ClassNotFoundException e) {      // If unsuccessful, create a new game from scratch
            adventureGameView.model = new AdventureGame("TinyGame");
            selectGameLabel.setText("No save file was loaded. New game created.");
        }
        finally {
            adventureGameView.updateScene("");
            adventureGameView.updateItems();
        }
    }

    /**
     * Load the Game from a file
     *
     * @param GameFile file to load
     * @throws IOException in the case of a file I/O error.
     * @throws ClassNotFoundException in the case that the class does not exist.
     * @return loaded Tetris Model
     */
    public AdventureGame loadGame(String GameFile) throws IOException, ClassNotFoundException {
        // Reading the object from a file
        FileInputStream file = null;
        ObjectInputStream in = null;
        try {
            file = new FileInputStream(GameFile);
            in = new ObjectInputStream(file);
            return (AdventureGame) in.readObject();
        } finally {
            if (in != null) {
                in.close();
                file.close();
            }
        }
    }

}
