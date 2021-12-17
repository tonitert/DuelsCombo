package com.tonero.duelsCombo.commands.subcommands;

import com.tonero.duelsCombo.DuelsCombo;
import com.tonero.duelsCombo.commands.Command;
import com.tonero.duelsCombo.saveData.KitData;
import me.realized.duels.api.Duels;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

public class SetComboCommand extends Command {
    Duels duelsAPI;
    DuelsCombo plugin;
    public SetComboCommand(final DuelsCombo plugin, final Duels api){
        super(plugin, api, "setcombo", "setCombo [kit] [comboDuel:true|false] [noDamageTicks] [knockbackMultiplier] [bowKnockbackMultiplier]", plugin.getLang().getSetOptionsDesc(),5, false);
        this.duelsAPI = api;
        this.plugin = plugin;
    }
    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        try{
            if(args.length < 5){
                plugin.getLang().sendTo(sender, plugin.getLang().getUsageFormat() + super.getUsage(), true);
                return;
            }
            KitData kitData = plugin.getSaveDataManager().getKit(args[2]);
            if(kitData == null){
                plugin.getLang().sendTo(sender, plugin.getLang().getInvalidKit(), true);
                return;
            }
            boolean isComboDuel;

            try{
                isComboDuel = Boolean.parseBoolean(args[3]);
            }
            catch(Exception e){
                plugin.getLang().sendTo(sender, plugin.getLang().getUsageFormat() + super.getUsage(), true);
                return;
            }

            int noDamageTicks;
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
            if(args.length >= 6){
                try{
                    double kbAmount = Double.parseDouble(args[5]);
                    kitData.setKnockbackMultiplier(kbAmount);
                }
                catch(Exception e){
                    plugin.getLang().sendTo(sender, plugin.getLang().getUsageFormat() + super.getUsage(), true);
                    return;
                }
            }
            if(args.length >= 7){
                try{
                    double kbAmount = Double.parseDouble(args[6]);
                    kitData.setBowKnockbackMultiplier(kbAmount);
                }
                catch(Exception e){
                    plugin.getLang().sendTo(sender, plugin.getLang().getUsageFormat() + super.getUsage(), true);
                    return;
                }
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
