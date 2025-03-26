package states;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import views.SettingsView;

import java.util.ArrayList;

/**
 * DefaultState Class. The default settings.
 */
public class DefaultState extends State{

    /**
     * DefaultState Constructor.
     *
     * @param settingsView The visualization of the settings menu.
     */
    public DefaultState(SettingsView settingsView){
        super(settingsView);
    }

    /**
     * Changes the size of all text in the game.
     */
    public void changeTextSize(){
        // Reset text size in main game window
        Font font = new Font("Arial", 16);
        ArrayList<Button> buttons = settingsView.getAdventureGameView().getAllButtons();
        setButtonFonts(buttons, font);        // Reset button fonts
        ArrayList<Label> labels = settingsView.getAdventureGameView().getAllLabels();
        setLabelFonts(labels, font);          // Reset label fonts
        settingsView.getAdventureGameView().getInputTextField().setFont(font);  // Reset text field font
        settingsView.getAdventureGameView().changeFont(font);    // Update font attribute for adventure game view

        // Reset text size in settings window
        buttons = settingsView.getButtons();
        buttons.addAll(settingsView.getContrastButtons());
        buttons.addAll(settingsView.getDarkModeButtons());
        setButtonFonts(buttons, font);       // Reset all button fonts (except for text size buttons)
        labels = settingsView.getLabels();
        setLabelFonts(labels, font);        // Reset font for all labels (except for settings title)
    }

    /**
     * Changes the color contrast in the game.
     */
    public void changeContrast() {
        // Reset color contrast in main game window
        ArrayList<ImageView> images = settingsView.getAdventureGameView().getImages();
        setImageContrast(images);       // Reset text opacity of all images
        ArrayList<Label> labels = settingsView.getAdventureGameView().getAllLabels();
        setTextOpacity(labels);         // Reset text opacity of all labels
        settingsView.getAdventureGameView().getInputTextField().setOpacity(1.0);    // Reset text opacity of input text field
        changeBgColors();     // Reset background of all windows


        // Update contrast & textOpacity attributes in adventure game view - will be used to update all other windows as well
        settingsView.getAdventureGameView().changeContrast(0.0, 1.0);

        // Change color contrast in settings window
        labels = settingsView.getLabels();
        labels.add(settingsView.getSettingsLabel());
        setTextOpacity(labels);        // Reset text opacity of all labels in settings window

        // Adjust contrast of all buttons
        ArrayList<Button> buttons = settingsView.getAdventureGameView().getAllButtons();
        buttons.addAll(settingsView.getAllButtons());
        setButtonContrast(buttons);
    }

    /**
     * Sets game to dark or light mode.
     */
    public void changeDarkMode() {
        changeBgColors();
        // Change text color in main game window
        ArrayList<Label> labels = settingsView.getAdventureGameView().getAllLabels();
        setLabelTextColors(labels);            // Change text color of labels
        settingsView.getAdventureGameView().changeTextColor("-fx-text-fill: white;");       // Update textColor attribute for adventure game view

        // Change text color in settings window
        labels = settingsView.getLabels();
        labels.add(settingsView.getSettingsLabel());
        setLabelTextColors(labels);        // Change text color of labels
    }

    /**
     * Sets the background color of all views to their default colors.
     */
    private void changeBgColors() {
        settingsView.getDialogVbox().setStyle("-fx-background-color: #121212;");   // Update background of the settings view
        // Update background in adventure game view
        settingsView.getAdventureGameView().getGridPane().setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));
        ArrayList<VBox> vBoxes = settingsView.getAdventureGameView().getVBoxes();
        for (VBox v: vBoxes) {
            v.setStyle("-fx-background-color: #000000;");
        }
        ArrayList<ScrollPane> scrollPanes = settingsView.getAdventureGameView().getScrollPanes();
        for (ScrollPane sp: scrollPanes) {
            sp.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        }
        settingsView.getAdventureGameView().changeBackgrounds("#000000", "#121212");    // Update bgColor attribute in adventure game view
    }

    /**
     * Changes the font of each button in the list to default font (Arial, 16).
     *
     * @param buttons list of buttons to edit
     * @param font the font to apply
     */
    private void setButtonFonts(ArrayList<Button> buttons, Font font) {
        for (Button b: buttons) {
            b.setFont(font);
        }
    }

    /**
     * Changes the font of each label in the list to the default font (Arial, 16).
     *
     * @param labels list of labels to edit
     * @param font the font to apply
     */
    private void setLabelFonts(ArrayList<Label> labels, Font font) {
        for (Label l: labels) {
            l.setFont(font);
        }
    }

    /**
     * Changes the text color of each label in the list to the default color (white).
     *
     * @param labels list of labels to edit
     */
    private void setLabelTextColors(ArrayList<Label> labels) {
        for (Label l: labels) {
            l.setStyle("-fx-text-fill: white;");
        }
    }

    /**
     * Sets the contrast of each image in the list to the default contrast (0.0).
     *
     * @param images list of images to edit
     */
    private void setImageContrast(ArrayList<ImageView> images) {
        for (ImageView i: images) {
            i.setEffect(new ColorAdjust());
        }
    }

    /**
     * Changes the opacity of each label in the list to the default opacity (1.0).
     *
     * @param labels list of labels to edit
     */
    private void setTextOpacity(ArrayList<Label> labels) {
        for (Label l: labels) {
            l.setOpacity(1.0);
        }
    }

    /**
     * Sets the contrast of each button in the list to the default contrast (0.0).
     *
     * @param buttons list of buttons to edit
     */
    private void setButtonContrast(ArrayList<Button> buttons) {
        for (Button b: buttons) {
            b.setEffect(new ColorAdjust());
        }
    }
}
