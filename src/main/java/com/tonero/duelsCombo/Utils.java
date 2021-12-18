package com.tonero.duelsCombo;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.tonero.duelsCombo.saveData.KitData;
import me.realized.duels.api.Duels;
import me.realized.duels.api.kit.Kit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class Utils {
	static void sendPacket(Player player, Player player2) {
		Vector vel = player.getVelocity();
		PacketContainer velPacket = new PacketContainer(PacketType.Play.Server.ENTITY_VELOCITY);
		velPacket.getIntegers()
				.write(0, player.getEntityId())
				.write(1, (int) (vel.getX() * 8000D))
				.write(2, (int) (vel.getY() * 8000D))
				.write(3, (int) (vel.getZ() * 8000D));

		try{
			DuelsCombo.getInstance().getProtocolManager().sendServerPacket(player, velPacket);
			DuelsCombo.getInstance().getProtocolManager().sendServerPacket(player2, velPacket);
		} catch (InvocationTargetException e) {
			DuelsCombo.getInstance().getLogger().warning("Could not send velocity packet");
		}
	}

	static void sendPacket(Player player) {
		Vector vel = player.getVelocity();
		PacketContainer velPacket = new PacketContainer(PacketType.Play.Server.ENTITY_VELOCITY);
		velPacket.getIntegers()
				.write(0, player.getEntityId())
				.write(1, (int) (vel.getX() * 8000D))
				.write(2, (int) (vel.getY() * 8000D))
				.write(3, (int) (vel.getZ() * 8000D));
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
}
