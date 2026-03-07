# WoW Tank Sim

A desktop application for simulating World of Warcraft TBC Feral Druid tank stats and analyzing crit immunity. Import your character from Classic WoW Armory or manually select items per slot.

## Prerequisites

- JDK 17 or later

## Build

```sh
./gradlew build
```

## Run

```sh
./gradlew run
```

## Package as native distribution

```sh
./gradlew package
```

This creates a platform-specific distributable under `build/compose/binaries/`.

## Tech stack

- Kotlin 1.9 / Compose Multiplatform (Desktop) / Material 3
- Ktor (HTTP client)
- Gradle 8.5
