package views;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.util.Duration;
import states.*;

/**
 * Class ResetView.
 *
 * Resets game settings.
 */
public class ResetView {
    /**
     * The visualization of the settings menu.
     */
    SettingsView settingsView;

    /**
     * The visualization of the game.
     */
    AdventureGameView adventureGameView;

    /**
     * Buttons to reset and cancel.
     */
    Button resetButton, cancelButton;

    /**
     * Label to confirm reset has been done.
     */
    Label resetConfirmLabel;

    /**
     * ResetView Constructor.
     *
     * @param settingsView The visualization of the settings menu.
     */
    public ResetView (SettingsView settingsView) {
        // Initialize attributes
        this.settingsView = settingsView;
        this.adventureGameView = settingsView.adventureGameView;

        // Set up stage
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);

        // Set up settings reset (confirmation) button
        resetButton = new Button("Reset");
        resetButton.setId("ResetConfirmButton");
        customizeButton(resetButton, 100, 50);
        AdventureGameView.makeButtonAccessible(resetButton, "Reset Settings Confirmation Button",
                "This button confirms the request to reset the game settings.",
                "This button confirms the request to reset the game settings. Click it in order to set all game settings to its default states.");
        resetButton.setOnAction(e -> {
            adventureGameView.getButtonSound().play();
            settingsView.setState(new DefaultState(this.settingsView));     // Change state to default
            settingsView.applyChanges();        // Apply default settings
            dialog.close();
            settingsView.saveButton.setDisable(true);       // Disable save button

            // Enable all text size buttons
            for (Button b: settingsView.getTextButtons()) {
                b.setDisable(false);
            }

            // Enable all contrast buttons
            for (Button b: settingsView.getContrastButtons()) {
                b.setDisable(false);
            }

            // Enable dark & light mode buttons
            for (Button b: settingsView.getDarkModeButtons()) {
                b.setDisable(false);
            }

            // Display settings reset message for a few seconds
            settingsView.statusMsg.setText("Settings Reset!");
            settingsView.statusMsg.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(1.5)
            );
            visiblePause.setOnFinished(
                    event -> settingsView.statusMsg.setVisible(false)
            );
            visiblePause.play();
        });

        cancelButton = new Button("Cancel");
        cancelButton.setId("CancelButton");
        customizeButton(cancelButton, 100, 50);
        AdventureGameView.makeButtonAccessible(cancelButton, "Cancel Button",
                "This button cancels the request to reset the game settings.",
                "This button cancels the request to reset the game settings. Click it in order to keep the current game settings.");
        cancelButton.setOnAction(e -> {
            adventureGameView.getButtonSound().play();
            dialog.close();
        });

        // Set up reset confirmation window label
        resetConfirmLabel =  new Label("Reset game settings? All settings will be set to default.");
        resetConfirmLabel.setAlignment(Pos.CENTER);
        resetConfirmLabel.setStyle(adventureGameView.textColor);
        resetConfirmLabel.setFont(adventureGameView.textFont);
        resetConfirmLabel.setPrefWidth(400);
        resetConfirmLabel.setTextOverrun(OverrunStyle.CLIP);
        resetConfirmLabel.setWrapText(true);
        resetConfirmLabel.setOpacity(adventureGameView.textOpacity);

        // Position buttons
        HBox confirmButtons = new HBox();
        confirmButtons.getChildren().addAll(cancelButton, resetButton);
        confirmButtons.setSpacing(30);
        confirmButtons.setAlignment(Pos.CENTER);

        // Position all elements in window
        VBox dialogVbox = new VBox(20, resetConfirmLabel, confirmButtons);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: " + adventureGameView.subBgColor + ";");
        dialogVbox.setAlignment(Pos.CENTER);

        // Display reset confirmation window
        Scene dialogScene = new Scene(dialogVbox, 450, 175);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * customizeButton
     * __________________________
     * Stylizes a button, setting its width and height to the given dimensions.
     *
     * @param inputButton the button to customize
     * @param w width
     * @param h height
     */
    private void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(adventureGameView.textFont);
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        inputButton.setEffect(adventureGameView.contrast);
    }

}
