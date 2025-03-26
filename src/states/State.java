package states;

import views.SettingsView;

/**
 * Abstract Class State.
 */
public abstract class State {
    /**
     * The settings menu visualization.
     */
    SettingsView settingsView;

    /**
     * The size of the text.
     */
    public String textSize = "default";

    /**
     * The contrast.
     */
    public String contrast = "default";

    /**
     * The mode of the current game.
     * True if game is in dark mode. Else, false.
     */
    public Boolean darkMode = true;

    /**
     * State Constructor.
     *
     * @param settingsView The visualization of the settings menu.
     */
    State(SettingsView settingsView) {
        this.settingsView = settingsView;
    }

    /**
     * Changes the size of all text in the game.
     */
    public abstract void changeTextSize();

    /**
     * Changes the color contrast in the game.
     */
    public abstract void changeContrast();

    /**
     * Sets game to dark or light mode.
     */
    public abstract void changeDarkMode();
}
