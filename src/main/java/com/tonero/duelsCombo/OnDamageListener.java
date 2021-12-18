package com.tonero.duelsCombo;

import com.tonero.duelsCombo.customItems.ComboNamespacedKey;
import com.tonero.duelsCombo.saveData.KitData;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.realized.duels.api.Duels;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Objects;

public class OnDamageListener implements Listener {

	private final Duels duelsAPI;

	@EqualsAndHashCode
	@AllArgsConstructor
	private static class LaunchData {
		@Getter
		private final Location arrowPos;

		@Getter
		private final double kb;
	}

	private final HashMap<Player, LaunchData> players = new HashMap<>();

	public OnDamageListener(){
		DuelsCombo.getInstance().getDuelsAPI().registerListener(this);
		DuelsCombo.getInstance().getServer().getPluginManager().registerEvents(this, DuelsCombo.getInstance());
		duelsAPI = DuelsCombo.getInstance().getDuelsAPI();
	}

	@EventHandler
	private void onBowShoot(EntityShootBowEvent e){
		if(!(e.getEntity() instanceof Player)) return;
		Player player = (Player) e.getEntity();
		ItemStack main = e.getBow();
		if(main == null) return;
		ItemMeta meta = main.getItemMeta();
		if(meta == null) return;
		PersistentDataContainer cont = meta.getPersistentDataContainer();
		Double kb = cont.get(ComboNamespacedKey.SHOOTER_BOW_KNOCKBACK.getNamespacedKey(), PersistentDataType.DOUBLE);
		Short tp = cont.get(ComboNamespacedKey.SWITCH_POSITIONS_ON_HIT.getNamespacedKey(), PersistentDataType.SHORT);
		if(kb != null && kb != 0){
			e.getProjectile().getPersistentDataContainer().set(ComboNamespacedKey.SHOOTER_BOW_KNOCKBACK.getNamespacedKey(), PersistentDataType.DOUBLE, kb);
		}
		if(tp != null && tp != 0){
			e.getProjectile().getPersistentDataContainer().set(ComboNamespacedKey.SWITCH_POSITIONS_ON_HIT.getNamespacedKey(), PersistentDataType.SHORT, (short) 1);
		}
	}

	@EventHandler
	private void onProjectileHit(ProjectileHitEvent e){
		Double kb = e.getEntity().getPersistentDataContainer().get(ComboNamespacedKey.SHOOTER_BOW_KNOCKBACK.getNamespacedKey(), PersistentDataType.DOUBLE);
		if(kb != null){
			Location projLoc = e.getEntity().getLocation();
			ProjectileSource shooter = e.getEntity().getShooter();
			if(!(shooter instanceof Player)) return;
			Player p = (Player) shooter;
			players.put(p, new LaunchData(projLoc, kb));
			new BukkitRunnable(){
				@Override
				public void run() {
					LaunchData dat = players.get(p);
					if(dat == null) return;
					if(dat.arrowPos == projLoc) players.remove(p);
				}
			}.runTaskLater(DuelsCombo.getInstance(), /* 30 seconds */30 * 20L);
		}
	}

	@EventHandler
	private void onInteract(PlayerInteractEvent e){
		Player p = e.getPlayer();
		LaunchData dat = players.remove(p);
		if(dat == null) return;

		Location playerLoc = p.getLocation();
		if(!Objects.equals(dat.arrowPos.getWorld(), playerLoc.getWorld())) return;
		p.setVelocity(dat.arrowPos.toVector().add(new Vector(0, 2, 0)).subtract(playerLoc.toVector()).multiply(-dat.kb));
		Utils.sendPacket(p);
	}

	@EventHandler
	private void onDamage(EntityDamageByEntityEvent event){
		if(!(event.getEntity() instanceof Player)) return;
		if(event.getDamager() instanceof Arrow){
			Player player = (Player) event.getEntity();
			Arrow arrow = (Arrow) event.getDamager();
			if(!(arrow.getShooter() instanceof Player)) return;
			Player shooter = (Player) arrow.getShooter();
			if(Objects.equals(event.getDamager().getPersistentDataContainer().get(ComboNamespacedKey.SWITCH_POSITIONS_ON_HIT.getNamespacedKey(), PersistentDataType.SHORT), (short) 1)){
				Location oldPLoc = player.getLocation();
				player.teleport(shooter.getLocation());
				shooter.teleport(oldPLoc);
			}
			if(!Utils.checkInMatch(shooter, player)) return;
			KitData kitData = Utils.getKitData(player);
			double kbMp = kitData.getBowKnockbackMultiplier();
			if(kbMp == 1d) return;
			new BukkitRunnable(){
				@Override
				public void run() {
					player.setVelocity(player.getLocation().toVector().subtract(shooter.getLocation().toVector()).multiply(kbMp));
					Utils.sendPacket(player, shooter);
				}
			}.runTaskLater(DuelsCombo.getInstance(), 1L);
			return;
		}
		if(!(event.getDamager() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if(!Utils.checkInMatch(player, (Player) event.getDamager())) return;
		KitData kitData = Utils.getKitData(player);
		double kb = kitData.getKnockbackMultiplier();
		if(kb == 1) return;
		Vector kbDir = event.getDamager().getLocation().getDirection().normalize();
		new BukkitRunnable(){
			@Override
			public void run() {
				player.setVelocity(kbDir.multiply(kb));
				Utils.sendPacket(player, (Player) event.getDamager());
			}
		}.runTaskLater(DuelsCombo.getInstance(), 1L);
	}
}
