# zuul-networked

World of Zuul is a new, incredibly boring adventure game. We have used it at the Hanzehogeschool Groningen as an example Java project. To make the project just a tad bit more challenging, a new project named `zuul-networked` came to life.

The purpose of this project is to fork the World of Zuul project and redesign it to support multiplayer environments over TCP. This version of the World of Zuul offers a server and client instance to make multiplayer 'gaming' possible. Just start the server and connect your clients!

At this moment this fork offers the following features:

  - A simple server and client for multiplayer gaming
  - The default rooms shipped with the original World of Zuul project
  - The default commands to request `help`, `go` to another room or `exit` the game
  - A new command to go `back` to the previous room you were in
  - A new command to `list` the players in the current room
  - A new command to see the currently `online` players

### Usage

First compile the source code into a jar. Then, from the command line, start the server:
```sh
$ java -jar zuul-networked.jar server
```

At this point, the server will start listening for new incoming connections on port 8123. Now connect some clients:
```sh
$ java -jar zuul-networked.jar client 127.0.0.1 robbietjuh
```

In this example, the client will try to connect to your local host with the username `robbietjuh`. You should of course change the IP address and username in this example to your likings.

### Contribution

All pull requests are very much appreciated :-) Challenge your /dev/brain and help us make this project less boring by forking it! I would love to see other's contributions and am very curious as to what you can come up with to make this project more interesting.

**Free Software, Hell Yeah!**