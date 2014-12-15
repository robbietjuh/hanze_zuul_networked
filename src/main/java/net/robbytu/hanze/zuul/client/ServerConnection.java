package net.robbytu.hanze.zuul.client;/* vim: set expandtab tabstop=4 shiftwidth=4 softtabstop=4: */

import java.io.*;
import java.net.Socket;

/**
 * ServerConnection
 *
 * @author R. de Vries <r.devries@robbytu.net>
 * @version 1.0.0
 * @date 12-12-14
 * @copyright 2014 RobbytuProjects
 * @license MIT License
 */
public class ServerConnection {
    private Socket clientSocket;

    private DataInputStream inputStream;
    private PrintStream outputStream;

    private final String protocolVersion = "1";

    /**
     * Constructs a new server connection and connects to the server
     * @param host Hostname or IP address of the server
     * @param playername Player name
     */
    public ServerConnection(String host, String playername) {
        try {
            // Connect to the server on port 8123
            this.clientSocket = new Socket(host, 8123);

            this.inputStream = new DataInputStream(this.clientSocket.getInputStream());
            this.outputStream = new PrintStream(this.clientSocket.getOutputStream());

            // Set up the listener
            new Thread(new ServerListener(this.clientSocket, this.inputStream)).start();

            // Authenticate the player
            this.sendCommand("PROTO " + this.protocolVersion + " " + playername);
        } catch (Exception ignored) {
            ClientMain.game.serverLostConnectionEvent();
        }
    }

    /**
     * Send a command to the server
     * @param command Commandline
     */
    public void sendCommand(String command) {
        this.outputStream.println(command);
    }

    /**
     * Check wether the client is connected to the server
     * @return Wether the client is connected to the server
     */
    public boolean isConnected() {
        return this.clientSocket.isConnected();
    }
}
