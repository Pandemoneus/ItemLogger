package com.pandemoneus.itemLogger.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.inventory.ItemStack;

import com.pandemoneus.itemLogger.ILUtil;

/**
 * Entity Listener for the ItemLogger plugin. Triggers on entity deaths.
 * 
 * @author Pandemoneus
 * 
 */
public final class ILEntityListener extends EntityListener {
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

			ILUtil.writeToFile(player, lastDamageCause, nearbyPlayers, ILUtil.formatItemStackList(list));
		}
	}
}
