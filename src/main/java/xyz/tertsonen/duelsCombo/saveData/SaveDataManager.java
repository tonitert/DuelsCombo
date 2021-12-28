package xyz.tertsonen.duelsCombo.saveData;

import me.realized.duels.api.kit.Kit;
import org.jetbrains.annotations.Nullable;
import xyz.tertsonen.duelsCombo.DuelsCombo;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class SaveDataManager {
    private final HashMap<String, KitData> kits = new HashMap<>();
    /**
     * @param name Kit name
     * @return Returns a KitData object if the specified kit exists.
     */
    @Nullable
    public KitData getKit(String name){
        if(!kitsFolderCreated) return null;
        return kits.get(name);
    }

    public KitData getOrCreate(String name){
        if(!kitsFolderCreated) return null;
        KitData kitData = kits.get(name);
        if(kitData != null) return kitData;
        Kit kit = DuelsCombo.getInstance().getDuelsAPI().getKitManager().get(name);
        if(kit == null){
            return null;
        }
        kitData = new KitData();
        if(!kitData.loadOrCreateKit(kitsFolderPath + name + ".yml", name, true)) return null;
        kits.put(name, kitData);
        return kitData;
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, KitData> getKits(){
        return (HashMap<String, KitData>) kits.clone();
    }

    private String kitsFolderPath;
    private boolean kitsFolderCreated = false;

    public SaveDataManager(File dataFolder){
        if(!dataFolder.exists()){
            try{
                if(!dataFolder.mkdir()){
                    DuelsCombo.getInstance().getLogger().warning("Couldn't create data folder.");
                    return;
                }
            }
            catch (Exception e){
                DuelsCombo.getInstance().getLogger().warning("Couldn't create data folder: ");
                e.printStackTrace();
                return;
            }
        }
        kitsFolderPath = "./" + dataFolder.getPath() + "/kits/";
        File kitsFolder = new File(kitsFolderPath);
        if(!kitsFolder.exists()){
            try{
                if(!kitsFolder.mkdir()){
                    DuelsCombo.getInstance().getLogger().severe("Couldn't create kit folder.");
                }
            }
            catch (Exception e){
                DuelsCombo.getInstance().getLogger().severe("Couldn't create kit folder: ");
                e.printStackTrace();
            }
            return;
        }
        kitsFolderCreated = true;
        String[] contents = kitsFolder.list();
        for (String path: Objects.requireNonNull(contents)) {
            if(path.endsWith(".yml")){
                KitData kitData = new KitData();
                if(kitData.loadOrCreateKit(kitsFolderPath + path, "", false)){
                    kits.put(kitData.getKitName(), kitData);
                }
            }
        }

    }

}
