package com.tonero.duelsCombo.commands.subcommands;

import com.tonero.duelsCombo.DuelsCombo;
import com.tonero.duelsCombo.commands.Command;
import com.tonero.duelsCombo.customItems.ItemFlag;
import me.realized.duels.api.Duels;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

public class ListFlagsCommand extends Command {
    Duels duelsAPI;
    DuelsCombo plugin;
    public ListFlagsCommand(final DuelsCombo plugin, final Duels api){
        super(plugin, api, "listflags", "listflags", plugin.getLang().getSetOptionsDesc(),1, false);
        this.duelsAPI = api;
        this.plugin = plugin;
    }
    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        try{
            DuelsCombo.getInstance().getLang().sendTo(sender, DuelsCombo.getInstance().getLang().getFlagsHeader(), false);
            for (ItemFlag flag : ItemFlag.values()) {
                DuelsCombo.getInstance().getLang().sendTo(sender, String.format(DuelsCombo.getInstance().getLang().getFlagFormat(), flag.getUserFriendlyName(), flag.getType().toString().toLowerCase()), false);
            }
            DuelsCombo.getInstance().getLang().sendTo(sender, DuelsCombo.getInstance().getLang().getFlagsFooter(), false);
        }
        catch(Exception ex){
            plugin.getLang().sendTo(sender, plugin.getLang().getExceptionWhileRunningCommand(), true);
            DuelsCombo.getInstance().getLogger().log(Level.SEVERE, plugin.getLang().getExceptionWhileRunningCommand());
            ex.printStackTrace();
        }

    }
}
