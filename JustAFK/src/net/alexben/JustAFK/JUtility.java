package net.alexben.JustAFK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class JUtility
{
	// Define variables
	private static JustAFK plugin = null;
	private static final Logger log = Logger.getLogger("Minecraft");
	private static final String pluginName = ChatColor.GREEN + "JustAFK" + ChatColor.RESET;
	private static final String pluginNameNoColor = "JustAFK";
	private static final HashMap<String, HashMap<String, Object>> save = new HashMap<String, HashMap<String, Object>>();

	public static void initialize(JustAFK instance)
	{
		plugin = instance;
	}

	/**
	 * Returns the logger for the current plugin instance.
	 * 
	 * @return the logger instance.
	 */
	public static Logger getLog()
	{
		return log;
	}

	/**
	 * Sends <code>msg</code> to the console with type <code>type</code>.
	 * 
	 * @param type the type of message.
	 * @param msg the message to send.
	 */
	public static void log(String type, String msg)
	{
		if(type.equalsIgnoreCase("info")) log.info("[" + pluginNameNoColor + "] " + msg);
		else if(type.equalsIgnoreCase("warning")) log.warning("[" + pluginNameNoColor + "] " + msg);
		else if(type.equalsIgnoreCase("severe")) log.severe("[" + pluginNameNoColor + "] " + msg);
	}

	/**
	 * Sends a server-wide message.
	 * 
	 * @param msg the message to send.
	 */
	public static void serverMsg(String msg)
	{
		if(plugin.options.getSettingBoolean("tagmessages"))
		{
			Bukkit.getServer().broadcastMessage("[" + pluginName + "] " + msg);
		}
		else Bukkit.getServer().broadcastMessage(msg);

	}

	/**
	 * Sends a message to a player prepended with the plugin name.
	 * 
	 * @param player the player to message.
	 * @param msg the message to send.
	 */
	public static void sendMessage(Player player, String msg)
	{
		if(plugin.options.getSettingBoolean("tagmessages"))
		{
			player.sendMessage("[" + pluginName + "] " + msg);
		}
		else
		{
			player.sendMessage(msg);
		}
	}

	/**
	 * Sets the <code>player</code>'s away status to <code>boolean</code>, with certainty set to <code>certain</code>.
	 * 
	 * @param player the player to update.
	 * @param away the away status to set.
	 * @param certain the certainty status to set.
	 */
	public static void setAway(final Player player, boolean away, boolean certain)
	{
		// Hide or display the player based on their away status.
		if(away && certain)
		{
			if(plugin.options.getSettingBoolean("hideawayplayers"))
			{
				for(Player onlinePlayer : Bukkit.getOnlinePlayers())
				{
					onlinePlayer.hidePlayer(plugin, player);
				}
			}
		}
		else if(!away)
		{
			removeData(player, "isafk");
			removeData(player, "iscertain");
			removeData(player, "message");
			removeData(player, "position");

			for(Player onlinePlayer : Bukkit.getOnlinePlayers())
			{
				onlinePlayer.showPlayer(plugin, player);
			}
		}

		// Save their availability
		saveData(player, "isafk", away);
		saveData(player, "iscertain", certain);

		// Send the server-wide message
		if(plugin.options.getSettingBoolean("broadcastawaymsg"))
		{
			if(away && certain)
			{
				if(getData(player, "message") != null)
				{
					serverMsg(ChatColor.RED + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("public_away_reason").replace("{name}", player.getDisplayName()).replace("{message}", getData(player, "message").toString())));
				}
				else
				{
					serverMsg(ChatColor.RED + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("public_away_generic").replace("{name}", player.getDisplayName())));
				}

			}
			else if(!away && certain)
			{
				serverMsg(ChatColor.RED + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("public_return").replace("{name}", player.getDisplayName())));
			}
		}

		// If auto-kick is enabled then start the delayed task
		if(away && plugin.options.getSettingBoolean("autokick") && !hasPermission(player, "justafk.immune"))
		{
			if(player.isInsideVehicle() && !plugin.options.getSettingBoolean("kickwhileinvehicle")) return;

			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
			{
				@Override
				public void run()
				{
					if(!isAway(player)) return;

					// Remove their data, show them, and then finally kick them
					removeAllData(player);

					for(Player onlinePlayer : Bukkit.getOnlinePlayers())
					{
						onlinePlayer.showPlayer(plugin, player);
					}

					player.kickPlayer(ChatColor.translateAlternateColorCodes('&', plugin.language.getSettingString("kick_reason")));

					// Log it to the console
					log("info", StringEscapeUtils.unescapeJava(plugin.language.getSettingString("auto_kick").replace("{name}", player.getDisplayName())));
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
	public static void setAwayMessage(Player player, String msg)
	{
		saveData(player, "message", msg);
	}

	/**
	 * Returns true if the <code>player</code> is currently AFK.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public static boolean isAway(Player player)
	{
		return getAwayPlayers(true).contains(player) || getAwayPlayers(false).contains(player);
	}

	/**
	 * Returns true if the <code>player</code> is currently AFK, with a certainty of <code>certain</code>.
	 * 
	 * @param player the player to check.
	 * @param certain the certainty to check.
	 * @return boolean
	 */
	public static boolean isAway(Player player, boolean certain)
	{
		return getAwayPlayers(certain).contains(player);
	}

	/**
	 * Returns an ArrayList of all currently away players, with certainty set to <code>certain</code>.
	 * 
	 * @param certain the certainty of being AFK.
	 * @return ArrayList
	 */
	public static ArrayList<Player> getAwayPlayers(boolean certain)
	{
		ArrayList<Player> players = new ArrayList<Player>();

		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(getData(player, "isafk") == null || getData(player, "isafk").equals(false)) continue;
			if(certain && (getData(player, "iscertain") == null || getData(player, "iscertain").equals(false))) continue;
			players.add(player);
		}

		return players;
	}

	/**
	 * Returns true if <code>player</code> has the permission called <code>permission</code>.
	 * 
	 * @param player the player to check.
	 * @param permission the permission to check for.
	 * @return boolean
	 */
	public static boolean hasPermission(OfflinePlayer player, String permission)
	{
		return player == null || player.getPlayer().hasPermission(permission);
	}

	/**
	 * Returns true if <code>player</code> has the permission called <code>permission</code> or is an OP.
	 * 
	 * @param player the player to check.
	 * @param permission the permission to check for.
	 * @return boolean
	 */
	public static boolean hasPermissionOrOP(OfflinePlayer player, String permission)
	{
		return player == null || player.isOp() || player.getPlayer().hasPermission(permission);
	}

	/**
	 * Checks movement for all online players and marks them as AFK if need-be.
	 */
	public static void checkActivity()
	{
		// Get all online players
		for(Player player : Bukkit.getOnlinePlayers())
		{
			// Make sure they aren't already away
			if(!isAway(player) && !hasPermissionOrOP(player, "justafk.immune"))
			{
				// Define variables
				boolean active = true;
				boolean certain = false;

				// Check their movement
				if(getData(player, "position") != null)
				{
					if(player.isInsideVehicle() && ((Location) getData(player, "position")).getPitch() == player.getLocation().getPitch()) active = false;
					else if((((Location) getData(player, "position")).getYaw() == player.getLocation().getYaw() && ((Location) getData(player, "position")).getPitch() == player.getLocation().getPitch())) active = false;

					if(!active && (((Location) getData(player, "position")).getX() == player.getLocation().getX()) && ((Location) getData(player, "position")).getY() == player.getLocation().getY() && ((Location) getData(player, "position")).getZ() == player.getLocation().getZ()) certain = true;
				}

				if(!active)
				{
					// Check for lack of other activity
					Long lastActive = Long.parseLong("" + getData(player, "lastactive"));
					Long checkFreq = Long.parseLong("" + plugin.options.getSettingInt("movementcheckfreq")) * 1000;

					if(lastActive >= System.currentTimeMillis() - checkFreq) return;

					// They player is AFK, set their status
					setAway(player, true, certain);

					// Message them
					player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("auto_away")));
				}

				saveData(player, "position", player.getLocation());
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
	public static void saveData(OfflinePlayer player, String name, Object data)
	{
		// Create new save for the player if one doesn't already exist
		if(!save.containsKey(player.getName()))
		{
			save.put(player.getName(), new HashMap<String, Object>());
		}

		// Prepend the data with "jafk" to avoid plugin collisions and save the data
		save.get(player.getName()).put(name.toLowerCase(), data);
	}

	/**
	 * Returns the data with the key <code>name</code> from <code>player</code>'s HashMap.
	 * 
	 * @param player the player to check.
	 * @param name the key to grab.
	 */
	public static Object getData(OfflinePlayer player, String name)
	{
		if(save.containsKey(player.getName()) && save.get(player.getName()).containsKey(name))
		{
			return save.get(player.getName()).get(name);
		}
		return null;
	}

	/**
	 * Removes the data with the key <code>name</code> from <code>player</code>.
	 * 
	 * @param player the player to remove data from.
	 * @param name the key of the data to remove.
	 */
	public static void removeData(OfflinePlayer player, String name)
	{
		if(save.containsKey(player.getName())) save.get(player.getName()).remove(name.toLowerCase());
	}

	/**
	 * Removes all data for the <code>player</code>.
	 * 
	 * @param player the player whose data to remove.
	 */
	public static void removeAllData(OfflinePlayer player)
	{
		save.remove(player.getName());
	}
}
