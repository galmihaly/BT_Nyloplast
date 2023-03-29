package hu.logcontrol.wasteprogram.models;

public class RawMaterialTypeMass {

    private String timeStamp;
    private String wasteCode;
    private String materialType;
    private String storageBoxIdentifier;
    private String massData;
    private String comment;

    public RawMaterialTypeMass(String timeStamp, String wasteCode, String materialType, String storageBoxIdentifier, String massData, String comment) {
        this.timeStamp = timeStamp;
        this.wasteCode = wasteCode;
        this.materialType = materialType;
        this.storageBoxIdentifier = storageBoxIdentifier;
        this.massData = massData;
        this.comment = comment;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getWasteCode() {
        return wasteCode;
    }

    public String getMaterialType() {
        return materialType;
    }

    public String getStorageBoxIdentifier() {
        return storageBoxIdentifier;
    }

    public String getMassData() {
        return massData;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() { return String.format("%s;%s;%s;%s;%s;%s", this.timeStamp, this.wasteCode, this.materialType, this.storageBoxIdentifier, this.massData, this.comment); }

    public static String getCSVHeader(){
        return "TimeStamp;WasteCode;MaterialType;StorageBoxIdentifier;MassData;Comment";
    }
}
