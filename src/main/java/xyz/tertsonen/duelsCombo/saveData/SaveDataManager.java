package xyz.tertsonen.duelsCombo.saveData;

import me.realized.duels.api.kit.Kit;
import me.realized.duels.api.kit.KitManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.tertsonen.duelsCombo.DuelsCombo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class SaveDataManager {

    private final KitManager kitManager;

    private final SaveHandler saveHandler;

    private final HashMap<String, KitData> kits = new HashMap<>();
    /**
     * @param name Kit name
     * @return Returns a KitData object if the specified kit exists.
     */
    @Nullable
    public KitData getKit(String name){
        if(!kitsFolderCreated) return null;
        KitData kit = kits.get(name);
        return kit == null ? null : kit.clone();
    }

    public KitData getOrCreate(String name){
        if(!kitsFolderCreated) return null;
        KitData kitData = kits.get(name);
        if(kitData != null) return kitData.clone();
        Kit kit = kitManager.get(name);
        if(kit == null){
            return null;
        }
        kitData = new KitData();
        if(!kitData.loadOrCreateKit(kitsFolderPath + name + ".yml", name, true)) return null;
        saveChanges(kitData);
        kits.put(name, kitData);
        return kitData.clone();
    }

    public boolean deleteKit(String name){
        KitData kitData = kits.remove(name);
        if(kitData == null) return false;
        try{
            kitData.deleteKit();
        }
        catch(IOException e){
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, KitData> getKits(){
        HashMap<String, KitData> map = (HashMap<String, KitData>) kits.clone();
        // Return deep copy
        map.forEach((s, kitData) -> map.put(s, kitData.clone()));
        return map;
    }

    private String kitsFolderPath;
    private boolean kitsFolderCreated = false;

    public SaveDataManager(@NotNull File dataFolder, @NotNull KitManager kitManager, @NotNull SaveHandler saveHandler) {
        this.saveHandler = saveHandler;
        this.kitManager = kitManager;
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
        kitsFolderPath = dataFolder.getAbsolutePath() + "/kits/";
        File kitsFolder = new File(kitsFolderPath);
        if(!kitsFolder.exists()){
            try{
                if(!kitsFolder.mkdir()){
                    DuelsCombo.getInstance().getLogger().severe("Couldn't create kit folder.");
                    return;
                }
            }
            catch (Exception e){
                DuelsCombo.getInstance().getLogger().severe("Couldn't create kit folder: ");
                e.printStackTrace();
                return;
            }
            kitsFolderCreated = true;
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

    public void saveChanges(KitData kitData){
        kits.put(kitData.getKitName(), kitData);
        kitData.saveChanges(saveHandler);
    }

}
