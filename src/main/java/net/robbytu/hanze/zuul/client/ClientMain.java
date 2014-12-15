package net.robbytu.hanze.zuul.client;/* vim: set expandtab tabstop=4 shiftwidth=4 softtabstop=4: */

/**
 * ClientMain
 *
 * @author R. de Vries <r.devries@robbytu.net>
 * @version 1.0.0
 * @date 12-12-14
 * @copyright 2014 RobbytuProjects
 * @license MIT License
 */
public class ClientMain {
    public static ServerConnection serverConnection;
    public static GameClient game;

    /**
     * Starts the client
     * @param args Commandline arguments
     */
    public static void main(String[] args) {
        if(args.length != 3) {
            System.out.println("Please specify the server and a username in the command line arguments.");
            return;
        }

        System.out.println("Connecting to " + args[1] + "...");
        System.out.println();

        game = new GameClient();
        serverConnection = new ServerConnection(args[1], args[2]);

        if(serverConnection.isConnected()) {
            System.out.println("Connected to the server!");
            System.out.println();
            game.play();
            return;
        }

        System.out.println("Could not connect to the specified server!");
    }
}
