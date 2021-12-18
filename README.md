# Sudoku Game with GUI written in Java

## Description

This application includes a 9x9 Graphical User Interface based sudoku game which can be connected to the Game server. 
Game Server acts as a link between the client and database to save the state of an ongoing game and reload it at any time to continue from where it was left.

## Pre-requisites:

1. The ` sqlite-jdbc.jar ` file to connect to the database
    In case of Eclipse - it imported as an external JAR in the library.
        Project -> properties -> Java build path -> libraries -> Add External JARs

2. Database file having the table - 'Players' with the following schema:

    The project already has a file [Database file](./javabook.db) - `javabook.db` for this purpose.


## Application Components:

Game GUI - also acts as client, Game logic, Game Server, Database.


## Application Features:

1. Options to connect and disconnect to the server - Just to show the client-server model using multi-threaded server and client instances.

2. Players can also save and load their game states – an extension of the client-server model by connecting the server to database to save and load the states. There are different options to save and load the games, they also make/terminate a connection to server directly.


## How to run the Game:

1. Once you are done with the pre-requisites section, run the file `server.java` within the [src directory](./src). This starts the Game server.

2. Within the [src directory](./src) run the `App.java` file as a Java application, to start the GUI.

3. Play The GAME:

*** Use the ‘Start Game’ button to start a game, this will give you a default game.
*** As you keep filling the blank entries, you can also check your progress by using the ‘Check’ button at any time – to verify your entries.
*** ‘Clear Board’ lets you start from scratch.

4. Tip – if you want to start a random game instead of a saved game, just input one number in any one of the ‘bordered’ entries for numbers - Not the ones that the player has to fill.
Once you input one number -> Hit the ‘start game’ button for a different game.


## Future Scope:

The game uses a back tracking algorithm to check if a solution is right and solve it. A bit of enhancement in the algorithm and a GUI component with an option to solve the game using an AI agent. So, the values will be filled fully once the ‘Agent solve’ button is hit.
Currently, short on time. Will do later.