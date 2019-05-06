package net.alexben.JustAFK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

public class JUtility
{
	// Define variables
	private static JustAFK plugin = null;
	private static final HashMap<UUID, HashMap<MessageTypes, Object>> save = new HashMap<UUID, HashMap<MessageTypes, Object>>();
	private static char colourChar = '&'; 
	
	// Initialise the static class 
	public static void initialize(JustAFK instance) {
		plugin = instance;
		String tryColourChar = plugin.options.getSettingString("colourchar"); 
		if (tryColourChar != null) {
			if (tryColourChar.length() == 1) {
				colourChar = tryColourChar.charAt(0); 
			}
		}
	}
	
	// Message sending functions 
	/**
	 * Sends <code>msg</code> to the console with type <code>type</code>.
	 * 
	 * @param type the type of message.
	 * @param msg the message to send.
	 */
	public static void log(String type, String msg) {
		if (type.equalsIgnoreCase("info")) plugin.getLogger().info(msg);
		else if (type.equalsIgnoreCase("warning")) plugin.getLogger().warning(msg);
		else if (type.equalsIgnoreCase("severe")) plugin.getLogger().severe(msg);
	}
	
	/**
	 * Replaces '{plugin}' and '{version}' with the plugin name and version 
	 * 
	 * @param msg The string to parse 
	 * @return The parsed message 
	 */
	public static String updatePluginVersionMessages(String msg) {
		if (msg != null) {
			return msg.replaceAll("\\{plugin\\}", plugin.getDescription().getName()).replaceAll("\\{version\\}", plugin.getDescription().getVersion()); 
		}
		else return ""; 
	}
	
	/**
	 * Replaces '{name}' with the name of the player 
	 * 
	 * @param playerName The name of the player 
	 * @param msg The message 
	 * @return The message with the replacements 
	 */
	public static String updatePlayerNameMessages(String playerName, String msg) {
		if ((msg != null) && (playerName != null)) {
			return msg.replaceAll("\\{name\\}", playerName); 
		}
		else return ""; 
	}
	
	/**
	 * Replaces '{' + placeholder + '}' with a replacement value 
	 * 
	 * @param placeholder The placeholder to replace 
	 * @param replacement The text to replace the placeholder with 
	 * @param msg The message 
	 * @return The message with the placeholders replaced with the replacement value 
	 */
	public static String updateMessagePlaceholders(String placeholder, String replacement, String msg) {
		if ((placeholder != null) && (msg != null)) {
			return msg.replaceAll("\\{" + placeholder + "\\}", replacement); 
		}
		else return ""; 
	}
	
	/**
	 * Inserts PlaceholderAPI placeholders 
	 * 
	 * @param player The player 
	 * @param msg The text to update 
	 * @return The message with the replaced placeholders 
	 */
	public static String updatePlaceholderAPIPlaceholders(Player player, String msg) {
		return PlaceholderAPI.setPlaceholders(player, msg); 
	}
	
	/**
	 * This functions unescapes Java escape characters and implements colour codes 
	 * 
	 * @param msg The emssage to parse 
	 * @return The parsed message 
	 */
	public static String updateMessageColours(String msg) {
		if (msg == null) return ""; 
		else return StringEscapeUtils.unescapeJava(ChatColor.translateAlternateColorCodes(colourChar, msg)); 
	}
	
	/**
	 * Sends a message to the console. 
	 * 
	 * @param msg the message to send. 
	 */
	public static void consoleMsg(String msg) {
		Bukkit.getServer().getConsoleSender().sendMessage(plugin.getPluginName(true, true) + updateMessageColours(msg));
	}
	
	/**
	 * Sends a server-wide message.
	 * 
	 * @param msg the message to send.
	 * @param permission the permission the message recipients require 
	 */
	public static void serverMsg(String msg) {
		serverMsg(msg, Server.BROADCAST_CHANNEL_USERS); 
	}
	public static void serverMsg(String msg, String permission) {
		msg = updateMessageColours(msg); 
		if (plugin.options.getSettingBoolean("tagmessages")) {
			Bukkit.getServer().broadcast(plugin.getPluginName(true, true) + msg, permission);
		}
		else Bukkit.getServer().broadcast(msg, permission);
	}
	
	/**
	 * Sends a message to a player prepended with the plugin name.
	 * 
	 * @param player the player to message.
	 * @param msg the message to send.
	 */
	public static void sendMessage(CommandSender sender, String msg) {
		msg = updateMessageColours(msg); 
		if (plugin.options.getSettingBoolean("tagmessages")) {
			sender.sendMessage(plugin.getPluginName(true, true) + msg);
		}
		else sender.sendMessage(msg);
	}
	
	// Utility functions 
	public static enum MessageTypes {
		ISAFK, 
		AFKISCERTAIN, 
		RETURNISCERTAIN,
		AFKMESSAGE, 
		POSITION,
		LASTACTIVE, 
		AFKREASON,
		RETURNREASON
	}
	
	/**
	 * Sets the <code>player</code>'s away status to <code>boolean</code>, with certainty set to <code>certain</code>.
	 * 
	 * @param player the player to update.
	 * @param away the away status to set.
	 * @param certain the certainty status to set.
	 */
	public static void setAway(final Player player, boolean away, boolean certain, String reason) {
		// Hide or display the player based on their away status.
		if (away && certain) {
			if (plugin.options.getSettingBoolean("hideawayplayers")) {
				for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
					onlinePlayer.hidePlayer(plugin, player);
				}
			}
			removeData(player, MessageTypes.POSITION);
			saveData(player, MessageTypes.POSITION, player.getLocation());
			removeData(player, MessageTypes.RETURNREASON); 
			removeData(player, MessageTypes.AFKREASON); 
			saveData(player, MessageTypes.AFKREASON, reason); 
			saveData(player, MessageTypes.AFKISCERTAIN, certain);
		}
		else if (!away) {
			removeAllData(player); 

			showPlayer(player); 
			
			saveData(player, MessageTypes.LASTACTIVE, System.currentTimeMillis()); 
			saveData(player, MessageTypes.RETURNREASON, reason); 
			saveData(player, MessageTypes.RETURNISCERTAIN, certain);
		}
		else if (away && !certain) {
			saveData(player, MessageTypes.AFKISCERTAIN, certain);
		}
		
		// Save their availability
		saveData(player, MessageTypes.ISAFK, away);
		
		// Send the server-wide message
		if (plugin.options.getSettingBoolean("broadcastawaymsg")) {
			if (away && certain) {
				if (getData(player, MessageTypes.AFKMESSAGE) != null) {
					serverMsg(updateMessagePlaceholders("reason", getData(player, MessageTypes.AFKREASON).toString(), updateMessagePlaceholders("message", getData(player, MessageTypes.AFKMESSAGE).toString(), updatePlayerNameMessages(player.getName(), plugin.language.getSettingString("public_away_reason")))));
				}
				else {
					serverMsg(updateMessagePlaceholders("reason", getData(player, MessageTypes.AFKREASON).toString(), updatePlayerNameMessages(player.getName(), plugin.language.getSettingString("public_away_generic"))));
				}

			}
			else if (!away && certain) {
				serverMsg(updateMessagePlaceholders("reason", getData(player, MessageTypes.RETURNREASON).toString(), updatePlayerNameMessages(player.getName(), plugin.language.getSettingString("public_return"))));
			}
		}
		
		// If auto-kick is enabled then start the delayed task
		if (away && (plugin.options.getSettingBoolean("autokick") || plugin.options.getSettingBoolean("lightning")) && (!player.hasPermission("justafk.immune"))) {
			if (player.isInsideVehicle() && !plugin.options.getSettingBoolean("kickwhileinvehicle")) return;
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				private Boolean kick = !player.hasPermission("justafk.immune.kick"); 
				private Boolean lightning = !player.hasPermission("justafk.immune.lightning"); 
				
				@Override
				public void run()
				{
					if (!isAway(player)) return;

					// Remove their data, show them, and then finally kick them
					removeAllData(player);
					
					if (lightning) {
						player.getWorld().strikeLightning(player.getLocation()); 
					}
					if (kick) {
						showPlayer(player); 
						player.kickPlayer(updateMessageColours(plugin.language.getSettingString("kick_reason")));
						// Log it to the console
						if (plugin.options.getSettingBoolean("broadcastkickmessage")) {
							serverMsg(updatePlayerNameMessages(player.getName(), plugin.language.getSettingString("auto_kick")));
						}
						else {
							consoleMsg(updatePlayerNameMessages(player.getName(), plugin.language.getSettingString("auto_kick"))); 
						}
					}
				}
			}, plugin.options.getSettingInt("kicktime") * 20);
		}
	}
	
	/**
	 * Sets the <code>player</code>'s away message to <code>msg</code>.
	 * 
	 * @param player the player to update.
	 * @param msg the message to
	 */
	public static void setAwayMessage(Player player, String msg) {
		saveData(player, MessageTypes.AFKMESSAGE, msg);
	}
	
	/**
	 * Returns true if the <code>player</code> is currently AFK.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public static boolean isAway(Player player) {
		return getAwayPlayers(true).contains(player) || getAwayPlayers(false).contains(player);
	}
	
	/**
	 * Returns true if the <code>player</code> is currently AFK, with a certainty of <code>certain</code>.
	 * 
	 * @param player the player to check.
	 * @param certain the certainty to check.
	 * @return boolean
	 */
	public static boolean isAway(Player player, boolean certain) {
		return getAwayPlayers(certain).contains(player);
	}
	
	/**
	 * Returns an ArrayList of all currently away players, with certainty set to <code>certain</code>.
	 * 
	 * @param certain the certainty of being AFK.
	 * @return ArrayList
	 */
	public static ArrayList<Player> getAwayPlayers(boolean certain) {
		ArrayList<Player> players = new ArrayList<Player>();

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (getData(player, MessageTypes.ISAFK) == null || getData(player, MessageTypes.ISAFK).equals(false)) continue;
			if (certain && (getData(player, MessageTypes.AFKISCERTAIN) == null || getData(player, MessageTypes.AFKISCERTAIN).equals(false))) continue;
			players.add(player);
		}

		return players;
	}
	
	/**
	 * Checks movement for all online players and marks them as AFK if need-be.
	 */
	public static void checkActivity() {
		// Get all online players
		for (Player player : Bukkit.getOnlinePlayers()) {
			// Make sure they aren't already away
			if (!isAway(player) && !player.hasPermission("justafk.immune.afk") && (getData(player, MessageTypes.LASTACTIVE) != null)) {
				if (player.isInsideVehicle() && plugin.options.getSettingBoolean("invehicleisautoafkimmune")) {
					saveData(player, MessageTypes.POSITION, player.getLocation());
					continue;
				}
				else if (player.isSleepingIgnored() && plugin.options.getSettingBoolean("sleepingimmuneisautoafkimmune")) {
					saveData(player, MessageTypes.POSITION, player.getLocation());
					continue;
				}
				else if (player.isSleeping() && plugin.options.getSettingBoolean("sleepingisautoafkimmune")) {
					saveData(player, MessageTypes.POSITION, player.getLocation());
					continue;
				}
				else if (player.isSneaking() && plugin.options.getSettingBoolean("sneakingisautoafkimmune")) {
					saveData(player, MessageTypes.POSITION, player.getLocation());
					continue;
				}
				else if (player.isDead() && plugin.options.getSettingBoolean("deadisautoafkimmune")) {
					saveData(player, MessageTypes.POSITION, player.getLocation());
					continue;
				}
				
				
				// Define variables
				boolean active = true;
				boolean certain = false;
				
				// Check their movement
				if (getData(player, MessageTypes.POSITION) != null) {
					Location playerLocation = player.getLocation(); 
					Location afkLocation = (Location) getData(player, MessageTypes.POSITION); 
					
					if ((plugin.options.getSettingBoolean("returnonlook") == false) || (((afkLocation.getYaw() == playerLocation.getYaw()) && (afkLocation.getPitch() == playerLocation.getPitch()) && (afkLocation.getDirection() == playerLocation.getDirection())))) {
						active = false;
					}
					if ((!active) && ((afkLocation.getX() == playerLocation.getX())) && (afkLocation.getY() == playerLocation.getY()) && (afkLocation.getZ() == playerLocation.getZ())) {
						certain = true;
					}
				}
				
				if (!active) {
					// Check for lack of other activity
					Long lastActive = Long.parseLong(getData(player, MessageTypes.LASTACTIVE).toString());
					Long checkFreq = Long.parseLong(Integer.toString(plugin.options.getSettingInt("inactivetime"))) * 1000;
					
					if (lastActive >= System.currentTimeMillis() - checkFreq) continue;
					
					// They player is AFK, set their status
					setAway(player, true, certain, "no-activity");
					
					// Message them
					JUtility.sendMessage(player, plugin.language.getSettingString("auto_away"));
				}
				saveData(player, MessageTypes.POSITION, player.getLocation());
			}
			else if (getData(player, MessageTypes.LASTACTIVE) == null) {
				saveData(player, MessageTypes.LASTACTIVE, System.currentTimeMillis()); 
			}
		}
	}
	
	/**
	 * Saves <code>data</code> under the key <code>name</code> to <code>player</code>.
	 * 
	 * @param player the player to save data to.
	 * @param name the name of the data.
	 * @param data the data to save.
	 */
	public static void saveData(OfflinePlayer player, MessageTypes name, Object data) {
		// Create new save for the player if one doesn't already exist
		if (!save.containsKey(player.getUniqueId())) {
			save.put(player.getUniqueId(), new HashMap<MessageTypes, Object>());
		}
		save.get(player.getUniqueId()).put(name, data);
	}
	
	/**
	 * Returns the data with the key <code>name</code> from <code>player</code>'s HashMap.
	 * 
	 * @param player the player to check.
	 * @param name the key to grab.
	 */
	public static Object getData(OfflinePlayer player, MessageTypes name) {
		if (save.containsKey(player.getUniqueId()) && save.get(player.getUniqueId()).containsKey(name))
		{
			return save.get(player.getUniqueId()).get(name);
		}
		return null;
	}
	
	/**
	 * Removes the data with the key <code>name</code> from <code>player</code>.
	 * 
	 * @param player the player to remove data from.
	 * @param name the key of the data to remove.
	 */
	public static void removeData(OfflinePlayer player, MessageTypes name) {
		if (save.containsKey(player.getUniqueId())) save.get(player.getUniqueId()).remove(name);
	}
	
	/**
	 * Removes all data for the <code>player</code>.
	 * 
	 * @param player the player whose data to remove.
	 */
	public static void removeAllData(OfflinePlayer player) {
		save.remove(player.getUniqueId());
	}
	
	/**
	 * Unhides the player 
	 * 
	 * @param player the player to show
	 */
	public static void showPlayer(Player player) {
		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			onlinePlayer.showPlayer(plugin, player); 
		}
	}
	
	/**
	 * Kicks all AFK players 
	 * 
	 * @param force whether to kick exempt players or not 
	 */
	public static void kickAllAwayPlayers(boolean force) {
		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (isAway(onlinePlayer)) {
				if ((force == true) || (onlinePlayer.hasPermission("justafk.immune.kick") == false)) {
					showPlayer(onlinePlayer); 
					onlinePlayer.kickPlayer(updateMessageColours(plugin.language.getSettingString("mass_kick")));
				}
			}
		}
	}
}
