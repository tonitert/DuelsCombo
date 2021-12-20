package com.tonero.duelsCombo.commands.subcommands;

import com.tonero.duelsCombo.DuelsCombo;
import com.tonero.duelsCombo.commands.Command;
import com.tonero.duelsCombo.customItems.ItemFlag;
import me.realized.duels.api.Duels;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class SetFlagCommand extends Command {

	private final DuelsCombo plugin;

	public SetFlagCommand(final DuelsCombo plugin, final Duels api){
		super(plugin, api, "setitemflag", "setitemflag [flag] [value]", plugin.getLang().getSetItemStatsDesc(),3, false);
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		if(args.length < 4){
			plugin.getLang().sendTo(sender, plugin.getLang().getUsageFormat() + super.getUsage(), true);
			return;
		}

		if(!(sender instanceof Player)){
			sender.sendMessage(plugin.getLang().getOnlyInGameCommand());
			return;
		}

		ItemFlag flag = ItemFlag.getByUserFriendlyName(args[2]);
		if(flag == null){
			plugin.getLang().sendTo(sender, plugin.getLang().getInvalidFlag());
			return;
		}

		Player player = (Player) sender;
		ItemStack item = player.getInventory().getItemInMainHand();
		if(item.getType().equals(Material.AIR)){
			plugin.getLang().sendTo(sender, plugin.getLang().getNoItemInHand());
			return;
		}

		ItemMeta itemMeta = item.getItemMeta();
		if(itemMeta == null){
			plugin.getLang().sendTo(sender, plugin.getLang().getNoItemInHand());
			return;
		}

		try{
			switch (flag.getType()){
				case BOOL:
					boolean bool = Boolean.parseBoolean(args[3]);
					if(bool){
						itemMeta.getPersistentDataContainer().set(flag.getNamespacedKey(), PersistentDataType.SHORT, (short) 1);
					}else{
						Short val = itemMeta.getPersistentDataContainer().get(flag.getNamespacedKey(), PersistentDataType.SHORT);
						if(val != null){
							itemMeta.getPersistentDataContainer().remove(flag.getNamespacedKey());
						}
					}
					break;
				case DOUBLE:
					Double dbl = Double.parseDouble(args[3]);
					itemMeta.getPersistentDataContainer().set(flag.getNamespacedKey(), PersistentDataType.DOUBLE, dbl);
					break;
				case INT:
					Integer integer = Integer.parseInt(args[3]);
					itemMeta.getPersistentDataContainer().set(flag.getNamespacedKey(), PersistentDataType.INTEGER, integer);
					break;
			}
		}
		catch (NumberFormatException e){
			plugin.getLang().sendTo(sender, plugin.getLang().getUsageFormat());
			return;
		}

		item.setItemMeta(itemMeta);
		plugin.getLang().sendTo(sender, plugin.getLang().getSetOptionsSuccess(), true);
	}
}
