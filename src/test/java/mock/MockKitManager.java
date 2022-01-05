package mock;

import me.realized.duels.api.kit.Kit;
import me.realized.duels.api.kit.KitManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MockKitManager implements KitManager {
	HashMap<String, Kit> kits = new HashMap<>();

	@Nullable
	@Override
	public Kit get(@NotNull String name) {
		return kits.get(name);
	}

	@Nullable
	@Override
	public Kit create(@NotNull Player creator, @NotNull String name) {
		return kits.put(name, new MockKit(name, false, false));
	}

	@Nullable
	public Kit create(@NotNull String name) {
		return kits.put(name, new MockKit(name, false, false));
	}

	@Nullable
	@Override
	public Kit remove(@Nullable CommandSender source, @NotNull String name) {
		return kits.remove(name);
	}

	@Nullable
	@Override
	public Kit remove(@NotNull String name) {
		return kits.remove(name);
	}

	@NotNull
	@Override
	public List<Kit> getKits() {
		return new ArrayList<>(kits.values());
	}
}
