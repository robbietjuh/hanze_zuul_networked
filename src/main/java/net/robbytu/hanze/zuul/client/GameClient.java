package net.robbytu.hanze.zuul.client;/* vim: set expandtab tabstop=4 shiftwidth=4 softtabstop=4: */
/**
 *  This class is the main class of the "World of Zuul" application.
 *  "World of Zuul" is a very simple, text based adventure game.  Users
 *  can walk around some scenery. That's all. It should really be extended
 *  to make it more interesting!
 *
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 *
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 *
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.08
 */

public class GameClient
{
    private Parser parser;
    private boolean finished = false;

    /**
     * Create the game and initialise its internal map.
     */
    public GameClient()
    {
        parser = new Parser();
    }

    /**
     *  ClientMain play routine.  Loops until end of play.
     */
    public void play()
    {
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }

        System.out.println("Thank you for playing. Good bye.");
    }

    /**
     * Handles lost connections
     */
    public void serverLostConnectionEvent() {
        System.out.println();
        System.out.println("Lost the connection to the server.");
        this.finished = true;
    }

    /**
     * Print out the opening message for the player.
     */
    public void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul: Networked version!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command)
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) printHelp();
        else if (commandWord.equals("go")) goRoom(command);
        else if(commandWord.equals("back")) goBack();
        else if(commandWord.equals("list")) listUsers();
        else if(commandWord.equals("online")) getOnlineUsers();
        else if (commandWord.equals("quit")) wantToQuit = quit(command);

        // else command not recognised.
        return wantToQuit;
    }

    /**
     * List users in a room
     */
    private void listUsers() {
        ClientMain.serverConnection.sendCommand("LIST");
    }

    /**
     * List online users
     */
    private void getOnlineUsers() {
        ClientMain.serverConnection.sendCommand("ONLINE");
    }

    /**
     * Prints online users
     * @param users Users that are online
     */
    public void printOnlineUsers(String users) {
        System.out.println();
        System.out.println("The following users are logged in at this moment:");
        this.printUserList(users);
        System.out.println();
    }

    /**
     * Prints the list of users in a room
     * @param users Users in that room
     */
    public void printListUsers(String users) {
        System.out.println();
        System.out.println("The following users are in this room:");
        this.printUserList(users);
        System.out.println();
    }

    /**
     * Parses a userlist and prints it to stdout
     * @param users A list of users, comma seperated
     */
    private void printUserList(String users) {
        System.out.println(users.replace(",", ", "));
    }

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the
     * command words.
     */
    private void printHelp()
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /**
     * Requests the server to enter the room
     */
    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();
        ClientMain.serverConnection.sendCommand("GO " + direction);
    }

    /**
     * Requests the server to go back one room
     */
    private void goBack() {
        ClientMain.serverConnection.sendCommand("BACK");
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * Prints the description of a room
     * @param description Roomdescription
     * @param exits Exits for this room
     */
    public void printRoomDescription(String exits, String description) {
        System.out.println("You are " + description + ".\n");

        String exitString = "Exits:";
        for(String exit : exits.split(",")) {
            exitString += " " + exit;
        }

        System.out.println(exitString);
    }

    /**
     * Print an error message for the given component
     * @param component The component that failed
     */
    public void printErrorMessageForComponent(String component) {
        if(component.equals("BACK")) System.out.println("You cannot go back any further!");
        else if(component.equals("GO")) System.out.println("That's not an option :-(");
        else System.out.println("Command failed: " + component);
    }
}
