package net.alexben.JustAFK;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class JConfig extends YamlFilesBase
{
	
	private final String configPermission = "justafk.config.seemessages"; 
	
	public JConfig(JustAFK instance, String outFileName, String inFileName) {
		super(instance, instance.getLogger(), outFileName, inFileName);
	}
	
	@Override
	protected void saveNewFile() {
		save(); 
		logger.info("Configuration file " + theOutFile.getName() + " loaded "); 
		configList.put(theOutFile.getName(), this); 
	}

	@Override
	public void fullReload() {
		reload(); 
		Bukkit.broadcast(ChatColor.GREEN + plugin.getDescription().getName() + " : " + theOutFile.getName() + " configuration reloaded ", configPermission); 
	}

	@Override
	public void fullSave() {
		Boolean saved = save(); 
		if (saved == true) {
			Bukkit.broadcast(ChatColor.GREEN + plugin.getDescription().getName() + " : " + theOutFile.getName() + " configuration saved ", configPermission); 
		}
		else {
			Bukkit.broadcast(ChatColor.RED + plugin.getDescription().getName() + " : " + theOutFile.getName() + " configuration could not be saved ", configPermission); 
		}
	}

	public int getSettingInt(String id)
	{
		if (this.configuration.isInt(id))
		{
			return this.configuration.getInt(id);
		}
		else return -1;
	}

	public String getSettingString(String id)
	{
		/*
		if (this.configuration.isString(id))
		{
			return this.configuration.getString(id);
		}
		else return null;
		*/
		return this.configuration.getString(id);
	}

	public boolean getSettingBoolean(String id)
	{
		return !this.configuration.isBoolean(id) || this.configuration.getBoolean(id);
	}

	public double getSettingDouble(String id)
	{
		if (this.configuration.isDouble(id))
		{
			return this.configuration.getDouble(id);
		}
		else return -1;
	}

	public List<String> getSettingListString(String id)
	{
		if (this.configuration.isList(id))
		{
			return this.configuration.getStringList(id); 
		}
		else return new ArrayList<String>();
	}
	
	public Set<String> getSettingConfigurationSectionKeys(String id, Boolean recursive)
	{
		if (this.configuration.contains(id)) {
			return this.configuration.getConfigurationSection(id).getKeys(recursive); 
		}
		else return new HashSet<String>(); 
	}

}

