package com.tonero.duelsCombo;

import com.tonero.duelsCombo.saveData.KitData;
import me.realized.duels.api.event.match.MatchEndEvent;
import me.realized.duels.api.event.match.MatchStartEvent;
import me.realized.duels.api.match.Match;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class MatchStartManager implements Listener {
    static class PlayerData {
        public Player p;
        public int previousMaxNoDamageTicks;

        public PlayerData(Player p) {
            this.p = p;
            this.previousMaxNoDamageTicks = p.getMaximumNoDamageTicks();
        }
    }
    private HashMap<Match, PlayerData[]> matches = new HashMap<>();
    public MatchStartManager(){
        DuelsCombo.getInstance().getDuelsAPI().registerListener(this);
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
        KitData kit = DuelsCombo.getInstance().getSaveDataManager().getKit(event.getMatch().getKit().getName());
        if(kit == null) return;
        if(kit.isCombo()){
            PlayerData[] players = matches.remove(event.getMatch());
            if(players == null){
                return;
            }
            for (PlayerData data: players) {
                data.p.setMaximumNoDamageTicks(data.previousMaxNoDamageTicks);
            }
        }
    }

}