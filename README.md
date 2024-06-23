# README

## SimulationProject

**Project:** SimulationProject

**Description:**
This project is a simulation where characters (humans) and cows move around a terrain. The simulation is controlled by a timer that updates every second, simulating the passage of time. Characters hunt cows and collect carrots to multiply.

**How to run:**
mvn clean javafx:run

**Features:**
- A cycle with start and end conditions
- Characters and cows moving around a terrain
- Cows can be hunted by two characters
- Carrots can be collected by one character

**Technical Details:**
- The project is written in Java using the JavaFX library for GUI and animation.
- It uses a custom `Terrain` class to represent the game world, containing characters, cows, and carrots.
- The `Character` class represents individual humans with unique movement and behavior.
- The `Cow` class represents individual cows with unique movement and behavior.

**How it Works:**
1. The simulation starts upon launching the program; characters and cows move around the terrain.
2. The timer updates every second, simulating the passage of time.
3. When the cycle ends, the characters return to their houses, and the cows and carrots are removed from the terrain.
4. A new day-night cycle begins, and the process repeats.

**Future Development:**
- Improve the movement and behavior of the characters and cows.
- Enhance the appearance of the houses.
- Add a cycle counter.
- Add the ability to share or not share the rewards from cow hunting.
- Increase character variety.

**Known Issues:**
- The characters and cows may move beyond the borders of the terrain.
- Characters may follow cows outside the borders.

**Authors:**
Pierre Bayle

**Acknowledgments:**
Based on the concept from the CODE BH YouTube video: [Simulation Project](https://www.youtube.com/watch?v=qVOjXQUzOJw)
