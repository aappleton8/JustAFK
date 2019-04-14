package net.alexben.JustAFK;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class YamlFilesBase {
	protected static JavaPlugin plugin; 
	protected static Logger logger;  
	
	protected static Map<String, YamlFilesBase> configList = new HashMap<>(); 
	
	public YamlConfiguration configuration; 
	protected final File theOutFile; 
	private final String theInFile; 
	
	public YamlFilesBase(JavaPlugin pluginInstance, Logger loggerInstance, String outFileName, String inFileName) {
		plugin = pluginInstance; 
		logger = loggerInstance; 
		if (plugin.getDataFolder().exists() == false) {
			plugin.getDataFolder().mkdir(); 
		}
		theOutFile = new File(plugin.getDataFolder(), outFileName); 
		theInFile = inFileName; 
		configuration = loadFiles(); 
	}
	protected abstract void saveNewFile(); 
	/* - Suggested content below 
	save(); 
	logger.info("Configuration file " + theOutFile.getName() + " loaded "); 
	configList.put(theOutFile.getName(), this); 
	 */
	
	public void reload() {
		reload(false); 
	}
	public void reload(Boolean logMessage) {
		configuration = loadAConfiguration(theOutFile); 
		if (logMessage) {
			logger.info(theOutFile.getName() + " has been reloaded "); 
		}
	}
	public Boolean save() {
		try {
			configuration.save(theOutFile);
			return true; 
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Unable to save file " + theOutFile, e); 
			return false; 
		} 
	}
	public abstract void fullReload(); 
	public abstract void fullSave(); 
	public int getNumFiles() {
		return configList.size(); 
	}
	
	public static YamlConfiguration loadAConfiguration(File file) {
		new YamlConfiguration(); 
		YamlConfiguration currentConfigurationFile = YamlConfiguration.loadConfiguration(file); 
		return currentConfigurationFile; 
	}
	private static void copy(InputStream src, OutputStream dst) throws IOException{
		byte[] bytes = new byte[2048]; 
		int transfer; 
		while ((transfer = src.read(bytes)) > 0) {
			dst.write(bytes, 0, transfer); 
		}
		dst.close(); 
		src.close(); 
	}
	private static String getFileHeader(InputStream src) throws IOException {
		String header = null; 
		String line; 
		StringBuilder sb = new StringBuilder(); 
		sb.append("\n"); 
		BufferedReader br = new BufferedReader(new InputStreamReader(src));
		while ((line = br.readLine()) != null){  
			sb.append(line); 
			sb.append("\n"); 
		}
		br.close(); 
		src.close(); 
		header = sb.toString(); 
		return header; 
	}
	
	private YamlConfiguration loadFiles() {
		YamlConfiguration theConfiguration = null; 
		logger.info("Attempting to load the configuration file " + theOutFile.getName()); 
		if (theOutFile.exists() != true) {
			try {
				copy(plugin.getResource("resources/" + theInFile), new FileOutputStream(theOutFile)); 
				logger.info("Configuration file " + theOutFile.getName() + " created "); 
			} catch (FileNotFoundException e) {
				logger.log(Level.SEVERE, "Unable to create a configuration file", e); 
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Unable to create a configuration file", e); 
			} 
			catch (NullPointerException e) {
				logger.log(Level.SEVERE, "Unable to create a configuration file", e); 
			}
			theConfiguration = loadAConfiguration(theOutFile); 
			fileHeaders(theConfiguration); 
		}
		else {
			theConfiguration =  loadAConfiguration(theOutFile); 
		}
		return theConfiguration; 
	}
	private void fileHeaders (YamlConfiguration theConfiguration) {
		FileConfigurationOptions options = theConfiguration.options(); 
		String theHeader = null; 
		try {
			theHeader = getFileHeader(plugin.getResource("resources/" + theOutFile.getName().split("\\.")[0] + "Header.txt")); 
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Unable to create a header file", e); 
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Unable to create a header file", e); 
		} catch (NullPointerException e) {
			logger.log(Level.SEVERE, "Unable to create a header file", e); 
		}
		options.header(theHeader); 
		options.copyHeader(true); 
	}
}
