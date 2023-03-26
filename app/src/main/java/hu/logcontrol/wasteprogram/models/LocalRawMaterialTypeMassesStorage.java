package hu.logcontrol.wasteprogram.models;

import java.util.ArrayList;
import java.util.List;

public class LocalRawMaterialTypeMassesStorage {

    private static LocalRawMaterialTypeMassesStorage mInstance;
    private List<RawMaterialTypeMass> rawMaterialTypeMassList = new ArrayList<>();

    public static synchronized LocalRawMaterialTypeMassesStorage getInstance(){
        if(mInstance == null){
            mInstance = new LocalRawMaterialTypeMassesStorage();
        }

        return mInstance;
    }

    public List<RawMaterialTypeMass> getRawMaterialTypeMassList() {
        return rawMaterialTypeMassList;
    }

    public int getRawMaterialTypeMassListSize() { return rawMaterialTypeMassList.size(); }
}
