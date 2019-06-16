package net.alexben.JustAFK;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class JListener implements Listener {
	
	private static JustAFK plugin = null; 
	
	public JListener(JustAFK instance) {
		plugin = instance; 
	}
	
	
	/**
	 * Handle Player Listening
	 */
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	private void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		JUtility.saveData(player, JUtility.MessageTypes.ISAFK, false);
		JUtility.saveData(player, JUtility.MessageTypes.RETURNISCERTAIN, true);
		JUtility.saveData(player, JUtility.MessageTypes.LASTACTIVE, System.currentTimeMillis());
		JUtility.saveData(player, JUtility.MessageTypes.RETURNREASON, "join");
		
		JShowHidePlayers.hidePlayer(player, JUtility.getAwayPlayers(true)); 
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	private void onPlayerQuit(PlayerQuitEvent event) {
		JShowHidePlayers.showPlayer(event.getPlayer(), new ArrayList<Player>(Bukkit.getOnlinePlayers()));
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
				JUtility.sendMessagePlaceholder(player, plugin.language.getSettingString("private_return"), player);
			}
			else if ((yawChange || pitchChange || directionChange) && (returnOnLook)) {
				JUtility.setAway(player, false, true, "look");
				JUtility.sendMessagePlaceholder(player, plugin.language.getSettingString("private_return"), player);
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
			JUtility.sendMessagePlaceholder(player, plugin.language.getSettingString("private_return"), player);
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
			JUtility.sendMessagePlaceholder(player.getPlayer(), plugin.language.getSettingString("private_return"), player);
		}
		else {
			JUtility.saveData(player, JUtility.MessageTypes.LASTACTIVE, System.currentTimeMillis()); 
		}
	}
	
	@EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
	private void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer(); 
		
		if (JUtility.isAway(player)) {
			JUtility.setAway(player, false, true, "respawn");
			JUtility.sendMessagePlaceholder(player, plugin.language.getSettingString("private_return"), player);
		}
		else {
			JUtility.saveData(player, JUtility.MessageTypes.LASTACTIVE, System.currentTimeMillis()); 
		}
	}
	
	
	/**
	 * Handle Sign and Player Listening 
	 */
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer(); 
		
		if (JUtility.isAway(player)) {
			JUtility.setAway(player, false, true, "interaction");
			JUtility.sendMessagePlaceholder(player, ChatColor.AQUA + plugin.language.getSettingString("private_return"), player);
		}
		else if (event.getClickedBlock().getState() instanceof Sign) {
			JUtility.saveData(player, JUtility.MessageTypes.LASTACTIVE, System.currentTimeMillis()); 
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Sign sign = (Sign) event.getClickedBlock().getState(); 
				if (sign.getLine(0).equalsIgnoreCase("[afk]")) {
					if (player.hasPermission("justafk.afk")) {
						// Set the away message BEFORE setting away.
						JUtility.setAwayMessage(player, sign.getLine(1)); 
						// Now set away status
						JUtility.setAway(player, true, true, "sign");
						// Send the messages.
						JUtility.sendMessagePlaceholder(player, ChatColor.AQUA + plugin.language.getSettingString("private_away"), player);
					}
					else {
						event.setCancelled(true); 
						JUtility.sendMessagePlaceholder(player, plugin.language.getSettingString("no_permission"), null);
					}
				}
			}
		}
		else {
			JUtility.saveData(player, JUtility.MessageTypes.LASTACTIVE, System.currentTimeMillis()); 
		}
	}
}
