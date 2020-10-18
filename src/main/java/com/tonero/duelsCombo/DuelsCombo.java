package com.tonero.duelsCombo;

import com.tonero.duelsCombo.commands.Commands;
import com.tonero.duelsCombo.saveData.SaveDataManager;
import lombok.Getter;
import me.realized.duels.api.Duels;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DuelsCombo extends JavaPlugin {
    @Getter
    private static DuelsCombo instance;
    @Getter
    private Duels duelsAPI = null;
    @Getter
    private Lang lang;
    @Getter
    private SaveDataManager saveDataManager;
    @Getter
    private MatchStartManager matchStartManager;
    @Getter
    private boolean active = false;

    @Override
    public void onEnable(){

        instance = this;
        lang = new Lang();
        duelsAPI = (Duels) Bukkit.getServer().getPluginManager().getPlugin("Duels");
        saveDataManager = new SaveDataManager(getDataFolder());
        if(matchStartManager == null){
            matchStartManager = new MatchStartManager();
            duelsAPI.registerSubCommand("duels", new Commands(this,duelsAPI));
        }
        active = true;
    }
    @Override
    public void onDisable(){
        saveDataManager = null;
        lang = null;
        active = false;

    }

}
