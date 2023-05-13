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

1. Implemented the Google Cloud MySQL Database (https://github.com/sopra-fs23-group-37/server/issues/68)
2. Add filter to get games to only return open lobbys (https://github.com/sopra-fs23-group-37/server/issues/65)
3. Testing for everything + added a lot of tests
4. Created Game Table for the client side (https://github.com/sopra-fs23-group-37/client/issues/8)
5. Fixed tests failing (https://github.com/sopra-fs23-group-37/server/issues/77)
6. Fixed tests and bugs (https://github.com/sopra-fs23-group-37/server/issues/79)
7. Design fixed everywhere

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
3. Fix Guarding & Routing issue (https://github.com/sopra-fs23-group-37/client/issues/51)
4. Add header text to be variable ((https://github.com/sopra-fs23-group-37/client/issues/47)
5. Add token deletion when tab is closed (https://github.com/sopra-fs23-group-37/client/issues/43)

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


# Week 3 Contributions

## Beni

1. Closing tab should log out user & implement logout even if server returns error - (https://github.com/sopra-fs23-group-37/client/issues/62)
2. Welcome Text in header should display username - (https://github.com/sopra-fs23-group-37/client/issues/64)
3. Game Creation Page & Lobby Page Design & funktionalitäten anpassen - (https://github.com/sopra-fs23-group-37/client/issues/69)
4. Create Containers for Game Screen - (https://github.com/sopra-fs23-group-37/client/issues/76)
5. Implement Card Component with needed card features and game implementation example - (https://github.com/sopra-fs23-group-37/client/issues/6)
6. Enable Moves while playing - (https://github.com/sopra-fs23-group-37/client/issues/11)
7. Show opponent's hand - (https://github.com/sopra-fs23-group-37/client/issues/98)
8. Show Discarded Pile - (https://github.com/sopra-fs23-group-37/client/issues/108)

## Sinthu

1.	Created the Gamet-Table on Gamescreen (sopra-fs23-group-37/client#81)
2.	Implemented a new design for rulebook (sopra-fs23-group-37/client#106)
3.	Creating Leave-Game-Button on GameScreen (sopra-fs23-group-37/client#19)

## Carol
1. Refactored websocket both client and server side to be able to pass websocket connection flexibly between pages (https://github.com/sopra-fs23-group-37/client/issues/83, https://github.com/sopra-fs23-group-37/client/issues/40, https://github.com/sopra-fs23-group-37/server/issues/104)
2. Updated environment for server to ensure websocket connects successfully in production (https://github.com/sopra-fs23-group-37/client/issues/32, https://github.com/sopra-fs23-group-37/server/issues/100)
3. Handle disconnects and reconnects (https://github.com/sopra-fs23-group-37/client/issues/37 https://github.com/sopra-fs23-group-37/server/issues/108)
4. Make sure player turn is changed after move depending on cards still left (https://github.com/sopra-fs23-group-37/server/issues/114)
5. Allow surrender of game in server and client
6. Server-side tests

## Niels
1. Parsing the game state from server to local implementation (https://github.com/sopra-fs23-group-37/client/issues/55)
2. Display player hand and make cards selectable (https://github.com/sopra-fs23-group-37/client/issues/7)
3. Indicate which players turn it is (https://github.com/sopra-fs23-group-37/client/issues/9) and (https://github.com/sopra-fs23-group-37/client/issues/10)
4. Implemented the complete move logic and ui structure together with Beni (https://github.com/sopra-fs23-group-37/client/issues/11)
5. Minor ui updates all around

## Timon

1. Implemented the End Of Game screen (https://github.com/sopra-fs23-group-37/client/issues/16)
2. Implemented the End Of Round screen (https://github.com/sopra-fs23-group-37/client/issues/15)
3. Implemented the Game Point statistics (https://github.com/sopra-fs23-group-37/client/issues/88)
4. Adjusted the code so its fits to Carols solution of the new web-socket functionalities
5. Implemented the tests for the M3 assignment (https://github.com/sopra-fs23-group-37/server/issues/99)
6. Fixed the design of the game (https://github.com/sopra-fs23-group-37/client/issues/119)
7. Fixed the .scss styling

# Week 4 Contributions


## Timon

1. Implemented overlay after End Of Round screen, which pops up to wait for the other user to click enter (https://github.com/sopra-fs23-group-37/client/issues/126)
2. Implemented loading icon on game start (https://github.com/sopra-fs23-group-37/client/issues/125)
3. Implemented the overlay that pops up when the user leaves the game (https://github.com/sopra-fs23-group-37/client/issues/18)
4. Alert player when the user disconnected from the game (https://github.com/sopra-fs23-group-37/client/issues/38)
5. Fixed the .scss styling

## Niels

1. Improved UX for different screen sizes (https://github.com/sopra-fs23-group-37/client/issues/128)
2. Added UI in the gamescreen for hand, field and opponent, and adjusted for screen sizes (https://github.com/sopra-fs23-group-37/client/issues/131)
3. Minor UI updates all around
4. Bug fixes

## Beni

1. Implementing a check if user entered a game the user is not part of & handle it accordingly (Display the issue & relocate the user) - (https://github.com/sopra-fs23-group-37/client/issues/95)
2. Implementing the log & visibility of the opponent's last capture, so the user can strategize his own move - (https://github.com/sopra-fs23-group-37/client/issues/129)
3. Helping to add UI features

## Sinthu
1. Created Display Error when navigating to a lobby that the user is not a part of ( https://github.com/sopra-fs23-group-37/client/issues/96)
2. Created Display Error when a user joining a finished game (https://github.com/sopra-fs23-group-37/client/issues/99)
3. Beta Testing for Group38

## Carol
1. Added websocket methods to client and server to ensure the number of open games on the home screen is updated without reload (https://github.com/sopra-fs23-group-37/client/issues/139, https://github.com/sopra-fs23-group-37/client/issues/140, https://github.com/sopra-fs23-group-37/server/issues/137)
2. Handle invalid moves in the server and return an error message on an error channel that the client can handle (https://github.com/sopra-fs23-group-37/server/issues/87, https://github.com/sopra-fs23-group-37/client/issues/136)
3. Update server order of operations to ensure full deck is played before end of round and handle fast player moves (https://github.com/sopra-fs23-group-37/server/issues/126, https://github.com/sopra-fs23-group-37/server/issues/127)
4. Wrote tests for all of the above

# Week 5 Contributions

## Niels 
1. Implemented endpoint to join private game via code (https://github.com/sopra-fs23-group-37/server/issues/149)
2. Added backend functionalities for private games (https://github.com/sopra-fs23-group-37/server/issues/146)
3. Added functionalities to display statistics on home screen (https://github.com/sopra-fs23-group-37/client/issues/142)
4. Implemented tracking of user statistics (https://github.com/sopra-fs23-group-37/server/issues/150)
5. Added button to join private game (https://github.com/sopra-fs23-group-37/client/issues/158)
6. Added backend functionalities to treat ace as value of 1 (https://github.com/sopra-fs23-group-37/server/issues/162)
7. bugfixes

## Beni
1. method to check if move is valid and give the user a reasonable response for each case - (https://github.com/sopra-fs23-group-37/client/issues/148)
2. implement "dead jack" - jack gets put on table if no cards on table - (https://github.com/sopra-fs23-group-37/client/issues/150)
3. implement better UX-experience when selecting a card from table or hand & not have it moved to a different area - (https://github.com/sopra-fs23-group-37/client/issues/147)
4. implement an automatically execution of a player move instead of clicking on button once the selection is complete and valid otherwise give him feedback and make reselecting possible - (https://github.com/sopra-fs23-group-37/client/issues/149)
5. implement a way to determine if a card is already clicked and make the unselecting possible - (https://github.com/sopra-fs23-group-37/client/issues/162)
6. finish User Story: As a player, I want to have a great UX when making a move and have few clicks, so that playing is more fun - when the mentioned tasks & improvement of UI & stability - (https://github.com/sopra-fs23-group-37/client/issues/127)

## Carol
1. Updated Rulebooks rules to provide easier understanding (https://github.com/sopra-fs23-group-37/client/issues/146)
2. Added websocket subscription to statistics channel on Home page with method to update stats (https://github.com/sopra-fs23-group-37/client/issues/141)
3. Created method in websocket service to send statistics update, made sure method is triggered in ws controller for home and after every game update (https://github.com/sopra-fs23-group-37/server/issues/138)
4. Created error path for users trying to join their own game with useful message sent back to the user (https://github.com/sopra-fs23-group-37/server/issues/151)
5. Updated disconnect delay to ensure wait time for opponents is not excessive (https://github.com/sopra-fs23-group-37/server/issues/159)
6. Updated rules to better clarify that Ace is treated as 1 (https://github.com/sopra-fs23-group-37/client/issues/164)
7. Updated websocket service to return the last capture of the opppent with every roundinfo update (https://github.com/sopra-fs23-group-37/server/issues/138)
8. Tests for all of the above, bugfixes

## Sinthu
1.	Created a prompt when user log in for first time the user is directed tot he rulebook. (https://github.com/sopra-fs23-group-37/client/issues/154 
2.	Changed the homescreen, so that the user can navigate easily. (https://github.com/sopra-fs23-group-37/client/issues/163) 
3.	Created a button on GameScreen, so that the user can check the rulebook during the game. (https://github.com/sopra-fs23-group-37/client/issues/155) 
4.	Created a button to join private game (https://github.com/sopra-fs23-group-37/client/issues/158 
5.	Wrote Test-Suite to verify the functionality, performance.

## Timon
1. Implemented the avatar server functionalities (https://github.com/sopra-fs23-group-37/server/issues/169)
2. Displayed avatar in the header on home page and allow to update it when you  click on it (https://github.com/sopra-fs23-group-37/client/issues/168)
3. Implemented the avatars for the GameScreen (https://github.com/sopra-fs23-group-37/client/issues/174)
4. Implemented Firebase database for the avatar images (https://github.com/sopra-fs23-group-37/client/issues/169)
5. Write tests for the avatar server side (https://github.com/sopra-fs23-group-37/server/issues/172)

