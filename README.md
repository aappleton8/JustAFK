# JustAFK
## Description 
JustAFK is a Minecraft server Spigot plugin originally by alexbennett, available [here](https://github.com/alexbennett/JustAFK "Original JustAFK Repository"). 

JustAFK brings is a simple, yet powerful AFK (away from keyboard) plugin for Minecraft Spigot servers.

## Installation
To install JustAFK, simply drop the jar file into your server's plugin directory and restart it. A configuration file will be automatically created which you can edit to fine-tune your own installation.

## Version
This plugin is currently being reworked and is considered to be in its 'alpha' stage of its 'version 2'. 

## Licence 
This plugin, both the original version by alexbennet and this reworked version, is released under a MIT licence, available in the [LICENsE.txt](LICENSE.txt "LICENSE.txt") file. 

## Features
- Optional, automatic kicking of AFK players
- Outwits methods used to circumvent automatic AFK
- Configurable AFK time limit
- Configurable kick message
- Players are automatically hidden from others when going AFK
- Automatic detection of players returning from AFK *(e.g. when moving or chatting)*
- Set a status when going AFK
- Fully customizable messages via the generated `localization.yml` file

## Commands
- */afk*: Sets yourself to away, making your player invisible and broadcasting a message to the server.
- */afk [&lt;reason&gt;]*: Sets yourself to away as above, but also includes a reason.
- */justafk*: Get the plugin version. 
- */whosafk*: Shows you a list of who is currently set to away on the server.
- */setafk &lt;player&gt;*: Sets the specified player to away.
- */setafk &lt;player&gt; [&lt;reason&gt;]*: Sets the specified player to away, but also includes a reason.
- */afkhelp*: Show the help page. 
- */isafk &lt;player&gt;*: See if a player is AFK and get the AFK reason. 

## Permissions
- *justafk.basic*: The basic permission node which allows for full use of the plugin.
- *justafk.immune*: Makes the player immune to auto-afk and auto-kicking.
- *justafk.admin*: Gives the player access to the admin commands (seen above) included in the plugin.

## Configuration
This plugin has two configuration files. 'config.yml' configures various plugin parameters and `localisation.yml` configures the plugin messages. 

The configuration file has various options. 
```
tagmessages: true # If enabled, all AFK messages will be tagged with "[JustAFK]"
autokick: true # If enabled, players will be kicked automatically if inactive
kicktime: 300 # The amount of time in seconds that a player can be AFK before being kicked
kickreason: Reason # The reason to give when kicking an AFK player.
movementcheckfreq: 15 # The amount of time in seconds to check player movement for inactivity.
````

