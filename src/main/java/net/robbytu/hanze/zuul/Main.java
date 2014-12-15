package net.robbytu.hanze.zuul;/* vim: set expandtab tabstop=4 shiftwidth=4 softtabstop=4: */

import net.robbytu.hanze.zuul.client.ClientMain;
import net.robbytu.hanze.zuul.server.ServerMain;

/**
 * ClientMain
 *
 * @author R. de Vries <r.devries@robbytu.net>
 * @version 1.0.0
 * @date 12-12-14
 * @copyright 2014 RobbytuProjects
 * @license MIT License
 */
public class Main {
    /**
     * Bootstrapper
     */
    public static void main(String[] args) {
        System.out.println("World of Zuul: Networked version");

        if(args.length == 0) {
            System.out.println("Error: Please specify wether to start the server or client.");
            return;
        }

        if(args[0].equals("client")) ClientMain.main(args);
        else if(args[0].equals("server")) ServerMain.main(args);
        else System.out.println("I can either be a client or a server, not a " + args[0]);
    }
}
