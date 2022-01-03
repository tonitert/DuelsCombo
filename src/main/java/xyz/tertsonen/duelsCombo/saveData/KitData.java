package xyz.tertsonen.duelsCombo.saveData;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.tertsonen.duelsCombo.DuelsCombo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Logger;

@NoArgsConstructor
public class KitData {

    public static class InvalidIntegerException extends Exception {
        public InvalidIntegerException(String message) {
            super(message);
        }
    }
    private String configPath;

    @Getter
    @Setter
    private String kitName;

    @Getter
    @Setter
    private boolean combo;

    @Getter
    @Setter
    private double knockbackMultiplier = 1;

    @Getter
    @Setter
    private double maxKnockbackSpeedMultiplier = knockbackMultiplier*4;

    @Getter
    @Setter
    private double yKnockbackAmount = 0.3;

    @Getter
    private int maxNoDamageTicks;

    @Getter
    private boolean loaded = false;

    public void setMaxNoDamageTicks(int maxNoDamageTicks) throws InvalidIntegerException {
        if(maxNoDamageTicks < 0){
            throw new InvalidIntegerException("Integer maxScore must be above 1.");
        }
        this.maxNoDamageTicks = maxNoDamageTicks;
    }

    /**
     * Tries to load the specified kit from a file, and creates it if it does not exist
     * @param path path to kit save file
     * @param kitName kit name, name in already existing kit is preferred over this
     * @return true if succeeded
     */
    protected boolean loadOrCreateKit(@NotNull String path, String kitName, boolean createIfDoesntExist){
        if(!path.endsWith(".yml")){
            return false;
        }
        if(Files.exists(Paths.get(path))){
            try{
                YamlConfiguration kitConfig = YamlConfiguration.loadConfiguration(new File(path));
                parseKitData(kitConfig);
            }
            catch (Exception e){
                return false;
            }
        }else{
            if(createIfDoesntExist){
                this.kitName = kitName;
                this.configPath = path;
                saveChanges();
            }else return false;
        }
        this.configPath = path;
        loaded = true;
        return true;
    }

    protected boolean deleteKit() throws IOException {
        if(!loaded) return false;
        loaded = false;
        return Files.deleteIfExists(Paths.get(configPath));
    }

    private boolean parseBoolean(Object object){
        try{
            return (boolean) object;
        }
        catch(Exception e){
            return false;
        }
    }

    @Contract(pure = true)
    private @Nullable String parseString(Object object){
        try{
            return (String) object;
        }
        catch(Exception e){
            return null;
        }
    }

    private void parseKitData(@NotNull YamlConfiguration yamlData){
        this.kitName = parseString(yamlData.get("name"));
        this.combo = parseBoolean(yamlData.get("combo"));
        this.maxNoDamageTicks = yamlData.getInt("maxNoDamageTicks", 20);
        this.knockbackMultiplier = yamlData.getDouble("knockbackMultiplier", 1);
        this.maxKnockbackSpeedMultiplier = yamlData.getDouble("maxKnockbackSpeedMultiplier", knockbackMultiplier*4);
        this.yKnockbackAmount = yamlData.getDouble("yKnockbackAmount", 0.3);
    }

    public void saveChanges(){
        YamlConfiguration yamlData = new YamlConfiguration();
        yamlData.set("knockbackMultiplier", knockbackMultiplier);
        yamlData.set("maxNoDamageTicks", maxNoDamageTicks);
        yamlData.set("name", kitName);
        yamlData.set("combo", combo);
        yamlData.set("maxKnockbackSpeedMultiplier", knockbackMultiplier);
        yamlData.set("yKnockbackAmount", yKnockbackAmount);
        String data = yamlData.saveToString();
        Logger log = DuelsCombo.getInstance().getLogger();
        new BukkitRunnable() {
            @Override
            public void run() {
                try{
                    Files.write(Paths.get(configPath), data.getBytes());
                }
                catch(Exception ex){
                    log.severe("Exception while writing file:");
                    log.severe(ex.getMessage());
                    log.severe(Arrays.toString(ex.getStackTrace()));
                }
            }
        }.runTaskAsynchronously(DuelsCombo.getInstance());
    }


}
