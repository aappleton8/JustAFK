package net.alexben.JustAFK;

import org.bukkit.Bukkit;

public class JScheduler
{
	// Define variables
	private static JustAFK plugin = null;

	public static void initialize(JustAFK instance)
	{
		plugin = instance;
		startThreads();
	}

	public static void startThreads()
	{
		// Setup threads for checking player movement
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				JUtility.checkActivity();
			}
		}, 0, plugin.options.getSettingInt("movementcheckfreq") * 20);
	}

	public static void stopThreads()
	{
		Bukkit.getServer().getScheduler().cancelTasks(plugin);
	}
}
