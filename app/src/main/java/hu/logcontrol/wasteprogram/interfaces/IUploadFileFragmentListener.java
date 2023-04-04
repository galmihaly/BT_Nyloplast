package hu.logcontrol.wasteprogram.interfaces;

public interface IUploadFileFragmentListener {
    String getGlobalPath();
    String getUsername();
    String getPassword();
    String getLocalPath();
    String getPortNumber();
    String getHostName();
    boolean getLocalSaveCheckBoxState();
}
