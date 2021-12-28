package xyz.tertsonen.duelsCombo.commands;

import me.realized.duels.api.Duels;
import me.realized.duels.api.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.tertsonen.duelsCombo.DuelsCombo;
import xyz.tertsonen.duelsCombo.Lang;
import xyz.tertsonen.duelsCombo.commands.subcommands.ListCommand;
import xyz.tertsonen.duelsCombo.commands.subcommands.ListFlagsCommand;
import xyz.tertsonen.duelsCombo.commands.subcommands.SetComboCommand;
import xyz.tertsonen.duelsCombo.commands.subcommands.SetFlagCommand;

import java.util.HashMap;
import java.util.Map;

public class Commands extends SubCommand {

    private final Map<String, Command> subCommands = new HashMap<>();
    private final Lang lang;

    public Commands(DuelsCombo plugin, Duels api){
        super("combo", null, null, "duels.admin", false, 1, "c");
        lang = plugin.getLang();
        register(
                new SetComboCommand(plugin, api),
                new ListCommand(plugin, api),
                new SetFlagCommand(plugin, api),
                new ListFlagsCommand(plugin, api)
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
            lang.sendTo(sender, lang.getHelpHeader(), true);
            subCommands.values().forEach(command -> lang.sendTo(sender, label + " " + args[0] + " " + command.getUsage() + "\n" + command.getDescription(), true));
            lang.sendTo(sender, lang.getHelpFooter(), true);
            return;
        }
        final Command command = subCommands.get(args[1].toLowerCase());
        if(command == null){
            lang.sendTo(sender, lang.getHelpHeader(), true);
            subCommands.values().forEach(cmd -> lang.sendTo(sender, label + " " + args[0] + " " + cmd.getUsage() + "\n" + cmd.getDescription(), true));
            lang.sendTo(sender, lang.getHelpFooter(), true);
            return;
        }
        if (command.isPlayerOnly() && !(sender instanceof Player)) {
            lang.sendTo(sender, lang.getPlayerOnly(), true);
            return;
        }
        if (args.length < command.getLength()) {
            lang.sendTo(sender, label + " " + args[0] + " " + command.getUsage() + "\n" + command.getDescription(), true);
            return;
        }
        command.execute(sender, label, args);

    }
}
