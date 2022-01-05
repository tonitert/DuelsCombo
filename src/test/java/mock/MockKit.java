package mock;

import lombok.AllArgsConstructor;
import me.realized.duels.api.kit.Kit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class MockKit implements Kit {

	private String name;

	private boolean permission;

	private boolean arenaSpecific;

	@NotNull
	@Override
	public String getName() {
		return name;
	}

	@NotNull
	@Override
	public ItemStack getDisplayed() {
		return new ItemStack(Material.AIR);
	}

	@Override
	public boolean isUsePermission() {
		return permission;
	}

	@Override
	public void setUsePermission(boolean usePermission) {
		permission = usePermission;
	}

	@Override
	public boolean isArenaSpecific() {
		return arenaSpecific;
	}

	@Override
	public void setArenaSpecific(boolean arenaSpecific) {
		this.arenaSpecific = arenaSpecific;
	}

	@Override
	public boolean equip(@NotNull Player player) {
		return false;
	}

	@Override
	public boolean isRemoved() {
		return false;
	}
}
