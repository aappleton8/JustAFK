package net.alexben.JustAFK;

import java.util.ArrayList;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JCommands implements CommandExecutor {
	
	private static JustAFK plugin = null; 
	
	public JCommands(JustAFK instance) {
		plugin = instance; 
	}
	
	
	/**
	 * The help command 
	 * 
	 * @param sender The command sender 
	 */
	public void helpCommand(CommandSender sender) {
		sender.sendMessage(plugin.language.getSettingString("help_header"));
		sender.sendMessage(plugin.language.getSettingString("help_justafk"));
		sender.sendMessage(plugin.language.getSettingString("help_afkhelp")); 
		sender.sendMessage(plugin.language.getSettingString("help_afk"));
		sender.sendMessage(plugin.language.getSettingString("help_whosafk")); 
		sender.sendMessage(plugin.language.getSettingString("help_setafk"));
		sender.sendMessage(plugin.language.getSettingString("help_isafk")); 
	}
	
	/**
	 * Handle Commands
	 * 
	 * @param sender The command sender 
	 * @param command The command 
	 * @param label The command label used 
	 * @param args The command arguments 
	 * 
	 * @return Whether the command executed successfully or not 
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (command.getName().equalsIgnoreCase("afkhelp")) {
			helpCommand(sender); 
		}
		else if (command.getName().equalsIgnoreCase("justafk")) {
			JUtility.sendMessage(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("version_message"))); 
		}
		else if (command.getName().equalsIgnoreCase("afk")) {
			if (sender instanceof Player) {
				if (sender.hasPermission("justafk.afk")) {
					Player player = (Player) sender; 
					if (JUtility.isAway(player)) {
						JUtility.setAway(player, false, true);
						JUtility.sendMessage(player, ChatColor.AQUA + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("private_return")));
					}
					else {
						// If they included an away message then set it BEFORE setting away.
						if (args.length > 0) {
							String msg = StringUtils.join(args, " ");
							JUtility.setAwayMessage(player, msg);
						}
						// Now set away status
						JUtility.setAway(player, true, true);
						// Send the messages.
						JUtility.sendMessage(player, ChatColor.AQUA + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("private_away")));
					}
					return true;
				}
				else {
					JUtility.sendMessage(sender, plugin.language.getSettingString("no_permission"));
				}
			}
			else {
				JUtility.sendMessage(sender, plugin.language.getSettingString("player_command"));
			}
		}
		else if (command.getName().equalsIgnoreCase("setafk")) {
			if (sender.hasPermission("justafk.setafk")) {
				if (args.length > 0) {
					@SuppressWarnings("deprecation")
					Player player = Bukkit.getPlayer(args[0]); 
					if (player == null) {
						JUtility.sendMessage(sender, plugin.language.getSettingString("no_player")); 
					}
					else {
						if (!JUtility.isAway(player)) {
							JUtility.setAway(player, true, true);
							JUtility.sendMessage(player, ChatColor.GRAY + "" + ChatColor.ITALIC + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("setafk_away_private").replace("{name}", sender.getName())));
						}
						else {
							JUtility.setAway(player, false, true);
							JUtility.sendMessage(player, ChatColor.GRAY + "" + ChatColor.ITALIC + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("setafk_return_private").replace("{name}", sender.getName())));
						}
						return true;
					}
				}
				else {
					return false; 
				}
			}
			else {
				JUtility.sendMessage(sender, plugin.language.getSettingString("no_permission"));
			}
		}
		else if (command.getName().equalsIgnoreCase("whosafk")) {
			if (sender.hasPermission("justafk.list")) {
				if (JUtility.getAwayPlayers(true).isEmpty()) {
					JUtility.sendMessage(sender, StringEscapeUtils.unescapeJava(plugin.language.getSettingString("nobody_away")));
				}
				else {
					ArrayList<String> playerNames = new ArrayList<String>();
					for (Player item : JUtility.getAwayPlayers(true)) {
						playerNames.add(item.getName());
					}
					JUtility.sendMessage(sender, ChatColor.AQUA + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("currently_away")) + " " + StringUtils.join(playerNames, ", "));
				}
				return true;
			}
			else {
				JUtility.sendMessage(sender, plugin.language.getSettingString("no_permission"));
			}
		}
		else if (command.getName().equalsIgnoreCase("isafk")) {
			
		}
		else {
			return false; 
		}
		return true; 
	}
}
