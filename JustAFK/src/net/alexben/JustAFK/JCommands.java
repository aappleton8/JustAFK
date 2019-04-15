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
	 * Handle Commands
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		Player player = (Player) sender;

		if(JUtility.hasPermissionOrOP(player, "justafk.basic"))
		{
			if(command.getName().equalsIgnoreCase("afk"))
			{
				if(JUtility.isAway(player))
				{
					JUtility.setAway(player, false, true);
					JUtility.sendMessage(player, ChatColor.AQUA + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("private_return")));

					return true;
				}

				// If they included an away message then set it BEFORE setting away.
				if(args.length > 0)
				{
					String msg = StringUtils.join(args, " ");

					JUtility.setAwayMessage(player, msg);
				}

				// Now set away status
				JUtility.setAway(player, true, true);

				// Send the messages.
				JUtility.sendMessage(player, ChatColor.AQUA + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("private_away")));

				return true;
			}
			else if(command.getName().equalsIgnoreCase("whosafk"))
			{
				if(JUtility.getAwayPlayers(true).isEmpty())
				{
					JUtility.sendMessage(player, StringEscapeUtils.unescapeJava(plugin.language.getSettingString("nobody_away")));
					return true;
				}

				ArrayList<String> playerNames = new ArrayList<String>();

				for(Player item : JUtility.getAwayPlayers(true))
				{
					playerNames.add(item.getName());
				}

				JUtility.sendMessage(player, ChatColor.AQUA + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("currently_away")) + " " + StringUtils.join(playerNames, ", "));

				return true;
			}
			else if(command.getName().equalsIgnoreCase("setafk") && JUtility.hasPermissionOrOP(player, "justafk.admin"))
			{
				@SuppressWarnings("deprecation")
				Player editing = Bukkit.getPlayer(args[0]);

				if(editing != null)
				{
					if(!JUtility.isAway(editing))
					{
						JUtility.setAway(editing, true, true);
						JUtility.sendMessage(editing, ChatColor.GRAY + "" + ChatColor.ITALIC + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("setafk_away_private").replace("{name}", player.getDisplayName())));
					}
					else
					{
						JUtility.setAway(editing, false, true);
						JUtility.sendMessage(editing, ChatColor.GRAY + "" + ChatColor.ITALIC + StringEscapeUtils.unescapeJava(plugin.language.getSettingString("setafk_return_private").replace("{name}", player.getDisplayName())));
					}

					return true;
				}
				else
				{
					player.sendMessage(ChatColor.RED + "/setafk <player>");
				}
			}
			else if(command.getName().equalsIgnoreCase("justafk"))
			{
				player.sendMessage(ChatColor.AQUA + "JustAFK" + ChatColor.GRAY + " is a plugin created for Bukkit intended for the use");
				player.sendMessage(ChatColor.GRAY + "of simple - yet powerful - away messages and other features");
				player.sendMessage(ChatColor.GRAY + "within Minecraft survival multiplayer.");
				player.sendMessage("");
				player.sendMessage(ChatColor.GRAY + "Author: " + ChatColor.AQUA + "_Alex");
				player.sendMessage(ChatColor.GRAY + "Source: " + ChatColor.AQUA + "http://github.com/alexbennett/Minecraft-JustAFK");

				return true;
			}
		}

		return false;
	}

}
