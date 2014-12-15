package net.robbytu.hanze.zuul.game;/* vim: set expandtab tabstop=4 shiftwidth=4 softtabstop=4: */

import net.robbytu.hanze.zuul.server.ClientAuthenticator;
import net.robbytu.hanze.zuul.server.ServerMain;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Stack;

/**
 * Player
 *
 * @author R. de Vries <r.devries@robbytu.net>
 * @version 1.0.0
 * @date 12-12-14
 * @copyright 2014 RobbytuProjects
 * @license MIT License
 */
public class Player {
    private Room currentRoom;
    private Stack<Room> history;
    private String name;
    private PrintStream outputStream;

    /**
     * Constructs a new player, saving player name and output streams
     * into memory.
     * @param playerName The name of the player
     * @param outputStream The output stream used to communicate with the client
     */
    public Player(String playerName, PrintStream outputStream) {
        // Set up the player's name and room that it's in
        this.name = playerName;
        this.setCurrentRoom(ServerMain.sharedGame.defaultRoom);
        this.history = new Stack<Room>();
        this.outputStream = outputStream;
    }

    /**
     * Returns the name of the player
     * @return The name of the player
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the room this player is currently in
     * @return The room this player is currently in
     */
    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    /**
     * Sets the room this player is currently in. This function also
     * adds the room to switch to, into the history of rooms this user
     * has been in. If the user is already in a room, the player
     * will leaves that room.
     * @param room The room to join
     */
    public void setCurrentRoom(Room room) {
        if(this.currentRoom != null) {
            this.history.add(this.currentRoom);
            this.currentRoom.leaveRoom(this);
        }
        this.currentRoom = room;
        this.currentRoom.joinRoom(this);
    }

    /**
     * Checks the history of rooms this player has been in and
     * goes back 1 room if possible. Returns false if there is no
     * room in history.
     * @return Returns a boolean, indicating wether the user was switched back
     */
    public boolean goBack() {
        if(this.history.empty()) return false;

        this.currentRoom.leaveRoom(this);
        this.currentRoom = this.history.pop();
        this.currentRoom.joinRoom(this);

        return true;
    }

    /**
     * Unregister the player from memory
     */
    public void unregister() {
        this.getCurrentRoom().leaveRoom(this);
        ClientAuthenticator.removeAuthenticatedPlayer(this.getName());
    }

    /**
     * Write a message to the client
     * @param message The message to send
     */
    public void writeToClient(String message) {
        this.outputStream.println(message);
    }
}
