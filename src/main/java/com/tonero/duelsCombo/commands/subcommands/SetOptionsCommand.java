package com.tonero.duelsCombo.commands.subcommands;

import com.tonero.duelsCombo.DuelsCombo;
import com.tonero.duelsCombo.commands.Command;
import com.tonero.duelsCombo.saveData.KitData;
import me.realized.duels.api.Duels;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

public class SetOptionsCommand extends Command {
    Duels duelsAPI;
    DuelsCombo plugin;
    public SetOptionsCommand(final DuelsCombo plugin, final Duels api){
        super(plugin, api, "set", "set [kit] [comboDuel:true|false] [noDamageTicks]", plugin.getLang().getSetOptionsDesc(),5, false);
        this.duelsAPI = api;
        this.plugin = plugin;
    }
    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        try{
            if(args.length != 5){
                plugin.getLang().sendTo(sender, plugin.getLang().getUsageFormat() + super.getUsage(), true);
                return;
            }
            KitData kitData = plugin.getSaveDataManager().getKit(args[2]);
            if(kitData == null){
                plugin.getLang().sendTo(sender, plugin.getLang().getInvalidKit(), true);
                return;
            }
            boolean isComboDuel = Boolean.parseBoolean(args[3]);
            if(kitData == null){
                plugin.getLang().sendTo(sender, plugin.getLang().getUsageFormat() + super.getUsage(), true);
                return;
            }
            int noDamageTicks = 1;
            try{
                noDamageTicks = Integer.parseInt(args[4]);
            }
            catch(Exception e){
                plugin.getLang().sendTo(sender, plugin.getLang().getUsageFormat() + super.getUsage(), true);
                return;
            }
            if(noDamageTicks < 0){
                plugin.getLang().sendTo(sender, plugin.getLang().getInvalidNoDamageTicks(), true);
                return;
            }
            kitData.setMaxNoDamageTicks(noDamageTicks);
            kitData.setCombo(isComboDuel);
            kitData.saveChanges();
            plugin.getLang().sendTo(sender, plugin.getLang().getSetOptionsSuccess(), true);
        }
        catch(Exception ex){
            plugin.getLang().sendTo(sender, plugin.getLang().getExceptionWhileRunningCommand(), true);
            DuelsCombo.getInstance().getLogger().log(Level.SEVERE, plugin.getLang().getExceptionWhileRunningCommand());
            ex.printStackTrace();
        }

    }
}
