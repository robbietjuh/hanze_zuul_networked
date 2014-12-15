package net.robbytu.hanze.zuul.server;/* vim: set expandtab tabstop=4 shiftwidth=4 softtabstop=4: */

/**
 * Logger
 *
 * @author R. de Vries <r.devries@robbytu.net>
 * @version 1.0.0
 * @date 12-12-14
 * @copyright 2014 RobbytuProjects
 * @license MIT License
 */
public class Logger {
    /**
     * Prints a message to the console
     * @param message Message to print
     */
    public static void print(String message) {
        System.out.println("[LOG] " + message);
    }

    /**
     * Prints a message to the console with a net.robbytu.hanze.zuul.client IP
     * @param clientIp The IP address of the net.robbytu.hanze.zuul.client
     * @param message Message to print
     */
    public static void print(String clientIp, String message) {
        System.out.println("[LOG] " + clientIp + ": " + message);
    }
}
