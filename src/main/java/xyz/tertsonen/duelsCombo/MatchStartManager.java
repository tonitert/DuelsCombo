package xyz.tertsonen.duelsCombo;

import me.realized.duels.api.event.kit.KitRemoveEvent;
import me.realized.duels.api.event.match.MatchEndEvent;
import me.realized.duels.api.event.match.MatchStartEvent;
import me.realized.duels.api.kit.Kit;
import me.realized.duels.api.match.Match;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.tertsonen.duelsCombo.saveData.KitData;

import java.util.HashMap;

public class MatchStartManager implements Listener {

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
    }

    @EventHandler
    void onMatchStart(MatchStartEvent event){
        if(!DuelsCombo.getInstance().isActive()) return;
        Kit kit = event.getMatch().getKit();
        if(kit == null) return;
        KitData kitData = DuelsCombo.getInstance().getSaveDataManager().getKit(event.getMatch().getKit().getName());
        if(kitData == null) return;
        if(kitData.isCombo()){
            matches.put(event.getMatch(), new PlayerData[]{new PlayerData(event.getPlayers()[0]), new PlayerData(event.getPlayers()[1])});
            for (Player player: event.getPlayers()) {
                player.setMaximumNoDamageTicks(kitData.getMaxNoDamageTicks());
            }
        }
    }

    @EventHandler
    void kitDelete(KitRemoveEvent kitRemoveEvent) {

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



}