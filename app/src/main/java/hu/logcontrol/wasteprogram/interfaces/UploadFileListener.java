package hu.logcontrol.wasteprogram.interfaces;

public interface UploadFileListener {
    void sendLocalSavePath(String path);
    void sendLocalSaveCheckbox(boolean state);
}
