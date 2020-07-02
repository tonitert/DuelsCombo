package com.tonero.duelsCombo;

import com.tonero.duelsCombo.saveData.KitData;
import me.realized.duels.api.event.match.MatchEndEvent;
import me.realized.duels.api.event.match.MatchStartEvent;
import me.realized.duels.api.match.Match;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;
import java.util.logging.Level;

public class MatchStartManager implements Listener {
    class PlayerData {
        public Player p;
        public int prevMaxNoDamageTicks;

        public PlayerData(Player p) {
            this.p = p;
            this.prevMaxNoDamageTicks = p.getMaximumNoDamageTicks();
        }
    }
    private HashMap<Match, PlayerData[]> matches = new HashMap<>();
    public MatchStartManager(){
        DuelsCombo.getDuelsAPI().registerListener(this);
    }
    @EventHandler
    void onMatchStart(MatchStartEvent event){
        KitData kit = DuelsCombo.getSaveDataManager().getKit(event.getMatch().getKit().getName());
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
        KitData kit = DuelsCombo.getSaveDataManager().getKit(event.getMatch().getKit().getName());
        if(kit == null) return;
        if(kit.isCombo()){
            PlayerData[] players = matches.remove(event.getMatch());
            if(players == null){
                DuelsCombo.getInstance().getLogger().log(Level.SEVERE, "Warning: PlayerData was null. MaxNoDamageTicks left at match setting.");
                return;
            }
            for (PlayerData data: players) {
                data.p.setMaximumNoDamageTicks(data.prevMaxNoDamageTicks);
            }
        }
    }

}