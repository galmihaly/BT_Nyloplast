package hu.logcontrol.wasteprogram.interfaces;

public interface GeneralListener {
    void sendBarcodeNextCheckBoxState(boolean state);
    void sendKeyboardCheckBox(boolean state);
    void sendFileSeparatorCharachter(String charachter);
    String getFileSeparatorCharacter();
}
