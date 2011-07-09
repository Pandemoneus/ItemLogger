package com.pandemoneus.itemLogger;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.pandemoneus.itemLogger.commands.ILCommands;
import com.pandemoneus.itemLogger.listeners.ILEntityListener;
import com.pandemoneus.itemLogger.logger.Log;

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
	private final ILEntityListener entityListener = new ILEntityListener();
	private PermissionHandler permissionsHandler;
	private boolean permissionsFound = false;

	private static final String VERSION = "1.01";
	private static final String PLUGIN_NAME = "ItemLogger";

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
		Log.info(PLUGIN_NAME + " v" + VERSION + " enabled");

		getCommand("itemlogger").setExecutor(cmdExecutor);
		getCommand("il").setExecutor(cmdExecutor);
		setupPermissions();

		PluginManager pm = getServer().getPluginManager();

		pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener,
				Priority.Lowest, this);
	}

	/**
	 * Returns the version of the plugin.
	 * 
	 * @return the version of the plugin
	 */
	public static String getVersion() {
		return VERSION;
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
