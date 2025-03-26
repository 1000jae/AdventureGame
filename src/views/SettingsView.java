package views;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.util.Duration;
import states.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class SettingsView.
 *
 * Holds game settings options.
 */
public class SettingsView {

    /**
     * The visualization of game.
     */
    AdventureGameView adventureGameView;

    /**
     * Buttons to reset, close, and save settings.
     */
    Button closeWindowButton, saveButton, resetButton;

    /**
     * Buttons for text font choices.
     */
    Button smallTextSizeButton, defaultTextSizeButton, largeTextSizeButton;

    /**
     * Buttons for contrast choices.
     */
    Button lowContrastButton, defaultContrastButton, highContrastButton;

    /**
     * Buttons for dark/light mode choices.
     */
    Button lightModeButton, darkModeButton;

    /**
     * The current state of the setting.
     */
    State state;

    /**
     * Labels to show on t visualization.
     */
    Label settingsLabel, statusMsg, textSizeLabel, contrastLabel, darkModeLabel;

    /**
     * Hold all buttons and labels.
     */
    VBox dialogVbox = new VBox();

    /**
     * SettingsView Constructor.
     *
     * @param adventureGameView The visualization of the game.
     */
    public SettingsView (AdventureGameView adventureGameView) {
        // Initialize attributes
        this.adventureGameView = adventureGameView;
        this.state = new DefaultState(this);

        // Set up stage
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);

        // Set up buttons
        closeWindowButton = new Button("Close Window");
        closeWindowButton.setId("closeWindowButton");
        closeWindowButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        closeWindowButton.setPrefSize(200, 50);
        closeWindowButton.setFont(new Font(16));
        closeWindowButton.setOnAction(e -> {
            adventureGameView.getButtonSound().play();
            dialog.close();
        });
        AdventureGameView.makeButtonAccessible(closeWindowButton, "close window",
                "This is a button to close the game settings window",
                "Use this button to close the game settings window.");

        // Save button
        saveButton = new Button("Save");
        saveButton.setId("SaveSettingsButton");
        customizeButton(saveButton, 100, 50);
        AdventureGameView.makeButtonAccessible(saveButton, "Save Button",
                "This button saves the changes made to the game settings.",
                "This button saves the changes made to the game settings. Click it in order to save the game settings.");
        this.saveButton.setDisable(true);      // Initially disabled - gets enabled when changes are made to settings
        addSaveEvent();

        // Reset button
        resetButton = new Button("Reset");
        resetButton.setId("ResetRequestButton");
        customizeButton(resetButton, 100, 50);
        AdventureGameView.makeButtonAccessible(resetButton, "Reset Settings Button",
                "This button reverts all changes made to the game settings.",
                "This button reverts all changes made to the game settings. Click it to request for the game settings to be reset.");
        addResetEvent();

        HBox textSizeStuff = setUpTextSizeButtons();     // Text size buttons
        HBox contrastStuff = setUpContrastButtons();     // Contrast buttons
        HBox darkModeStuff = setUpDarkModeButtons();     // Light & dark mode buttons

        // Setup display for save & reset buttons
        HBox saveButtons = new HBox();
        saveButtons.getChildren().addAll(resetButton, saveButton);
        saveButtons.setSpacing(20);
        // Setup label appears in bottom left corner whenever the status of settings is changed (i.e. saved, reset)
        statusMsg = new Label();
        statusMsg.setFont(new Font("Arial", 16));
        statusMsg.setStyle(adventureGameView.textColor);
        // Setup display format for save & reset stuff
        HBox saveStuff = new HBox();
        Pane buttonSpacing = new Pane();
        HBox.setHgrow(buttonSpacing, Priority.ALWAYS);
        saveStuff.getChildren().addAll(statusMsg, buttonSpacing, saveButtons);
        saveStuff.setAlignment(Pos.BOTTOM_CENTER);

        // Setup setting label
        settingsLabel = new Label("Settings");
        settingsLabel.setFont(new Font("Arial", 25));
        settingsLabel.setStyle(adventureGameView.textColor);
        settingsLabel.setOpacity(adventureGameView.textOpacity);

        // Place everything in a VBox to display
        Pane vSpacing = new Pane();
        VBox.setVgrow(vSpacing, Priority.ALWAYS);
        dialogVbox = new VBox(20, settingsLabel, textSizeStuff, contrastStuff, darkModeStuff, vSpacing, saveStuff);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: " + adventureGameView.subBgColor + ";");

        // Display settings window
        Scene dialogScene = new Scene(dialogVbox, 600, 400);
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

    /**
     * This method sets up the text size buttons.
     * @return HBox containing the formatted text size buttons
     */
    public HBox setUpTextSizeButtons() {
        smallTextSizeButton = new Button("Small");
        smallTextSizeButton.setId("smallTextSizeButton");
        customizeButton(smallTextSizeButton, 100, 50);
        smallTextSizeButton.setFont(new Font("Arial", 12));
        AdventureGameView.makeButtonAccessible(smallTextSizeButton, "Small Text Size Button",
                "This button decreases the font size for all text in the game.",
                "This button decreases the font size for all text in the game. Click it in order to make the game text smaller.");
        addSmallTextEvent();

        defaultTextSizeButton = new Button("Default");
        defaultTextSizeButton.setId("defaultTextSizeButton");
        customizeButton(defaultTextSizeButton, 100, 50);
        defaultTextSizeButton.setFont(new Font("Arial", 16));
        AdventureGameView.makeButtonAccessible(defaultTextSizeButton, "Default Text Size Button",
                "This button sets the font size to the text font for all text in game to the default size.",
                "This button sets the text font for all text in game to the default size. Click it in order to set the game text to its default size.");
        addDefaultTextEvent();

        largeTextSizeButton = new Button("Large");
        largeTextSizeButton.setId("largeTextSizeButton");
        customizeButton(largeTextSizeButton, 100, 50);
        largeTextSizeButton.setFont(new Font("Arial", 20));
        AdventureGameView.makeButtonAccessible(largeTextSizeButton, "Large Text Size Button",
                "This button increases the font size for all text in the game.",
                "This button decreases the font size for all text in the game. Click it in order to make the game text bigger.");
        addLargeTextEvent();

        // Setup display format for text size buttons
        HBox textSizeButtons = new HBox();
        textSizeButtons.getChildren().addAll(smallTextSizeButton, defaultTextSizeButton, largeTextSizeButton);
        textSizeButtons.setSpacing(10);
        textSizeButtons.setAlignment(Pos.CENTER);

        HBox textSizeStuff = new HBox();
        textSizeLabel = new Label("Text Size");
        textSizeLabel.setFont(adventureGameView.textFont);
        textSizeLabel.setStyle(adventureGameView.textColor);
        textSizeLabel.setOpacity(adventureGameView.textOpacity);
        textSizeLabel.setAlignment(Pos.CENTER);
        Pane textSizeSpacing = new Pane();
        HBox.setHgrow(textSizeSpacing, Priority.ALWAYS);
        textSizeStuff.getChildren().addAll(textSizeLabel, textSizeSpacing, textSizeButtons);
        textSizeStuff.setAlignment(Pos.CENTER);
        return textSizeStuff;
    }

    /**
     * This method sets up the contrast buttons.
     *
     * @return HBox containing the formatted contrast buttons.
     */
    public HBox setUpContrastButtons() {
        lowContrastButton = new Button("Low");
        lowContrastButton.setId("lowContrastButton");
        customizeButton(lowContrastButton, 100, 50);
        lowContrastButton.setFont(adventureGameView.textFont);
        AdventureGameView.makeButtonAccessible(lowContrastButton, "Low Contrast Button",
                "This button decreases the contrast of the game view.",
                "This button decreases the contrast of the game view. Click it in order to lower the color contrast.");
        addLowContrastEvent();

        defaultContrastButton = new Button("Default");
        defaultContrastButton.setId("defaultContrastButton");
        customizeButton(defaultContrastButton, 100, 50);
        defaultContrastButton.setFont(adventureGameView.textFont);
        AdventureGameView.makeButtonAccessible(defaultContrastButton, "Default Contrast Button",
                "This button sets the game's color contrast to the default contrast value.",
                "This button sets the game's color contrast to the default contrast value. Click it in order to set the color contrast back to default.");
        addDefaultContrastEvent();

        highContrastButton = new Button("High");
        highContrastButton.setId("highContrastButton");
        customizeButton(highContrastButton, 100, 50);
        highContrastButton.setFont(adventureGameView.textFont);
        AdventureGameView.makeButtonAccessible(lowContrastButton, "High Contrast Button",
                "This button increases the contrast of the game view.",
                "This button increases the contrast of the game view. Click it in order to raise the color contrast.");
        addHighContrastEvent();

        // Setup display format for contrast buttons
        HBox contrastButtons = new HBox();
        contrastButtons.getChildren().addAll(lowContrastButton, defaultContrastButton, highContrastButton);
        contrastButtons.setSpacing(10);
        contrastButtons.setAlignment(Pos.CENTER);

        HBox contrastStuff = new HBox();
        contrastLabel = new Label("Contrast");
        contrastLabel.setFont(adventureGameView.textFont);
        contrastLabel.setStyle(adventureGameView.textColor);
        contrastLabel.setOpacity(adventureGameView.textOpacity);
        contrastLabel.setAlignment(Pos.CENTER);
        Pane contrastSpacing = new Pane();
        HBox.setHgrow(contrastSpacing, Priority.ALWAYS);
        contrastStuff.getChildren().addAll(contrastLabel, contrastSpacing, contrastButtons);
        contrastStuff.setAlignment(Pos.CENTER);
        return contrastStuff;
    }

    /**
     * This method sets up the dark/light mode buttons.
     *
     * @return HBox containing the formatted dark/light mode buttons.
     */
    public HBox setUpDarkModeButtons() {
        lightModeButton = new Button("Light");
        lightModeButton.setId("lightModeButton");
        customizeButton(lightModeButton, 100, 50);
        lightModeButton.setFont(adventureGameView.textFont);
        AdventureGameView.makeButtonAccessible(lightModeButton, "Light Mode Button",
                "This button enables light mode.",
                "This button enables light mode. Click it in order to have the game display dark text on a light background.");
        addLightModeEvent();
        darkModeButton = new Button("Dark");
        darkModeButton.setId("darkModeButton");
        customizeButton(darkModeButton, 100, 50);
        darkModeButton.setFont(adventureGameView.textFont);
        AdventureGameView.makeButtonAccessible(darkModeButton, "Dark Mode Button",
                "This button enables dark mode.",
                "This button enables dark mode. Click it in order to have the game display light text on a dark background.");
        addDarkModeEvent();

        // Setup display format for dark & light mode buttons
        HBox darkModeButtons = new HBox();
        darkModeButtons.getChildren().addAll(darkModeButton, lightModeButton);
        darkModeButtons.setSpacing(10);
        darkModeButtons.setAlignment(Pos.CENTER);

        HBox darkModeStuff = new HBox();
        darkModeLabel = new Label("Dark/Light Mode");
        darkModeLabel.setFont(adventureGameView.textFont);
        darkModeLabel.setStyle(adventureGameView.textColor);
        darkModeLabel.setOpacity(adventureGameView.textOpacity);
        darkModeLabel.setAlignment(Pos.CENTER);
        Pane modeSpacing = new Pane();
        HBox.setHgrow(modeSpacing, Priority.ALWAYS);
        darkModeStuff.getChildren().addAll(darkModeLabel, modeSpacing, darkModeButtons);
        darkModeStuff.setAlignment(Pos.CENTER);
        return darkModeStuff;
    }

    /**
     * This method handles the event related to the
     * reset settings button.
     */
    public void addResetEvent() {
        resetButton.setOnAction(e -> {
            adventureGameView.getButtonSound().play();
            ResetView resetView = new ResetView(this);
        });     // Display confirmation window
    }

    /**
     * This method handles the event related to the
     * save settings button.
     */
    public void addSaveEvent() {
        saveButton.setOnAction(e -> {
            adventureGameView.getButtonSound().play();
            this.applyChanges();
            saveButton.setDisable(true);        // Disable save button
            // Display settings saved message for a few seconds
            statusMsg.setText("Settings Saved!");
            statusMsg.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(1.5)
            );
            visiblePause.setOnFinished(
                    event -> statusMsg.setVisible(false)
            );
            visiblePause.play();
        });
    }

    /**
     * This method handles the event related to the
     * small text size button.
     */
    public void addSmallTextEvent() {
        smallTextSizeButton.setOnAction(e -> {
            adventureGameView.getButtonSound().play();
            if (!(this.state instanceof AlteredState)) {
                setState(new AlteredState(this));      // Change state to altered
            }
            this.state.textSize = "small";
            this.saveButton.setDisable(false);      // Enable settings save button
            // To show that small text size has been selected, disable small text button & enable the other buttons
            smallTextSizeButton.setDisable(true);
            defaultTextSizeButton.setDisable(false);
            largeTextSizeButton.setDisable(false);
        });
    }

    /**
     * This method handles the event related to the
     * default text size button.
     */
    public void addDefaultTextEvent() {
        defaultTextSizeButton.setOnAction(e -> {
            adventureGameView.getButtonSound().play();
            if (!(this.state instanceof AlteredState)) {
                setState(new AlteredState(this));      // Change state to altered
            }
            this.state.textSize = "default";
            this.saveButton.setDisable(false);      // Enable settings save button
            // To show that default text size has been selected, disable small text button & enable the other buttons
            smallTextSizeButton.setDisable(false);
            defaultTextSizeButton.setDisable(true);
            largeTextSizeButton.setDisable(false);
        });
    }

    /**
     * This method handles the event related to the
     * large text size button.
     */
    public void addLargeTextEvent() {
        largeTextSizeButton.setOnAction(e -> {
            adventureGameView.getButtonSound().play();
            if (!(this.state instanceof AlteredState)) {
                setState(new AlteredState(this));      // Change state to altered
            }
            this.state.textSize = "large";
            this.saveButton.setDisable(false);      // Enable settings save button
            // To show that large text size has been selected, disable small text button & enable the other buttons
            smallTextSizeButton.setDisable(false);
            defaultTextSizeButton.setDisable(false);
            largeTextSizeButton.setDisable(true);
        });
    }

    /**
     * This method handles the event related to the
     * low contrast button.
     */
    public void addLowContrastEvent() {
        lowContrastButton.setOnAction(e -> {
            adventureGameView.getButtonSound().play();
            if (!(this.state instanceof AlteredState)) {
                setState(new AlteredState(this));      // Change state to altered
            }
            this.state.contrast = "low";
            this.saveButton.setDisable(false);      // Enable settings save button
            // To show that low contrast has been selected, disable low contrast button & enable the other buttons
            lowContrastButton.setDisable(true);
            defaultContrastButton.setDisable(false);
            highContrastButton.setDisable(false);
        });
    }

    /**
     * This method handles the event related to the
     * default contrast button.
     */
    public void addDefaultContrastEvent() {
        defaultContrastButton.setOnAction(e -> {
            adventureGameView.getButtonSound().play();
            if (!(this.state instanceof AlteredState)) {
                setState(new AlteredState(this));      // Change state to altered
            }
            this.state.contrast = "default";
            this.saveButton.setDisable(false);      // Enable settings save button
            // To show that default contrast has been selected, disable default contrast button & enable the other buttons
            lowContrastButton.setDisable(false);
            defaultContrastButton.setDisable(true);
            highContrastButton.setDisable(false);
        });
    }

    /**
     * This method handles the event related to the
     * high contrast button.
     */
    public void addHighContrastEvent() {
        highContrastButton.setOnAction(e -> {
            adventureGameView.getButtonSound().play();
            if (!(this.state instanceof AlteredState)) {
                setState(new AlteredState(this));      // Change state to altered
            }
            this.state.contrast = "high";
            this.saveButton.setDisable(false);      // Enable settings save button
            // To show that high contrast has been selected, disable high contrast button & enable the other buttons
            lowContrastButton.setDisable(false);
            defaultContrastButton.setDisable(false);
            highContrastButton.setDisable(true);
        });
    }

    /**
     * This method handles the event related to the
     * light mode button.
     */
    public void addLightModeEvent() {
        lightModeButton.setOnAction(e -> {
            adventureGameView.getButtonSound().play();
            if (!(this.state instanceof AlteredState)) {
                setState(new AlteredState(this));      // Change state to altered
            }
            this.state.darkMode = false;
            this.saveButton.setDisable(false);      //Enable settings save button
            // To show that light mode has been selected, disable light mode button & enable dark mode button
            lightModeButton.setDisable(true);
            darkModeButton.setDisable(false);
        });
    }

    /**
     * This method handles the event related to the
     * dark mode button.
     */
    public void addDarkModeEvent() {
        darkModeButton.setOnAction(e -> {
            adventureGameView.getButtonSound().play();
            if (!(this.state instanceof AlteredState)) {
                setState(new AlteredState(this));      // Change state to altered
            }
            this.state.darkMode = true;
            this.saveButton.setDisable(false);      //Enable settings save button
            // To show that dark mode has been selected, disable dark mode button & enable light mode button
            lightModeButton.setDisable(false);
            darkModeButton.setDisable(true);
        });
    }

    /**
     * This method applies the changes made to all game settings.
     */
    public void applyChanges() {
        if (anyPressed(getTextButtons()) || this.state instanceof DefaultState) {
            this.state.changeTextSize();
        }
        if (anyPressed(getContrastButtons()) || this.state instanceof DefaultState) {
            this.state.changeContrast();
        }
        if (anyPressed(getDarkModeButtons()) || this.state instanceof  DefaultState) {
            this.state.changeDarkMode();
        }
    }

    /**
     * Returns whether any of the buttons in the given list has been pressed.
     *
     * @param buttons The list of Buttons
     * @return true if any of the buttons are disabled, otherwise false.
     */
    public boolean anyPressed(ArrayList<Button> buttons) {
        for (Button b: buttons) {
            if (b.isDisabled()) {return true;}
        }
        return false;
    }

    /**
     * This method updates the state of this SettingsView
     * to the state given.
     *
     * @param state the state to change to
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * getAdventureGameView
     * __________________________
     * Getter method for the adventure game view associated with this settings view
     * @return adventureGameView
     */
    public AdventureGameView getAdventureGameView() {
        return adventureGameView;
    }

    /**
     * getDialogVbox
     * __________________________
     * Getter method for the dialog box associated with this settings view
     * @return dialogVbox
     */
    public VBox getDialogVbox() {
        return dialogVbox;
    }

    /**
     * getAllButtons
     * __________________________
     * Getter method for all buttons in this view
     * @return list of buttons
     */
    public ArrayList<Button> getAllButtons() {
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.addAll(getButtons());
        buttons.addAll(getTextButtons());
        buttons.addAll(getContrastButtons());
        return buttons;
    }

    /**
     * getButtons
     * __________________________
     * Getter method for general buttons (save, reset) in this view
     * @return list of buttons
     */
    public ArrayList<Button> getButtons() {
        return new ArrayList<>(Arrays.asList(saveButton, resetButton));
    }

    /**
     * getTextButtons
     * __________________________
     * Getter method for buttons corresponding to text size options
     * @return list of text size buttons
     */
    public ArrayList<Button> getTextButtons() {return new ArrayList<>(Arrays.asList(smallTextSizeButton, defaultTextSizeButton, largeTextSizeButton));}

    /**
     * getContrastButtons
     * __________________________
     * Getter method for buttons corresponding to contrast options
     * @return list of contrast option buttons
     */
    public ArrayList<Button> getContrastButtons() {return new ArrayList<>(Arrays.asList(lowContrastButton, defaultContrastButton, highContrastButton));}

    /**
     * getDarkModeButtons
     * __________________________
     * Getter method for dark/light mode buttons
     * @return list containing dark and light mode buttons
     */
    public ArrayList<Button> getDarkModeButtons() {return new ArrayList<>(Arrays.asList(lightModeButton, darkModeButton));}

    /**

     * getLabels
     * __________________________
     * Getter method for labels in this view
     * @return list of labels
     */
    public ArrayList<Label> getLabels() {
        return new ArrayList<>(Arrays.asList(statusMsg, textSizeLabel, contrastLabel, darkModeLabel));
    }

    /**
     * getSettingsLabel
     * __________________________
     * Getter method for the settings label in this view
     * @return settingsLabel
     */
    public Label getSettingsLabel() {
        return settingsLabel;
    }
}
