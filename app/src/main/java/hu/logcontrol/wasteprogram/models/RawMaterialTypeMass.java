package hu.logcontrol.wasteprogram.models;

public class RawMaterialTypeMass {

    private String timeStamp;
    private String wasteCode;
    private String materialType;
    private String storageBoxIdentifier;
    private String massData;

    public RawMaterialTypeMass(String timeStamp, String wasteCode, String materialType, String storageBoxIdentifier, String massData) {
        this.timeStamp = timeStamp;
        this.wasteCode = wasteCode;
        this.materialType = materialType;
        this.storageBoxIdentifier = storageBoxIdentifier;
        this.massData = massData;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getWasteCode() {
        return wasteCode;
    }

    public void setWasteCode(String wasteCode) {
        this.wasteCode = wasteCode;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getStorageBoxIdentifier() {
        return storageBoxIdentifier;
    }

    public void setStorageBoxIdentifier(String storageBoxIdentifier) {
        this.storageBoxIdentifier = storageBoxIdentifier;
    }

    public String getMassData() {
        return massData;
    }

    public void setMassData(String massData) {
        this.massData = massData;
    }

    public static String getCSVHeader(){
        return "TimeStamp;WasteCode;MaterialType;StorageBoxIdentifier;MassData";
    }
}
