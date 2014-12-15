package net.robbytu.hanze.zuul.server;/* vim: set expandtab tabstop=4 shiftwidth=4 softtabstop=4: */

import net.robbytu.hanze.zuul.game.Player;
import net.robbytu.hanze.zuul.game.Room;

import java.io.*;
import java.net.Socket;
import java.util.Map;

/**
 * The ClientHandler handles new incoming player connections
 *
 * @author R. de Vries <r.devries@robbytu.net>
 * @version 1.0.0
 * @date 12-12-14
 * @copyright 2014 RobbytuProjects
 * @license MIT License
 */
public class ClientHandler implements Runnable {
    private Socket clientSocket = null;
    private String clientIdentifier;
    private DataInputStream inputStream;
    private PrintStream outputStream;
    private Player player;

    /**
     * Set up the initial client handler
     * @param socket The client socket
     * @throws IOException
     */
    public ClientHandler(Socket socket) throws IOException {
        // Set up instance variables
        this.clientSocket = socket;
        this.clientIdentifier = this.clientSocket.getInetAddress().getHostAddress();
        this.inputStream = new DataInputStream(this.clientSocket.getInputStream());
        this.outputStream = new PrintStream(this.clientSocket.getOutputStream());

        // Log that we have created a new client handler
        Logger.print(this.clientIdentifier, "A new client handler has been set up");
    }

    /**
     * Keeps the socket to the client alive and handles requests made by the net.robbytu.hanze.zuul.client
     */
    @Override
    public void run() {
        try {
            // First authenticate the player and protocol
            this.player = ClientAuthenticator.authenticatePlayer(this.inputStream, this.outputStream);
            this.clientIdentifier += ":" + this.player.getName();

            Logger.print(this.clientIdentifier, "The user has been authenticated as " + this.player.getName());

            this.sendInitialData();

            // Read and process commands as long as the client socket is active
            while(this.clientSocket.isConnected()) {
                String data = this.inputStream.readLine();
                String command[] = data.split(" ");

                Logger.print(this.clientIdentifier, " << " + data);

                if(command.length == 1 && command[0].equals("LIST")) listCommand();
                else if(command.length == 2 && command[0].equals("GO")) goCommand(command[1]);
                else if(command.length == 1 && command[0].equals("BACK")) backCommand();
                else if(command.length == 1 && command[0].equals("ONLINE")) onlineCommand();
                else this.writeToClient("ERR Command not found");
            }
        }
        catch(Exception e) {
            Logger.print(this.clientIdentifier, "An error occured: " + e.getMessage());
            this.writeToClient("ERR " + e.getMessage());
            this.closeSockets();
        }
    }

    /**
     * Sends initial data to the client
     */
    private void sendInitialData() {
        this.writeToClient("PROTO 1 User has authenticated");
        this.onlineCommand();
        this.writeToClient("ROOMDESC " + this.player.getCurrentRoom().getRoomDesc());
    }

    /**
     * List all players in the current room
     */
    private void listCommand() {
        // Fetch the current players in this room
        String playersInRoom = "";
        for(Player player : this.player.getCurrentRoom().getPlayersInRoom()) {
            playersInRoom += ((playersInRoom.length() == 0) ? "" : ",") + player.getName();
        }

        // Write the response to the client
        this.writeToClient("LIST " + playersInRoom);
    }

    /**
     * Go to another room
     * @param where Direction to go
     */
    private void goCommand(String where) {
        Room toJoin = this.player.getCurrentRoom().getExit(where);
        if(toJoin == null) {
            // We can't join this room: it does not exist
            this.writeToClient("FAIL GO");
        }
        else {
            // Join the room!
            this.player.setCurrentRoom(toJoin);
            this.writeToClient("ROOMDESC " + this.player.getCurrentRoom().getRoomDesc());
        }
    }

    /**
     * Go back to the previous room, if it has been set
     */
    private void backCommand() {
        this.writeToClient(this.player.goBack() ? "ROOMDESC " + this.player.getCurrentRoom().getRoomDesc() : "FAIL BACK");
    }

    /**
     * Send a list of online players to the net.robbytu.hanze.zuul.client
     */
    private void onlineCommand() {
        String playersOnline = "";
        for(String player : ClientAuthenticator.getAuthenticatedPlayerNames()) {
            playersOnline += ((playersOnline.length() == 0) ? "" : ",") + player;
        }

        this.writeToClient("ONLINE " + playersOnline);
    }

    /**
     * Write a message to the client
     * @param message Message to send
     */
    private void writeToClient(String message) {
        Logger.print(this.clientIdentifier, " >> " + message);
        this.player.writeToClient(message);
    }

    /**
     * Write a message to all connected clients
     * @param message Message to send
     */
    private void writeToAllClients(String message) {
        for(Map.Entry<String, Player> entry : ClientAuthenticator.authenticatedPlayers().entrySet()) {
            entry.getValue().writeToClient(message);
        }
    }

    /**
     * Closes all sockets
     */
    private void closeSockets() {
        try {
            // Close the socket if the net.robbytu.hanze.zuul.client is still connected
            if(this.clientSocket.isConnected()) {
                this.inputStream.close();
                this.outputStream.close();
                this.clientSocket.close();
            }

            // Unregister the player
            this.player.unregister();
        } catch (IOException ignored) {} finally {
            // Log it
            Logger.print(this.clientIdentifier, "The client connection has been closed.");
        }
    }
}
