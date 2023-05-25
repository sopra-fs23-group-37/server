# Project 2-and-10 (SoPra FS23 - Group 37)

## Introduction

2-and-10 is a captivating and engaging card game that originates from Balkan region. The game showcases an appealing blend of strategy, skill, and a dash of luck, providing an enjoyable experience to both beginner and experienced players. The objective of the game is straightforward - to collect cards from the table that sum up to the value of the card in your hand, aiming to capture as many points as possible.

One of the main appeals of 2-and-10 is the need for strategic thinking and calculation. The game not only requires you to maximize your points but also strategize on how to minimize your opponent's potential score. As the game progresses, the ability to anticipate opponents' moves becomes increasingly important, adding an additional layer of depth and complexity to the game.

## Technologies
- HTTP/Rest
- gradle, spring, java and mysql google cloud database
- github, sonarqube and google cloud

## High-level components
- Websocket: This project uses the springframework.web.socket library to transfer data easily and in real-time for the users, round and game overall. The WSGameController class manages all incoming requests. The WebsocketService manages all outgoing messages over the websocket, with methods available to send different DTOs to different subscribers (e.g. WSGameStatusDTO to all clients that subscribed to a specific game).
- The GameService and RoundService classes manage the bulk of the game logic, from setting up to finishing a game. 
- External Carddeck API: In order not to re-invent the wheel for a well-known use case (requiring a card deck with methods to shuffle, draw, etc.), this project leverages https://deckofcardsapi.com/. The full documentation can be found on the site. Within the project, the CardDeckService is responsible for managing all requests to the API and returning data in the format expected by the rest of the application.
- Database: Application.java uses the jpa interface to store data defined in the entity classes in a remote google cloud mySQL server.

## Launch & Deployment
### First get to know Spring Boot
Before you set up the environment, get to know Spring Boot and what REST is
- [Spring-Boot documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/index.html)
- [Spring Guides](http://spring.io/guides)
    - [Building a RESTful Web Service](http://spring.io/guides/gs/rest-service/)
    - [Building REST services with Spring](http://spring.io/guides/tutorials/bookmarks/)

### Setup with your IDE of choice
Download your IDE of choice:
- [Eclipse](http://www.eclipse.org/downloads/)
- [IntelliJ](https://www.jetbrains.com/idea/download/)
- [Visual Studio Code](https://code.visualstudio.com/)
- Or any other IDE

Make Sure ```Java 17``` is installed on your system <br/>
-> **For Windows Users:** please make sure your ```JAVA_HOME``` environment variable is set to the correct version of Java

1. File -> Open... -> server
2. Accept to import the project as a ``gradle project`` <br/><br/>
   Then build the project in your IDE
3. Right click the ```build.gradle``` file and choose ```Run Build```

### Setup for VS Code
The following extensions will help you to run it more easily:
- `pivotal.vscode-spring-boot`
- `vscjava.vscode-spring-initializr`
- `vscjava.vscode-spring-boot-dashboard`
- `vscjava.vscode-java-pack`
- `richardwillis.vscode-gradle` <br/><br/>
  **Note for VS Code Users:** You'll need to build the project first with Gradle, just click on the `build` command in the _Gradle Tasks_ extension. Then check the _Spring Boot Dashboard_ extension if it already shows `soprafs22` and hit the play button to start the server. If it doesn't show up, restart VS Code and check again.

### Building with Gradle
You can use the local Gradle Wrapper to build the application.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

You can get more Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/) if you want.

### Build the application
Run in the server file in your terminal
```
./gradlew build
```

### Run the local deployment
```
./gradlew bootRun
```
### Run the tests

```
./gradlew test
```
### Database
You have to take no arrangements for the database. The database will be running automatically.

## Roadmap
1. In the future, you could add a speed mode, where the users had limited time to play a move and could set that before the game start.
2. In addition, it would be nice to have a spectate mode, where other player could watch the card game.
3. It would also be interesting if users could also use a chat function in the game.
4. Giving users the possibility to play against an AI could be a good extension, so that they can play even if nobody else is available.

## Authors and acknowledgment

- [Benjamin Bajralija](https://github.com/bbajrari)
- [Carol Ernst](https://github.com/carolernst-uzh)
- [Niels Zweifel](https://github.com/itsniezwe)
- [Sinthuyan Sivayogarajah](https://github.com/Sinthuyan97)
- [Timon Fopp](https://github.com/trofej)

## License
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
