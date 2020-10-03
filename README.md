# MovingBlocks
Plugin to move blocks in Minecraft 1.16.2

Shield: [![CC BY-SA 4.0][cc-by-sa-shield]][cc-by-sa]

This work is licensed under a
[Creative Commons Attribution-ShareAlike 4.0 International License][cc-by-sa].

[![CC BY-SA 4.0][cc-by-sa-image]][cc-by-sa]

[cc-by-sa]: http://creativecommons.org/licenses/by-sa/4.0/
[cc-by-sa-image]: https://licensebuttons.net/l/by-sa/4.0/88x31.png
[cc-by-sa-shield]: https://img.shields.io/badge/License-CC%20BY--SA%204.0-lightgrey.svg

__Description:__

This plugin allow you to move blocks by following a pattern.
It works on Minecraft servers 1.15.2 and 1.16.2 (PaperSpigot recommended for performances).

This plugin is in beta. Please do not use it if you found a bug.

__Commands:__

Main command: **/moveblock**:
- /moveblock create <name>: Create a circuit for the block. Needs you to look at the block you want (range: 5 blocks).
- /moveblock remove <name>: Delete an existing circuit
- /moveblock addpoint <name>: Add a point to the circuit you gave. The position added is your current position in the world.
- /moveblock removepoint <name> <index>: Remove an existing point by using it's index. 
- /moveblock setpoint <name> <index>: Replace an existing point in the circuit by using the index.
- /moveblock run <name>: Run an existing circuit.
  
If a circuit is already running, all the modifications will be canceled. Please wait for a circuit to be done before modify it.

__Dependences:__

- Depend from SpigotMC/PaperSpigot
- Depend on Google GSon (included in Spigot/PaperMC since 1.8.8). 
