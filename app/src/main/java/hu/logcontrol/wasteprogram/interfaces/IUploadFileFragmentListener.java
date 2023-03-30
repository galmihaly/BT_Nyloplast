package hu.logcontrol.wasteprogram.interfaces;

public interface IUploadFileFragmentListener {
    String getGlobalPath();
    String getUsername();
    String getPassword();
    boolean getLocalSaveCheckBoxState();
}
