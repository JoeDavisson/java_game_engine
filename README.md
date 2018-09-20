# java_game_engine

A java game engine that does pixel-perfect collisions on rotated, scaled, and light-sourced sprites, and sound mixing in software.

There are three sample games: ShootSomeRocks (an Asteroids-type game), Simple and MahJongg.

## Building

Run `./gradlew fatJar`.

## Playing

To play a game, you need to use the `appletviewer` application which is part of the standard Java distribution.

To play ShootSomeRocks, for example, run the following commands:

```
cd games/ShootSomeRocks
appletviewer ShootSomeRocks.html
```

> The HTML files for each game could be opened in normal browsers, but most browsers no longer support Applets.

Alternatively, you can also run a game with Gradle by running `./gradlew play<Game>` where `<Game>` is one of the
existing games.

For example to play `Simple`, run `./gradlew playSimple`.

The game is built automatically if necessary.

## Clean generated files

To delete all compiled files and generated jars, run `./gradlew clean`.

## Screenshots

![Screenshot](/games/ShootSomeRocks/ShootSomeRocks.jpg "Screenshot")
![Screenshot](/screenshots/ShootSomeRocks1.jpg "Screenshot")
![Screenshot](/screenshots/ShootSomeRocks2.jpg "Screenshot")
![Screenshot](/screenshots/ShootSomeRocks3.jpg "Screenshot")
![Screenshot](/screenshots/ShootSomeRocks4.jpg "Screenshot")

![Screenshot](/games/MahJongg/MahJongg.jpg "Screenshot")
![Screenshot](/screenshots/MahJongg1.jpg "Screenshot")
![Screenshot](/screenshots/MahJongg2.jpg "Screenshot")
