package com.tonero.duelsCombo.customItems;

import com.tonero.duelsCombo.DuelsCombo;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public enum ItemFlag {

	/**
	 * Amount of knockback to give to the shooter of the bow. Useful for grappling hook bows.
	 */
	SHOOTER_BOW_KNOCKBACK("shooterBowKnockback", "projectile-shooter-knockback", DataType.DOUBLE, true),
	/**
	 * Whether to switch player positions on projectile hit.
	 */
	SWITCH_POSITIONS_ON_HIT("switchPositionsOnHit", "switch-positions-on-projectile-hit", DataType.BOOL, true),
	/**
	 * Whether to make the bow shoot instantly, like in Beta 1.8.
	 */
	BOW_INSTANT_SHOOT("bowInstantShoot", "bow-instant-shoot", DataType.BOOL, false),
	/**
	 * Cooldown between bow shots.
	 */
	TIME_BETWEEN_BOW_SHOTS("timeBetweenBowShots", "time-between-bow-shots", DataType.INT, false),
	PROJECTILE_VELOCITY_MULTIPLIER("projectileVelocityMultiplier", "projectile-velocity-multiplier", DataType.DOUBLE, false),
	PROJECTILE_EXPLOSION_SIZE("arrowExplosionSize", "projectile-explosion-size", DataType.DOUBLE, true),
	PROJECTILE_EXPLOSION_DESTROY_BLOCKS("projectileExplosionDestroyBlocks", "projectile-explosion-destroy-blocks", DataType.BOOL, true);

	@Getter
	private final String key;
	@Getter
	private final String userFriendlyName;
	@Getter
	private final DataType type;
	@Getter
	private final boolean projectileFlag;

	private static final HashMap<String, ItemFlag> lookup = new HashMap<>();
	private static HashMap<NamespacedKey, ItemFlag> nsKeyLookup = null;

	static {
		for (ItemFlag value : ItemFlag.values()) {
			lookup.put(value.getUserFriendlyName(), value);
		}
	}

	@Nullable
	public static ItemFlag getByUserFriendlyName(String userFriendlyName){
		return lookup.get(userFriendlyName);
	}

	@Nullable
	public static ItemFlag getByNameSpacedKey(NamespacedKey namespacedKey){
		if(nsKeyLookup == null){
			nsKeyLookup = new HashMap<>();
			for (ItemFlag value : ItemFlag.values()) {
				nsKeyLookup.put(value.getNamespacedKey(), value);
			}
		}
		return nsKeyLookup.get(namespacedKey);
	}

	private NamespacedKey nKey = null;

	public NamespacedKey getNamespacedKey(){
		if(nKey != null) return nKey;
		nKey = new NamespacedKey(DuelsCombo.getInstance(), key);
		return nKey;
	}

	ItemFlag(String key, String userFriendlyName, DataType type, boolean projectileFlag){
		this.key = key;
		this.userFriendlyName = userFriendlyName;
		this.type = type;
		this.projectileFlag = projectileFlag;
	}

	public static boolean getBool(@NotNull ItemFlag itemFlag, PersistentDataHolder dataHolder) {
		if(itemFlag.getType() != DataType.BOOL) return false;
		Short val = dataHolder.getPersistentDataContainer().get(itemFlag.getNamespacedKey(), PersistentDataType.SHORT);
		return val != null && val == (short) 1;
	}

	public static @Nullable Double getDouble(@NotNull ItemFlag itemFlag, PersistentDataHolder dataHolder) {
		if(itemFlag.getType() != DataType.DOUBLE) return null;
		return dataHolder.getPersistentDataContainer().get(itemFlag.getNamespacedKey(), PersistentDataType.DOUBLE);
	}

	public static @Nullable Integer getInt(@NotNull ItemFlag itemFlag, PersistentDataHolder dataHolder) {
		if(itemFlag.getType() != DataType.INT) return null;
		return dataHolder.getPersistentDataContainer().get(itemFlag.getNamespacedKey(), PersistentDataType.INTEGER);
	}
}
