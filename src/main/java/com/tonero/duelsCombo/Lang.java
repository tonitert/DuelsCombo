package com.tonero.duelsCombo;


import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Lang {
    @Getter
    private final String helpHeader = "&7--------------- &9Komennot:&7 ---------------";
    @Getter
    private final String helpFooter = "-----------------------------------------";
    @Getter
    private final String usageFormat = "Usage:";
    @Getter
    private final String playerOnly = "Vain pelaaja voi suorittaa tämän komennon.";
    @Getter
    private final String setOptionsDesc = "Asettaa kitin asetukset.";
    @Getter
    private final String setItemStatsDesc = "Asettaa kädessä olevan esineen asetukset.";
    @Getter
    private final String createItemDesc = "Luo esineen annetuilla tiedoilla. playerBowKnockback: Pelaajalle annettava knockback nuolta kohti.";
    @Getter
    private final String setOptionsSuccess = "Asetettu.";
    @Getter
    private final String invalidKit = "Tuota kittiä ei ole olemassa.";
    @Getter
    private final String invalidNoDamageTicks = "Argumentin noDamageTicks on oltava vähintään 0.";
    @Getter
    private final String exceptionWhileRunningCommand = "Komentoa suoritettaessa tapahtui virhe.";
    @Getter
    private final String kitsHeader = "&7--------------- &9Kitit:&7 ---------------";
    @Getter
    private final String kitFormat = "&7Kit: %s Combo: %b MaxNoDamageTicks: %o";
    @Getter
    private final String kitsFooter = "&7-------------------------------------";
    @Getter
    private final String onlyInGameCommand = "Tämä komento toimii vain pelaajan kutsumana.";
    @Getter
    private final String noItemInHand = "Sinulla täytyy olla esine kädessä.";


    public static String color(final String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public String format(String message, boolean prefix){
        return color((prefix ? "&9[DuelsCombo] &7" : "") + message);
    }

    public void sendTo(CommandSender sender, String string, boolean prefix){
        sender.sendMessage(format(string, prefix));
    }

    public void sendTo(CommandSender sender, String string){
        sendTo(sender, string, true);
    }
}
