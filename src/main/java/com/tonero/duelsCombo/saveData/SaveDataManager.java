package com.tonero.duelsCombo.saveData;

import com.tonero.duelsCombo.DuelsCombo;
import me.realized.duels.api.kit.Kit;

import java.io.File;
import java.util.HashMap;

public class SaveDataManager {
    private HashMap<String, KitData> kits = new HashMap<>();
    /**
     * @param name Kit name
     * @return Returns a KitData object if the specified kit exists.
     */
    public KitData getKit(String name){
        KitData kitData = kits.get(name);
        if(kitData != null){
            return kitData;
        }
        Kit kit = DuelsCombo.getDuelsAPI().getKitManager().get(name);
        if(kit == null){
            return null;
        }
        kitData = new KitData(kitsFolderPath + name + ".yml", name, true);
        kits.put(name, kitData);
        return kitData;
    }
    public HashMap<String, KitData> getKits(){
        return new HashMap<String, KitData> (kits);
    }
    File dataFolder;
    private String kitsFolderPath;

    public SaveDataManager(File dataFolder){
        this.dataFolder = dataFolder;
        if(!dataFolder.exists()){
            dataFolder.mkdir();
        }
        kitsFolderPath = "./" + dataFolder.getPath() + "/kits/";
        File kitsFolder = new File(kitsFolderPath);
        if(!kitsFolder.exists()){
            kitsFolder.mkdir();
            return;
        }
        String[] contents = kitsFolder.list();
        for (String path: contents) {
            if(path.endsWith(".yml")){
                KitData kitData = new KitData(kitsFolderPath + path, path.substring(0,path.lastIndexOf(".")), false);
                if(kitData.getKitName() != null && kitData.isExists() != false){
                    kits.put(kitData.getKitName(), kitData);
                }
            }
        }

    }

}
