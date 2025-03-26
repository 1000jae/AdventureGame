package commands;

import AdventureModel.AdventureGame;

/**
 * DropAll Class. Drops all objects in the player's inventory to the current room.
 */
public class DropAll implements ObjectCommand{

    /**
     * The game being played.
     */
    AdventureGame model;

    /**
     * DropAll Constructor.
     *
     * @param model The current AdventureGame the Player is playing.
     */
    public DropAll(AdventureGame model){
        this.model = model;
    }

    /**
     * The execute method for DropAll.
     * This method adds all the objects in the player's inventory into the current room the player is at.
     * Then it clears out the inventory.
     */
    @Override
    public void execute() {
        this.model.getPlayer().getCurrentRoom().objectsInRoom.addAll(this.model.getPlayer().inventory);
        this.model.getPlayer().inventory.clear();
    }
}
