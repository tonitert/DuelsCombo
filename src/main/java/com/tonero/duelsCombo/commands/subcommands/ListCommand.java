package com.tonero.duelsCombo.commands.subcommands;

import com.tonero.duelsCombo.DuelsCombo;
import com.tonero.duelsCombo.commands.Command;
import com.tonero.duelsCombo.saveData.KitData;
import me.realized.duels.api.Duels;
import org.bukkit.command.CommandSender;

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
            DuelsCombo.getLang().sendTo(sender, DuelsCombo.getLang().getKitsHeader(), false);
            for (Map.Entry<String, KitData> e: DuelsCombo.getSaveDataManager().getKits().entrySet()) {
                DuelsCombo.getLang().sendTo(sender, String.format(DuelsCombo.getLang().getKitFormat(), e.getKey(), e.getValue().isCombo(), e.getValue().getMaxNoDamageTicks()), false);
            }
            DuelsCombo.getLang().sendTo(sender, DuelsCombo.getLang().getKitsFooter(), false);
        }
        catch(Exception ex){
            plugin.getLang().sendTo(sender, plugin.getLang().getExceptionWhileRunningCommand(), true);
            DuelsCombo.getInstance().getLogger().log(Level.SEVERE, plugin.getLang().getExceptionWhileRunningCommand());
            ex.printStackTrace();
        }

    }
}
