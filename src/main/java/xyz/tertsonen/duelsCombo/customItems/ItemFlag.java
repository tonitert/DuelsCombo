package xyz.tertsonen.duelsCombo.customItems;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.tertsonen.duelsCombo.DuelsCombo;

import java.util.HashMap;

public class ItemFlag<T> {

	/*
	 * key: the key used for saving the flags in the NBT data
	 * userFriendlyName: the flag name shown to the player
	 * projectileFlag: whether to pass the flag to a launched projectile
	 */

	/**
	 * Amount of knockback to give to the shooter of the bow. Useful for grappling hook bows.
	 */
	public static ItemFlag<Double> SHOOTER_BOW_KNOCKBACK = new ItemFlag<>("shooterBowKnockback", "projectile-shooter-knockback", PersistentDataType.DOUBLE, true);
	/**
	 * The speed at which the hit entity will be pushed.
	 */
	public static ItemFlag<Double> PROJECTILE_PUSH_AMOUNT = new ItemFlag<>("projectilePushAmount", "projectile-push-amount", PersistentDataType.DOUBLE, true);
	/**
	 * Whether to switch player positions on projectile hit.
	 */
	public static ItemFlag<Boolean> SWITCH_POSITIONS_ON_HIT = new ItemFlag<>("switchPositionsOnHit", "switch-positions-on-projectile-hit", Bool.BOOL, true);
	/**
	 * Whether to make the bow shoot instantly, like in Beta 1.8.
	 */
	public static ItemFlag<Boolean> BOW_INSTANT_SHOOT = new ItemFlag<>("bowInstantShoot", "bow-instant-shoot", Bool.BOOL, false);
	/**
	 * Cooldown between bow shots.
	 */
	public static ItemFlag<Integer> TIME_BETWEEN_BOW_SHOTS = new ItemFlag<>("timeBetweenBowShots", "time-between-bow-shots", PersistentDataType.INTEGER, false);
	public static ItemFlag<Double> PROJECTILE_VELOCITY_MULTIPLIER = new ItemFlag<>("projectileVelocityMultiplier", "projectile-velocity-multiplier", PersistentDataType.DOUBLE, false);
	/**
	 *	Size of the explosion which will be created when the projectile hits something.
	 */
	public static ItemFlag<Double> PROJECTILE_EXPLOSION_SIZE = new ItemFlag<>("arrowExplosionSize", "projectile-explosion-size", PersistentDataType.DOUBLE, true);
	public static ItemFlag<Boolean> PROJECTILE_EXPLOSION_DESTROY_BLOCKS = new ItemFlag<>("projectileExplosionDestroyBlocks", "projectile-explosion-destroy-blocks", Bool.BOOL, true);
	/**
	 * Multiplier for the amount of randomness the server adds to bows by default.
	 */
	public static ItemFlag<Double> PROJECTILE_LAUNCH_DIRECTION_RANDOMNESS_MULTIPLIER = new ItemFlag<>("projectileSpread", "projectile-direction-randomness-multiplier", PersistentDataType.DOUBLE, false);

	@Getter
	private final String key;
	@Getter
	private final String userFriendlyName;
	@Getter
	private final PersistentDataType<?, T> type;
	@Getter
	private final boolean projectileFlag;

	private static final HashMap<String, ItemFlag<?>> lookup = new HashMap<>();
	private static HashMap<NamespacedKey, ItemFlag<?>> namespacedKeyLookup = null;

	public static final ItemFlag<?>[] VALUES = {
			SHOOTER_BOW_KNOCKBACK,
			PROJECTILE_PUSH_AMOUNT,
			SWITCH_POSITIONS_ON_HIT,
			BOW_INSTANT_SHOOT,
			TIME_BETWEEN_BOW_SHOTS,
			PROJECTILE_VELOCITY_MULTIPLIER,
			PROJECTILE_EXPLOSION_SIZE,
			PROJECTILE_EXPLOSION_DESTROY_BLOCKS,
			PROJECTILE_LAUNCH_DIRECTION_RANDOMNESS_MULTIPLIER
	};

	static {
		for (ItemFlag<?> value : VALUES) {
			lookup.put(value.getUserFriendlyName(), value);
		}
	}

	@Nullable
	public static ItemFlag<?> getByUserFriendlyName(String userFriendlyName){
		return lookup.get(userFriendlyName);
	}

	@Nullable
	public static ItemFlag<?> getByNameSpacedKey(NamespacedKey namespacedKey){
		if(namespacedKeyLookup == null){
			namespacedKeyLookup = new HashMap<>();
			for (ItemFlag<?> value : VALUES) {
				namespacedKeyLookup.put(value.getNamespacedKey(), value);
			}
		}
		return namespacedKeyLookup.get(namespacedKey);
	}

	private NamespacedKey nKey = null;

	public NamespacedKey getNamespacedKey(){
		if(nKey != null) return nKey;
		nKey = new NamespacedKey(DuelsCombo.getInstance(), key);
		return nKey;
	}

	ItemFlag(String key, String userFriendlyName, PersistentDataType<?,T> dataType, boolean projectileFlag){
		this.key = key;
		this.userFriendlyName = userFriendlyName;
		this.type = dataType;
		this.projectileFlag = projectileFlag;
	}

	@Nullable
	public T getValue(@NotNull PersistentDataHolder holder){
		return holder.getPersistentDataContainer().get(getNamespacedKey(), type);
	}
}
