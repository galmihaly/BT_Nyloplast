package hu.logcontrol.wasteprogram.models;

import java.util.ArrayList;
import java.util.List;

public class LocalRawMaterialsStorage {

    private static LocalRawMaterialsStorage mInstance;
    private List<RawMaterial> rawMaterialList = new ArrayList<>();

    public static synchronized LocalRawMaterialsStorage getInstance(){
        if(mInstance == null){
            mInstance = new LocalRawMaterialsStorage();
        }

        return mInstance;
    }

    public List<RawMaterial> getRawMaterialList(){
        return rawMaterialList;
    }

    public int getRawMaterialListSize() { return rawMaterialList.size(); }
}
