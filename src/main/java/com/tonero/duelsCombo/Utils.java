package com.tonero.duelsCombo;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.tonero.duelsCombo.customItems.ItemFlag;
import com.tonero.duelsCombo.saveData.KitData;
import me.realized.duels.api.Duels;
import me.realized.duels.api.kit.Kit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.Objects;

public class Utils {
	static void sendVelocityPacket(Player player, Player player2) {
		PacketContainer velPacket = makeVelocityPacket(player);

		try{
			DuelsCombo.getInstance().getProtocolManager().sendServerPacket(player, velPacket);
			DuelsCombo.getInstance().getProtocolManager().sendServerPacket(player2, velPacket);
		} catch (InvocationTargetException e) {
			DuelsCombo.getInstance().getLogger().warning("Could not send velocity packet");
		}
	}

	private static @NotNull PacketContainer makeVelocityPacket(@NotNull Player player) {
		Vector vel = player.getVelocity();
		PacketContainer velPacket = new PacketContainer(PacketType.Play.Server.ENTITY_VELOCITY);
		velPacket.getIntegers()
				.write(0, player.getEntityId())
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
	static void passBowFlags(ItemStack bow, Entity firedProjectile){
		if(bow.getItemMeta() == null) return;
		ItemMeta meta = bow.getItemMeta();
		PersistentDataContainer cont = meta.getPersistentDataContainer();
		PersistentDataContainer entityCont = firedProjectile.getPersistentDataContainer();
		String pluginName = DuelsCombo.getInstance().getName().toLowerCase(Locale.ROOT);
		for (NamespacedKey key : cont.getKeys()) {
			if(!key.getNamespace().equals(pluginName)) continue;
			ItemFlag flag = ItemFlag.getByNameSpacedKey(key);
			if(flag == null) continue;
			if(flag.isProjectileFlag()){
				entityCont.set(key, flag.getType().dataType, Objects.requireNonNull(cont.get(key, flag.getType().dataType)));
			}
		}
	}

	static void setProjectileSpeedFromMultiplier(@NotNull ItemStack item, @NotNull Entity projectile){
		Double mul = ItemFlag.getDouble(ItemFlag.PROJECTILE_VELOCITY_MULTIPLIER, item.getItemMeta());
		mul = mul == null ? 1d : mul;
		projectile.setVelocity(projectile.getVelocity().multiply(mul));
	}
}
