package net.robbytu.hanze.zuul.server;/* vim: set expandtab tabstop=4 shiftwidth=4 softtabstop=4: */

import net.robbytu.hanze.zuul.game.Game;

/**
 * ClientMain
 *
 * @author R. de Vries <r.devries@robbytu.net>
 * @version 1.0.0
 * @date 12-12-14
 * @copyright 2014 RobbytuProjects
 * @license MIT License
 */
public class ServerMain {
    public static Game sharedGame;

    /**
     * Starts the server
     */
    public static void main(String[] args) {
        // Create a new shared game instance
        sharedGame = new Game();

        // Start the server
        new Server().startServer();
    }
}
