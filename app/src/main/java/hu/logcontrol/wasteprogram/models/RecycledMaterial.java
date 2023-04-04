package hu.logcontrol.wasteprogram.models;

public class RecycledMaterial {

    private String timeStamp;
    private String materialType;
    private String storageBoxIdentifier;
    private String massData;
    private String comment;

    private static char separator;

    public RecycledMaterial(String timeStamp, String materialType, String storageBoxIdentifier, String massData, String comment) {
        this.timeStamp = timeStamp;
        this.materialType = materialType;
        this.storageBoxIdentifier = storageBoxIdentifier;
        this.massData = massData;
        this.comment = comment;
    }

    public String getTimeStamp() {
        return timeStamp;
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

    public void setSeparator(char separator) {
        RecycledMaterial.separator = separator;
    }

    @Override
    public String toString() {
        return String.format("%s" + separator + "%s" + separator + "%s" + separator + "%s" + separator + "%s", this.timeStamp, this.materialType, this.storageBoxIdentifier, this.massData, this.comment);
    }

    public static String getCSVHeader(){
        return "TimeStamp" + separator + "MaterialType" + separator + "StorageBoxIdentifier" + separator + "MassData" + separator + "Comment";
    }
}
