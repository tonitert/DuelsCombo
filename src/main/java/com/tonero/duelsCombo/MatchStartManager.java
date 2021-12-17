package com.tonero.duelsCombo;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.tonero.duelsCombo.saveData.KitData;
import me.realized.duels.api.Duels;
import me.realized.duels.api.event.match.MatchEndEvent;
import me.realized.duels.api.event.match.MatchStartEvent;
import me.realized.duels.api.kit.Kit;
import me.realized.duels.api.match.Match;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Objects;

public class MatchStartManager implements Listener {

    private final Duels duelsAPI;

    private static class PlayerData {
        public Player player;
        public int previousMaxNoDamageTicks;

        public PlayerData(Player player) {
            this.player = player;
            this.previousMaxNoDamageTicks = player.getMaximumNoDamageTicks();
        }
    }

    private final HashMap<Match, PlayerData[]> matches = new HashMap<>();

    public MatchStartManager(){
        DuelsCombo.getInstance().getDuelsAPI().registerListener(this);
        DuelsCombo.getInstance().getServer().getPluginManager().registerEvents(this, DuelsCombo.getInstance());
        duelsAPI = DuelsCombo.getInstance().getDuelsAPI();
    }

    @EventHandler
    void onMatchStart(MatchStartEvent event){
        if(!DuelsCombo.getInstance().isActive()) return;
        KitData kit = DuelsCombo.getInstance().getSaveDataManager().getKit(event.getMatch().getKit().getName());
        if(kit == null) return;
        if(kit.isCombo()){
            matches.put(event.getMatch(), new PlayerData[]{new PlayerData(event.getPlayers()[0]), new PlayerData(event.getPlayers()[1])});
            for (Player player: event.getPlayers()) {
                player.setMaximumNoDamageTicks(kit.getMaxNoDamageTicks());
            }
        }
    }

    @EventHandler
    void onMatchEnd(MatchEndEvent event){
        Kit kit = event.getMatch().getKit();
        if(kit == null) return;
        KitData kitData = DuelsCombo.getInstance().getSaveDataManager().getKit(kit.getName());
        if(kitData == null) return;
        if(kitData.isCombo()){
            PlayerData[] players = matches.remove(event.getMatch());
            if(players == null){
                return;
            }
            for (PlayerData data: players) {
                data.player.setMaximumNoDamageTicks(data.previousMaxNoDamageTicks);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        if(event.getDamager() instanceof Arrow){
            Player player = (Player) event.getEntity();
            Arrow arrow = (Arrow) event.getDamager();
            if(!(arrow.getShooter() instanceof Player)) return;
            Player shooter = (Player) arrow.getShooter();
            if(!checkInMatch(shooter, player)) return;
            KitData kitData = getKitData(player);
            new BukkitRunnable(){
                @Override
                public void run() {
                    player.setVelocity(player.getLocation().toVector().subtract(shooter.getLocation().toVector()).multiply(kitData.getBowKnockbackMultiplier()));
                    sendPacket(player, shooter);
                }
            }.runTaskLater(DuelsCombo.getInstance(), 1L);
            return;
        }
        if(!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if(!checkInMatch(player, (Player) event.getDamager())) return;
        KitData kitData = getKitData(player);
        double kb = kitData.getKnockbackMultiplier();
        if(kb == 1) return;
        Vector kbDir = event.getDamager().getLocation().getDirection().normalize();
        new BukkitRunnable(){
            @Override
            public void run() {
                player.setVelocity(kbDir.multiply(kb));
                sendPacket(player, (Player) event.getDamager());
            }
        }.runTaskLater(DuelsCombo.getInstance(), 1L);
    }

    private KitData getKitData(Player player){
        Kit kit = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(duelsAPI.getArenaManager().get(player)).getMatch()).getKit());
        return DuelsCombo.getInstance().getSaveDataManager().getKit(kit.getName());
    }

    private boolean checkInMatch(Player p1, Player p2){
        if(!duelsAPI.getArenaManager().isInMatch(p1)) return false;
        return duelsAPI.getArenaManager().isInMatch(p2);
    }

    private void sendPacket(Player player, Player player2){
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



}