package hu.logcontrol.wasteprogram.models;

import java.util.Date;
import java.util.TimeZone;

public class RawMaterial {

    private String date;
    private String doseNumber;
    private String materialType;

    public RawMaterial(String date, String doseNumber, String materialType) {
        this.date = date;
        this.doseNumber = doseNumber;
        this.materialType = materialType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
