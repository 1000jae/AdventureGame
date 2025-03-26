package commands;

import AdventureModel.AdventureGame;

/**
 * TakeAll Class. Takes all objects in the current room into the player's inventory.
 */
public class TakeAll implements ObjectCommand{

    /**
     * The game being played.
     */
    AdventureGame model;


    /**
     * TakeAll Constructor.
     *
     * @param model The current AdventureGame the Player is playing.
     */
    public TakeAll(AdventureGame model){
        this.model = model;
    }

    /**
     * The execute method for TakeAll.
     * This method adds all the objects in the current room the player is to the player's inventory.
     * Then it clears out the list of objects in the room.
     */
    @Override
    public void execute() {
        this.model.getPlayer().inventory.addAll(this.model.getPlayer().getCurrentRoom().objectsInRoom);
        this.model.getPlayer().getCurrentRoom().objectsInRoom.clear();
    }
}
