package xyz.tertsonen.duelsCombo.commands.subcommands;

import me.realized.duels.api.Duels;
import org.bukkit.command.CommandSender;
import xyz.tertsonen.duelsCombo.DuelsCombo;
import xyz.tertsonen.duelsCombo.commands.Command;
import xyz.tertsonen.duelsCombo.customItems.ItemFlag;

import java.util.Locale;
import java.util.logging.Level;

public class ListFlagsCommand extends Command {
    Duels duelsAPI;
    DuelsCombo plugin;
    public ListFlagsCommand(final DuelsCombo plugin, final Duels api){
        super(plugin, api, "listflags", "listflags", plugin.getLang().getListFlagsDesc(),1, false);
        this.duelsAPI = api;
        this.plugin = plugin;
    }
    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        try{
            DuelsCombo.getInstance().getLang().sendTo(sender, DuelsCombo.getInstance().getLang().getFlagsHeader(), false);
            for (ItemFlag<?> flag : ItemFlag.VALUES) {
                /*
                    TODO: showing the class name to the user is not a good long term solution for implementing types for flags,
                     a wrapper class for PersistentDataType could be better
                 */
                DuelsCombo.getInstance().getLang().sendTo(sender, String.format(DuelsCombo.getInstance().getLang().getFlagFormat(), flag.getUserFriendlyName(), flag.getType().getComplexType().getSimpleName().toLowerCase(Locale.ROOT)), false);
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
