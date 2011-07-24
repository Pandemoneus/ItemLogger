package com.pandemoneus.itemLogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;


/**
 * Utility class for ItemLogger.
 * 
 * @author Pandemoneus
 * 
 */
public final class ILUtil {
	private static HashMap<String, File> map = new HashMap<String, File>();
	private static final String PATH = "plugins" + File.separator
			+ ItemLogger.getPluginName() + File.separator + "Logs"
			+ File.separator;

	private ILUtil() {

	}

	/**
	 * Checks whether an item contains additional data.
	 * 
	 * @param typeID
	 *            item typeID
	 * @return true if item contains additional data, otherwise false
	 */
	public static boolean isItemWithData(int typeID) {
		return typeID == 6 || typeID == 17 || typeID == 18 || typeID == 31
				|| typeID == 35 || typeID == 43 || typeID == 44
				|| typeID == 351;
	}

	/**
	 * Formats a list of ItemStacks.
	 * 
	 * The format will be as following: ITEM NAME x AMOUNT (ID=TYPEID:DATA)
	 * Also sorts that list in alphabetical order.
	 * 
	 * @param list
	 *            the list of ItemStacks
	 * @return a sorted, formatted list of ItemStacks
	 */
	public static String[] formatItemStackList(List<ItemStack> list) {
		if (list == null) {
			return new String[0];
		}

		int n = list.size();
		int i = 0;
		String[] temp = new String[n];

		int typeID;
		String formatted;

		for (ItemStack is : list) {
			typeID = is.getTypeId();

			if (isItemWithData(typeID)) {
				String stackString = is.toString();
				String dataString = is.getData().toString();

				// ItemStack name
				formatted = dataString
						.substring(0, dataString.lastIndexOf("("));
				// + amount
				formatted += " x "
						+ stackString.replaceAll("[^0-9]", " ").trim();
				// + ID
				formatted += " (ID="
						+ typeID
						+ ":"
						+ dataString.substring(dataString.lastIndexOf("(") + 1,
								dataString.lastIndexOf(")")) + ")";

			} else {
				String stackString = is.toString();

				// ItemStack name + amount
				formatted = stackString.substring(
						stackString.lastIndexOf("{") + 1,
						stackString.lastIndexOf("}"));
				// + ID
				formatted += " (ID=" + typeID + ")";
			}

			temp[i] = String.valueOf(formatted);
			i++;
		}

		Arrays.sort(temp);

		return temp;
	}

	/**
	 * Writes a string array to a file with the passed player's name.
	 * 
	 * @param player
	 *            the player
	 * @param lastDamageCause
	 *            the damage that caused the player to die
	 * @param s
	 *            the string array to write
	 */
	public static void writeToFile(Player player, DamageCause lastDamageCause, ArrayList<Player> nearbyPlayers, String[] s) {
		if (player == null || s == null || lastDamageCause == null || nearbyPlayers == null) {
			return;
		}

		File temp = new File(PATH);
		temp.mkdirs();

		loadFiles();

		String playerName = player.getName().toLowerCase() + ".txt";
		int xPos = player.getLocation().getBlockX();
		int yPos = player.getLocation().getBlockY();
		int zPos = player.getLocation().getBlockZ();
		
		File f;

		if (map.containsKey(playerName)) {
			f = map.get(playerName);
		} else {
			f = new File(PATH + playerName);
		}

		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(f, true)));

			DateFormat formatter;
			Date date;

			formatter = new SimpleDateFormat("dd-MMM-yy, KK:mm:ss a");
			date = new Date();
			String dateRep = formatter.format(date);

			out.println(dateRep);
			out.println("Died because of: "
					+ formatDamageCause(lastDamageCause));
			out.println("At: X=" + xPos + ", Z=" + zPos + ", Y=" + yPos);
			out.print("Nearby players:");
			if (nearbyPlayers.isEmpty()) {
				out.print(" --None--");
			} else {
				for (Player p : nearbyPlayers) {
					out.print(" " + p.getName());
				}
			}
			out.println();
			out.println("----------------------");

			for (String str : s) {
				out.println(str);
			}

			out.println();

			out.flush();
			out.close();
		} catch (IOException e) {
			Log.severe("Failed writing log files for "
					+ ItemLogger.getPluginName());
			e.printStackTrace();
		}
	}

	private static void loadFiles() {
		File temp = new File(PATH);

		File[] fileList = temp.listFiles();

		if (fileList == null) {
			return;
		}

		for (File f : fileList) {
			map.put(f.getName(), f);
		}
	}

	/**
	 * Deletes all files in the log directory.
	 */
	public static void clearDirectory() {
		File temp = new File(PATH);

		File[] fileList = temp.listFiles();

		if (fileList == null) {
			return;
		}

		for (File f : fileList) {
			f.delete();
		}

		map.clear();
	}

	/**
	 * Deletes the log of a specific player.
	 * 
	 * This method does not do anything if the player had no log yet.
	 * 
	 * @param playerName
	 *            the name of the player
	 * @return true if successful, otherwise false
	 */
	public static boolean deletePlayerLog(String playerName) {
		if (playerName == null || playerName.equals("\\s+")) {
			return false;
		}

		loadFiles();
		String tmp = playerName.toLowerCase() + ".txt";

		if (map.containsKey(tmp)) {
			File f = map.get(tmp);
			f.delete();
			map.remove(tmp);

			return true;
		} else {
			return false;
		}
	}

	public static String formatDamageCause(DamageCause lastDamageCause) {
		if (lastDamageCause == null) {
			return "Unknown";
		}

		String cause = "Unknown";

		switch (lastDamageCause) {
		case BLOCK_EXPLOSION:
			cause = "TNT";
			break;
		case CONTACT:
			cause = "Cactus/Spikes";
			break;
		case DROWNING:
			cause = "Drowning";
			break;
		case ENTITY_ATTACK:
			cause = "Player/Mob attack";
			break;
		case ENTITY_EXPLOSION:
			cause = "TNT/Creeper/Ghast Fireball explosion";
			break;
		case FALL:
			cause = "Falling";
			break;
		case FIRE:
			cause = "Burning";
			break;
		case FIRE_TICK:
			cause = "Burning";
			break;
		case LAVA:
			cause = "Lava";
			break;
		case LIGHTNING:
			cause = "Struck by lightning";
			break;
		case SUFFOCATION:
			cause = "Suffocation caused by a block;";
			break;
		case VOID:
			cause = "Falling into endless Void";
			break;
		default:
			cause = "Unknown";
			break;
		}

		return cause;
	}
}
