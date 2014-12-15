package net.robbytu.hanze.zuul.server;/* vim: set expandtab tabstop=4 shiftwidth=4 softtabstop=4: */


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server
 *
 * @author R. de Vries <r.devries@robbytu.net>
 * @version 1.0.0
 * @date 12-12-14
 * @copyright 2014 RobbytuProjects
 * @license MIT License
 */
public class Server {
    protected int serverPort = 8123;
    protected ServerSocket serverSocket = null;
    protected boolean stillRunning = true;
    protected Thread runningThread = null;

    /**
     * Starts the server
     */
    public void startServer() {
        // Try to create a new server socket that listens on the specified server port
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
            Logger.print("The server is now listening on port " + this.serverPort + ".");
        }
        catch (IOException e) {
            Logger.print("Could not set up a socket for the Zuul server. " +
                         "Check that the server can use port " + this.serverPort + ".");
        }

        // Loop forever to keep the socket open and accept all
        // incoming connections. Then hand them over to a new net.robbytu.hanze.zuul.client handler.
        while(this.serverSocket.isBound()) {
            try {
                Socket clientSocket = this.serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
