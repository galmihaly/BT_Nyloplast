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

    private int isSuccesWriteToLocalStorage;
    private int isSuccesWriteToGlobalStorage;

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
                        isSuccesWriteToGlobalStorage = saveToGlobalFile(rawMaterialList, globalPathElements, localPathElements[1]);
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
                        isSuccesWriteToGlobalStorage = saveToGlobalFile(rawMaterialTypeMassList, globalPathElements, localPathElements[1]);
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
                        isSuccesWriteToGlobalStorage = saveToGlobalFile(recycledMaterialList, globalPathElements, localPathElements[1]);
                    }

                    break;
                }
            }

            savingGlobalMessageHandling(isSuccesWriteToGlobalStorage);

        }
        catch (InterruptedException e) {

            e.printStackTrace();

            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
            sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);
        }

        return null;
    }

    private <T> int saveToGlobalFile(List<T> list, String[] globalPathElements, String fileName) {
        int isSucces = 0;

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
                    writer.write(header + "\n");
                    for (int i = 0; i < list.size(); i++) {
                        writer.write(list.get(i).toString() + "\n");
                    }
                    writer.flush();

                    if(ipAddress.equals("")) return -1;
                    if(portNumber.equals("")) return -2;

                    client.connect(ipAddress, Integer.parseInt(portNumber));

                    if(userName.equals("")) return -3;
                    if(password.equals("")) return -4;

                    boolean isConnected = client.login(userName, password);

                    if(isConnected){
                        client.setFileType(FTP.ASCII_FILE_TYPE);
                        client.enterLocalPassiveMode();
                        client.sendCommand("OPTS UTF8 ON");
                        fis = new FileInputStream(file1);

                        if(path.equals("")){ return -5; }

                        String fileNameWithPath = path + File.separator + fileName;
                        Log.e("fileNameWithPath", fileNameWithPath);

                        boolean isUpload = client.storeFile(fileNameWithPath, fis);
                        if(isUpload){
                            if(file1.exists()){
                                if(file1.delete()){
                                    ApplicationLogger.logging(LogLevel.FATAL, "Nincs beállítva a kiszolgáló neve a beállításokban!");
                                }
                            }
                        }
                        else {
                            return -6;
                        }

                        fis.close();
                        client.logout();
                    }
                    else{
                        return -7;
                    }
                }
            }
        }
        catch (IOException ex){
            ApplicationLogger.logging(LogLevel.FATAL, ex.getMessage());
            return -8;
        }

        try {
            writer.close();
        } catch (IOException e) {

            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
            return -9;
        }

        return isSucces;
    }

    public <T> int saveToLocalFile(File file, List<T> list) {

        int isSucces = 1;

        try {
            if(!file.exists()){ file.createNewFile(); }

            if (file.exists()){
                fos = new FileOutputStream(file);
                if(list != null) {

                    writer = new FileWriter(fos.getFD());
                    writer.write(header + "\n");
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

            isSucces = 0;
            return isSucces;
        }

        try {
            writer.close();
        } catch (IOException e) {

            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
            sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);

            isSucces = 0;
            return isSucces;
        }

        return isSucces;
    }

    private void savingGlobalMessageHandling(int isSuccesWriteToGlobalStorage) {
        switch (isSuccesWriteToGlobalStorage){
            case 0:{
                ApplicationLogger.logging(LogLevel.INFORMATION, "Az fájl létrehozása és hozzádadása a mappaútvonalhoz sikerült!");
                sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_SUCCES);
                break;
            }
            case -1:{
                ApplicationLogger.logging(LogLevel.FATAL, "Nincs beállítva a kiszolgáló neve a beállításokban!");
                sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);
                break;
            }
            case -2:{
                ApplicationLogger.logging(LogLevel.INFORMATION, "Nincs beállítva portszám a beállításokban!");
                sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);
                break;
            }
            case -3: {
                ApplicationLogger.logging(LogLevel.FATAL, "Nincs beállítva felhasználónév a beállításokban!");
                sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);
                break;
            }
            case -4:{
                ApplicationLogger.logging(LogLevel.INFORMATION, "Nincs beállítva jelszó a beállításokban!");
                sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);
                break;
            }
            case -5:{
                ApplicationLogger.logging(LogLevel.INFORMATION, "Nincs beállítva elérési útvonal a beállításokban!");
                sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);
                break;
            }
            case -6: {
                ApplicationLogger.logging(LogLevel.FATAL, "Nem sikerült feltölteni a fájlt az FTP szerverre!");
                sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);
                break;
            }
            case -7:{
                ApplicationLogger.logging(LogLevel.FATAL, "Nem sikerült bejelentkezni az FTP szerverbe!!");
                sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);
                break;
            }
            case -8:{
                ApplicationLogger.logging(LogLevel.FATAL, "A szerverhez való csatlakozás közben hiba keletkezett!!");
                sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);
                break;
            }
            case -9:{
                ApplicationLogger.logging(LogLevel.FATAL, "A fájlíró bezárásakor hiba lépett fel!!");
                sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);
                break;
            }
        }
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
