package xyz.tertsonen.duelsCombo;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import me.realized.duels.api.Duels;
import me.realized.duels.api.kit.Kit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import xyz.tertsonen.duelsCombo.customItems.ItemFlag;
import xyz.tertsonen.duelsCombo.saveData.KitData;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Utils {

	static void sendVelocityPacket(Entity entity, Player... players) {
		PacketContainer velPacket = makeVelocityPacket(entity);
		try{
			if (entity instanceof Player)
				DuelsCombo.getInstance().getProtocolManager().sendServerPacket((Player) entity, velPacket);
			for (Player playerToSend : players) {
				DuelsCombo.getInstance().getProtocolManager().sendServerPacket(playerToSend, velPacket);
			}
		} catch (InvocationTargetException e) {
			DuelsCombo.getInstance().getLogger().warning("Could not send velocity packet");
		}
	}

	private static @NotNull PacketContainer makeVelocityPacket(@NotNull Entity entity) {
		Vector vel = entity.getVelocity();
		PacketContainer velPacket = new PacketContainer(PacketType.Play.Server.ENTITY_VELOCITY);
		velPacket.getIntegers()
				.write(0, entity.getEntityId())
				.write(1, (int) (vel.getX() * 8000D))
				.write(2, (int) (vel.getY() * 8000D))
				.write(3, (int) (vel.getZ() * 8000D));
		return velPacket;
	}

	static void sendVelocityPacket(Player player) {
		PacketContainer velPacket = makeVelocityPacket(player);
		try{
			DuelsCombo.getInstance().getProtocolManager().sendServerPacket(player, velPacket);
		} catch (InvocationTargetException e) {
			DuelsCombo.getInstance().getLogger().warning("Could not send velocity packet");
			e.printStackTrace();
		}
	}

	static KitData getKitData(Player player){
		Duels api = DuelsCombo.getInstance().getDuelsAPI();
		Kit kit = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(api.getArenaManager().get(player)).getMatch()).getKit());
		return DuelsCombo.getInstance().getSaveDataManager().getKit(kit.getName());
	}

	static boolean checkInMatch(Player p1, Player p2){
		Duels api = DuelsCombo.getInstance().getDuelsAPI();
		if(!api.getArenaManager().isInMatch(p1)) return false;
		return api.getArenaManager().isInMatch(p2);
	}

	static boolean isArrow(Material mat){
		return mat == Material.ARROW || mat == Material.SPECTRAL_ARROW || mat == Material.TIPPED_ARROW;
	}

	/**
	 * Finds the first arrow in the inventory
	 * @param player the player whose inventory will be searched
	 * @return the first ItemStack which is an arrow, else null
	 */
	static Material takeFirstArrow(Player player, boolean decrement){
		PlayerInventory playerInventory = player.getInventory();
		if(isArrow(playerInventory.getItemInMainHand().getType())) {
			ItemStack stack = playerInventory.getItemInMainHand();
			Material mat = stack.getType();
			if(decrement){
				stack.setAmount(stack.getAmount()-1);
				player.updateInventory();
			}
			return mat;
		}
		if(isArrow(playerInventory.getItemInOffHand().getType())) {
			ItemStack stack = playerInventory.getItemInOffHand();
			Material mat = stack.getType();
			if(decrement){
				stack.setAmount(stack.getAmount()-1);
				player.updateInventory();
			}
			return mat;
		}
		for (ItemStack itemStack : playerInventory) {
			if(itemStack != null && isArrow(itemStack.getType())) {
				Material mat = itemStack.getType();
				if(decrement){
					itemStack.setAmount(itemStack.getAmount()-1);
					player.updateInventory();
				}
				return mat;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	static <T> void passBowFlags(@NotNull ItemStack bow, Entity firedProjectile){
		if(bow.getItemMeta() == null) return;
		ItemMeta meta = bow.getItemMeta();
		PersistentDataContainer cont = meta.getPersistentDataContainer();
		PersistentDataContainer entityCont = firedProjectile.getPersistentDataContainer();
		String pluginName = DuelsCombo.getInstance().getName().toLowerCase(Locale.ROOT);
		for (NamespacedKey key : cont.getKeys()) {
			if(!key.getNamespace().equals(pluginName)) continue;
			ItemFlag<T> flag = (ItemFlag<T>) ItemFlag.getByNameSpacedKey(key);
			if(flag == null) continue;
			if(flag.isProjectileFlag()){
				PersistentDataType<?,T> type = flag.getType();
				entityCont.set(key, type, Objects.requireNonNull(cont.get(key, type)));
			}
		}
	}

	static void setProjectileSpeedFromMultiplier(@NotNull ItemStack item, @NotNull Entity projectile, Entity shooter){
		if(item.getItemMeta() == null) return;
		Double mul = ItemFlag.PROJECTILE_VELOCITY_MULTIPLIER.getValue(item.getItemMeta());
		mul = mul == null ? 1d : mul;
		projectile.setVelocity(projectile.getVelocity().multiply(mul));
		if(shooter instanceof Player)
			Utils.sendVelocityPacket(projectile, (Player) shooter);
	}

	static void setLaunchedProjectileSpread(ItemStack item, Entity shooter, Entity projectile){
		if(item.getItemMeta() == null) return;
		Double mul = ItemFlag.PROJECTILE_LAUNCH_DIRECTION_RANDOMNESS_MULTIPLIER.getValue(item.getItemMeta());
		if(mul != null && mul != 1d) {
			if(mul != 0d){
				Arrow tmpArrow = shooter.getWorld().spawnArrow(projectile.getLocation(), shooter.getLocation().getDirection(), (float) projectile.getVelocity().length(), 12 * mul.floatValue());
				projectile.setVelocity(tmpArrow.getVelocity());
				tmpArrow.remove();
			}else{
				projectile.setVelocity(shooter.getLocation().getDirection().multiply(projectile.getVelocity().length()));
			}
		}
	}

	static void createExtraProjectiles(@NotNull ItemStack item, Entity projectile, Entity shooter) {
		if(item.getItemMeta() == null) return;
		Integer amount = ItemFlag.PROJECTILE_AMOUNT.getValue(item.getItemMeta());
		if (amount == null || amount <= 1) return;
		if(item.getItemMeta() == null) return;
		Double mul = ItemFlag.PROJECTILE_LAUNCH_DIRECTION_RANDOMNESS_MULTIPLIER.getValue(item.getItemMeta());
		if (mul == null) mul = 1d;
		for (int integer = 1; integer < amount; integer++) {
			Arrow newProjectile = shooter.getWorld().spawnArrow(projectile.getLocation(), shooter.getLocation().getDirection(), (float) projectile.getVelocity().length(), 12 * mul.floatValue());
			Utils.setProjectileSpeedFromMultiplier(item, newProjectile, shooter);
			Utils.passBowFlags(item, newProjectile);
			Utils.setLaunchedProjectileSpread(item, shooter, newProjectile);
		}
	}

	static void handleLaunchedProjectile(ItemStack item, Entity shooter, Entity projectile) {
		createExtraProjectiles(item, projectile, shooter);
		Utils.setProjectileSpeedFromMultiplier(item, projectile, shooter);

		Utils.passBowFlags(item, projectile);
		Utils.setLaunchedProjectileSpread(item, shooter, projectile);
	}

	static void doProjectileExplosion(@NotNull ProjectileHitEvent e) {
		Double explosionSize = ItemFlag.PROJECTILE_EXPLOSION_SIZE.getValue(e.getEntity());
		boolean custom = Boolean.TRUE.equals(ItemFlag.PROJECTILE_KNOCKBACK_EXPLOSION.getValue(e.getEntity()));
		if (!custom){
			if (explosionSize == null || explosionSize == 0d) return;
			boolean breakBlocks = Boolean.TRUE.equals(ItemFlag.PROJECTILE_EXPLOSION_DESTROY_BLOCKS.getValue(e.getEntity()));
			e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), (float) (double) explosionSize, false, breakBlocks);
			return;
		}

		// Custom explosion

		Double explosionRange = ItemFlag.PROJECTILE_CUSTOM_EXPLOSION_KNOCKBACK_RANGE.getValue(e.getEntity());
		explosionRange = explosionRange == null ? 10d : explosionRange;
		explosionSize = explosionSize == null ? 1d : explosionSize;
		List<Entity> entities = e.getEntity().getNearbyEntities(explosionRange, explosionRange, explosionRange);
		ArrayList<Player> playersToSend = new ArrayList<>(entities.size() + 1);
		if(e.getEntity().getShooter() instanceof Player)
			playersToSend.add((Player) e.getEntity().getShooter());
		List<Entity> launchedEntities = new ArrayList<>(entities.size());
		Location hitLoc = null;
		if (e.getHitBlock() != null) hitLoc = e.getHitBlock().getLocation();
		if (hitLoc == null && e.getHitEntity() != null) hitLoc = e.getHitEntity().getLocation();
		if(hitLoc == null) hitLoc = e.getEntity().getLocation();

		Boolean showEffect = ItemFlag.PROJECTILE_KNOCKBACK_EXPLOSION_SHOW_EFFECT.getValue(e.getEntity());
		if(showEffect == null || showEffect){
			e.getEntity().getWorld().createExplosion(hitLoc, 0, false, false);
		}


		for (Entity entity : entities) {
			Vector vec = entity.getLocation().subtract(hitLoc).toVector();
			double len = vec.length();
			if (len > explosionRange) continue;
			vec.normalize().multiply(0.1d / (len / explosionRange) * explosionSize).add(new Vector(0d,0.3d,0d));
			if(!NumberConversions.isFinite(vec.getX()) || !NumberConversions.isFinite(vec.getY()) || !NumberConversions.isFinite(vec.getZ())) continue;
			entity.setVelocity(entity.getVelocity().add(vec));
			launchedEntities.add(entity);
			if(entity instanceof Player) playersToSend.add((Player) entity);
		}
		Player[] playersToSendArr = playersToSend.toArray(new Player[0]);
		for (Entity launchedEntity : launchedEntities) {
			Utils.sendVelocityPacket(launchedEntity, playersToSendArr);
		}

	}
}
