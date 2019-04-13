package net.alexben.JustAFK;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class JustAFK extends JavaPlugin 
{
	public JConfig options; 
	public JConfig language; 
	private JListener jl; 
	
	@Override 
	public void onLoad()
	{
		options = new JConfig(this, "config.yml", "config.yml"); 
		language = new JConfig(this, "localisation.yml", "localisation.yml"); 
		jl = new JListener(this); 
		Bukkit.getServer().getConsoleSender().sendMessage(language.getSettingString("load_message").replaceAll("\\{plugin\\}", getDescription().getName()).replaceAll("\\{version\\}", getDescription().getVersion())); 
	}

	@Override
	public void onEnable()
	{
		// Initialise the scheduler and the utilities 
		JUtility.initialize(this);
		JScheduler.initialize(this);

		// Check for CommandBook
		if(Bukkit.getPluginManager().getPlugin("CommandBook") != null)
		{
			JUtility.log("warning", "CommandBook has been detected.");
			JUtility.log("warning", "Please ensure that the CommandBook AFK component has been disabled.");
			JUtility.log("warning", "If this hasn't been done, JustAFK will not work.");
		}

		// Register and load commands
		JCommands commandHandler = new JCommands(this); 
		getCommand("afk").setExecutor(commandHandler);
		getCommand("justafk").setExecutor(commandHandler);
		getCommand("whosafk").setExecutor(commandHandler);
		getCommand("setafk").setExecutor(commandHandler);
		getCommand("afkhelp").setExecutor(commandHandler); 
		
		// Register the listener 
		PluginManager pm = getServer().getPluginManager(); 
		pm.registerEvents(jl, this); 

		// Register all currently online players
		for(Player player : getServer().getOnlinePlayers())
		{
			Bukkit.getServer().getConsoleSender().sendMessage("[JustAFK] Looping through online players");
			JUtility.saveData(player, "isafk", false);
			JUtility.saveData(player, "iscertain", true);
			JUtility.saveData(player, "lastactive", System.currentTimeMillis());

			if(options.getSettingBoolean("hideawayplayers"))
			{
				for(Player awayPlayer : JUtility.getAwayPlayers(true))
				{
					player.hidePlayer(this, awayPlayer);
				}
			}
		}

		// Log that JustAFK successfully loaded
		//JUtility.consoleMsg(language.getSettingString("enable_message").replaceAll("\\{plugin\\}", getDescription().getName()).replaceAll("\\{version\\}", getDescription().getVersion()));;
	}

	@Override
	public void onDisable()
	{
		JScheduler.stopThreads();

		JUtility.log("info", "JustAFK has been disabled!");
	}

}
