package hu.logcontrol.wasteprogram.models;

public class RawMaterial {

    private String timeStamp;
    private String doseNumber;
    private String materialType;

    public RawMaterial(String timeStamp, String doseNumber, String materialType) {
        this.timeStamp = timeStamp;
        this.doseNumber = doseNumber;
        this.materialType = materialType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getDoseNumber() {
        return doseNumber;
    }

    public String getMaterialType() {
        return materialType;
    }

    @Override
    public String toString() { return String.format("%s;%s;%s", this.timeStamp, this.materialType, this.doseNumber); }

    public static String getCSVHeader(){
        return "TimeStamp;RawMaterialType;RawMaterialCount";
    }
}
