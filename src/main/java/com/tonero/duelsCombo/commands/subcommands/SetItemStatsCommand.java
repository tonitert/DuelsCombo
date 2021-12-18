package com.tonero.duelsCombo.commands.subcommands;

import com.tonero.duelsCombo.DuelsCombo;
import com.tonero.duelsCombo.commands.Command;
import com.tonero.duelsCombo.customItems.ComboNamespacedKey;
import me.realized.duels.api.Duels;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class SetItemStatsCommand extends Command {

	private Duels duelsAPI;

	private DuelsCombo plugin;

	public SetItemStatsCommand(final DuelsCombo plugin, final Duels api){
		super(plugin, api, "setitemstats", "setitemstats [shooterBowKnockback] [switchPositions]", plugin.getLang().getSetOptionsDesc(),3, false);
		this.duelsAPI = api;
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		if(args.length < 3){
			plugin.getLang().sendTo(sender, plugin.getLang().getUsageFormat() + super.getUsage(), true);
			return;
		}

		if(!(sender instanceof Player)){
			sender.sendMessage(plugin.getLang().getOnlyInGameCommand());
			return;
		}

		double playerBowKnockback;
		try{
			playerBowKnockback = Double.parseDouble(args[2]);
		}
		catch(NumberFormatException e){
			plugin.getLang().sendTo(sender, plugin.getLang().getUsageFormat());
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
		if(args.length >= 4){
			boolean switchPositions = Boolean.parseBoolean(args[3]);
			if(switchPositions){
				itemMeta.getPersistentDataContainer().set(ComboNamespacedKey.SWITCH_POSITIONS_ON_HIT.getNamespacedKey(), PersistentDataType.SHORT, (short) 1);
			}else{
				Short val = itemMeta.getPersistentDataContainer().get(ComboNamespacedKey.SWITCH_POSITIONS_ON_HIT.getNamespacedKey(), PersistentDataType.SHORT);
				if(val != null){
					itemMeta.getPersistentDataContainer().remove(ComboNamespacedKey.SWITCH_POSITIONS_ON_HIT.getNamespacedKey());
				}
			}
		}
		itemMeta.getPersistentDataContainer().set(ComboNamespacedKey.SHOOTER_BOW_KNOCKBACK.getNamespacedKey(), PersistentDataType.DOUBLE, playerBowKnockback);
		item.setItemMeta(itemMeta);
		plugin.getLang().sendTo(sender, plugin.getLang().getSetOptionsSuccess(), true);
	}
}
