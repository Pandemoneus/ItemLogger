package com.pandemoneus.itemLogger;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.inventory.ItemStack;


/**
 * Entity Listener for the ItemLogger plugin. Triggers on entity deaths.
 * 
 * @author Pandemoneus
 * 
 */
public final class ILEntityListener extends EntityListener {
	
	private ItemLogger plugin;
	
	public ILEntityListener(ItemLogger plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Recognizes player deaths, their dropped items and writes the list of the
	 * dropped items to a file.
	 * 
	 * @param event
	 *            event information passed by Bukkit
	 */
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			// dummy cause
			DamageCause lastDamageCause = EntityDamageEvent.DamageCause.CUSTOM;

			if (player.getLastDamageCause() != null) {
				lastDamageCause = player.getLastDamageCause()
						.getCause();
			}
			
			List<Entity> nearbyEntities = player.getNearbyEntities(10.0, 10.0, 10.0);
			ArrayList<Player> nearbyPlayers = new ArrayList<Player>();
			
			for (Entity e : nearbyEntities) {
				if (e instanceof Player) {
					nearbyPlayers.add((Player) e);
				}
			}

			List<ItemStack> list = event.getDrops();

			if (plugin.useFlatFile) {
				ILUtil.writeToFile(player, lastDamageCause, nearbyPlayers, ILUtil.formatItemStackList(list));
			} else {
				writeToDatabase(player, lastDamageCause, nearbyPlayers, ILUtil.formatItemStackList(list));
			}
		}
	}
	
	public void writeToDatabase(Player player, DamageCause lastDamageCause, ArrayList<Player> nearbyPlayers, String[] s) {
		if (player == null || s == null || lastDamageCause == null || nearbyPlayers == null) {
			return;
		}
		
		String playerName = player.getName();
		int xPos = player.getLocation().getBlockX();
		int yPos = player.getLocation().getBlockY();
		int zPos = player.getLocation().getBlockZ();
		
		String nearbyPlayerNames = "";
		
		for (Player p : nearbyPlayers) {
			nearbyPlayerNames += p.getName() + ", ";
		}
		
		if (!nearbyPlayerNames.equals("")) {
			nearbyPlayerNames = nearbyPlayerNames.substring(0, nearbyPlayerNames.length() - 2);
		}
		
		String droppedItemsList = "";
		
		for (String str : s) {
			droppedItemsList += str + ", ";
		}
		
		if (!droppedItemsList.equals("")) {
			droppedItemsList = droppedItemsList.substring(0, droppedItemsList.length() - 2);
		}
		
		DateFormat formatter;
		Date date;

		formatter = new SimpleDateFormat("dd-MMM-yy, KK:mm:ss a");
		date = new Date();
		String dateRep = formatter.format(date);
		
		if (plugin.MySQL) {
			try {
				plugin.manageMySQL.insertQuery("INSERT INTO logs VALUES ('" + dateRep+ "', '" + playerName + "', 'X="+ xPos + " Z=" + zPos + " Y=" + yPos + "', '"+ ILUtil.formatDamageCause(lastDamageCause)+ "', '" + nearbyPlayerNames + "', '"+ droppedItemsList + "');");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			plugin.manageSQLite.insertQuery("INSERT INTO logs VALUES ('" + dateRep+ "', '" + playerName + "', 'X="+ xPos + " Z=" + zPos + " Y=" + yPos + "', '"+ ILUtil.formatDamageCause(lastDamageCause)+ "', '" + nearbyPlayerNames + "', '"+ droppedItemsList + "');");		}
	}
}
