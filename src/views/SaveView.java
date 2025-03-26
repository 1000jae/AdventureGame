package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.*;

/**
 * Class SaveView.
 *
 * Saves Serialized adventure games.
 */
public class SaveView {
    /**
     * String to show when save is successful.
     */
    static String saveFileSuccess = "Saved Adventure Game!!";

    /**
     * String to show save file does not exist.
     */
    static String saveFileExistsError = "Error: File already exists";

    /**
     * String to show file does not end in .ser.
     */
    static String saveFileNotSerError = "Error: File must end with .ser";

    /**
     * Label to show an error in saving the game.
     */
    private Label saveFileErrorLabel = new Label("");

    /**
     * Label shown to user to when asking to save game.
     */
    private Label saveGameLabel = new Label(String.format("Enter name of file to save"));

    /**
     * For text input.
     */
    private TextField saveFileNameTextField = new TextField("");

    /**
     * Button to save the game.
     */
    private Button saveGameButton = new Button("Save Game");

    /**
     * Button to close the window.
     */
    private Button closeWindowButton = new Button("Close Window");

    /**
     * The visualization of the game.
     */
    private AdventureGameView adventureGameView;

    /**
     * SaveView Constructor.
     *
     * @param adventureGameView The visualization of the game.
     */
    public SaveView(AdventureGameView adventureGameView) {
        this.adventureGameView = adventureGameView;
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: " + adventureGameView.subBgColor + ";");
        saveGameLabel.setId("SaveGame"); // DO NOT MODIFY ID
        saveFileErrorLabel.setId("SaveFileErrorLabel");
        saveFileNameTextField.setId("SaveFileNameTextField");
        saveGameLabel.setStyle(adventureGameView.textColor);
        saveGameLabel.setFont(adventureGameView.textFont);
        saveGameLabel.setOpacity(adventureGameView.textOpacity);
        saveFileErrorLabel.setStyle(adventureGameView.textColor);
        saveFileErrorLabel.setFont(adventureGameView.textFont);
        saveFileErrorLabel.setOpacity(adventureGameView.textOpacity);
        saveFileNameTextField.setStyle("-fx-text-fill: #000000;");
        saveFileNameTextField.setFont(adventureGameView.textFont);
        saveFileNameTextField.setOpacity(adventureGameView.textOpacity);

        String gameName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".ser";
        saveFileNameTextField.setText(gameName);

        saveGameButton = new Button("Save board");
        saveGameButton.setId("SaveBoardButton"); // DO NOT MODIFY ID
        saveGameButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        saveGameButton.setPrefSize(200, 50);
        saveGameButton.setFont(adventureGameView.textFont);
        saveGameButton.setEffect(adventureGameView.contrast);
        AdventureGameView.makeButtonAccessible(saveGameButton, "save game", "This is a button to save the game", "Use this button to save the current game.");
        saveGameButton.setOnAction(e -> {
            adventureGameView.getButtonSound().play();
            saveGame();
        });

        closeWindowButton = new Button("Close Window");
        closeWindowButton.setId("closeWindowButton"); // DO NOT MODIFY ID
        closeWindowButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        closeWindowButton.setPrefSize(200, 50);
        closeWindowButton.setFont(adventureGameView.textFont);
        closeWindowButton.setEffect(adventureGameView.contrast);
        closeWindowButton.setOnAction(e -> {
            adventureGameView.getButtonSound().play();
            dialog.close();
        });

        AdventureGameView.makeButtonAccessible(closeWindowButton, "close window", "This is a button to close the save game window", "Use this button to close the save game window.");

        VBox saveGameBox = new VBox(10, saveGameLabel, saveFileNameTextField, saveGameButton, saveFileErrorLabel, closeWindowButton);
        saveGameBox.setAlignment(Pos.CENTER);

        dialogVbox.getChildren().add(saveGameBox);
        Scene dialogScene = new Scene(dialogVbox, 400, 400);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Saves the Game
     * Save the game to a serialized (binary) file.
     * Get the name of the file from saveFileNameTextField.
     * Files will be saved to the Games/Saved directory.
     * If the file already exists, set the saveFileErrorLabel to the text in saveFileExistsError
     * If the file doesn't end in .ser, set the saveFileErrorLabel to the text in saveFileNotSerError
     * Otherwise, load the file and set the saveFileErrorLabel to the text in saveFileSuccess
     */
    private void saveGame() {
        File gameFile = new File("Games/Saved/" + saveFileNameTextField.getText());

        if (!saveFileNameTextField.getText().endsWith(".ser")) {saveFileErrorLabel.setText(saveFileNotSerError);}
        else if (gameFile.exists()) {saveFileErrorLabel.setText(saveFileExistsError);}
        else {
            adventureGameView.model.saveModel(gameFile);
            saveFileErrorLabel.setText(saveFileSuccess);
        }
    }
}

