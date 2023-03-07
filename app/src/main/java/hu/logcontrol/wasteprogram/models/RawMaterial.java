package hu.logcontrol.wasteprogram.models;

import java.util.Date;
import java.util.TimeZone;

public class RawMaterial {

    private Date date;
    private String doseNumber;
    private String materialType;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
