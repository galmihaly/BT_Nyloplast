package hu.logcontrol.wasteprogram.models;

import java.util.ArrayList;
import java.util.List;

public class LocalRecycLedMaterialsStorage {

    private static LocalRecycLedMaterialsStorage mInstance;
    private List<RecycledMaterial> recycledMaterialList = new ArrayList<>();

    public static synchronized LocalRecycLedMaterialsStorage getInstance(){
        if(mInstance == null){
            mInstance = new LocalRecycLedMaterialsStorage();
        }

        return mInstance;
    }

    public List<RecycledMaterial> getRecycledMaterialList() {
        return recycledMaterialList;
    }

    public int getRecycLedMaterialListSize() { return recycledMaterialList.size(); }
}
