# Skybot

Minecraft 1.15.2 mod compatible with the Forge API.

This mod is meant to automate ingame farming: Potatoes, carrots, sugarcane, beetroot, pumpkins... in Skyblock gamemodes.

### Farming
The bot's behavior is controlled by hierarchical finite state machines (HFSM).
![Alt Text](github_resources/farming_self_perspective_cut.gif)
![Alt Text](github_resources/farming_outside_view.gif)



### Pathfinding
Skybot has a simple 3D pathfinder based on the A* algorithm, even though it's currenly more of a proof of concept than a real feature. I'm working on a more practical and efficient version that will give bots the ability to operate in much more complex environments.
![Alt Text](github_resources/simple_pathfinding.gif)
![Alt Text](github_resources/not_so_simple_pathfinding.gif)



### Ingame options menu
Some aspects of how Skybot behaves can be changed via the ingame menu.
![](github_resources/skybot_settings.PNG)



### Config file
All preferences are saved in a file called skybot_config.txt in the .minecraft/config folder.


## How to install it

How to build and run it: From a console or terminal, go into the folder with the gradlew file in it. Then use "gradlew build" (or "./gradlew build") to build the mod. After that, go into build/libs. The build command from before will have generated a .jar file (by default called skybot-x.xx.x.jar).

To use the mod, install Forge 1.15.2 (the easiest way to do that is via TLauncher (https://tlauncher.org/en/). Open the .minecraft folder where Minecraft is installed, then move the jar file from before into .minecraft/mods and run the game.

### Note to self:
Use "gradlew genIntellijRuns" to get the client run config back.
