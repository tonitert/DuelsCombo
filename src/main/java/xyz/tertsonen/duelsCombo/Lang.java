package xyz.tertsonen.duelsCombo;


import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

//TODO: Load strings from file
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
    private final String listFlagsDesc = "Näyttää kaikki mahdolliset flagit.";
    @Getter
    private final String listDesc = "Näyttää kaikkien kittien asetukset.";
    @Getter
    private final String setComboDesc = "Asettaa kitin asetukset.";
    @Getter
    private final String listItemFlagsDesc = "Näyttää esineen flagit.";
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
    private final String kitFormat = "&7Kit: %s Combo: %b No damage ticks: %o Knockback multiplier: %f Maximum knockback multiplier: %f Y axis knockback: %f";
    @Getter
    private final String kitsFooter = "&7-------------------------------------";
    @Getter
    private final String noKitsMessage = "Kittejä ei löytynyt.";
    @Getter
    private final String onlyInGameCommand = "Tämä komento toimii vain pelaajan kutsumana.";
    @Getter
    private final String noItemInHand = "Sinulla täytyy olla esine kädessä.";
    @Getter
    private final String invalidFlag = "Tuota flagia ei löytynyt.";
    @Getter
    private final String flagsHeader = "&7--------------- &9Flagit:&7 ---------------";
    @Getter
    private final String flagFormat = "&7Flag: %s Tyyppi: %s";
    @Getter
    private final String flagsFooter = "&7-------------------------------------";
    @Getter
    private final String itemFlagFormat = "&7Flag: %s Arvo: %s";
    @Getter
    private final String noFlagsMessage = "Flageja ei löytynyt.";


    public static String color(final String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public String format(String message, boolean prefix){
        return color((prefix ? "&9[DuelsCombo] &7" : "&7") + message);
    }

    public void sendTo(CommandSender sender, String string, boolean prefix){
        sender.sendMessage(format(string, prefix));
    }

    public void sendTo(CommandSender sender, String string){
        sendTo(sender, string, true);
    }
}
