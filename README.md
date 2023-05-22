# SoPra FS23 - Group 37 2-and-10

## Introduction

2-and-10 is an interactive, dynamic, online card game. Central to the 2-and-10 experience are the real-time, thrilling game sessions.

Whether you’re playing with friends or challenging players from across the globe, 2-and-10 ensures a seamless and exciting gaming experience.

What truly sets 2-and-10 apart is its unique public and private game sessions. Private sessions allow you to enjoy intense matches with your circle of friends.

Public sessions, on the other hand, open up to all registered users around the world. 2-and-10 is more than just a card game, it's a global community and a test of strategy.

## Technologies
- HTTP/Rest
- gradle, spring, java and mysql google cloud database
- github, sonarqube and google cloud

## High-level components
- Websocket: Socket.java uses the springframework.web.socket library to transfer data easily and in real-time for the users, round and game overall.
- Database: Application.java uses the jpa interface to store all kinds of information in a remote google cloud mySQL server.

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


Make Sure ```Java 15``` is installed on your system <br/>
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

## Authors and acknowledgment

- [Benjamin Bajralija](https://github.com/bbajrari)
- [Carol Ernst](https://github.com/carolernst-uzh)
- [Niels Zweifel](https://github.com/itsniezwe)
- [Sinthuyan Sivayogarajah](https://github.com/Sinthuyan97)
- [Timon Fopp](https://github.com/trofej)

## License
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
