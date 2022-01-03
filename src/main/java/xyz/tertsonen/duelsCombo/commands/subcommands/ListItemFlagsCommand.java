package xyz.tertsonen.duelsCombo.commands.subcommands;

import me.realized.duels.api.Duels;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.tertsonen.duelsCombo.DuelsCombo;
import xyz.tertsonen.duelsCombo.commands.Command;
import xyz.tertsonen.duelsCombo.customItems.ItemFlag;

import java.util.logging.Level;

public class ListItemFlagsCommand extends Command {
    Duels duelsAPI;
    DuelsCombo plugin;
    public ListItemFlagsCommand(final DuelsCombo plugin, final Duels api){
        super(plugin, api, "listitemflags", "listitemflags", plugin.getLang().getSetOptionsDesc(),1, false);
        this.duelsAPI = api;
        this.plugin = plugin;
    }
    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        try{
            if(!(sender instanceof Player)){
                plugin.getLang().sendTo(sender, plugin.getLang().getOnlyInGameCommand());
                return;
            }
            Player player = (Player) sender;
            ItemStack item = player.getInventory().getItemInMainHand();
            if(item.getItemMeta() == null){
                plugin.getLang().sendTo(player, plugin.getLang().getNoFlagsMessage());
                return;
            }

            PersistentDataContainer cont = item.getItemMeta().getPersistentDataContainer();
            StringBuilder toSend = new StringBuilder();
            toSend.append(DuelsCombo.getInstance().getLang().getFlagsHeader()).append('\n');
            boolean hasKeys = false;
            for (NamespacedKey key : cont.getKeys()) {
                ItemFlag flag = ItemFlag.getByNameSpacedKey(key);
                if(flag == null) continue;
                Object val = cont.get(key, (PersistentDataType<?, ?>) flag.getType().dataType);
                if(val == null) continue;
                hasKeys = true;
                toSend.append(String.format(plugin.getLang().getItemFlagFormat(),
                        flag.getUserFriendlyName(),
                        val)).append('\n');
            }
            if(hasKeys){
                DuelsCombo.getInstance().getLang().sendTo(sender, toSend.append('\n').append(DuelsCombo.getInstance().getLang().getFlagsFooter()).toString(), false);
                return;
            }
            plugin.getLang().sendTo(player, plugin.getLang().getNoFlagsMessage());
        }
        catch(Exception ex){
            plugin.getLang().sendTo(sender, plugin.getLang().getExceptionWhileRunningCommand(), true);
            DuelsCombo.getInstance().getLogger().log(Level.SEVERE, plugin.getLang().getExceptionWhileRunningCommand());
            ex.printStackTrace();
        }

    }
}
