package xyz.tertsonen.duelsCombo.commands;

import lombok.Getter;
import me.realized.duels.api.Duels;
import org.bukkit.command.CommandSender;
import xyz.tertsonen.duelsCombo.DuelsCombo;

public abstract class Command {
    @Getter
    private final String name;
    @Getter
    private final String usage;
    @Getter
    private final String description;
    @Getter
    private final int length;
    @Getter
    private final boolean playerOnly;

    protected Command(final DuelsCombo extension, final Duels api, final String name, final String usage, final String description, final int length, final boolean playerOnly) {
        this.name = name;
        this.usage = usage;
        this.description = description;
        this.length = length;
        this.playerOnly = playerOnly;
    }

    public abstract void execute(final CommandSender sender, final String label, final String[] args);
}
