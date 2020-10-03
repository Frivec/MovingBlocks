# MovingBlocks
Plugin to move blocks in Minecraft 1.16.2

MovingBlocks by Frivec is licensed under CC BY-SA 4.0. To view a copy of this license, visit https://creativecommons.org/licenses/by-sa/4.0

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
