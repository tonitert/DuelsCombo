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
    private static Duels duelsAPI = null;
    @Getter
    private static Lang lang;
    @Getter
    private static SaveDataManager saveDataManager;
    @Getter
    private static MatchStartManager matchStartManager;

    @Override
    public void onEnable(){

        instance = this;
        lang = new Lang();
        duelsAPI = (Duels) Bukkit.getServer().getPluginManager().getPlugin("Duels");
        saveDataManager = new SaveDataManager(getDataFolder());
        matchStartManager = new MatchStartManager();
        duelsAPI.registerSubCommand("duels", new Commands(this,duelsAPI));


    }
    @Override
    public void onDisable(){

    }

}
