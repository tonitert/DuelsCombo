package xyz.tertsonen.duelsCombo;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import me.realized.duels.api.Duels;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.tertsonen.duelsCombo.commands.Commands;
import xyz.tertsonen.duelsCombo.saveData.SaveDataManager;
import xyz.tertsonen.duelsCombo.saveData.SaveHandlerImpl;

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
    private BukkitListener bukkitListener;
    @Getter
    private boolean active = false;
    @Getter
    private ProtocolManager protocolManager;

    @Override
    public void onEnable(){
        instance = this;
        lang = new Lang();
        duelsAPI = (Duels) Bukkit.getServer().getPluginManager().getPlugin("Duels");
        if(duelsAPI == null){
            getLogger().severe("Duels plugin cannot be found. Disabling plugin..");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        saveDataManager = new SaveDataManager(getDataFolder(), duelsAPI.getKitManager(), new SaveHandlerImpl());
        if(matchStartManager == null){
            matchStartManager = new MatchStartManager();
            bukkitListener = new BukkitListener();
            duelsAPI.registerSubCommand("duels", new Commands(this,duelsAPI));
        }
        active = true;
        protocolManager = ProtocolLibrary.getProtocolManager();
    }
    @Override
    public void onDisable(){
        instance = null;
        active = false;
    }
}
