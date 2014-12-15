package net.robbytu.hanze.zuul.client;/* vim: set expandtab tabstop=4 shiftwidth=4 softtabstop=4: */

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * ServerListener
 *
 * @author R. de Vries <r.devries@robbytu.net>
 * @version 1.0.0
 * @date 12-12-14
 * @copyright 2014 RobbytuProjects
 * @license MIT License
 */
public class ServerListener implements Runnable {
    private Socket clientSocket;
    private DataInputStream inputStream;

    /**
     * Constructor for the server listener
     * @param clientSocket The client socket
     * @param inputStream The input stream for the client socket
     */
    public ServerListener(Socket clientSocket, DataInputStream inputStream) {
        this.clientSocket = clientSocket;
        this.inputStream = inputStream;
    }

    /**
     * Listens for data from the server, parses it and then processes it
     */
    @Override
    public void run() {
        // Only loop if the connection is still open
        while(this.clientSocket.isConnected()) {
            try {
                // Receive the data and parse it
                String receivedData = this.inputStream.readLine();
                String[] parsedData = receivedData.split(" ");

                if(parsedData[0].equals("PROTO")) ClientMain.game.printWelcome();
                else if(parsedData[0].equals("ONLINE")) ClientMain.game.printOnlineUsers(parsedData[1]);
                else if(parsedData[0].equals("LIST")) ClientMain.game.printListUsers(parsedData[1]);
                else if(parsedData[0].equals("ROOMDESC")) {
                    parsedData = receivedData.split(" ", 3);
                    ClientMain.game.printRoomDescription(parsedData[1], parsedData[2]);
                }
                else if(parsedData[0].equals("FAIL")) ClientMain.game.printErrorMessageForComponent(parsedData[1]);
                else {
                    System.out.println();
                    System.out.println(" ---- ");
                    System.out.println("Unknown command from server: " + parsedData[0]);
                    System.out.println("Is this client up to date?");
                    System.out.println(" ---- ");
                    System.out.println();
                }

            } catch (IOException ignored) {}
        }
    }
}
