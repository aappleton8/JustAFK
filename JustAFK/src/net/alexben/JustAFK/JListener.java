package net.alexben.JustAFK;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JListener implements Listener {
	
	private static JustAFK plugin = null; 
	
	public JListener(JustAFK instance) {
		plugin = instance; 
	}
	
	/**
	 * Handle Listening
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	private void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();

		JUtility.saveData(player, "isafk", false);
		JUtility.saveData(player, "iscertain", true);
		JUtility.saveData(player, "lastactive", System.currentTimeMillis());

		if(plugin.options.getSettingBoolean("hideawayplayers"))
		{
			for(Player awayPlayer : JUtility.getAwayPlayers(true))
			{
				player.hidePlayer(plugin, awayPlayer);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	private void onPlayerQuit(PlayerQuitEvent event)
	{
		
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onPlayerMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		boolean certain = Boolean.parseBoolean(JUtility.getData(player, "iscertain").toString());
		boolean yawChange = event.getFrom().getYaw() != event.getTo().getYaw();
		boolean pitchChange = event.getFrom().getPitch() != event.getTo().getPitch();

		if(!JUtility.isAway(player)) return;

		if(certain)
		{
			JUtility.setAway(player, false, certain);
			JUtility.sendMessage(player, ChatColor.AQUA + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("private_return")));
			return;
		}
		else
		{
			if(!player.isInsideVehicle())
			{
				if(pitchChange)
				{
					JUtility.setAway(player, false, certain);
					JUtility.sendMessage(player, ChatColor.AQUA + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("private_return")));
					return;
				}
			}

			if(yawChange || pitchChange)
			{
				JUtility.setAway(player, false, certain);
				JUtility.sendMessage(player, ChatColor.AQUA + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("private_return")));
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onPlayerChat(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();

		JUtility.saveData(player, "lastactive", System.currentTimeMillis());

		if(JUtility.isAway(player))
		{
			JUtility.setAway(player, false, Boolean.parseBoolean(JUtility.getData(player, "iscertain").toString()));
			JUtility.sendMessage(player, ChatColor.AQUA + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("private_return")));
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onInventoryClick(InventoryClickEvent event)
	{
		@SuppressWarnings("deprecation")
		OfflinePlayer player = Bukkit.getOfflinePlayer(event.getWhoClicked().getName());

		JUtility.saveData(player, "lastactive", System.currentTimeMillis());

		assert player.isOnline() : player.getName() + " is not online.";
		if(JUtility.isAway(player.getPlayer()))
		{
			JUtility.setAway(player.getPlayer(), false, Boolean.parseBoolean(JUtility.getData(player, "iscertain").toString()));
			JUtility.sendMessage(player.getPlayer(), ChatColor.AQUA + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("private_return")));
		}
	}
}
