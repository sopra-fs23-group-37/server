# Week 1 Contributions

## Carol

1. Created Websocket Controller for the Game
2. Added websocketjoin method to GameService to update player and game status when websocket is connected
3. Created Websocket Configuration and updated necessary files
4. Created CreateGame page in the client with create Game button that calls REST API
5. Created Lobby page in the client that connects to the websocket of a specific game by id and checks whether players are connected

## Niels

1. Implemented carddeck api call to create a deck
2. Implemented carddeck api call to draw cards
3. Implemented carddeck api call to shuffle the deck
4. Created the player class
5. Added unit tests for 1-4

## Sinthu

1. Implemented/edited login functionality
2. Implemented/edited registration functionality
3. Implemented new design for registration-page
4. Tested user-registration manually

## Timon

1. Implemented login functionality
2. Implemented logout functionality
3. Implemented registration functionality
4. Did tests for all of that and additional tests
5. Created ground functionalities for game and session creation and GET, POST and PUT DTOs and methods

## Beni

1. Fixed login functionality
2. Tested user login & logout functionalities
4. Implemented new design for general application & homescreen
5. Add feature to trigger buttons with Enter
6. Fixed logout functionalities


# Week 2 Contributions

## Carol

1. Created game join REST endpoint
2. Updated Home screen with the correct methods to create/join games via REST & websocket
3. Created game start websocket endpoint
4. Created RoundService, related entitites, and new round methods
5. Created method in round service to deal cards
6. Updated Server to consistently use Repositories and better split class responsibilities
7. Created method in roundservice to check whether players still have cards after move
8. Created method in roundservice to end round, new method in player to count points
9. Wrote test for all of the above


