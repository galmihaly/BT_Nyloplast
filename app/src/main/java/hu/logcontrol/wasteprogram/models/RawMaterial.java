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

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDoseNumber() {
        return doseNumber;
    }

    public void setDoseNumber(String doseNumber) {
        this.doseNumber = doseNumber;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    @Override
    public String toString() { return String.format("%s;%s;%s", this.timeStamp, this.materialType, this.doseNumber); }

    public static String getCSVHeader(){
        return "TimeStamp;RawMaterialType;RawMaterialCount";
    }
}
