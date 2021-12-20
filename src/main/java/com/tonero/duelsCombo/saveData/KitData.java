package com.tonero.duelsCombo.saveData;

import com.tonero.duelsCombo.DuelsCombo;
import lombok.Getter;
import lombok.Setter;
import me.realized.duels.api.kit.KitManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KitData {

    public static class InvalidIntegerException extends Exception {

        public InvalidIntegerException(String message) {
            super(message);
        }
    }

    private YamlConfiguration yamlData;

    @Getter
    private boolean exists;

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
    private double bowKnockbackMultiplier = 1;

    @Getter
    @Setter
    private double maxKnockbackSpeedMultiplier = knockbackMultiplier*4;

    @Getter
    @Setter
    private double yKnockbackAmount = 0.3;

    @Getter
    private int maxNoDamageTicks;
    public void setMaxNoDamageTicks(int maxNoDamageTicks) throws InvalidIntegerException {
        if(maxNoDamageTicks < 0){
            throw new InvalidIntegerException("Integer maxScore must be above 1.");
        }
        this.maxNoDamageTicks = maxNoDamageTicks;
    }

    public KitData(YamlConfiguration yamlData, String configPath){
        this.yamlData = yamlData;
        this.configPath = configPath;
    }

    public KitData(String configPath, String kitName, boolean createFile) {
        this.kitName = kitName;
        if(createFile){
            this.configPath = configPath;
            if(Files.exists(Paths.get(configPath))){
                loadKitData(configPath, DuelsCombo.getInstance().getDuelsAPI().getKitManager());
            }else{
                this.yamlData = new YamlConfiguration();
                yamlData.set("name", kitName);
                exists = true;
                saveChanges();
            }

        }else{
            this.configPath = configPath;
            if(Files.exists(Paths.get(configPath))){
                loadKitData(configPath, DuelsCombo.getInstance().getDuelsAPI().getKitManager());
            }else{
                this.exists = false;
            }
        }
    }

    private void loadKitData(String path, KitManager kitManager){

        if(!path.endsWith(".yml")){
            exists = false;
            return;
        }
        YamlConfiguration kitConfig = YamlConfiguration.loadConfiguration(new File(path));
        try{
            String arenaName = kitConfig.getString("name");
            if(arenaName == null){
                exists = false;
                DuelsCombo.getInstance().getLogger().warning("Arena at " + path + " doesn't have a name parameter, skipping");
                return;
            }
            if(kitManager.get(arenaName) == null){
                exists = false;
                DuelsCombo.getInstance().getLogger().warning("Arena " + arenaName + " wasn't found, skipping");
                return;
            }
            exists = true;
            this.yamlData = kitConfig;
            parseKitData();
        }
        catch(Exception ex){
            DuelsCombo.getInstance().getLogger().log(Level.SEVERE,"Error while parsing arena configuration at " + path + ":\n");
            ex.printStackTrace();
            this.yamlData = new YamlConfiguration();
            exists = false;
        }
    }

    private boolean parseBoolean(Object object){
        try{
            return (boolean) object;
        }
        catch(Exception e){
            return false;
        }
    }

    private String parseString(Object object){
        try{
            return (String) object;
        }
        catch(Exception e){
            return null;
        }
    }

    private void parseKitData(){
        this.kitName = parseString(yamlData.get("name"));
        this.combo = parseBoolean(yamlData.get("combo"));
        this.maxNoDamageTicks = yamlData.getInt("maxNoDamageTicks", 0);
        this.knockbackMultiplier = yamlData.getDouble("knockbackMultiplier", 1);
        this.bowKnockbackMultiplier = yamlData.getDouble("bowKnockbackMultiplier", 1);
        this.maxKnockbackSpeedMultiplier = yamlData.getDouble("maxKnockbackSpeedMultiplier", knockbackMultiplier*4);
        this.yKnockbackAmount = yamlData.getDouble("yKnockbackAmount", 0.3);
    }

    public void saveChanges(){
        yamlData.set("bowKnockbackMultiplier", bowKnockbackMultiplier);
        yamlData.set("knockbackMultiplier", knockbackMultiplier);
        yamlData.set("maxNoDamageTicks", 0);
        yamlData.set("name", kitName);
        yamlData.set("combo", combo);
        yamlData.set("maxKnockbackSpeedMultiplier", knockbackMultiplier);
        yamlData.set("yKnockbackAmount", yKnockbackAmount);
        String data = yamlData.saveToString();
        new BukkitRunnable() {
            @Override
            public void run() {
                try{
                    Files.write(Paths.get(configPath), data.getBytes());
                }
                catch(Exception ex){
                    Logger log = DuelsCombo.getInstance().getLogger();
                    log.severe("Exception while writing file:");
                    log.severe(ex.getMessage());
                    log.severe(Arrays.toString(ex.getStackTrace()));
                }
            }
        }.runTaskAsynchronously(DuelsCombo.getInstance());
    }


}
