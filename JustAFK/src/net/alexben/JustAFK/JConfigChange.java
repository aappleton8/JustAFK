package net.alexben.JustAFK;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.OfflinePlayer;

public class JConfigChange implements CommandExecutor {

	private static JustAFK plugin = null; 
	
	public JConfigChange(JustAFK instance) {
		plugin = instance; 
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (command.getName().equalsIgnoreCase("afkconfig")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("save")) {
					if (sender.hasPermission("justafk.config.save")) {
						plugin.options.fullSave();
						plugin.language.fullSave();
					}
					else {
						JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_permission"), null);
					}
				}
				else if (args[0].equalsIgnoreCase("reload")) {
					if (sender.hasPermission("justafk.config.reload")) {
						plugin.options.fullReload();
						plugin.language.fullReload();
					}
					else {
						JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_permission"), null);
					}
				}
				else {
					return false; 
				}
			}
			else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("list")) {
					if (sender.hasPermission("justafk.config.get")) {
						if (args[1].equalsIgnoreCase("config") || args[1].equalsIgnoreCase("options")) {
							JUtility.sendMessagePlaceholder(sender, plugin.options.getRootFileKeys().toString(), null); 
						}
						else if (args[1].equalsIgnoreCase("language") || args[1].equalsIgnoreCase("localisation")) {
							JUtility.sendMessagePlaceholder(sender, plugin.language.getRootFileKeys().toString(), null); 
						}
						else if (args[1].equalsIgnoreCase("players") || args[1].equalsIgnoreCase("player")) {
							JUtility.sendMessagePlaceholder(sender, plugin.players.configuration.getConfigurationSection("players").getKeys(false).toString(), null); 
						}
						else {
							return false; 
						}
					}
					else {
						JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_permission"), null);
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
							JUtility.sendMessagePlaceholder(sender, "The " + args[2] + " field in the " + plugin.getName() + " " + args[1] + " file is " + plugin.options.getSettingString(args[2]) + " ", null); 
						}
						else if (args[1].equalsIgnoreCase("language") || args[1].equalsIgnoreCase("localisation")) {
							JUtility.sendMessagePlaceholder(sender, "The " + args[2] + " field in the " + plugin.getName() + " " + args[1] + " file is " + plugin.language.getSettingString(args[2]) + " ", null); 
						}
						else if (args[1].equalsIgnoreCase("players") || args[1].equalsIgnoreCase("player")) {
							@SuppressWarnings("deprecation")
							OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]); 
							if (player == null) {
								JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_player"), null); 
								return true; 
							}
							else if (player.getUniqueId() == null) {
								JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_player"), null); 
								return true; 
							}
							JUtility.sendMessagePlaceholder(sender, "The " + args[2] + " player has the following information in the " + plugin.getName() + " " + args[1] + " file: " + plugin.players.configuration.getConfigurationSection("players." + player.getUniqueId()), null);
						}
						else {
							return false; 
						}
					}
					else {
						JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_permission"), null);
					}
				}
				else {
					return false; 
				}
			}
			else if (args.length == 4) {
				if (args[0].equalsIgnoreCase("set")) {
					if (args[1].equalsIgnoreCase("config") || args[1].equalsIgnoreCase("options")) {
						if (sender.hasPermission("justafk.config.set.config")) {
							if (plugin.options.getRootFileKeys().contains(args[2].toLowerCase())) {
								if (args[2].equalsIgnoreCase("movementcheckfreq") || args[2].equalsIgnoreCase("kicktime") || args[2].equalsIgnoreCase("inactivetime")) {
									try {
										int val = Integer.parseInt(args[3]); 
										plugin.options.setSettingAnyNoCheck(args[2].toLowerCase(), val); 
										JUtility.sendMessagePlaceholder(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updateMessagePlaceholders("val", args[3], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_field_set")))), null); 
									}
									catch (NumberFormatException e) {
										JUtility.sendMessagePlaceholder(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updateMessagePlaceholders("val", args[3], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_field_notset")))), null); 
									}
								}
								else if (args[2].equalsIgnoreCase("colourchar")) {
									if (args[2].length() == 1) {
										plugin.options.setSettingAnyNoCheck(args[2].toLowerCase(), args[3]);
										JUtility.sendMessagePlaceholder(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updateMessagePlaceholders("val", args[3], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_field_set")))), null); 
									}
									else {
										JUtility.sendMessagePlaceholder(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updateMessagePlaceholders("val", args[3], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_field_notset")))), null); 
									}
								}
								else {
									if (args[3].equalsIgnoreCase("true")) {
										plugin.options.setSettingAnyNoCheck(args[2].toLowerCase(), true);
										JUtility.sendMessagePlaceholder(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updateMessagePlaceholders("val", args[3], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_field_set")))), null); 
									}
									else if (args[3].equalsIgnoreCase("false")) {
										plugin.options.setSettingAnyNoCheck(args[2].toLowerCase(), false);
										JUtility.sendMessagePlaceholder(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updateMessagePlaceholders("val", args[3], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_field_set")))), null); 
									}
									else {
										JUtility.sendMessagePlaceholder(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updateMessagePlaceholders("val", args[3], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_field_notset")))), null); 
									}
								}
							}
							else {
								JUtility.sendMessagePlaceholder(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_not_field"))), null);
							}
						}
						else {
							JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_permission"), null);
						}
					}
					else if (args[1].equalsIgnoreCase("language") || args[1].equalsIgnoreCase("localisation")) {
						if (sender.hasPermission("justafk.config.set.localisation")) {
							if (plugin.language.getRootFileKeys().contains(args[2].toLowerCase())) {
								plugin.language.setSettingAnyNoCheck(args[2].toLowerCase(), args[3]);
								JUtility.sendMessagePlaceholder(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updateMessagePlaceholders("val", args[3], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_field_set")))), null); 
							}
							else {
								JUtility.sendMessagePlaceholder(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_not_field"))), null);
							}
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
			else if (args.length > 4) {
				if (args[0].equalsIgnoreCase("set")) {
					if (args[1].equalsIgnoreCase("language") || args[1].equalsIgnoreCase("localisation")) {
						if (sender.hasPermission("justafk.config.set.localisation")) {
							if (plugin.language.getRootFileKeys().contains(args[2].toLowerCase())) {
								String msg = args[3]; 
								for (int i = 4; i < args.length; i++) {
									msg += " "; 
									msg += args[i]; 
								}
								plugin.language.setSettingAnyNoCheck(args[2].toLowerCase(), msg);
								JUtility.sendMessagePlaceholder(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updateMessagePlaceholders("val", args[3], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_field_set")))), null); 
							}
							else {
								JUtility.sendMessagePlaceholder(sender, JUtility.updateMessagePlaceholders("conf", args[1], JUtility.updatePluginVersionMessages(plugin.language.getSettingString("conf_not_field"))), null);
							}
						}
						else {
							JUtility.sendMessagePlaceholder(sender, plugin.language.getSettingString("no_permission"), null);
						}
					}
					else if ((args[1].equalsIgnoreCase("players") || args[1].equalsIgnoreCase("localisation")) && (args.length == 5)) {
						if (sender.hasPermission("justafk.config.set.players") || sender.hasPermission("justafk.player.*")) {
							if (args[1].equalsIgnoreCase("afkexempt") || args[1].equalsIgnoreCase("seehidden") || args[1].equalsIgnoreCase("kickexempt") || args[1].equalsIgnoreCase("lightningexempt")) {
								String parameter = args[3].toLowerCase(); 
								@SuppressWarnings("deprecation")
								OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]); 
								JUtility.setPlayerValue(sender, player, parameter, args[4], sender.getName().equalsIgnoreCase(player.getName())); 
							}
							else {
								return false; 
							}
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
