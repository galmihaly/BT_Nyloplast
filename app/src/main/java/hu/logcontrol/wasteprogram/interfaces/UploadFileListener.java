package hu.logcontrol.wasteprogram.interfaces;

public interface UploadFileListener {
    void sendGlobalSavePath(String path);
    void sendLocalSavePath(String path);
    void sendLocalSaveCheckbox(boolean state);
    void sendLocalSaveConstraintState(boolean state);
}
