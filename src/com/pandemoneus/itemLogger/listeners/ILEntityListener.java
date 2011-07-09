package com.pandemoneus.itemLogger.listeners;

import java.util.List;

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
			// dummy cause
			DamageCause lastDamageCause = EntityDamageEvent.DamageCause.CUSTOM;

			if (event.getEntity().getLastDamageCause() != null) {
				lastDamageCause = event.getEntity().getLastDamageCause()
						.getCause();
			}

			List<ItemStack> list = event.getDrops();

			ILUtil.writeToFile((Player) event.getEntity(), lastDamageCause,
					ILUtil.formatItemStackList(list));
		}
	}
}
