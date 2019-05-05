# JustAFK
## Description 
JustAFK is a Minecraft server Spigot plugin originally by alexbennett, available [here](https://github.com/alexbennett/JustAFK "Original JustAFK Repository"). 

JustAFK brings is a simple, yet powerful AFK (away from keyboard) plugin for Minecraft Spigot servers.

## Installation
To install JustAFK, simply drop the jar file into the server's plugin directory and restart it. A configuration file will be automatically created which you can edit to fine-tune the installation.

## Version
This plugin is currently being reworked and is considered to be in its 'alpha' stage of its 'version 2'. The latest alpha build is 2.0.1.0; it is availble in the 'releases' section. The plugin will be for Minecraft versions between 1.7.6 and 1.14.0. 

## Licence 
This plugin, both the original version by alexbennet and this reworked version, is released under a MIT licence, available in the [LICENSE.txt](LICENSE.txt "LICENSE.txt") file. 

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
- */afkkickall [force]: Kick all AFK players. 
- */afkconfig save|reload|{get &lt;file&gt; &lt;field&gt;}|{set &lt;file&gt; &lt;field&gt; &lt;value&gt;}: Save, reload, get and set config file values. 

## Permissions
- justafk.*:
    description: The root JustAFK permission
    default: op
    children:
      justafk.afk: true
      justafk.list: true
      justafk.isafk: true
      justafk.setafk: true
      justafk.kickall.force: true
      justafk.immune.*: true
      justafk.config.*: true
- justafk.afk:
    description: The default permission for players to set their AFK status.
    default: op
- justafk.list:
    description: The permission to list AFK players. 
    default: op
- justafk.isafk:
    description: The permission to see if a player is AFK. 
    default: op
- justafk.setafk:
    description: The permission to set another player as AFK.
    default: op
- justafk.immune.*:
    description: Makes the player immune to automatic kicking and lightning.
    default: op
    children:
      justafk.immune.afk: true
      justafk.immune.kick: true
      justafk.immune.lightning: true
- justafk.immune.afk:
    description: Makes the player immune to being set to AFK after a set interval of time. 
    default: op
- justafk.immune.kick:
    description: Makes the player immune to being kicked. 
    default: op
- justafk.immune.lightning:
    description: Makes the player immune to lightning. 
    default: op
- justafk.kickall.force:
    description: Lets the player kick all AFK players 
    default: op
    children:
      justafk.kickall: true
- justafk.kickall:
    description: Lets the player kick all non-exempt AFK players 
    default: op
- justafk.config.*:
    description: The root config management permissions. 
    default: op
    children: 
      justafk.config.seemessages: true
      justafk.config.save: true
      justafk.config.reload: true
      justafk.config.set: true
- justafk.config.seemessages:
    description: Makes the player able to see config messages. 
    default: op
- justafk.config.save:
    description: Makes the player able to save the config. 
    default: op
- justafk.config.reload:
    description: Makes the player able to reload the config.
    default: op
- justafk.config.set:
    description: Set the config values 
    default: op
    children:
      justafk.config.get: true
- justafk.config.get:
    description: Get the config values 
    default: op

## Configuration
This plugin has two configuration files. `config.yml` configures various plugin parameters and `localisation.yml` configures the plugin messages. 

The config.yml file has various options, as shown below. 
```
tagmessages: true # If enabled, all AFK messages will be tagged with "[JustAFK]"
colourchar: '&' # The Bukkit colour code character (do not change this)
autokick: true # If enabled, players will be kicked automatically if inactive
lightning: false # Whether to strike AFK players with lightning or not 
hideawayplayers: true # Whether to make AFK players invisible or not 
broadcastawaymsg: true # Whether to broadcast when players go away or not 
broadcastkickmessage: true # Whether to broadcast AFK player kick messages or not 
movementcheckfreq: 30 # The amount of time in seconds to check player movement for inactivity.
kicktime: 60 # The amount of time in seconds that a player can be AFK before being kicked
inactivetime: 90 # The amount of time in seconds that a player can be inactive before being set to AFK 
kickwhileinvehicle: true # Whether to kick players in vehicles or not 
returnonlook: true # Whether players should be returned from being AFK by looking around or not 
sleepingimmuneisafkimmune: false # If sleeping-immune players are immune from being automatically set AFK 
sneakingisautiafkimmune: false # If sneaking players are immune from being automatically set AFK 
sleepingisautoafkimmune: false # If sleeping players are immune from being automatically set AFK 
deadisautoafkimmune: false # If dead players are immune from being automatically set AFK 
invehicleisautoafkimmune: false # If players in vehicles are immune from being automatically set AFK 
```

The options for the localisation.yml file are shown below. 
```
enable_message: '{plugin} {version} has been successfully enabled.' 
auto_kick: '{name} has been kicked for inactivity.'
auto_away: 'Are you still there? Move around, if you are.'
private_return: 'You are no longer away.'
public_return: '{name} is no longer away with the reason "{reason}".'
private_away: 'You have been set to away.'
public_away_reason: '&c{name} has been set to away for "{reason}", giving the message: "{message}".'
public_away_generic: '&c{name} has been set to away for "{reason}".'
currently_away: '&eThese players are currently set to away: {names}'
nobody_away: 'There is nobody currently set to away.'
setafk_away_private: 'You have been set to away by {name}.'
setafk_return_private: 'You have been set to available by {name}.'
kick_reason: '&cYou were inactive for too long!' # The reason to give when kicking an AFK player.
player_command: '&cYou must be a player to use this command.'
no_permission: '&cYou do not have permission to use this command.'
help_header: '&3{plugin} commands'
help_justafk: '/justafk - Get information about the plugin'
help_afk: '/afk [<reason>] - Set yourself as AFK with an optional reason'
help_whosafk: '/whosafk - List the AFK players'
help_setafk: '/setafk <player> [<reason>] - Set another player as AFK with an optional reason'
help_afkhelp: '/afkhelp - The help command for {plugin}'
help_isafk: '/isafk <player> - Check if another player is AFK and get the reason'
help_afkconfig: '/afkconfig <save|reload> - Save of reload the configuration files'
help_afkkick: '/afkkickall [force] - Kick all players with the option of forcing immune players to be kicked'
version_message: '&1{plugin} is version {version}.'
no_player: '&cThe specified player could not be found.'
not_afk: '{name} is not AFK.'
is_afk: '{name} is away.'
afk_reason: '{name} is away for reason: {reason}.'
afk_message: '{name} went away for reason: {message}.' 
use_afkhelp: 'Use /afkhelp for help on the plugin.'
conf_save_success: '&a{plugin} {conf} configuration saved.'
conf_save_fail: '&c{plugin} {conf} configuration could not be saved.'
conf_reload: '&a{plugin} {conf} configuration reloaded.'
conf_not_field: '&cThat is not a field in the {plugin} {conf} file.'
conf_field_set: '&aThe {plugin} {conf} file field has been set to {val}.'
conf_field_notset: '&cThe {plugin} {conf} file field could not be set to {val}.'
mass_kick: '&cYou were kicked for being inactive'
````

