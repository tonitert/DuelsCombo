package xyz.tertsonen.duelsCombo.commands.subcommands;

import me.realized.duels.api.Duels;
import org.bukkit.command.CommandSender;
import xyz.tertsonen.duelsCombo.DuelsCombo;
import xyz.tertsonen.duelsCombo.commands.Command;
import xyz.tertsonen.duelsCombo.saveData.KitData;
import xyz.tertsonen.duelsCombo.saveData.SaveDataManager;

import java.util.Map;
import java.util.logging.Level;

public class ListCommand extends Command {
    Duels duelsAPI;
    DuelsCombo plugin;
    public ListCommand(final DuelsCombo plugin, final Duels api){
        super(plugin, api, "list", "list", plugin.getLang().getSetOptionsDesc(),2, false);
        this.duelsAPI = api;
        this.plugin = plugin;
    }
    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        try{
            SaveDataManager mgr = DuelsCombo.getInstance().getSaveDataManager();
            if(mgr.getKits().isEmpty()){
                DuelsCombo.getInstance().getLang().sendTo(sender, DuelsCombo.getInstance().getLang().getNoKitsMessage(), true);
                return;
            }
            DuelsCombo.getInstance().getLang().sendTo(sender, DuelsCombo.getInstance().getLang().getKitsHeader(), false);
            for (Map.Entry<String, KitData> e: mgr.getKits().entrySet()) {
                DuelsCombo.getInstance().getLang().sendTo(sender,
                        String.format(DuelsCombo.getInstance().getLang().getKitFormat(),
                                e.getKey(),
                                e.getValue().isCombo(),
                                e.getValue().getMaxNoDamageTicks(),
                                e.getValue().getKnockbackMultiplier(),
                                e.getValue().getMaxKnockbackSpeedMultiplier(),
                                e.getValue().getYKnockbackAmount()), false);
            }
            DuelsCombo.getInstance().getLang().sendTo(sender, DuelsCombo.getInstance().getLang().getKitsFooter(), false);
        }
        catch(Exception ex){
            plugin.getLang().sendTo(sender, plugin.getLang().getExceptionWhileRunningCommand(), true);
            DuelsCombo.getInstance().getLogger().log(Level.SEVERE, plugin.getLang().getExceptionWhileRunningCommand());
            ex.printStackTrace();
        }

    }
}
