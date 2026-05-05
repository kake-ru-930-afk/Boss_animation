# Boss Animation Mod

A Minecraft Forge mod for version 1.20.1 that adds an animated boss using GeckoLib.

## Features

- Animated boss entity with custom model
- Boss bar tracking
- Multiple animations (idle, walk, attack)
- Smooth animation transitions

## Building

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Gradle (included via gradlew)

### Steps

1. Clone the repository:
```bash
git clone https://github.com/kake-ru-930-afk/Boss_animation.git
cd Boss_animation
```

2. Build the project:
```bash
# Windows
gradlew build

# Linux/Mac
./gradlew build
```

3. The JAR file will be generated at:
```
build/libs/bossmods-1.0.0.jar
```

## Installation

1. Install Minecraft Forge 1.20.1
2. Place the JAR file in your `mods` folder:
   - Windows: `%appdata%/.minecraft/mods/`
   - Linux: `~/.minecraft/mods/`
   - Mac: `~/Library/Application Support/minecraft/mods/`
3. Launch Minecraft with Forge

## Usage

Summon the boss with:
```
/summon bossmods:animated_boss ~ ~ ~
```

## Dependencies

- Minecraft Forge 1.20.1
- GeckoLib 4.4.5

## License

MIT
