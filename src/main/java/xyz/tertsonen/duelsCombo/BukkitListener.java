package xyz.tertsonen.duelsCombo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import xyz.tertsonen.duelsCombo.customItems.ItemFlag;
import xyz.tertsonen.duelsCombo.saveData.KitData;

import java.util.HashMap;
import java.util.Objects;

public class BukkitListener implements Listener {

	@EqualsAndHashCode
	@AllArgsConstructor
	private static class LaunchData {
		@Getter
		private final Location arrowPos;

		@Getter
		private final double kb;
	}

	private final HashMap<Player, LaunchData> players = new HashMap<>();

	private final HashMap<Player, Boolean> canFire = new HashMap<>();

	private final HashMap<Player, BukkitTask> isHoldingDown = new HashMap<>();

	public BukkitListener(){
		DuelsCombo.getInstance().getDuelsAPI().registerListener(this);
		DuelsCombo.getInstance().getServer().getPluginManager().registerEvents(this, DuelsCombo.getInstance());
	}

	@EventHandler
	private void onBowShoot(EntityShootBowEvent e){
		if(e.getBow() == null) return;
		Utils.setProjectileSpeedFromMultiplier(e.getBow(), e.getProjectile());
		Utils.passBowFlags(e.getBow(), e.getProjectile());
	}

	@EventHandler
	private void onProjectileHit(ProjectileHitEvent e){
		Double kb = e.getEntity().getPersistentDataContainer().get(ItemFlag.SHOOTER_BOW_KNOCKBACK.getNamespacedKey(), PersistentDataType.DOUBLE);
		ProjectileSource shooterSource = e.getEntity().getShooter();
		if(!(shooterSource instanceof Player)) return;
		Player shooter = (Player) shooterSource;
		if(kb != null){
			Location projLoc = e.getEntity().getLocation();
			players.put(shooter, new LaunchData(projLoc, kb));
			new BukkitRunnable(){
				@Override
				public void run() {
					LaunchData dat = players.get(shooter);
					if(dat == null) return;
					if(dat.arrowPos == projLoc) players.remove(shooter);
				}
			}.runTaskLater(DuelsCombo.getInstance(), /* 30 seconds */30 * 20L);
		}
		Double explosionSize = ItemFlag.getDouble(ItemFlag.PROJECTILE_EXPLOSION_SIZE, e.getEntity());
		if(explosionSize != null && explosionSize != 0d){
			boolean breakBlocks = ItemFlag.getBool(ItemFlag.PROJECTILE_EXPLOSION_DESTROY_BLOCKS, e.getEntity());
			e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), (float) (double) explosionSize, false, breakBlocks);
		}

		if(e.getEntity() instanceof Arrow){
			Entity hitEntity = e.getHitEntity();
			if(hitEntity != null){
				Arrow arrow = (Arrow) e.getEntity();
				Short val = arrow.getPersistentDataContainer().get(ItemFlag.SWITCH_POSITIONS_ON_HIT.getNamespacedKey(), PersistentDataType.SHORT);
				if(val != null && val == ((short) 1)){
					new BukkitRunnable(){
						@Override
						public void run() {
							Location l2 = hitEntity.getLocation();
							Location oldHitEntityLoc = new Location(l2.getWorld(), l2.getX(), l2.getY(), l2.getZ());
							Location l3 = shooter.getLocation();
							Location shooterLoc = new Location(l3.getWorld(), l3.getX(), l3.getY(), l3.getZ());
							hitEntity.teleport(shooterLoc);
							shooter.teleport(oldHitEntityLoc);
						}
					}.runTaskLater(DuelsCombo.getInstance(), 1L);
				}
			}
		}
	}

	@EventHandler
	private void onLogin(PlayerLoginEvent e){
		//Just in case
		canFire.remove(e.getPlayer());
	}

	@EventHandler
	private void onInteract(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(p.getGameMode() != GameMode.SPECTATOR && e.getItem() != null &&
				(e.getItem().getType() == Material.BOW || e.getItem().getType() == Material.CROSSBOW) &&
				e.getItem().getItemMeta() != null &&
				ItemFlag.getBool(ItemFlag.BOW_INSTANT_SHOOT, e.getItem().getItemMeta())) {
			Integer cd = ItemFlag.getInt(ItemFlag.TIME_BETWEEN_BOW_SHOTS, e.getItem().getItemMeta());
			if(!canFire.containsKey(p) && isHoldingDown.get(p) != null){
				Material arrow = Utils.takeFirstArrow(p, !(p.getGameMode() == GameMode.CREATIVE || e.getItem().getEnchantmentLevel(Enchantment.ARROW_INFINITE) != 0));
				if(arrow != null){
					Class<? extends AbstractArrow> arrowClass = null;
					switch (arrow){
						case ARROW:
						case TIPPED_ARROW:
							arrowClass = Arrow.class;
							break;
						case SPECTRAL_ARROW:
							arrowClass = SpectralArrow.class;
							break;
					}
					assert arrowClass != null;
					Projectile proj = p.launchProjectile(arrowClass);
					Utils.setProjectileSpeedFromMultiplier(e.getItem(), proj);
					Utils.passBowFlags(e.getItem(), proj);
					if(cd != null){
						canFire.put(e.getPlayer(), false);
						new BukkitRunnable(){
							@Override
							public void run() {
								canFire.remove(e.getPlayer());
							}
						}.runTaskLater(DuelsCombo.getInstance(), cd);
					}
				}
			}
			if(cd != null){
				BukkitRunnable rnbl = new BukkitRunnable() {
					@Override
					public void run() {
						isHoldingDown.remove(p);
					}
				};
				isHoldingDown.put(p, rnbl.runTaskLater(DuelsCombo.getInstance(), cd));
				e.setCancelled(true);
			}
		}
		LaunchData dat = players.remove(p);
		if(dat == null) return;

		Location playerLoc = p.getLocation();
		if(!Objects.equals(dat.arrowPos.getWorld(), playerLoc.getWorld())) return;
		p.setVelocity(dat.arrowPos.toVector().add(new Vector(0, 2, 0)).subtract(playerLoc.toVector()).multiply(-dat.kb));
		Utils.sendVelocityPacket(p);
	}

	@EventHandler
	private void onDamage(EntityDamageByEntityEvent e){
		if(!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) return;
		Player hitter = (Player) e.getDamager();
		if(!Utils.checkInMatch(hitter, (Player) e.getEntity())) return;
		KitData kitData = Utils.getKitData(hitter);
		double kb = kitData.getKnockbackMultiplier();
		if(kb == 1) return;
		Vector kbDir = hitter.getLocation().getDirection();
		if(kbDir.getY() < 0) kbDir.setY(0);
		kbDir.add(new Vector(0,kitData.getYKnockbackAmount(),0));
		double dmg = e.getDamage();
		e.setCancelled(true);
		if(e.getEntity() instanceof LivingEntity){
			((LivingEntity) e.getEntity()).damage(dmg);
		}

		//Check if velocity after applying kb is bigger than maxVelocity
		double maxKb = kitData.getMaxKnockbackSpeedMultiplier();
		Vector eVel = e.getEntity().getVelocity();
		Vector maxVel = eVel.clone().normalize().multiply(maxKb);
		Vector newEVel = eVel.clone().add(kbDir.multiply(kb));
		//Scalar projection
		double scalarProj = maxVel.dot(newEVel)/maxVel.length();
		double scalarWithoutAdd = maxVel.dot(eVel)/maxVel.length();

		if(scalarWithoutAdd < maxVel.length()){
			if(scalarProj < maxVel.length()){
				e.getEntity().setVelocity(newEVel);
			}else{
				e.getEntity().setVelocity(eVel.add(maxVel.subtract(eVel)));
			}
			Utils.sendVelocityPacket(hitter);
			if(e.getEntity() instanceof Player){
				Utils.sendVelocityPacket((Player) e.getEntity());
			}
		}

	}
}
