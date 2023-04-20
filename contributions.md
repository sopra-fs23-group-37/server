# Week 1 Contributions

## Carol

1. Created Websocket Controller for the Game - (https://github.com/sopra-fs23-group-37/server/issues/57)
2. Added websocketjoin method to GameService to update player and game status when websocket is connected (https://github.com/sopra-fs23-group-37/server/issues/57)
3. Created Websocket Configuration and updated necessary files (https://github.com/sopra-fs23-group-37/server/issues/57)
4. Created CreateGame page in the client with create Game button that calls REST API (https://github.com/sopra-fs23-group-37/server/issues/4)
5. Created Lobby page in the client that connects to the websocket of a specific game by id and checks whether players are connected (https://github.com/sopra-fs23-group-37/server/issues/20)
6. Wrote tests for all of the above

## Niels

1. Implemented carddeck api call to create a deck (https://github.com/sopra-fs23-group-37/server/issues/36)
2. Implemented carddeck api call to draw cards
3. Implemented carddeck api call to shuffle the deck
4. Created the player class (https://github.com/sopra-fs23-group-37/server/issues/61)
5. Added unit tests for 1-4

## Sinthu

1. Implemented/edited login functionality (https://github.com/sopra-fs23-group-37/client/issues/1)
2. Implemented/edited registration functionality (https://github.com/sopra-fs23-group-37/client/issues/2)
3. Implemented new design for registration-page (https://github.com/sopra-fs23-group-37/client/issues/26)
4. Tested user-registration manually

## Timon

1. Implemented login functionality (https://github.com/sopra-fs23-group-37/server/issues/21)
2. Implemented logout functionality (https://github.com/sopra-fs23-group-37/server/issues/21)
3. Implemented registration functionality + password checks (https://github.com/sopra-fs23-group-37/server/issues/22)
4. Did tests for all of that and additional tests
5. Created ground functionalities for game and session creation and GET, POST and PUT DTOs and methods (https://github.com/sopra-fs23-group-37/server/issues/28)

## Beni

1. Fixed login functionalities (https://github.com/sopra-fs23-group-37/client/issues/27)
2. Tested user login & logout functionalities (https://github.com/sopra-fs23-group-37/client/issues/27)
4. Implemented new design for general application & homescreen (https://github.com/sopra-fs23-group-37/client/issues/3, https://github.com/sopra-fs23-group-37/client/issues/24)
5. Add feature to trigger buttons with Enter (https://github.com/sopra-fs23-group-37/client/issues/25)
6. Fixed logout functionalities & Guarding + Routing (https://github.com/sopra-fs23-group-37/client/issues/27)


# Week 2 Contributions

## Carol

1. Created game join REST endpoint (https://github.com/sopra-fs23-group-37/server/issues/29)
2. Updated Home screen with the correct methods to create/join games via REST & websocket 
3. Created game start websocket endpoint (https://github.com/sopra-fs23-group-37/server/issues/34)
4. Created RoundService, related entitites, and new round methods (https://github.com/sopra-fs23-group-37/server/issues/34)
5. Created method in round service to deal cards (https://github.com/sopra-fs23-group-37/server/issues/34)
6. Updated Server to consistently use Repositories and better split class responsibilities 
7. Created method in roundservice to check whether players still have cards after move (https://github.com/sopra-fs23-group-37/server/issues/43)
8. Created method in roundservice to end round, new method in player to count points (https://github.com/sopra-fs23-group-37/server/issues/46)
9. Created methods in gameservice to add points from round to game (https://github.com/sopra-fs23-group-37/server/issues/47) determine if the game is over with a winner or set up a new round instead(https://github.com/sopra-fs23-group-37/server/issues/49)
10. Changed the tab icon & text in the client (https://github.com/sopra-fs23-group-37/client/issues/44)
11. Wrote test for all of the above
12. Fixed Sonarqube issues

## Beni
1. Implement Login Page Design according to FIGMA (https://github.com/sopra-fs23-group-37/client/issues/1)
2. Implement and design the Game-Creation page according to FIGMA (https://github.com/sopra-fs23-group-37/client/issues/21)
3. Fix Guarding & Routing bug (https://github.com/sopra-fs23-group-37/client/issues/51)
4. Fix header text ((https://github.com/sopra-fs23-group-37/client/issues/47)
5. Fix token deletion when tab is closed (https://github.com/sopra-fs23-group-37/client/issues/43)

## Niels
1. Implemented game logic for move validation (https://github.com/sopra-fs23-group-37/server/issues/41)
2. Better option to determine starting player (https://github.com/sopra-fs23-group-37/server/issues/35)
3. Implemented endpoint for move (https://github.com/sopra-fs23-group-37/server/issues/40)
4. Update game and round after each move (https://github.com/sopra-fs23-group-37/server/issues/60)
5. Testing for everything
6. Updated ui in frontend according to figma
7. minor fixes everywhere

## Sinthu
1. Implemented/edited registration functionality (https://github.com/sopra-fs23-group-37/client/issues/2)
2. Created a design for rulebook and defined the rules for the game (https://github.com/sopra-fs23-group-37/client/issues/17)
3. Fixed Design Error in Rulebook (https://github.com/sopra-fs23-group-37/client/issues/49)

## Timon

1. Implemented the Google Cloud MySQL Database (https://github.com/sopra-fs23-group-37/server/issues/68)
2. Add filter to get games to only return open lobbys (https://github.com/sopra-fs23-group-37/server/issues/65)
3. Testing for everything + added a lot of tests
4. Created Game Table for the client side (https://github.com/sopra-fs23-group-37/client/issues/8)
5. Fixed tests failing (https://github.com/sopra-fs23-group-37/server/issues/77)
6. Fixed tests and bugs (https://github.com/sopra-fs23-group-37/server/issues/79)
7. Design fixed everywhere


