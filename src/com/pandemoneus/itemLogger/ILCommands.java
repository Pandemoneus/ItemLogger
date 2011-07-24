package com.pandemoneus.itemLogger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nijiko.permissions.PermissionHandler;

/**
 * Command class. Available commands are:
 * il
 * il clearlogs
 * il deletelog [player]
 * 
 * @author Pandemoneus
 * 
 */
public final class ILCommands implements CommandExecutor {

	private ItemLogger plugin;

	/**
	 * Associates this object with a plugin
	 * 
	 * @param plugin
	 *            the plugin
	 */
	public ILCommands(ItemLogger plugin) {
		this.plugin = plugin;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (args != null) {
			if (sender instanceof Player) {
				if (plugin.getPermissionsFound()) {
					usePermissionsStructure((Player) sender, cmd, commandLabel,
							args);
				} else {
					useNormalStructure((Player) sender, cmd, commandLabel, args);
				}
			} else {
				sender.sendMessage(ChatColor.RED
						+ "Sorry, you are not a player!");
			}
		}

		return true;
	}

	private void usePermissionsStructure(Player sender, Command cmd,
			String commandLabel, String[] args) {
		PermissionHandler ph = plugin.getPermissionsHandler();

		if (args.length == 0) {
			// show help
			if (ph.has(sender, "itemlogger.help")) {
				showHelp(sender);
			} else {
				sender.sendMessage(ChatColor.RED
						+ "You are not authorized to use this command.");
			}
		} else if (args.length == 1) {
			// commands with 0 arguments
			String command = args[0];

			// clear logs
			if (command.equalsIgnoreCase("clearlogs")
					|| command.equalsIgnoreCase("cl")) {
				if (ph.has(sender, "itemlogger.log.clearlogs")) {
					clearLogs(sender);
				} else {
					sender.sendMessage(ChatColor.RED
							+ "You are not authorized to use this command.");
				}
			}
		} else if (args.length == 2) {
			// commands with 1 argument
			String command = args[0];
			String param1 = args[1];

			// delete log of [player]
			if (command.equalsIgnoreCase("deletelog")
					|| command.equalsIgnoreCase("dl")) {
				if (ph.has(sender, "itemlogger.log.deletelog")) {
					deleteLogOfPlayer(sender, param1);
				} else {
					sender.sendMessage(ChatColor.RED
							+ "You are not authorized to use this command.");
				}
			}
		}
	}

	private void useNormalStructure(Player sender, Command cmd,
			String commandLabel, String[] args) {
		if (sender.isOp()) {
			if (args.length == 0) {
				// show help
				showHelp(sender);
			} else if (args.length == 1) {
				// commands with 0 arguments
				String command = args[0];

				// clear logs
				if (command.equalsIgnoreCase("clearlogs")
						|| command.equalsIgnoreCase("cl")) {
					clearLogs(sender);
				}
			} else if (args.length == 2) {
				// commands with 1 argument
				String command = args[0];
				String param1 = args[1];

				// delete log of [player]
				if (command.equalsIgnoreCase("deletelog")
						|| command.equalsIgnoreCase("dl")) {
					deleteLogOfPlayer(sender, param1);
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED
					+ "You are not authorized to use this command.");
		}
	}

	private void showHelp(Player sender) {
		sender.sendMessage(ChatColor.YELLOW + "Available commands:");
		sender.sendMessage(ChatColor.GOLD
				+ "/il clearlogs - delete all saved logs");
		sender.sendMessage(ChatColor.GOLD
				+ "/il deletelog [player] - delete the log of [player]");
	}

	private void clearLogs(Player sender) {
		Log.info("'" + sender.getName() + "' requested clearing of all logs");

		ILUtil.clearDirectory();

		Log.info("Done...");
		sender.sendMessage(ChatColor.GREEN + "All logs deleted!");
	}

	private void deleteLogOfPlayer(Player sender, String player) {
		Log.info("'" + sender.getName() + "' requested to delete the log of '"
				+ player + "'");

		if (ILUtil.deletePlayerLog(player)) {
			sender.sendMessage(ChatColor.GREEN + "Log of '" + player
					+ "' deleted!");
			Log.info("Done...");
		} else {
			sender.sendMessage(ChatColor.RED + "Log of '" + player
					+ "' does not exist! Is the name spelt correctly?");
			Log.info("Failed, log of '" + player + "' does not exist");
		}
	}
}
