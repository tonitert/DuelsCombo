package com.tonero.duelsCombo;


import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Lang {
    @Getter
    private String helpHeader = "&7--------------- &9Komennot:&7 ---------------";
    @Getter
    private String helpFooter = "-----------------------------------------";
    @Getter
    private String usageFormat = "Usage:";
    @Getter
    private String playerOnly = "Vain pelaaja voi suorittaa tämän komennon.";
    @Getter
    private String setOptionsDesc = "Asettaa kitin asetukset.";
    @Getter
    private String createItemDesc = "Luo esineen annetuilla tiedoilla. playerBowKnockback: Pelaajalle annettava knockback nuolta kohti.";
    @Getter
    private String setOptionsSuccess = "Asetettu.";
    @Getter
    private String invalidKit = "Tuota kittiä ei ole olemassa.";
    @Getter
    private String invalidNoDamageTicks = "Argumentin noDamageTicks on oltava vähintään 0";
    @Getter
    private String exceptionWhileRunningCommand = "Komentoa suoritattaessa tapahtui virhe.";
    @Getter
    private String kitsHeader = "&7--------------- &9Kitit:&7 ---------------";
    @Getter
    private String kitFormat = "&7Kit: %s Combo: %b MaxNoDamageTicks: %o";
    @Getter
    private String kitsFooter = "&7-------------------------------------";
    @Getter
    private String onlyInGameCommand = "Tämä komento toimii vain pelaajan kutsumana.";
    @Getter
    private String noItemInHand = "Sinulla täytyy olla esine kädessä.";


    public static String color(final String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public String format(String message, boolean prefix){
        return color((prefix ? "&9[Duels] &7" : "") + message);
    }

    public void sendTo(CommandSender sender, String string, boolean prefix){
        sender.sendMessage(format(string, prefix));
    }

    public void sendTo(CommandSender sender, String string){
        sendTo(sender, string, true);
    }
}
