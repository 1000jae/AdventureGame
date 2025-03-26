package states;

import javafx.geometry.Insets;
import javafx.scene.control.*;
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
 * AlteredState Class. Make changes to the Settings.
 */
public class AlteredState extends State{

    /**
     * AlteredState Constructor.
     *
     * @param settingsView The visualization of the settings menu.
     */
    public AlteredState(SettingsView settingsView){
        super(settingsView);
    }

    /**
     * Changes the size of all text in the game.
     */
    public void changeTextSize(){
        // Determine the font size to be used
        Font font;
        if (this.textSize.equals("small")) {
            font = new Font("Arial", 12);
        } else if (this.textSize.equals("large")) {
            font = new Font("Arial", 20);
        } else {
            font = new Font("Arial", 16);
        }

        // Change text size in main game window
        ArrayList<Button> buttons = settingsView.getAdventureGameView().getAllButtons();
        setButtonFonts(buttons, font);          // Change button fonts
        ArrayList<Label> labels = settingsView.getAdventureGameView().getAllLabels();
        setLabelFonts(labels, font);            // Change label fonts
        settingsView.getAdventureGameView().getInputTextField().setFont(font);     // Change text field font
        settingsView.getAdventureGameView().changeFont(font);       // Update font attribute for adventure game view

        // Change text size in settings window
        buttons = settingsView.getButtons();
        buttons.addAll(settingsView.getContrastButtons());
        buttons.addAll(settingsView.getDarkModeButtons());
        setButtonFonts(buttons, font);      // Change all button fonts (except for text size buttons)
        labels = settingsView.getLabels();
        setLabelFonts(labels, font);        // Change font for all labels (except for settings title)
    }

    /**
     * Changes the color contrast in the game.
     */
    public void changeContrast() {
        // Determine the contrast adjustments
        ColorAdjust adjustment = new ColorAdjust();
        double textOpacity = 1.0;
        String bgColorMain = settingsView.getAdventureGameView().getMainBgColor();
        String bgColorSub = settingsView.getAdventureGameView().getSubBgColor();
        if (this.contrast.equals("low")) {
            adjustment.setContrast(-0.4);
            textOpacity = 0.7;
            // If main background color is white (i.e. light mode & high/default contrast is on), make backgrounds light grey
            if (bgColorMain.equals("#ffffff")) {
                bgColorMain = "#e8e8e8";
                bgColorSub = "#dddddd";
            }
            // If main background color is dark grey (i.e. dark mode & high/default contrast is on), make backgrounds dark grey
            if (bgColorMain.equals("#000000")) {
                bgColorMain = "#3f3f3f";
                bgColorSub = "#4c4c4c";
            }
            changeBgColors(bgColorMain, bgColorSub);
        } else if (this.contrast.equals("high")) {
            // If main background color is light grey (i.e. light mode & low contrast is on), make backgrounds white
            if (bgColorMain.equals("#e8e8e8")) {
                bgColorMain = "#ffffff";
                bgColorSub = "#f7f7f7";
            }
            // If main background color is dark gray (i.e. dark mode & low contrast is on), make backgrounds black
            if (bgColorMain.equals("#3f3f3f")){
                bgColorMain = "#000000";
                bgColorSub = "#121212";
            }
            changeBgColors(bgColorMain, bgColorSub);
            adjustment.setContrast(0.2);
        } else {
            // If main background color is light grey (i.e. light mode & low contrast is on), make backgrounds white
            if (bgColorMain.equals("#e8e8e8")) {
                bgColorMain = "#ffffff";
                bgColorSub = "#f7f7f7";
            }
            // If main background color is dark gray (i.e. dark mode & low contrast is on), make backgrounds black
            if (bgColorMain.equals("#3f3f3f")){
                bgColorMain = "#000000";
                bgColorSub = "#121212";
            }
            adjustment.setContrast(0.0);
        }

        // Change color contrast in main game window
        ArrayList<ImageView> images = settingsView.getAdventureGameView().getImages();
        setImageContrast(images, adjustment);        // Adjust color contrast of all images
        ArrayList<Label> labels = settingsView.getAdventureGameView().getAllLabels();
        setTextOpacity(labels, textOpacity);         // Adjust text opacity of all labels
        settingsView.getAdventureGameView().getInputTextField().setOpacity(textOpacity);    // Adjust input text field opacity
        changeBgColors(bgColorMain, bgColorSub);     // Change background of all windows

        // Update contrast & textOpacity attributes in adventure game view - will be used to update all other windows as well
        settingsView.getAdventureGameView().changeContrast(adjustment.getContrast(), textOpacity);

        // Change color contrast in settings window
        labels = settingsView.getLabels();
        labels.add(settingsView.getSettingsLabel());
        setTextOpacity(labels, textOpacity);        // Adjust text opacity of all labels in settings window

        // Adjust contrast of all buttons
        ArrayList<Button> buttons = settingsView.getAdventureGameView().getAllButtons();
        buttons.addAll(settingsView.getAllButtons());
        setButtonContrast(buttons, adjustment);
    }

    /**
     * Sets game to dark or light mode.
     */
    public void changeDarkMode() {
        String mainBgColor = settingsView.getAdventureGameView().getMainBgColor();
        if (darkMode) {
            if (mainBgColor.equals("#e8e8e8")) {    // If background is light grey (i.e. low contrast is on), change background to dark grey
                changeBgColors("#3f3f3f", "#4c4c4c");
            }
            if (mainBgColor.equals("#ffffff")){    // Otherwise, if background is white (i.e. contrast is default/high), change background to black
                changeBgColors("#000000", "#121212");
            }
            changeTextColor("-fx-text-fill: white;");
        }
        else {
            if (mainBgColor.equals("#3f3f3f")) {    // If background is dark grey (i.e. low contrast is on), change background to light grey
                changeBgColors("#e8e8e8", "#dddddd");
            }
            if (mainBgColor.equals("#000000")) {   // Otherwise, if background is black (i.e. contrast is default/high), change background to white
                changeBgColors("#ffffff", "#f7f7f7");
            }
            changeTextColor("-fx-text-fill: black;");
        }
    }

    /**
     * Updates the background color of all views
     *
     * @param mainColor the new background color for the main game (i.e. AdventureGameView)
     * @param subColor the new background color for the other views
     */
    private void changeBgColors(String mainColor, String subColor) {
        settingsView.getDialogVbox().setStyle("-fx-background-color: " + subColor + ";");   // Update background of the settings view
        // Update background in adventure game view
        settingsView.getAdventureGameView().getGridPane().setBackground(new Background(new BackgroundFill(
                Color.valueOf(mainColor),
                new CornerRadii(0),
                new Insets(0)
        )));
        ArrayList<VBox> vBoxes = settingsView.getAdventureGameView().getVBoxes();
        for (VBox v: vBoxes) {
            v.setStyle("-fx-background-color: " + mainColor + ";");
        }
        ArrayList<ScrollPane> scrollPanes = settingsView.getAdventureGameView().getScrollPanes();
        for (ScrollPane sp: scrollPanes) {
            sp.setStyle("-fx-background: " + mainColor + "; -fx-background-color:transparent;");
        }
        settingsView.getAdventureGameView().changeBackgrounds(mainColor, subColor);    // Update bgColor attribute in adventure game view
    }

    /**
     * Updates the color of text in the game
     *
     * @param color the new text color
     */
    private void changeTextColor(String color) {
        // Change text color in main game window
        ArrayList<Label> labels = settingsView.getAdventureGameView().getAllLabels();
        setLabelTextColors(labels, color);            // Change text color of labels
        settingsView.getAdventureGameView().changeTextColor(color);       // Update textColor attribute for adventure game view

        // Change text color in settings window
        labels = settingsView.getLabels();
        labels.add(settingsView.getSettingsLabel());
        setLabelTextColors(labels, color);        // Change text color of labels
    }

    /**
     * Changes the font of each button in the list to the given font.
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
     * Changes the font of each label in the list to the given font.
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
     * Changes the text color of each label in the list to the given color.
     *
     * @param labels list of labels to edit
     * @param color the text color to apply
     */
    private void setLabelTextColors(ArrayList<Label> labels, String color) {
        for (Label l: labels) {
            l.setStyle(color);
        }
    }

    /**
     * Changes the contrast of each image in the list to the given adjustment.
     *
     * @param images list of images to edit
     * @param adjustment the contrast adjustment to apply
     */
    private void setImageContrast(ArrayList<ImageView> images, ColorAdjust adjustment) {
        for (ImageView i: images) {
            i.setEffect(adjustment);
        }
    }

    /**
     * Changes the opacity of each label in the list to the given value.
     *
     * @param labels list of labels to edit
     * @param opacity the opacity to apply
     */
    private void setTextOpacity(ArrayList<Label> labels, double opacity) {
        for (Label l: labels) {
            l.setOpacity(opacity);
        }
    }

    /**
     * Changes the contrast of each button in the list to the given adjustment.
     *
     * @param buttons list of buttons to edit
     * @param adjustment the opacity to apply
     */
    private void setButtonContrast(ArrayList<Button> buttons, ColorAdjust adjustment) {
        for (Button b: buttons) {
            b.setEffect(adjustment);
        }
    }
}
