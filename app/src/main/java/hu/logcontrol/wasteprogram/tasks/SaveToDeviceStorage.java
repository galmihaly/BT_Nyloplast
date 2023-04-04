package hu.logcontrol.wasteprogram.tasks;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;

import hu.logcontrol.wasteprogram.enums.HandlerMessageIdentifiers;
import hu.logcontrol.wasteprogram.helpers.FileHelper;
import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.helpers.JSONFileHelper;
import hu.logcontrol.wasteprogram.logger.ApplicationLogger;
import hu.logcontrol.wasteprogram.logger.LogLevel;
import hu.logcontrol.wasteprogram.models.LocalEncryptedPreferences;
import hu.logcontrol.wasteprogram.models.LocalRawMaterialTypeMassesStorage;
import hu.logcontrol.wasteprogram.models.LocalRawMaterialsStorage;
import hu.logcontrol.wasteprogram.models.LocalRecycLedMaterialsStorage;
import hu.logcontrol.wasteprogram.models.RawMaterial;
import hu.logcontrol.wasteprogram.models.RawMaterialTypeMass;
import hu.logcontrol.wasteprogram.models.RecycledMaterial;
import hu.logcontrol.wasteprogram.taskmanager.CustomThreadPoolManager;

public class SaveToDeviceStorage implements Callable {

    private WeakReference<CustomThreadPoolManager> customThreadPoolManagerWeakReference;

    private List<RawMaterial> rawMaterialList;
    private List<RawMaterialTypeMass> rawMaterialTypeMassList;
    private List<RecycledMaterial> recycledMaterialList;

    private Message message = null;

    private Context context;
    private RunModes runMode;
    private String header;
    private String fileExtension;

    private FileOutputStream fos;
    private FileWriter writer;
    private File file;

    private boolean isSuccesWriteToLocalStorage = false;
    private boolean isSuccesWriteToGlobalStorage = false;

    private boolean isEnabledWriteToLocalStorage;

    private String[] globalPathElements;
    private String[] localPathElements;

    private LocalEncryptedPreferences preferences;

    public SaveToDeviceStorage(Context context, RunModes runMode, String header, String fileExtension) {
        this.context = context.getApplicationContext();
        this.runMode = runMode;
        this.header = header;
        this.fileExtension = fileExtension;
    }

    public void setCustomThreadPoolManager(CustomThreadPoolManager customThreadPoolManager) {
        this.customThreadPoolManagerWeakReference = new WeakReference<>(customThreadPoolManager);
    }

    @Override
    public Object call() {

        try {
            if (Thread.interrupted()) throw new InterruptedException();

            preferences = LocalEncryptedPreferences.getInstance(
                    "values",
                    MasterKeys.AES256_GCM_SPEC,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            isEnabledWriteToLocalStorage = preferences.getBooleanValueByKey("IsEnableSaveLocalStorage");

            switch (runMode){
                case CREATE_RAWMATERIAL:{

                    rawMaterialList = LocalRawMaterialsStorage.getInstance().getRawMaterialList();

                    localPathElements = FileHelper.getSavedLocalFullPath(context.getApplicationContext(), "_RawMaterialList", fileExtension);
                    if(localPathElements != null){
                        file = new File(localPathElements[0] + File.separator + localPathElements[1]);
                        if(isEnabledWriteToLocalStorage) isSuccesWriteToLocalStorage = saveToLocalFile(file, rawMaterialList);
                    }

                    globalPathElements = FileHelper.getSavedGlobalFullPath(context.getApplicationContext());
                    if(globalPathElements != null && localPathElements != null){
                        file = new File(localPathElements[1]);
                        isSuccesWriteToGlobalStorage = saveToGlobalFile(file, rawMaterialList, globalPathElements, localPathElements[1]);
                    }

                    break;
                }
                case CREATE_RAWMATERIALTYPEMASS:{

                    rawMaterialTypeMassList = LocalRawMaterialTypeMassesStorage.getInstance().getRawMaterialTypeMassList();

                    localPathElements = FileHelper.getSavedLocalFullPath(context.getApplicationContext(), "_RawMaterialTypeMassList", fileExtension);
                    if(localPathElements != null){
                        file = new File(localPathElements[0] + File.separator + localPathElements[1]);
                        if(isEnabledWriteToLocalStorage) isSuccesWriteToLocalStorage = saveToLocalFile(file, rawMaterialTypeMassList);
                    }

                    globalPathElements = FileHelper.getSavedGlobalFullPath(context.getApplicationContext());
                    if(globalPathElements != null && localPathElements != null){
                        file = new File(localPathElements[1]);
                        isSuccesWriteToGlobalStorage = saveToGlobalFile(file, rawMaterialTypeMassList, globalPathElements, localPathElements[1]);
                    }

                    break;
                }
                case CREATE_RECYCLEDMATERIAL:{

                    recycledMaterialList = LocalRecycLedMaterialsStorage.getInstance().getRecycledMaterialList();

                    localPathElements = FileHelper.getSavedLocalFullPath(context.getApplicationContext(), "_RecycledMaterialList", fileExtension);
                    if(localPathElements != null){
                        file = new File(localPathElements[0] + File.separator + localPathElements[1]);
                        if(isEnabledWriteToLocalStorage) isSuccesWriteToLocalStorage = saveToLocalFile(file, recycledMaterialList);
                    }

                    globalPathElements = FileHelper.getSavedGlobalFullPath(context.getApplicationContext());
                    if(globalPathElements != null && localPathElements != null){
                        file = new File(localPathElements[1]);
                        isSuccesWriteToGlobalStorage = saveToGlobalFile(file, recycledMaterialList, globalPathElements, localPathElements[1]);
                    }

                    break;
                }
            }

            if(isSuccesWriteToLocalStorage && isSuccesWriteToGlobalStorage || !isSuccesWriteToLocalStorage && isSuccesWriteToGlobalStorage){
                ApplicationLogger.logging(LogLevel.INFORMATION, "Az fájl létrehozása és hozzádadása a mappaútvonalhoz sikerült!");
                sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_SUCCES);
            }
            else {
                ApplicationLogger.logging(LogLevel.INFORMATION, "Az fájl létrehozása és hozzádadása a mappaútvonalhoz nem sikerült!");
                sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);
            }
        }
        catch (InterruptedException e) {

            e.printStackTrace();

            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
            sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);
        }

        return null;
    }

    private <T> boolean saveToGlobalFile(File file, List<T> list, String[] globalPathElements, String fileName) {
        boolean isSucces = true;

        String ipAddress = globalPathElements[0];
        String portNumber = globalPathElements[1];
        String userName = globalPathElements[2];
        String password = globalPathElements[3];
        String path = globalPathElements[4];

        Log.e("ipAddress", ipAddress);
        Log.e("portNumber", portNumber);
        Log.e("userName", userName);
        Log.e("password", password);
        Log.e("path", path);

        FTPClient client = new FTPClient();
        FileInputStream fis = null;
        try {

            File file1 = new File(context.getFilesDir() + File.separator + fileName);

            if(!file1.exists()) file1.createNewFile();

            if(file1.exists()){
                fos = new FileOutputStream(file1);
                if(list != null) {

                    writer = new FileWriter(fos.getFD());
                    writer.write(header + "\n\n");
                    for (int i = 0; i < list.size(); i++) {
                        writer.write(list.get(i).toString() + "\n");
                    }
                    writer.flush();

                    client.connect(ipAddress, Integer.parseInt(portNumber));

                    boolean b = client.login(userName, password);
                    Log.e("b", String.valueOf(b));
                    client.setFileType(FTP.ASCII_FILE_TYPE);
                    client.enterLocalPassiveMode();
                    client.sendCommand("OPTS UTF8 ON");
                    fis = new FileInputStream(file1);
                    String fileNameWithPath = path + File.separator + fileName;
                    Log.e("fileNameWithPath", fileNameWithPath);
                    boolean isUpload = client.storeFile(fileNameWithPath, fis);

                    if(isUpload){
                        if(file1.exists()){
                            if(file1.delete()){
                                ApplicationLogger.logging(LogLevel.FATAL, "A fájl sikeresen tötölve lett a lokális mappából!");
                            }
                        }
                        isSucces = true;
                    }
                    else {
                        ApplicationLogger.logging(LogLevel.FATAL, "Nem sikerült feltölteni a fájlt az FTP szerverre!");
                        sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);
                    }

                    fis.close();
                    client.logout();
                }
            }
        }
        catch (IOException ex){

            ApplicationLogger.logging(LogLevel.FATAL, ex.getMessage());
            sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);

            isSucces = false;
            return isSucces;
        }

        try {
            writer.close();
        } catch (IOException e) {

            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
            sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);

            isSucces = false;
            return isSucces;
        }

        return isSucces;
    }

    public <T> boolean saveToLocalFile(File file, List<T> list) {

        boolean isSucces = true;

        try {
            if(!file.exists()){ file.createNewFile(); }

            if (file.exists()){
                fos = new FileOutputStream(file);
                if(list != null) {

                    writer = new FileWriter(fos.getFD());
                    writer.write(header + "\n\n");
                    for (int i = 0; i < list.size(); i++) {
                        writer.write(list.get(i).toString() + "\n");
                    }
                    writer.flush();
                }
            }
        }
        catch (IOException ex){

            ApplicationLogger.logging(LogLevel.FATAL, ex.getMessage());
            sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);

            isSucces = false;
            return isSucces;
        }

        try {
            writer.close();
        } catch (IOException e) {

            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
            sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);

            isSucces = false;
            return isSucces;
        }

        return isSucces;
    }

    private void sendMessageToPresenterHandler(CreateFileEnums createFileEnum){

        switch (createFileEnum){
            case THREAD_INTERRUPTED:{
                message = Helper.createMessage(HandlerMessageIdentifiers.TRHEAD_INTERRUPTED, "Hiba történt!");
                break;
            }
            case CREATEFILE_FAILED:{
                message = Helper.createMessage(HandlerMessageIdentifiers.FILECREATE_FAILED, "Hiba történt a mentés során!");
                break;
            }
            case CREATEFILE_SUCCES:{
                message = Helper.createMessage(HandlerMessageIdentifiers.TEXTFILE_ADD_TO_DIRECTORY_PATH_SUCCES, "Sikeres mentés!");
                break;
            }
        }

        if(customThreadPoolManagerWeakReference != null && customThreadPoolManagerWeakReference.get() != null) {
            if(message != null) { customThreadPoolManagerWeakReference.get().sendResultToPresenter(message); }
        }
    }

    private enum  CreateFileEnums{
        THREAD_INTERRUPTED,
        CREATEFILE_SUCCES,
        CREATEFILE_FAILED
    }

    public enum RunModes{
        CREATE_RAWMATERIAL,
        CREATE_RAWMATERIALTYPEMASS,
        CREATE_RECYCLEDMATERIAL
    }
}
