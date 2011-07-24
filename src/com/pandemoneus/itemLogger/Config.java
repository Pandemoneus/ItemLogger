package com.pandemoneus.itemLogger;

import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.Plugin;
import org.bukkit.util.config.Configuration;

/**
 * The configuration file for the ItemLogger plugin, uses YML.
 * 
 * @author Pandemoneus
 * 
 */
public final class Config {

	private ItemLogger plugin;
	private static String pluginName;
	private static String pluginVersion;

	/**
	 * File handling
	 */
	private static String directory = "plugins" + File.separator
			+ ItemLogger.getPluginName() + File.separator;
	private File configFile = new File(directory + File.separator
			+ "config.yml");
	private Configuration bukkitConfig = new Configuration(configFile);

	/**
	 * Default settings
	 */
	public boolean useFlatFile = false;
	public boolean useMySQL = false;
	public String dbHost = "host";
	public String dbUser = "username";
	public String dbPass = "password";
	public String dbDatabase = "database";
	

	/**
	 * Associates this object with a plugin
	 * 
	 * @param plugin
	 *            the plugin
	 */
	public Config(ItemLogger plugin) {
		this.plugin = plugin;
		pluginName = ItemLogger.getPluginName();
	}

	/**
	 * Loads the configuration used by this plugin.
	 * 
	 * @return true if config loaded without errors
	 */
	public boolean loadConfig() {
		boolean isErrorFree = true;
		pluginVersion = ItemLogger.getVersion();

		new File(directory).mkdir();

		if (configFile.exists()) {
			bukkitConfig.load();
			if (bukkitConfig.getString("Version", "").equals(pluginVersion)) {
				// config file exists and is up to date
				Log.info(pluginName + " config file found, loading config...");
				loadData();
			} else {
				// config file exists but is outdated
				Log.info(pluginName
						+ " config file outdated, adding old data and creating new values. "
						+ "Make sure you change those!");
				loadData();
				writeDefault();
			}
		} else {
			// config file does not exist
			try {
				Log.info(pluginName
						+ " config file not found, creating new config file...");
				configFile.createNewFile();
				writeDefault();
			} catch (IOException ioe) {
				Log.severe("Could not create the config file for " + pluginName + "!");
				ioe.printStackTrace();
				isErrorFree = false;
			}
		}

		return isErrorFree;
	}

	private void loadData() {
		useFlatFile = bukkitConfig.getBoolean("Log.UseFlatFile", false);
		useMySQL = bukkitConfig.getBoolean("Log.MySQL.enabled", false);
		dbHost = bukkitConfig.getString("Log.MySQL.host", "host");
		dbUser = bukkitConfig.getString("Log.MySQL.username", "username");
		dbPass = bukkitConfig.getString("Log.MySQL.password", "password");
		dbDatabase = bukkitConfig.getString("Log.MySQL.database", "database");
	}

	private void writeDefault() {
		bukkitConfig.setHeader("### Learn more about how this config can be edited and changed to your preference on the forum page. ###");
		write("Version", pluginVersion);
		write("Log.UseFlatFile", useFlatFile);
		write("Log.MySQL.enabled", useMySQL);
		write("Log.MySQL.host", dbHost);
		write("Log.MySQL.username", dbUser);
		write("Log.MySQL.password", dbPass);
		write("Log.MySQL.database", dbDatabase);

		loadData();
	}

	/**
	 * Reads a string representing a long from the config file.
	 * 
	 * Returns '0' when an exception occurs.
	 * 
	 * @param key
	 *            the key
	 * @param def
	 *            default value
	 * @return the long specified in 'key' from the config file, '0' on errors
	 */
	@SuppressWarnings("unused")
	private long readLong(String key, String def) {
		bukkitConfig.load();

		// Bukkit Config has no getLong(..)-method, so we are using Strings
		String value = bukkitConfig.getString(key, def);

		long tmp = 0;

		try {
			tmp = Long.parseLong(value);
		} catch (NumberFormatException nfe) {
			Log.warning("Error parsing a long from the config file. Key=" + key);
			nfe.printStackTrace();
		}

		return tmp;
	}

	private void write(String key, Object o) {
		bukkitConfig.load();
		bukkitConfig.setProperty(key, o);
		bukkitConfig.save();
	}

	/**
	 * Returns the config file.
	 * 
	 * @return the config file
	 */
	public File getConfigFile() {
		return configFile;
	}

	/**
	 * Returns the associated plugin.
	 * 
	 * @return the associated plugin
	 */
	public Plugin getPlugin() {
		return plugin;
	}
}
