package net.robbytu.hanze.zuul.server;/* vim: set expandtab tabstop=4 shiftwidth=4 softtabstop=4: */

import net.robbytu.hanze.zuul.game.Player;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Set;

/**
 * ClientAuthenticator
 *
 * @author R. de Vries <r.devries@robbytu.net>
 * @version 1.0.0
 * @date 12-12-14
 * @copyright 2014 RobbytuProjects
 * @license MIT License
 */
public class ClientAuthenticator {
    private static HashMap<String, Player> authenticatedClients = new HashMap<String, Player>();

    /**
     * Try to authenticate a player
     * @param inputStream The input stream to the net.robbytu.hanze.zuul.client
     * @param outputStream The output stream to the net.robbytu.hanze.zuul.client
     * @return The name of the player
     * @throws Exception
     */
    public static Player authenticatePlayer(DataInputStream inputStream, PrintStream outputStream) throws Exception {
        // Wait for the initial handshake
        String[] handshake = inputStream.readLine().split(" ");

        // Digest the handshake
        if(handshake.length != 3)
            throw new Exception("Invalid handshake");
        else if(!handshake[0].equals("PROTO") && !handshake[1].equals("1"))
            throw new Exception("The requested protocol is not understood");
        else if(ClientAuthenticator.isAuthenticated(handshake[2]))
            throw new Exception("The playername is already in use");
        else
            return ClientAuthenticator.createAuthenticatedPlayer(handshake[2], outputStream);
    }

    /**
     * Authenticate a new player
     * @param playerName The name of the player
     * @return The Player instance
     */
    private static Player createAuthenticatedPlayer(String playerName, PrintStream outputStream) {
        Player player = new Player(playerName, outputStream);
        ClientAuthenticator.authenticatedClients.put(playerName, player);

        return player;
    }

    /**
     * Check wether a player is authenticated or not
     * @param playerName The name of the player
     * @return True or false, based on the fact wether the player is authenticated or not
     */
    public static boolean isAuthenticated(String playerName) {
        return ClientAuthenticator.authenticatedClients.containsKey(playerName);
    }

    /**
     * Remove a player from the authenticated players list
     * @param playerName The name of the player
     */
    public static void removeAuthenticatedPlayer(String playerName) {
        ClientAuthenticator.authenticatedClients.remove(playerName);
    }

    /**
     * Get the Player object
     * @param playerName Player name
     * @return Player
     */
    public static Player getPlayer(String playerName) {
        return ClientAuthenticator.authenticatedClients.get(playerName);
    }

    /**
     * Get the authenticated players and their names
     * @return Authenticated players
     */
    public static Set<String> getAuthenticatedPlayerNames() {
        return ClientAuthenticator.authenticatedClients.keySet();
    }

    public static HashMap<String, Player> authenticatedPlayers() {
        return ClientAuthenticator.authenticatedClients;
    }
}
