package views;

import AdventureModel.*;
import AdventureModel.AdventureGame;
import AdventureModel.AdventureObject;
import AdventureModel.Timer;
import commands.DropAll;
import commands.TakeAll;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.input.KeyEvent; //you will need these!
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.event.EventHandler; //you will need this too!
import javafx.scene.AccessibleRole;
import javafx.scene.Node;

import java.io.File;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Class AdventureGameView.
 *
 * This is the Class that will visualize your model.
 */
public class AdventureGameView {
    /**
     * Model of the game.
     */
    AdventureGame model;

    /**
     * Stage on which all is rendered.
     */
    Stage stage;

    /**
     * Buttons
     */
    Button saveButton, loadButton, helpButton, settingsButton, showInvButton, showObjButton;

    /**
     * Buttons to take or drop all objects.
     */
    Button takeAllButton, dropAllButton;

    /**
     * List to track all images.
     */
    ArrayList<ImageView> images = new ArrayList<>();

    /**
     * Checks if help is displayed.
     */
    Boolean helpToggle = false;

    /**
     * Hold images and buttons.
     */
    GridPane gridPane = new GridPane();

    /**
     * Hold room description and/or instructions.
     */
    Label roomDescLabel = new Label();

    /**
     * List to track various labels (inv, obj, command).
     */
    ArrayList<Label> labels = new ArrayList<>();

    /**
     * Hold room items.
     */
    VBox objectsInRoom = new VBox();

    /**
     * Hold inventory items.
     */
    VBox objectsInInventory = new VBox();

    /**
     * Hold room image.
     */
    ImageView roomImageView;

    /**
     * For user input.
     */
    TextField inputTextField;

    /**
     * Track font size changes.
     */
    Font textFont = new Font("Arial", 16);

    /**
     * Manage the audio systems and play/stop audio.
     */
    private AudioContext audio;

    /**
     * Track changes in text color.
     */
    String textColor = "-fx-text-fill: white;";

    /**
     * Track contrast adjustments on text.
     */
    double textOpacity = 1.0;

    /**
     * Track contrast adjustments on images.
     */
    ColorAdjust contrast = new ColorAdjust();

    /**
     * Track changes in this view's background color.
     */
    String mainBgColor = "#000000";

    /**
     * Track changes in other views' background colors.
     */
    String subBgColor = "#121212";

    /**
     * List to track the various VBoxes used in this view.
     */
    ArrayList<VBox> vBoxes = new ArrayList<>();

    /**
     * List to track the various scroll panes used in this view.
     */
    ArrayList<ScrollPane> scrollPanes = new ArrayList<>();

    /**
     * The current room of player.
     */
    Object currRoom;

    /**
     * The timer for game time.
     */
    private Timer gameTimer;

    /**
     * To plays audio.
     */
    private MediaPlayer mediaPlayer;

    /**
     * Plays a sound effect for when buttons are clicked
     */
    private static final AudioClip buttonClick =
            new AudioClip("https://opengameart.org/sites/default/files/Menu%20Selection%20Click.wav");



    /**
     * Adventure Game View Constructor.
     * Initializes attributes.
     *
     * @param model The current AdventureGame being played.
     * @param stage The stage the visualization of the game is being added.
     */
    public AdventureGameView(AdventureGame model, Stage stage) {
        this.model = model;
        this.stage = stage;
        intiUI();
    }

    /**
     * Initialize the UI
     */
    public void intiUI() {

        // setting up the stage
        this.stage.setTitle("group_50's Adventure Game"); //Replace <YOUR UTORID> with your UtorID

        //Inventory + Room items
        objectsInInventory.setSpacing(10);
        objectsInInventory.setAlignment(Pos.TOP_CENTER);
        objectsInRoom.setSpacing(10);
        objectsInRoom.setAlignment(Pos.TOP_CENTER);

        // GridPane, anyone?
        gridPane.setPadding(new Insets(20));
        gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));

        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(150);
        ColumnConstraints column2 = new ColumnConstraints(650);
        ColumnConstraints column3 = new ColumnConstraints(150);
        column3.setHgrow( Priority.SOMETIMES ); //let some columns grow to take any extra space
        column1.setHgrow( Priority.SOMETIMES );

        // Row constraints
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints( 550 );
        RowConstraints row3 = new RowConstraints();
        row1.setVgrow( Priority.SOMETIMES );
        row3.setVgrow( Priority.SOMETIMES );

        gridPane.getColumnConstraints().addAll( column1 , column2 , column1 );
        gridPane.getRowConstraints().addAll( row1 , row2 , row1 );

        // Buttons
        saveButton = new Button("Save");
        saveButton.setId("Save");
        customizeButton(saveButton, 100, 50);
        makeButtonAccessible(saveButton, "Save Button", "This button saves the game.", "This button saves the game. Click it in order to save your current progress, so you can play more later.");
        addSaveEvent();

        loadButton = new Button("Load");
        loadButton.setId("Load");
        customizeButton(loadButton, 100, 50);
        makeButtonAccessible(loadButton, "Load Button", "This button loads a game from a file.", "This button loads the game from a file. Click it in order to load a game that you saved at a prior date.");
        addLoadEvent();

        helpButton = new Button("Instructions");
        helpButton.setId("Instructions");
        customizeButton(helpButton, 150, 50);
        makeButtonAccessible(helpButton, "Help Button", "This button gives game instructions.", "This button gives instructions on the game controls. Click it to learn how to play.");
        addInstructionEvent();

        settingsButton = new Button("Settings");
        settingsButton.setId("Settings");
        customizeButton(settingsButton, 100, 50);
        makeButtonAccessible(settingsButton, "Settings Button", "This button shows the game settings.", "This button shows the game settings. Click it in order to adjust the game settings.");
        addSettingsEvent();

        HBox topButtons = new HBox();
        topButtons.getChildren().addAll(saveButton, loadButton, helpButton, settingsButton);
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);

        // create Show All Object Button (Room)
        showInvButton = new Button("Show");
        showInvButton.setId("Show Inventory");
        customizeButton(showInvButton, 100, 50);
        makeButtonAccessible(showInvButton, "Show Inventory Button", "This button shows all the objects in the inventory.", "This button displays all the objects in the inventory. Click it to see your inventory.");
        addShowInv();

        // create Show All Object Button (Inventory)
        showObjButton = new Button("Show");
        showObjButton.setId("Show Room");
        customizeButton(showObjButton, 100, 50);
        makeButtonAccessible(showObjButton, "Show Room Button", "This button shows all the objects in the room.", "This button displays all the objects in the room. Click it to see all the objects in the room.");
        addShowObj();

        // create Take All Object Button
        takeAllButton = new Button("Take All");
        takeAllButton.setId("Take All");
        customizeButton(takeAllButton, 100, 50);
        makeButtonAccessible(takeAllButton, "Take All Button", "This button moves all objects in the room to the inventory.", "This button takes all objects in the room and puts into your inventory. Click it to take all objects in the current room.");
        addTakeAll();

        // create Drop All Object Button
        dropAllButton = new Button("Drop All");
        dropAllButton.setId("Drop All");
        customizeButton(dropAllButton, 100, 50);
        makeButtonAccessible(dropAllButton, "Drop All Button", "This button moves all objects in the inventory to the room.", "This button drops all objects in your inventory and puts into the room. Click it to drop all objects in your inventory.");
        addDropAll();

        inputTextField = new TextField();
        inputTextField.setFont(new Font("Arial", 16));
        inputTextField.setFocusTraversable(true);

        inputTextField.setAccessibleRole(AccessibleRole.TEXT_AREA);
        inputTextField.setAccessibleRoleDescription("Text Entry Box");
        inputTextField.setAccessibleText("Enter commands in this box.");
        inputTextField.setAccessibleHelp("This is the area in which you can enter commands you would like to play.  Enter a command and hit return to continue.");
        addTextHandlingEvent(); //attach an event to this input field

        //labels for inventory and room items
        Label objLabel =  new Label("Objects in Room");
        objLabel.setAlignment(Pos.CENTER);
        objLabel.setStyle(textColor);
        objLabel.setFont(new Font("Arial", 16));
        labels.add(objLabel);

        Label invLabel =  new Label("Your Inventory");
        invLabel.setAlignment(Pos.CENTER);
        invLabel.setStyle(textColor);
        invLabel.setFont(new Font("Arial", 16));
        labels.add(invLabel);

        VBox vbO = new VBox();
        vbO.getChildren().addAll(objLabel, showObjButton, takeAllButton);
        vbO.setSpacing(10);

        VBox vbI = new VBox();
        vbI.getChildren().addAll(invLabel, showInvButton, dropAllButton);
        vbI.setSpacing(10);

        //add all the widgets to the GridPane
        gridPane.add( vbO, 0, 0, 1, 1 );  // Add label
        gridPane.add( topButtons, 1, 0, 1, 1 );  // Add buttons
        gridPane.add( vbI, 2, 0, 1, 1 );  // Add label

        Label commandLabel = new Label("What would you like to do?");
        commandLabel.setStyle(textColor);
        commandLabel.setFont(new Font("Arial", 16));
        labels.add(commandLabel);

        updateScene(""); //method displays an image and whatever text is supplied
        updateItems(); //update items shows inventory and objects in rooms

        // adding the text area and submit button to a VBox
        VBox textEntry = new VBox();
        textEntry.setStyle("-fx-background-color: #000000;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(commandLabel, inputTextField);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);
        gridPane.add( textEntry, 0, 2, 3, 1 );
        vBoxes.add(textEntry);

        // Render everything
        var scene = new Scene( gridPane ,  1000, 800);
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();

        playBackgroundMusic(); // after everything is rendered, play background music with it
        // need to move this, over here it stops playing the moment the main screen changes (like with opening inv
        // menus or with instructions, also doesn't play under the speaker talking - maybe needs its own thread too??)
        // it stops playing when the room is changed also

        //add change
        currRoom = this.model.getPlayer().getCurrentRoom();

        stage.setOnCloseRequest(e -> {
            // Setting this up to stop any playing audio when closing the application window
            stopArticulation();
        });

        //save current room
        currRoom = this.model.getPlayer().getCurrentRoom();

        //Create gameTimer and start the time
        gameTimer = Timer.getInstance();
        gameTimer.startTimer();
    }


    /**
     * makeButtonAccessible
     * __________________________
     * For information about ARIA standards, see
     * https://www.w3.org/WAI/standards-guidelines/aria/
     *
     * @param inputButton the button to add screenreader hooks to
     * @param name ARIA name
     * @param shortString ARIA accessible text
     * @param longString ARIA accessible help text
     */
    public static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }

    /**
     * customizeButton
     * __________________________
     *
     * @param inputButton the button to make stylish :)
     * @param w width
     * @param h height
     */
    private void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(textFont);
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        inputButton.setEffect(contrast);
    }

    /**
     * addTextHandlingEvent
     * __________________________
     * Add an event handler to the myTextField attribute
     *
     * Your event handler should respond when users
     * hits the ENTER or TAB KEY. If the user hits
     * the ENTER Key, strip white space from the
     * input to myTextField and pass the stripped
     * string to submitEvent for processing.
     *
     * If the user hits the TAB key, move the focus
     * of the scene onto any other node in the scene
     * graph by invoking requestFocus method.
     */
    private void addTextHandlingEvent() {
        inputTextField.setOnKeyPressed(new EventHandler<KeyEvent>(){
            public void handle(KeyEvent k) {
                String textInput = inputTextField.getText();
                if (k.getCode() == KeyCode.ENTER) {
                    submitEvent(textInput.strip());
                    inputTextField.setText("");
                }
                if (k.getCode() == KeyCode.TAB) {gridPane.requestFocus();}
            }
        });
    }


    /**
     * submitEvent
     * __________________________
     *
     * @param text the command that needs to be processed
     */
    private void submitEvent(String text) {

        text = text.strip(); //get rid of white space
        stopArticulation(); //if speaking, stop

        if (text.equalsIgnoreCase("LOOK") || text.equalsIgnoreCase("L")) {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription();
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            if (!objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
            articulateRoomDescription(); //all we want, if we are looking, is to repeat description.
            return;
        } else if (text.equalsIgnoreCase("HELP") || text.equalsIgnoreCase("H")) {
            showInstructions();
            return;
        } else if (text.equalsIgnoreCase("COMMANDS") || text.equalsIgnoreCase("C")) {
            showCommands(); //this is new!  We did not have this command in A1
            return;
        }
        // if user inputs equivalence of "INVENTORY" call show all objects in inventory
        else if (text.equalsIgnoreCase("INVENTORY") || text.equalsIgnoreCase("I")) {
            showAllObjects("INVENTORY");
            return;
        }
        // if user inputs equivalence of "OBJECTS" call show all objects in room
        else if (text.equalsIgnoreCase("OBJECTS") || text.equalsIgnoreCase("O")) {
            showAllObjects("ROOM"); //this is new!  We did not have this command in A1
            return;
        }

        //try to move!
        String output = this.model.interpretAction(text); //process the command!

        if (output == null || (!output.equals("GAME OVER") && !output.equals("FORCED") && !output.equals("HELP"))) {
            updateScene(output);
            updateItems();
        } else if (output.equals("GAME OVER")) {
            //print time elapsed on screen
            gameTimer.endTimer();
            updateScene("You took: " +
                   gameTimer.getTime() + " seconds");
            updateItems();
            PauseTransition pause = new PauseTransition(Duration.seconds(10));
            pause.setOnFinished(event -> {
                Platform.exit();
            });
            pause.play();
        } else if (output.equals("FORCED")) {
            //write code here to handle "FORCED" events!
            //Your code will need to display the image in the
            //current room and pause, then transition to
            //the forced room.
            inputTextField.setEditable(false);      // Prevent user from interacting with objects & text field in forced events
            objectsInInventory.setDisable(true);
            objectsInRoom.setDisable(true);
            updateScene("");
            updateItems();
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event -> {
                inputTextField.setEditable(true);       // Make it so that the user can interact with objects & text field once they reach a non-forced room
                objectsInInventory.setDisable(false);
                objectsInRoom.setDisable(false);
                submitEvent("FORCED");
            });
            pause.play();
        }
    }


    /**
     * showCommands
     * __________________________
     *
     * update the text in the GUI (within roomDescLabel)
     * to show all the moves that are possible from the
     * current room.
     */
    private void showCommands() {
        roomDescLabel.setText("You can move in the following directions:\n" + model.getPlayer().getCurrentRoom().getCommands());
    }


    /**
     * updateScene
     * __________________________
     *
     * Show the current room, and print some text below it.
     * If the input parameter is not null, it will be displayed
     * below the image.
     * Otherwise, the current room description will be displayed
     * below the image.
     *
     * @param textToDisplay the text to display below the image.
     */
    public void updateScene(String textToDisplay) {

        getRoomImage(); //get the image of the current room
        formatText(textToDisplay); //format the text to display
        roomDescLabel.setPrefWidth(500);
        roomDescLabel.setPrefHeight(500);
        roomDescLabel.setTextOverrun(OverrunStyle.CLIP);
        roomDescLabel.setWrapText(true);
        VBox roomPane = new VBox(roomImageView,roomDescLabel);
        roomPane.setPadding(new Insets(10));
        roomPane.setAlignment(Pos.TOP_CENTER);
        roomPane.setStyle("-fx-background-color: " + mainBgColor + ";");
        vBoxes.add(roomPane);

        gridPane.add(roomPane, 1, 1);
        stage.sizeToScene();

        //finally, articulate the description
        if (textToDisplay == null || textToDisplay.isBlank()) articulateRoomDescription();

        if (!this.model.getPlayer().getCurrentRoom().equals(currRoom)){
            autoSaveGame();}
    }

    /**
     * formatText
     * __________________________
     *
     * Format text for display.
     *
     * @param textToDisplay the text to be formatted for display.
     */
    private void formatText(String textToDisplay) {
        if (textToDisplay == null || textToDisplay.isBlank()) {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription() + "\n";
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            if (objectString != null && !objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
            else roomDescLabel.setText(roomDesc);
        } else roomDescLabel.setText(textToDisplay);
        roomDescLabel.setStyle(textColor);
        roomDescLabel.setFont(textFont);
        roomDescLabel.setAlignment(Pos.CENTER);
    }

    /**
     * getRoomImage
     * __________________________
     *
     * Get the image for the current room and place
     * it in the roomImageView
     */
    private void getRoomImage() {

        int roomNumber = this.model.getPlayer().getCurrentRoom().getRoomNumber();
        String roomImage = this.model.getDirectoryName() + "/room-images/" + roomNumber + ".png";

        Image roomImageFile = new Image(roomImage);
        roomImageView = new ImageView(roomImageFile);
        roomImageView.setPreserveRatio(true);
        roomImageView.setFitWidth(400);
        roomImageView.setFitHeight(400);
        roomImageView.setEffect(contrast);
        images.add(roomImageView);

        //set accessible text
        roomImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        roomImageView.setAccessibleText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
        roomImageView.setFocusTraversable(true);
    }

    // Helper Method
    /**
     * getButton
     * ----------------
     * Return a button with the image of the given object.
     *
     * @param o the object to be associated with the created button
     * @return a button associated with given object
     */
    private Button getButton(AdventureObject o) {
        // Create ImageView with object image
        Image image = new Image(model.getDirectoryName() + File.separator + "objectImages" + File.separator + o.getName() + ".jpg");
        ImageView objectImage = new ImageView();

        objectImage.setPreserveRatio(true);
        objectImage.setFitHeight(100);
        objectImage.setFitWidth(100);
        objectImage.setImage(image);
        objectImage.setEffect(contrast);
        images.add(objectImage);

        // Create button with object image
        Button objectButton = new Button();
        objectButton.setGraphic(objectImage);
        objectButton.setEffect(contrast);

        return objectButton;
    }

    /**
     * updateItems
     * __________________________
     *
     * This method is partially completed, but you are asked to finish it off.
     *
     * The method should populate the objectsInRoom and objectsInInventory Vboxes.
     * Each Vbox should contain a collection of nodes (Buttons, ImageViews, you can decide)
     * Each node represents a different object.
     *
     * Images of each object are in the assets
     * folders of the given adventure game.
     */
    public void updateItems() {

        // Get list of objects in current room
        ArrayList<AdventureObject> roomObjects = model.getPlayer().getCurrentRoom().objectsInRoom;

        objectsInRoom.getChildren().clear();        // Empty out objectsInRoom Vbox

        // Add all objects in a given room to the objectsInRoom Vbox
        for (AdventureObject o: roomObjects) {
            Button objectButton = getButton(o);

            makeButtonAccessible(objectButton, o.getName(), "This button puts the following object in your inventory: " + o.getName(),
                    o.getDescription() + ". Click on this object to put it in your inventory.");
            objectsInRoom.getChildren().add(objectButton);  // Put object image button in objectsInRoom Vbox

            objectButton.setOnAction(e -> {
                model.interpretAction("TAKE " + o.getName());
                updateItems();
            });
        }

        // Get list of objects in player inventory
        ArrayList<AdventureObject> inventory = model.getPlayer().inventory;

        objectsInInventory.getChildren().clear();        // Empty out objectsInInventory Vbox

        // Add all objects in player's current inventory to the objectsInRoom Vbox
        for (AdventureObject o: inventory) {
            Button objectButton = getButton(o);

            makeButtonAccessible(objectButton, o.getName(), "This button removes the following object from your inventory: " + o.getName(),
                    o.getDescription() + ". Click on this object to drop it from your inventory.");
            objectsInInventory.getChildren().add(objectButton);    // Put object image button in objectsInInventory Vbox

            objectButton.setOnAction(e -> {
                model.interpretAction("DROP " + o.getName());
                updateItems();
            });
        }

        ScrollPane scO = new ScrollPane(objectsInRoom);
        scO.setPadding(new Insets(10));
        scO.setStyle("-fx-background: " + mainBgColor + "; -fx-background-color:transparent;");
        scO.setFitToWidth(true);
        gridPane.add(scO,0,1);
        scrollPanes.add(scO);

        ScrollPane scI = new ScrollPane(objectsInInventory);
        scI.setPadding(new Insets(10));
        scI.setFitToWidth(true);
        scI.setStyle("-fx-background: " + mainBgColor + "; -fx-background-color:transparent;");
        gridPane.add(scI,2,1);
        scrollPanes.add(scI);
    }

    /**
     * Shows and voices out the game instructions.
     *
     * If helpToggle is FALSE:
     * -- display the help text in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- use whatever GUI elements to get the job done!
     * -- set the helpToggle to TRUE
     * -- REMOVE whatever nodes are within the cell beforehand!
     *
     * If helpToggle is TRUE:
     * -- redraw the room image in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- set the helpToggle to FALSE
     * -- Again, REMOVE whatever nodes are within the cell beforehand!
     */
    public void showInstructions() {
        ObservableList<Node> nodes = gridPane.getChildren();
        nodes.removeIf(n -> GridPane.getRowIndex(n) == 1 && GridPane.getRowIndex(n) == 1);  // Remove nodes within cell 1, 1

        if (helpToggle) {
            helpToggle = false;
            stopArticulation(); // if the help is toggled off, turn off the TTS instructions too
            updateScene("");
            updateItems();
        }
        else {
            helpToggle = true;
            audio = new AudioContext(new TTSStrategy());
            Label instr = new Label(model.getInstructions());
            labels.add(instr);
            instr.setFont(textFont);
            instr.setWrapText(true);
            instr.setTextFill(Color.WHITE);
            instr.setOpacity(textOpacity);
            ScrollPane instrDisplay = new ScrollPane(instr);        // Place instruction text into a scroll pane
            instrDisplay.setFitToWidth(true);
            instrDisplay.setStyle("-fx-background: " + mainBgColor + "; -fx-background-color:transparent;");
            scrollPanes.add(instrDisplay);
            gridPane.add(instrDisplay, 1, 1);     // Set cell 1, 1 to display instructions
            updateItems();
            audio.playAudio(model.getInstructions());
        }
    }

    /**
     * This method handles the event related to the
     * help button.
     */
    public void addInstructionEvent() {
        helpButton.setOnAction(e -> {
            buttonClick.play();
            stopArticulation(); //if speaking, stop
            showInstructions();
        });
    }

    /**
     * This method handles the event related to the
     * save button.
     */
    public void addSaveEvent() {
        saveButton.setOnAction(e -> {
            buttonClick.play();
            gridPane.requestFocus();
            SaveView saveView = new SaveView(this);
        });
    }

    /**
     * This method handles the event related to the
     * load button.
     */
    public void addLoadEvent() {
        loadButton.setOnAction(e -> {
            buttonClick.play();
            gridPane.requestFocus();
            LoadView loadView = new LoadView(this);
        });
    }

    /**
     * This method handles the event related to the
     * settings button.
     */
    public void addSettingsEvent() {
        settingsButton.setOnAction(e -> {
            buttonClick.play();
            gridPane.requestFocus();
            SettingsView settingsView = new SettingsView(this);
        });
    }


    /**
     * This method articulates Room Descriptions
     */
    public void articulateRoomDescription() {
        // changed this to choosing what strategy should be used, and playing it using the strategy and AudioContext
        String musicFile;
        String adventureName = this.model.getDirectoryName();
        String roomName = this.model.getPlayer().getCurrentRoom().getRoomName();
        String description = this.model.getPlayer().getCurrentRoom().getRoomDescription();

        if (!this.model.getPlayer().getCurrentRoom().getVisited()) {
            musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-long.mp3";
        } else {
            musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-short.mp3" ;
        }

        musicFile = musicFile.replace(" ","-");
        File audioTrack = new File(musicFile);

        if (audioTrack.isFile()) {
            // if the file path is valid, choose the strategy of playing the audio from the path
            audio = new AudioContext(new FileStrategy());
            description = musicFile;
            // updates the string to be passed to playAudio to the file path of the music file
        } else {
            audio = new AudioContext(new TTSStrategy());
        }

        audio.playAudio(description);
    }

    /**
     * This method stops articulations
     * (useful when transitioning to a new room or loading a new game)
     */
    public void stopArticulation() {
        // changed to call the stopAudio method of the AudioContext which will call its respective strategy to stop
        audio.stopAudio();
    }


    /**
     * This method saves the game when called
     */
    private void autoSaveGame(){
        //maybe new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())
        File gameFile = new File("Games" + File.separator +  "Saved" + File.separator +
                new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".ser");
        this.model.saveModel(gameFile);
    }

    /**
     * This method handles the event related to the
     * showObjButton.
     */
    public void addShowObj(){
        showObjButton.setOnAction(e -> {
            stopArticulation();
            buttonClick.play();
            showAllObjects("ROOM");
        });
    }

    /**
     * This method handles the event related to the
     * showInvButton.
     */
    public void addShowInv() {
        showInvButton.setOnAction(e -> {
            stopArticulation();
            buttonClick.play();
            showAllObjects("INVENTORY");
        });
    }

    /**
     * This method shows all objects in the inventory or room.
     *
     *  @param location the place the list of the objects is from
     */
    public void showAllObjects(String location) {
        ObservableList<Node> nodes = gridPane.getChildren();
        nodes.removeIf(n -> GridPane.getRowIndex(n) == 1 || GridPane.getRowIndex(n) == 0);

        int i = 0;
        int j = 1;
        GridPane invGrid = new GridPane();
        Label showLabel = new Label();

        // create button to go back to the game display
        Button closeButton = new Button("Close");
        closeButton.setId("Close");
        customizeButton(closeButton, 100, 50);
        closeButton.setOnAction(e -> {
            buttonClick.play();
            updateTopButtons();
            updateScene("");
            updateItems();
        });

        ArrayList<AdventureObject> objList;
        // get the list of objects in the inventory
        if (location.equals("INVENTORY")){
            objList = model.getPlayer().inventory;
            showLabel.setText("INVENTORY");
        }
        // get the list of objects in the room
        else {
            objList = model.getPlayer().getCurrentRoom().objectsInRoom;
            showLabel.setText("OBJECTS IN ROOM");
        }

        createLabel(showLabel, 20);

        VBox showBox = new VBox();
        showBox.getChildren().addAll(showLabel, closeButton);
        showBox.setSpacing(10);
        showBox.setAlignment(Pos.CENTER);

        // if there are no objects, display that there are no objects
        if (objList.isEmpty()){
            Label empty = new Label(showLabel.getText() + " IS EMPTY");
            createLabel(empty, 20);
            gridPane.add(showBox, 1, 0);
            gridPane.add(empty, 1, 1);
        }

        // else, create buttons for each object and display 5 in each row.
        else{
            for (AdventureObject obj : objList){
                if (i == 3){
                    j += 1;
                    i = 0;
                }
                invGrid.add(getObjects(obj), i, j);
                i+=1;
            }

            invGrid.setHgap(10);
            invGrid.setVgap(10);
            invGrid.setAlignment(Pos.CENTER);
            ScrollPane objSc = new ScrollPane();
            objSc.setContent(invGrid);
            objSc.setPadding(new Insets(10));
            objSc.setStyle("-fx-background: " + mainBgColor + "; -fx-background-color:transparent;");
            objSc.setFitToWidth(true);

            gridPane.add(showBox, 1, 0);
            gridPane.add(objSc, 1, 1);
        }
    }

    /**
     * This method creates a Button for an object.
     *
     * @param obj the object to create the button for.
     */
    private Button getObjects(AdventureObject obj){
        ImageView objImage = new ImageView(new Image(model.getDirectoryName() + File.separator + "objectImages" + File.separator + obj.getName() + ".jpg"));

        objImage.setPreserveRatio(true);
        objImage.setFitHeight(150);
        objImage.setFitWidth(150);
        objImage.setEffect(contrast);
        images.add(objImage);

        Button objectButton = new Button();
        objectButton.setGraphic(objImage);
        objectButton.setText(obj.getName());
        objectButton.setFont(textFont);
        objectButton.setTextAlignment(TextAlignment.CENTER);
        objectButton.setContentDisplay(ContentDisplay.TOP);
        makeButtonAccessible(objectButton, obj.getName(), "This button represents the following object: " + obj.getName(),
                "This button is " + obj.getDescription());
        return objectButton;
    }

    /**
     * This method customizes the given Label with the given size for the font.
     *
     * @param label the Label to be customized
     * @param size the font size the Label texts should have
     */
    private void createLabel(Label label, double size){
        label.setAlignment(Pos.CENTER);
        label.setStyle(textColor);
        label.setFont(new Font("Arial", size));
        label.setOpacity(textOpacity);
    }

    /**
     * This method shows the save, help, and oad buttons
     * at the top row of the game display.
     */
    public void updateTopButtons(){
        ObservableList<Node> nodes = gridPane.getChildren();
        nodes.removeIf(n -> GridPane.getRowIndex(n) == 0);

        Label objLabel =  new Label("Objects in Room");
        createLabel(objLabel, textFont.getSize());
        labels.add(objLabel);

        Label invLabel =  new Label("Your Inventory");
        createLabel(invLabel, textFont.getSize());
        labels.add(invLabel);


        VBox vbO = new VBox();
        vbO.getChildren().addAll(objLabel, showObjButton, takeAllButton);
        vbO.setSpacing(10);

        VBox vbI = new VBox();
        vbI.getChildren().addAll(invLabel, showInvButton, dropAllButton);
        vbI.setSpacing(10);

        HBox topButtons = new HBox();
        topButtons.getChildren().addAll(saveButton, loadButton, helpButton, settingsButton);
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);

        gridPane.add( vbO, 0, 0, 1, 1 );  // Add label
        gridPane.add( topButtons, 1, 0, 1, 1 );  // Add buttons
        gridPane.add( vbI, 2, 0, 1, 1 );  // Add label

    }

    /**
     * This method updates the font attribute to the given font
     *
     * @param font the font that should be applied to all text in game
     */
    public void changeFont(Font font) {textFont = font;}

    /**
     * This method updates the contrast and textOpacity attributes to the given adjustments
     *
     * @param contrastAdjustment the contrast adjustment that should be applied to the game
     * @param opacity the new text opacity
     */
    public void changeContrast(double contrastAdjustment, double opacity) {
        contrast.setContrast(contrastAdjustment);
        textOpacity = opacity;
    }

    /**
     * This method updates the mainBgColor and subBgColor attributes to the given corresponding colors
     *
     * @param mainColor the new background color for this view
     * @param subColor the new background color for the other views
     */
    public void changeBackgrounds(String mainColor, String subColor) {
        mainBgColor = mainColor;
        subBgColor = subColor;
    }

    /**
     * This method updates the textColor attribute to the given color
     *
     * @param color the new text color
     */
    public void changeTextColor(String color) {
        textColor = color;
    }

    /**
     * getInputTextField
     * __________________________
     * Getter method for input text field
     * @return the TextField where user inputs commands
     */
    public TextField getInputTextField() {
        return inputTextField;
    }

    /**
     * getAllButtons
     * __________________________
     * Getter method for buttons in this view
     * @return list of buttons
     */
    public ArrayList<Button> getAllButtons() {
        return new ArrayList<>(Arrays.asList(saveButton, loadButton, helpButton, settingsButton, showInvButton, showObjButton, takeAllButton, dropAllButton));
    }

    /**
     * getAllLabels
     * __________________________
     * Getter method for labels in this view
     * @return list of labels
     */
    public ArrayList<Label> getAllLabels() {
        ArrayList<Label> allLabels = new ArrayList<>(Arrays.asList(roomDescLabel));
        allLabels.addAll(labels);
        return allLabels;
    }

    /**
     * getVBoxes
     * __________________________
     * Getter method for VBoxes in this view
     * @return list of VBoxes
     */
    public ArrayList<VBox> getVBoxes() {
        return vBoxes;
    }

    /**
     * getScrollPanes
     * __________________________
     * Getter method for scroll panes in this view
     * @return list of ScrollPanes
     */
    public ArrayList<ScrollPane> getScrollPanes() {
        return scrollPanes;
    }

    /**
     * getImages
     * __________________________
     * Getter method for all images used in game
     * @return list of images
     */
    public ArrayList<ImageView> getImages() {
        return images;
    }

    /**
     * getGridPane
     * __________________________
     * Getter method for grid pane used in this view
     * @return gridPane
     */
    public GridPane getGridPane() {
        return gridPane;
    }

    /** getButtonSound
     * __________________________
     * Getter method for the button click sound used in this view
     * @return the AudioClip with the associated sound effect
     */
    public AudioClip getButtonSound() {
        return buttonClick;
    }

    /** playBackgroundMusic
     * __________________________
     * Method that plays the background music for the game if it exists.
     * Plays the file "background-music.mp3" in the game directory's sounds folder.
     */
    public void playBackgroundMusic() {
        String adventureName = this.model.getDirectoryName();

        String musicPath = "./" + adventureName + "/sounds/" + "background-music.mp3";
        musicPath = musicPath.replace(" ", "-");
        File musicFile = new File(musicPath);

        if (musicFile.isFile()) { // plays background music only if there is background music provided by the game
            Media sound = new Media(musicFile.toURI().toString());
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.7);
            mediaPlayer.play();
            // doesn't need a boolean to check if it's playing or not as it'll continue to loop as background music
        }
    }

    /**
     * getMainBgColor
     * __________________________
     * Getter method for mainBgColor
     * @return mainBgColor
     */
    public String getMainBgColor() {
        return mainBgColor;
    }

    /**
     * getSubBgColor
     * __________________________
     * Getter method for subBgColor
     * @return subBgColor
     */
    public String getSubBgColor() {
        return subBgColor;
    }

    /**
     * This method handles the events of the TakeAllButton.
     */
    public void addTakeAll(){
        TakeAll take = new TakeAll(this.model);
        takeAllButton.setOnAction(e -> {
            take.execute();
            buttonClick.play();
            updateItems();
        });
    }

    /**
     * This method handles the events of the DropAllButton.
     */
    public void addDropAll(){
        DropAll drop  = new DropAll(this.model);
        dropAllButton.setOnAction(e -> {
            drop.execute();
            buttonClick.play();
            updateItems();
        });
    }
}
