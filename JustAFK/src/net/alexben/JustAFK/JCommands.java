package net.alexben.JustAFK;

import java.util.ArrayList;

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
		JUtility.sendMessage(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_header")));
		JUtility.sendMessage(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_justafk")));
		JUtility.sendMessage(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_afkhelp"))); 
		JUtility.sendMessage(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_afk")));
		JUtility.sendMessage(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_whosafk"))); 
		JUtility.sendMessage(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_setafk")));
		JUtility.sendMessage(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_isafk"))); 
		JUtility.sendMessage(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_afkconfig")));
		JUtility.sendMessage(sender, JUtility.updatePluginVersionMessages(plugin.language.getSettingString("help_afkkick")));
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
			JUtility.sendMessage(sender, plugin.language.getSettingString("use_afkhelp")); 
		}
		else if (command.getName().equalsIgnoreCase("afk")) {
			if (sender instanceof Player) {
				if (sender.hasPermission("justafk.afk")) {
					Player player = (Player) sender; 
					if (JUtility.isAway(player)) {
						JUtility.setAway(player, false, true, "player-command");
						JUtility.sendMessage(player, ChatColor.AQUA + plugin.language.getSettingString("private_return"));
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
						JUtility.sendMessage(player, ChatColor.AQUA + plugin.language.getSettingString("private_away"));
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
						if (args.length > 1) {
							String message = ""; 
							for (int i = 1; i < args.length; i++) {
								message += args[i]; 
							}
							JUtility.saveData(player, JUtility.MessageTypes.AFKMESSAGE, message);
						}
						if (!JUtility.isAway(player)) {
							JUtility.setAway(player, true, true, "other-command");
							JUtility.sendMessage(player, JUtility.updatePlayerNameMessages(sender.getName(), plugin.language.getSettingString("setafk_away_private")));
						}
						else {
							JUtility.setAway(player, false, true, "other-command");
							JUtility.sendMessage(player, JUtility.updatePlayerNameMessages(sender.getName(), plugin.language.getSettingString("setafk_return_private")));
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
					JUtility.sendMessage(sender, plugin.language.getSettingString("nobody_away"));
				}
				else {
					ArrayList<String> playerNames = new ArrayList<String>();
					for (Player item : JUtility.getAwayPlayers(true)) {
						playerNames.add(item.getName());
					}
					JUtility.sendMessage(sender, JUtility.updateMessagePlaceholders("names", StringUtils.join(playerNames, ", "), plugin.language.getSettingString("currently_away")));
				}
				return true;
			}
			else {
				JUtility.sendMessage(sender, plugin.language.getSettingString("no_permission"));
			}
		}
		else if (command.getName().equalsIgnoreCase("isafk")) {
			if (sender.hasPermission("justafk.isafk")) {
				if (args.length == 1) {
					@SuppressWarnings("deprecation")
					Player player = Bukkit.getPlayer(args[0]); 
					if (player == null) {
						JUtility.sendMessage(sender, plugin.language.getSettingString("no_player")); 
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
							JUtility.sendMessage(sender, JUtility.updatePlayerNameMessages(args[0], plugin.language.getSettingString("not_afk"))); 
						}
						else {
							JUtility.sendMessage(sender, JUtility.updatePlayerNameMessages(args[0], plugin.language.getSettingString("is_afk")));
							if (reason != null) {
								JUtility.sendMessage(sender, JUtility.updateMessagePlaceholders("reason", reason, JUtility.updatePlayerNameMessages(args[0], plugin.language.getSettingString("afk_reason"))));
							}
							if (message != null) {
								JUtility.sendMessage(sender, JUtility.updateMessagePlaceholders("message", message, JUtility.updatePlayerNameMessages(args[0], plugin.language.getSettingString("afk_reason"))));
							}
						}
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
		else if (command.getName().equalsIgnoreCase("afkconfig")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("save")) {
					if (sender.hasPermission("justafk.config.save")) {
						plugin.options.fullSave();
						plugin.language.fullSave();
					}
					else {
						JUtility.sendMessage(sender, plugin.language.getSettingString("no_permission"));
					}
				}
				else if (args[0].equalsIgnoreCase("reload")) {
					if (sender.hasPermission("justafk.config.reload")) {
						plugin.options.fullReload();
						plugin.language.fullReload();
					}
					else {
						JUtility.sendMessage(sender, plugin.language.getSettingString("no_permission"));
					}
				}
				else {
					return false; 
				}
			}
			else if (args.length == 3) {
				if (args[0].equalsIgnoreCase("get")) {
					if (sender.hasPermission("justafk.config.get")) {
						if (args[1].equalsIgnoreCase("config") || args[1].equalsIgnoreCase("options")) {
							JUtility.sendMessage(sender, "The " + args[2] + " field in the " + plugin.getName() + " " + args[1] + " file is " + plugin.options.getSettingString(args[2]) + " "); 
						}
						else if (args[1].equalsIgnoreCase("language") || args[1].equalsIgnoreCase("localisation")) {
							JUtility.sendMessage(sender, "The " + args[2] + " field in the " + plugin.getName() + " " + args[1] + " file is " + plugin.language.getSettingString(args[2]) + " "); 
						}
						else {
							return false; 
						}
					}
					else {
						JUtility.sendMessage(sender, plugin.language.getSettingString("no_permission"));
					}
				}
				else {
					return false; 
				}
			}
			else if (args.length == 4) {
				if (args[0].equalsIgnoreCase("set")) {
					if (sender.hasPermission("justafk.config.set")) {
						if (args[1].equalsIgnoreCase("config") || args[1].equalsIgnoreCase("options")) {
							if (plugin.options.getRootFileKeys().contains(args[2].toLowerCase())) {
								if (args[2].equalsIgnoreCase("movementcheckfreq") || args[2].equalsIgnoreCase("kicktime") || args[2].equalsIgnoreCase("inactivetime")) {
									try {
										int val = Integer.parseInt(args[3]); 
										plugin.options.setSettingAnyNoCheck(args[2].toLowerCase(), val); 
										JUtility.sendMessage(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updateMessagePlaceholders("val", args[3], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_field_set"))))); 
									}
									catch (NumberFormatException e) {
										JUtility.sendMessage(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updateMessagePlaceholders("val", args[3], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_field_notset"))))); 
									}
								}
								else if (args[2].equalsIgnoreCase("colourchar")) {
									if (args[2].length() == 1) {
										plugin.options.setSettingAnyNoCheck(args[2].toLowerCase(), args[3]);
										JUtility.sendMessage(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updateMessagePlaceholders("val", args[3], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_field_set"))))); 
									}
									else {
										JUtility.sendMessage(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updateMessagePlaceholders("val", args[3], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_field_notset"))))); 
									}
								}
								else {
									if (args[3].equalsIgnoreCase("true")) {
										plugin.options.setSettingAnyNoCheck(args[2].toLowerCase(), true);
										JUtility.sendMessage(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updateMessagePlaceholders("val", args[3], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_field_set"))))); 
									}
									else if (args[3].equalsIgnoreCase("false")) {
										plugin.options.setSettingAnyNoCheck(args[2].toLowerCase(), false);
										JUtility.sendMessage(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updateMessagePlaceholders("val", args[3], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_field_set"))))); 
									}
									else {
										JUtility.sendMessage(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updateMessagePlaceholders("val", args[3], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_field_notset"))))); 
									}
								}
							}
							else {
								JUtility.sendMessage(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_not_field"))));
							}
						}
						else if (args[1].equalsIgnoreCase("language") || args[1].equalsIgnoreCase("localisation")) {
							if (plugin.language.getRootFileKeys().contains(args[2].toLowerCase())) {
								plugin.language.setSettingAnyNoCheck(args[2].toLowerCase(), args[3]);
								JUtility.sendMessage(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updateMessagePlaceholders("val", args[3], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_field_set"))))); 
							}
							else {
								JUtility.sendMessage(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_not_field"))));
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
				else {
					return false; 
				}
			}
			else {
				return false; 
			}
		}
		else if (command.getName().equals("afkkickall")) {
			if (args.length == 0) {
				if (sender.hasPermission("justafk.kickall")) {
					JUtility.kickAllAwayPlayers(false); 
				}
				else {
					JUtility.sendMessage(sender, plugin.language.getSettingString("no_permission"));
				}
			}
			else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("force")) {
					if (sender.hasPermission("justafk.kickall.force")) {
						JUtility.kickAllAwayPlayers(true); 
					}
					else {
						JUtility.sendMessage(sender, plugin.language.getSettingString("no_permission"));
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
		else {
			return false; 
		}
		return true; 
	}
}
