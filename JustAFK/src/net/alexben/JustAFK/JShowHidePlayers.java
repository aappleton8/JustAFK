package net.alexben.JustAFK;

import java.util.List;

import org.bukkit.entity.Player;

public class JShowHidePlayers {
	private static JustAFK plugin = null;
	
	public static void initialise(JustAFK instance) {
		plugin = instance; 
	}
	
	public static void showPlayer(Player player, List<Player> players) {
		for (Player p : players) {
			player.showPlayer(plugin, p); 
		}
	}
	
	public static void hidePlayer(Player player, List<Player> players) {
		if (plugin.options.getSettingBoolean("hideawayplayers")) {
			for (Player p : players) {
				if ((p.hasPermission("justafk.player.seehidden") == false) && (plugin.players.getSettingBooleanDefault("players." + p.getUniqueId().toString() + ".seehidden", false) == false)) {
					p.hidePlayer(plugin, player);
				}
			}
		}
	}
}
