LibGDX Connect 4 by Kevin Chen
===============================
This is a simple connect 4 game with some miscellaneous functionalities created with LibGDX
The app consists of 2 screens:

**MainScreen:**

Features 4 buttons:
- SFX: Plays a sound
- API: Displays some weather in San Francisco
- Game: Launches Player vs Player Connect 4 Game
- AI: Launches Player vs AI Connect 4 game
		
**GameScreen:**

The gameplay screen for Connect 4.

There is a 6x7 game board. Game board size is easily altered.

Game ends when either player reaches 4 consecutive pieces, or all the board space is used.

When the game ends it presents players with the option to reset the board, or exit to the main screen.
	
	
**Implementation notes:**

*Basic app logic under the 'project' package:*

- Assets.java: AssetManager to load and manage the art assets for the game.
- ProjectApplication.java: the base application
- WeatherAPI.java: handles the API call to OpenWeatherMap and parses data from the JSON using json-simple-1.1.1

*Connect 4 logic under the 'project.connect4' package:*

- AI.java: Defines the AI player. The AI needs some work...
- Cell.java: Defines a game board Cell. Keeps track of which player is in possession of each grid, and the textures to be rendered for each coordinate.
- GameLogic.java: Holds much of the game state. Checks for win conditions, move validity, and assists with AI.	

*Screens under the 'project.screens' package:*

- MainScreen.java: Defines the menu screen with the 4 buttons.
- GameScreen.java: Defines the game screen.
