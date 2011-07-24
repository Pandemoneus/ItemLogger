package com.pandemoneus.itemLogger;

import java.io.File;
import java.net.MalformedURLException;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.alta189.sqlLibrary.MySQL.mysqlCore;
import com.alta189.sqlLibrary.SQLite.sqlCore;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * The ItemLogger plugin.
 * 
 * Makes the server log dropped items on player deaths into a file.
 * 
 * @author Pandemoneus
 * 
 */
public final class ItemLogger extends JavaPlugin {
	/**
	 * Plugin related stuff
	 */
	private final ILCommands cmdExecutor = new ILCommands(this);
	private Config config = new Config(this);
	private final ILEntityListener entityListener = new ILEntityListener(this);
	private PermissionHandler permissionsHandler;
	private boolean permissionsFound = false;

	private static String version;
	private static final String PLUGIN_NAME = "ItemLogger";
	
	/**
	 * SQL stuff
	 * Credits to alta189
	 */
	public File pFolder = new File("plugins" + File.separator + PLUGIN_NAME); // Folder to store plugin settings file and database
	
	public mysqlCore manageMySQL; // MySQL handler
	public sqlCore manageSQLite; // SQLite handler
	
	public boolean MySQL = false;
	public boolean useFlatFile = false;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDisable() {
		Log.info(PLUGIN_NAME + " disabled");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = getDescription();
		version = pdfFile.getVersion();
		
		Log.info(PLUGIN_NAME + " v" + version + " enabled");

		getCommand("itemlogger").setExecutor(cmdExecutor);
		getCommand("il").setExecutor(cmdExecutor);
		setupPermissions();
		config.loadConfig();

		final PluginManager pm = getServer().getPluginManager();

		pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener,
				Priority.Lowest, this);
		
		MySQL = config.useMySQL;
		useFlatFile = config.useFlatFile;
		
		if (!useFlatFile) {
			if (MySQL) {
				// Declare MySQL Handler
				manageMySQL = new mysqlCore(Log.getLogger(), Log.getPrefix(), config.dbHost, config.dbDatabase, config.dbUser, config.dbPass);
				
				// Initialize MySQL Handler
				manageMySQL.initialize();
				
				try {
					if (manageMySQL.checkConnection()) {
						if (!manageMySQL.checkTable("logs")) {
							String query = "CREATE TABLE logs (time VARCHAR(255), player VARCHAR(255), location VARCHAR(255), death_cause VARCHAR(255), nearby_players TEXT, items_dropped TEXT);";
							manageMySQL.createTable(query);
						}
					} else {
						Log.severe("MySQL connection failed");
						Log.severe("Check your MySQL settings or disable it");
						MySQL = false;
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
			if (!MySQL) {	
				// Declare SQLite handler
				manageSQLite = new sqlCore(Log.getLogger(), Log.getPrefix(), PLUGIN_NAME, pFolder.getPath());
				
				// Initialize SQLite handler
				manageSQLite.initialize();
				
				// Check if the table exists, if it doesn't create it
				if (!manageSQLite.checkTable("logs")) {
					String query = "CREATE TABLE logs (time VARCHAR(255), player VARCHAR(255), location VARCHAR(255), death_cause VARCHAR(255), nearby_players TEXT, items_dropped TEXT);";
					manageSQLite.createTable(query);
				}	
			}
		}
	}

	/**
	 * Returns the version of the plugin.
	 * 
	 * @return the version of the plugin
	 */
	public static String getVersion() {
		return version;
	}

	/**
	 * Returns the name of the plugin.
	 * 
	 * @return the name of the plugin
	 */
	public static String getPluginName() {
		return PLUGIN_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getPluginName();
	}

	/**
	 * Returns whether the permissions plugin could be found.
	 * 
	 * @return true if permissions plugin could be found, otherwise false
	 */
	public boolean getPermissionsFound() {
		return permissionsFound;
	}

	/**
	 * Returns the permissionsHandler of this plugin if it exists.
	 * 
	 * @return the permissionsHandler of this plugin if it exists, otherwise
	 *         null
	 */
	public PermissionHandler getPermissionsHandler() {
		PermissionHandler ph = null;

		if (getPermissionsFound()) {
			ph = permissionsHandler;
		}

		return ph;
	}

	private void setupPermissions() {
		if (permissionsHandler != null) {
			return;
		}

		Plugin permissionsPlugin = getServer().getPluginManager().getPlugin(
				"Permissions");

		if (permissionsPlugin == null) {
			Log.warning("Permissions not detected, using normal command structure.");
			return;
		}

		permissionsFound = true;
		permissionsHandler = ((Permissions) permissionsPlugin).getHandler();
	}
}
