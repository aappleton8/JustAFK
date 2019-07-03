package net.alexben.JustAFK;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class JustAFK extends JavaPlugin 
{
	public JConfig options; 
	public JConfig language; 
	public JConfig players; 
	private JListener jl; 
	
	private Boolean placeholderAPIExists = false; 
	private Boolean essentialsXExists = false; 
	
	public Boolean getPlaceholderAPIExists() {
		return placeholderAPIExists; 
	}
	
	public Boolean essentialsXExists() {
		return essentialsXExists; 
	}
	
	@Override 
	public void onLoad() {
		options = new JConfig(this, "config.yml", "config.yml"); 
		language = new JConfig(this, "localisation.yml", "localisation.yml"); 
		players = new JConfig(this, "players.yml", "players.yml"); 
		jl = new JListener(this); 
	}

	@Override
	public void onEnable() {
		// Initialise the scheduler, the configuration and the utilities 
		JShowHidePlayers.initialise(this); 
		JUtility.initialise(this);
		JScheduler.initialise(this);
		JConfig.initialise(this); 
		
		// Check for CommandBook
		if (Bukkit.getPluginManager().getPlugin("CommandBook") != null)
		{
			JUtility.log("warning", "CommandBook has been detected.");
			JUtility.log("warning", "Please ensure that the CommandBook AFK component has been disabled.");
			JUtility.log("warning", "If this hasn't been done, JustAFK will not work.");
		}
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			JUtility.log("info", "PlaceholderAPI detected"); 
			placeholderAPIExists = true; 
			new AddPlaceholders(this).register(); 
		}
		if (Bukkit.getPluginManager().getPlugin("EssentialsX") != null) {
			JUtility.log("info", "EssentialsX detected");
			essentialsXExists = true; 
		}

		// Register and load the commands
		JCommands commandHandler = new JCommands(this); 
		getCommand("afk").setExecutor(commandHandler);
		getCommand("justafk").setExecutor(commandHandler);
		getCommand("whosafk").setExecutor(commandHandler);
		getCommand("setafk").setExecutor(commandHandler);
		getCommand("afkhelp").setExecutor(commandHandler); 
		getCommand("isafk").setExecutor(commandHandler);
		getCommand("afkconfig").setExecutor(new JConfigChange(this));
		getCommand("afkkickall").setExecutor(commandHandler);
		getCommand("afkplayer").setExecutor(commandHandler);
		
		// Register the listeners 
		PluginManager pm = getServer().getPluginManager(); 
		pm.registerEvents(jl, this); 

		// Log that JustAFK successfully loaded
		String enableMessage = "The 'enable_message' field is missing from localisation.yml "; 
		if (language.getSettingString("enable_message") == null) {
			JUtility.consoleMsgPlaceholder(ChatColor.RED + enableMessage, null); 
		}
		else {
			JUtility.consoleMsgPlaceholder(JUtility.updatePluginVersionMessages(language.getSettingString("enable_message")), null); 
		}
	}
	
	@Override
	public void onDisable() {
		JScheduler.stopThreads();

		JUtility.consoleMsgPlaceholder("JustAFK has been disabled!", null);
	}
	
	public String getPluginName(Boolean format, Boolean colour) {
		if ((colour == true) && (format == false)) {
			return ChatColor.GREEN + getDescription().getName() + ChatColor.RESET + " "; 
		}
		else if ((colour == false) && (format == true)) {
			return "[" + getDescription().getName() + "] "; 
		}
		else if ((colour == true) && (format == true)) {
			return ChatColor.WHITE + "[" + ChatColor.GREEN + getDescription().getName() + ChatColor.WHITE + "] " + ChatColor.RESET; 
		}
		else {
			return getDescription().getName(); 
		}
	}
}
