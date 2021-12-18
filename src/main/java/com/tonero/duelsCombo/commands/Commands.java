package com.tonero.duelsCombo.commands;

import com.tonero.duelsCombo.DuelsCombo;
import com.tonero.duelsCombo.Lang;
import com.tonero.duelsCombo.commands.subcommands.ListCommand;
import com.tonero.duelsCombo.commands.subcommands.SetComboCommand;
import com.tonero.duelsCombo.commands.subcommands.SetItemStatsCommand;
import me.realized.duels.api.Duels;
import me.realized.duels.api.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

//TODO replace passing of DuelsAPI references and plugin references to the static methods in the DuelsCombo class
public class Commands extends SubCommand {

    private final Map<String, Command> subCommands = new HashMap<>();
    private final Lang lang;
    private final DuelsCombo duelsCombo;
    private final Duels duelsAPI;

    public Commands(DuelsCombo plugin, Duels api){
        super("combo", null, null, "duels.admin", false, 1, "c");
        duelsCombo = plugin;
        lang = duelsCombo.getLang();
        duelsAPI = api;
        register(
                new SetComboCommand(duelsCombo, duelsAPI),
                new ListCommand(duelsCombo, duelsAPI),
                new SetItemStatsCommand(duelsCombo, duelsAPI)
        );

    }
    private void register(final Command... commands) {
        for (final Command command : commands) {
            this.subCommands.put(command.getName(), command);
        }
    }
    @Override
    public void execute(final CommandSender sender, final String label, final String[] args) {
        if(args.length == getLength()){
            lang.sendTo(sender, lang.getHelpHeader(), false);
            subCommands.values().forEach(command -> lang.sendTo(sender, label + " " + args[0] + " " + command.getUsage() + "\n" + command.getDescription(), false));
            lang.sendTo(sender, lang.getHelpFooter(), false);
            return;
        }
        final Command command = subCommands.get(args[1].toLowerCase());
        if(command == null){
            lang.sendTo(sender, lang.getHelpHeader(), false);
            subCommands.values().forEach(cmd -> lang.sendTo(sender, label + " " + args[0] + " " + cmd.getUsage() + "\n" + cmd.getDescription(), false));
            lang.sendTo(sender, lang.getHelpFooter(), false);
            return;
        }
        if (command.isPlayerOnly() && !(sender instanceof Player)) {
            lang.sendTo(sender, lang.getPlayerOnly(), true);
            return;
        }
        if (args.length < command.getLength()) {
            lang.sendTo(sender, label + " " + args[0] + " " + command.getUsage() + "\n" + command.getDescription(), false);
            return;
        }
        command.execute(sender, label, args);

    }
}
