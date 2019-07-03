package net.alexben.JustAFK;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
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
		JUtility.sendMessagePlaceholder(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_header")), null);
		JUtility.sendMessagePlaceholder(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_justafk")), null);
		JUtility.sendMessagePlaceholder(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_afkhelp")), null); 
		JUtility.sendMessagePlaceholder(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_afk")), null);
		JUtility.sendMessagePlaceholder(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_whosafk")), null); 
		JUtility.sendMessagePlaceholder(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_setafk")), null);
		JUtility.sendMessagePlaceholder(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_isafk")), null); 
		JUtility.sendMessagePlaceholder(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_afkconfig")), null);
		JUtility.sendMessagePlaceholder(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_afkkick")), null);
		JUtility.sendMessagePlaceholder(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_afkplayer")), null);
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
			JUtility.sendMessagePlaceholder(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("version_message")), null); 
			JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("use_afkhelp"), null); 
		}
		else if (command.getName().equalsIgnoreCase("afk")) {
			if (sender instanceof Player) {
				if (sender.hasPermission("justafk.afk")) {
					Player player = (Player) sender; 
					if (JUtility.isAway(player)) {
						JUtility.setAway(player, false, true, "player-command");
						JUtility.sendMessagePlaceholder(player, ChatColor.AQUA + plugin.language.getSettingString("private_return"), player);
					}
					else {
						// If they included an away message then set it BEFORE setting away.
						if (args.length > 0) {
							String msg = StringUtils.join(args, " ");
							JUtility.setAwayMessage(player, msg);
						}
						// Now set away status
						JUtility.setAway(player, true, true, "player-command");
						// Send the messages.
						JUtility.sendMessagePlaceholder(player, ChatColor.AQUA + plugin.language.getSettingString("private_away"), player);
					}
					return true;
				}
				else {
					JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_permission"), null);
				}
			}
			else {
				JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("player_command"), null);
			}
		}
		else if (command.getName().equalsIgnoreCase("setafk")) {
			if (sender.hasPermission("justafk.setafk")) {
				if (args.length > 0) {
					@SuppressWarnings("deprecation")
					Player player = Bukkit.getPlayer(args[0]); 
					if (player == null) {
						JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_player"), null); 
					}
					else {
						if (args.length > 1) {
							String message = ""; 
							for (int i = 1; i < args.length; i++) {
								message += args[i]; 
							}
							JUtility.saveData(player, JUtility.MessageTypes.AFKMESSAGE, message);
						}
						if (!JUtility.isAway(player)) {
							JUtility.setAway(player, true, true, "other-command");
							JUtility.sendMessagePlaceholder(player, JUtility.updatePlayerNameMessages(sender.getName(), plugin.language.getSettingString("setafk_away_private")), player);
						}
						else {
							JUtility.setAway(player, false, true, "other-command");
							JUtility.sendMessagePlaceholder(player, JUtility.updatePlayerNameMessages(sender.getName(), plugin.language.getSettingString("setafk_return_private")), player);
						}
						return true;
					}
				}
				else {
					return false; 
				}
			}
			else {
				JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_permission"), null);
			}
		}
		else if (command.getName().equalsIgnoreCase("whosafk")) {
			if (sender.hasPermission("justafk.list")) {
				if (JUtility.getAwayPlayers(true).isEmpty()) {
					JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("nobody_away"), null);
				}
				else {
					ArrayList<String> playerNames = new ArrayList<String>();
					for (Player item : JUtility.getAwayPlayers(true)) {
						playerNames.add(item.getName());
					}
					JUtility.sendMessagePlaceholder(sender, JUtility.updateMessagePlaceholders("names", StringUtils.join(playerNames, ", "), plugin.language.getSettingString("currently_away")), null);
				}
				return true;
			}
			else {
				JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_permission"), null);
			}
		}
		else if (command.getName().equalsIgnoreCase("isafk")) {
			if (sender.hasPermission("justafk.isafk")) {
				if (args.length == 1) {
					@SuppressWarnings("deprecation")
					Player player = Bukkit.getPlayer(args[0]); 
					if (player == null) {
						JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_player"), null); 
					}
					else {
						Boolean afk = (Boolean) JUtility.getData(player, JUtility.MessageTypes.ISAFK);
						Boolean certain = null; 
						String reason = null; 
						if (afk) {
							certain = (Boolean) JUtility.getData(player, JUtility.MessageTypes.AFKISCERTAIN); 
							reason = (String) JUtility.getData(player, JUtility.MessageTypes.AFKREASON); 
						}
						else {
							certain = (Boolean) JUtility.getData(player, JUtility.MessageTypes.RETURNISCERTAIN); 
							reason = (String) JUtility.getData(player, JUtility.MessageTypes.RETURNREASON); 
						}
						String message = (String) JUtility.getData(player, JUtility.MessageTypes.AFKMESSAGE); 
						if ((afk == null) || (afk == false) || (certain != true)) {
							JUtility.sendMessagePlaceholder(sender, JUtility.updatePlayerNameMessages(args[0], plugin.language.getSettingString("not_afk")), player); 
						}
						else {
							JUtility.sendMessagePlaceholder(sender, JUtility.updatePlayerNameMessages(args[0], plugin.language.getSettingString("is_afk")), player);
							if (reason != null) {
								JUtility.sendMessagePlaceholder(sender, JUtility.updateMessagePlaceholders("reason", reason, JUtility.updatePlayerNameMessages(args[0], plugin.language.getSettingString("afk_reason"))), player);
							}
							if (message != null) {
								JUtility.sendMessagePlaceholder(sender, JUtility.updateMessagePlaceholders("message", message, JUtility.updatePlayerNameMessages(args[0], plugin.language.getSettingString("afk_reason"))), player);
							}
						}
					}
				}
				else {
					return false; 
				}
			}
			else {
				JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_permission"), null);
			}
		}
		else if (command.getName().equalsIgnoreCase("afkkickall")) {
			if (args.length == 0) {
				if (sender.hasPermission("justafk.kickall")) {
					JUtility.kickAllAwayPlayers(false); 
					JUtility.sendMessagePlaceholder(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("mass_kicker")), null);
				}
				else {
					JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_permission"), null);
				}
			}
			else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("force")) {
					if (sender.hasPermission("justafk.kickall.force")) {
						JUtility.kickAllAwayPlayers(true); 
						JUtility.sendMessagePlaceholder(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("mass_kicker")), null);
					}
					else {
						JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_permission"), null);
					}
				}
				else {
					return false; 
				}
			}
			else {
				return false; 
			}
		}
		else if (command.getName().equalsIgnoreCase("afkplayer")) {
			OfflinePlayer player = null; 
			String parameter = ""; 
			String value = ""; 
			boolean self = false; 
			if (args.length == 3) {
				@SuppressWarnings("deprecation")
				OfflinePlayer rawPlayer = Bukkit.getOfflinePlayer(args[0]); 
				if (rawPlayer == null) {
					JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_player"), null);
					return true; 
				}
				else if (rawPlayer.getUniqueId() == null) { 
					JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_player"), null);
					return true;
				}
				else {
					player = rawPlayer; 
					if (player.getName().equalsIgnoreCase(sender.getName())) {
						self = true; 
					}
				}
				parameter = args[1].toLowerCase(); 
				value = args[2].toLowerCase(); 
			}
			else if (args.length == 2) {
				if (sender instanceof Player) {
					player = (Player) sender; 
					self = true; 
				}
				else {
					JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("player_command"), null);
					return true; 
				}
				parameter = args[0].toLowerCase(); 
				value = args[1].toLowerCase(); 
			}
			else {
				return false; 
			}
			if (args[1].equalsIgnoreCase("afkexempt") || args[1].equalsIgnoreCase("seehidden") || args[1].equalsIgnoreCase("kickexempt") || args[1].equalsIgnoreCase("lightningexempt")) {
				if ((sender.hasPermission("justafk.player." + parameter + ".others")) || (sender.hasPermission("justafk.player." + parameter) && (self == true))) {
					JUtility.setPlayerValue(sender, player, parameter, value, self);
				}
				else {
					JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_permission"), null);
				}
			}
			else {
				return false; 
			}
		}
		else {
			return false; 
		}
		return true; 
	}
}
