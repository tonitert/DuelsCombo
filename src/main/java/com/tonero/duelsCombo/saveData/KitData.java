package com.tonero.duelsCombo.saveData;

import com.tonero.duelsCombo.DuelsCombo;
import lombok.Getter;
import me.realized.duels.api.kit.KitManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class KitData {
    public class InvalidIntegerException extends Exception {

        public InvalidIntegerException(String message) {
            super(message);
        }
    }
    private YamlConfiguration yamlData;
    @Getter
    private boolean exists;
    private String configPath;
    @Getter
    private String kitName;
    private void setKitName(String name){
        this.kitName = name;
        yamlData.set("name", name);
    }
    @Getter
    private boolean combo;
    public void setCombo(boolean combo){
        this.combo = combo;
        yamlData.set("combo", combo);
    }
    @Getter
    private int maxNoDamageTicks;
    public void setMaxNoDamageTicks(int maxNoDamageTicks) throws InvalidIntegerException {
        if(maxNoDamageTicks < 0){
            throw new InvalidIntegerException("Integer maxScore must be above 1.");
        }
        this.maxNoDamageTicks = maxNoDamageTicks;
        yamlData.set("maxNoDamageTicks", maxNoDamageTicks);
    }

    public KitData(YamlConfiguration yamlData, String configPath){
        this.yamlData = yamlData;
        this.configPath = configPath;
    }
    public KitData(String configPath, String kitName, boolean createFile){
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
            if(kitManager.get(arenaName) == null){
                exists = false;
                return;
            }
            exists = true;
            this.yamlData = kitConfig;
            parseKitData();

        }
        catch(Exception ex){
            System.out.println("Error while parsing arena configuration at " + path + ":");
            System.out.println(ex);
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
    }
    public void saveChanges(){
        String data = yamlData.saveToString();
        new BukkitRunnable() {
            @Override
            public void run() {
                try{
                    Files.write(Paths.get(configPath), data.getBytes());
                }
                catch(Exception ex){
                    System.out.println("Exception while writing file:");
                    System.out.println(ex.getMessage());
                    ex.printStackTrace(System.out);
                }
            }
        }.runTaskAsynchronously(DuelsCombo.getInstance());
    }


}
