package com.tonero.duelsCombo.commands.subcommands;

import com.tonero.duelsCombo.DuelsCombo;
import com.tonero.duelsCombo.commands.Command;
import me.realized.duels.api.Duels;
import org.bukkit.command.CommandSender;

public class CreateItemCommand extends Command {

	private Duels duelsAPI;

	private DuelsCombo plugin;

	public CreateItemCommand(final DuelsCombo plugin, final Duels api){
		super(plugin, api, "createitem", "createitem [item id] [playerBowKnockback]", plugin.getLang().getSetOptionsDesc(),2, false);
		this.duelsAPI = api;
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {

	}
}
