# Sudoku Game with GUI written in Java


<img width="309" alt="Screenshot 2021-12-15 at 5 18 40 AM" src="https://user-images.githubusercontent.com/26367904/146637830-5b7a08f3-f340-4ce2-9d4f-5aee9981b71b.png">


## Description

This application includes a 9x9 Graphical User Interface based sudoku game which can be connected to the Game server. 
Game Server acts as a link between the client and database to save the state of an ongoing game and reload it at any time to continue from where it was left.

<img width="318" alt="Screenshot 2021-12-18 at 5 50 57 AM" src="https://user-images.githubusercontent.com/26367904/146638452-953fc35f-6784-47e0-8039-f052b26148b4.png">


## Pre-requisites:

1. The ` sqlite-jdbc.jar ` [file](./sqlite-jdbc-3.30.1.jar) to connect to the database
    In case of Eclipse - it imported as an external JAR in the library.

    > **Project -> properties -> Java build path -> libraries -> Add External JARs**

2. Database file having the table - **'Players'** with the following schema:
    
    ![image](https://user-images.githubusercontent.com/26367904/146637914-e03757c2-9cfc-4908-b6ad-a02aa730a3e0.png)

    The project already has a file [Database file](./javabook.db) - `javabook.db` for this purpose, with the table present.


## Application Components:

Game GUI - also acts as client, Game logic, Game Server, Database.


## Application Features:

1. Options to connect and disconnect to the server - Just to show the client-server model using multi-threaded server and client instances.

2. Players can also save and load their game states – an extension of the client-server model by connecting the server to database to save and load the states. There are different options to save and load the games, they also make/terminate a connection to server directly.


## How to run the Game:

1. Once you are done with the pre-requisites section, run the file [Server.java](./src/sudoku/Server.java) within the "src" directory. This starts the Game server.

2. Within the "src" directory run the [App.java](./src/sudoku/App.java) file as a Java application, to start the GUI.

3. Play The GAME:

- Use the **'Start Game’** button to start a game, this will give you a default game.
- As you keep filling the blank entries, you can also check your progress by using the **‘Check’** button at any time – to verify your entries.
- **‘Clear Board'** button lets you start from scratch or clear the current game.

4. Tip – if you want to start a random game instead of a saved game, just input one number in any one of the **bordered** entries for numbers - ***Not the ones that the player has to fill.***
Once you input one number -> Hit the **‘Start Game’** button for a different game.


## Future Scope:

The game uses a back tracking algorithm to check if a solution is right and solve it. A bit of enhancement in the algorithm and a GUI component with an option to solve the game using an AI agent. So, the values will be filled fully once the ‘Agent solve’ button is hit.
Currently, short on time. Will do later.


## Sources for references and code help:

1. www.github.com/coderodde 
2. www.stackexchange.com
3. www.stackoverflow.com
