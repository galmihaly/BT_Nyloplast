package hu.logcontrol.wasteprogram.models;

public class RawMaterial {

    private String timeStamp;
    private String doseNumber;
    private String materialType;
    private String comment;

    private static char separator;

    public RawMaterial(String timeStamp, String doseNumber, String materialType, String comment) {
        this.timeStamp = timeStamp;
        this.doseNumber = doseNumber;
        this.materialType = materialType;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public void setSeparator(char separator) {
        RawMaterial.separator = separator;
    }

    @Override
    public String toString() { return String.format("%s" + separator + "%s" + separator + "%s" + separator + "%s", this.timeStamp, this.materialType, this.doseNumber, this.comment); }

    public static String getCSVHeader(){
        return "TimeStamp" + separator + "RawMaterialType" + separator + "RawMaterialCount" + separator + "Comment";
    }
}
