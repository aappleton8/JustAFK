package net.alexben.JustAFK;

import org.bukkit.Bukkit;
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
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	private void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		JUtility.saveData(player, JUtility.MessageTypes.ISAFK, false);
		JUtility.saveData(player, JUtility.MessageTypes.RETURNISCERTAIN, true);
		JUtility.saveData(player, JUtility.MessageTypes.LASTACTIVE, System.currentTimeMillis());
		JUtility.saveData(player, JUtility.MessageTypes.RETURNREASON, "join");

		if (plugin.options.getSettingBoolean("hideawayplayers")) {
			for (Player awayPlayer : JUtility.getAwayPlayers(true)) {
				player.hidePlayer(plugin, awayPlayer);
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	private void onPlayerQuit(PlayerQuitEvent event) {
		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			onlinePlayer.showPlayer(plugin, event.getPlayer());
		}
		JUtility.removeAllData(event.getPlayer()); 
	}
	
	@EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
	private void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if (JUtility.isAway(player)) {
			boolean yawChange = event.getFrom().getYaw() != event.getTo().getYaw();
			boolean pitchChange = event.getFrom().getPitch() != event.getTo().getPitch();
			boolean directionChange = event.getFrom().getDirection() != event.getTo().getDirection(); 
			boolean xChange = event.getFrom().getX() != event.getTo().getX(); 
			boolean yChange = event.getFrom().getY() != event.getTo().getY(); 
			boolean zChange = event.getFrom().getZ() != event.getTo().getZ(); 
			
			boolean returnOnLook = plugin.options.getSettingBooleanDefault("returnonlook", true); 
			
			if (xChange || yChange || zChange) {
				JUtility.setAway(player, false, true, "movement");
				JUtility.sendMessage(player, plugin.language.getSettingString("private_return"));
			}
			else if ((yawChange || pitchChange || directionChange) &&(returnOnLook)) {
				JUtility.setAway(player, false, true, "look");
				JUtility.sendMessage(player, plugin.language.getSettingString("private_return"));
			}
		}
		else {
			JUtility.saveData(player, JUtility.MessageTypes.LASTACTIVE, System.currentTimeMillis()); 
		}
	}
	
	@EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
	private void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		if (JUtility.isAway(player)) {
			JUtility.setAway(player, false, true, "chat");
			JUtility.sendMessage(player, plugin.language.getSettingString("private_return"));
		}
		else {
			JUtility.saveData(player, JUtility.MessageTypes.LASTACTIVE, System.currentTimeMillis()); 
		}
	}
	
	@EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
	private void onInventoryClick(InventoryClickEvent event) {
		Player player = Bukkit.getPlayer(event.getWhoClicked().getUniqueId());
		
		if (JUtility.isAway(player)) {
			JUtility.setAway(player.getPlayer(), false, true, "inventory-click");
			JUtility.sendMessage(player.getPlayer(), plugin.language.getSettingString("private_return"));
		}
		else {
			JUtility.saveData(player, JUtility.MessageTypes.LASTACTIVE, System.currentTimeMillis()); 
		}
	}
}
