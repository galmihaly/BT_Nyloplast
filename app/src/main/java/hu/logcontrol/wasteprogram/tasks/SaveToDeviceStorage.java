package hu.logcontrol.wasteprogram.tasks;

import android.content.Context;
import android.os.Message;
import android.util.Log;

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

    public SaveToDeviceStorage(Context context, RunModes runMode, String header, String fileExtension) {
        this.context = context;
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

            isEnabledWriteToLocalStorage = JSONFileHelper.getBoolean(context.getApplicationContext(), "values.json", "IsEnableSaveLocalStorage");

            switch (runMode){
                case CREATE_RAWMATERIAL:{

                    rawMaterialList = LocalRawMaterialsStorage.getInstance().getRawMaterialList();

                    localPathElements = FileHelper.getSaveLocalFullPath(context.getApplicationContext(), "_RawMaterialList", fileExtension);
                    if(localPathElements != null){
                        file = new File(localPathElements[0] + File.separator + localPathElements[1]);
                        if(isEnabledWriteToLocalStorage) isSuccesWriteToLocalStorage = saveToLocalFile(file, rawMaterialList);
                    }

                    globalPathElements = FileHelper.getSaveGlobalFullPath(context.getApplicationContext());
                    if(globalPathElements != null && localPathElements != null){
                        file = new File(localPathElements[1]);
                        isSuccesWriteToGlobalStorage = saveToGlobalFile(file, rawMaterialList, globalPathElements);
                    }

                    break;
                }
                case CREATE_RAWMATERIALTYPEMASS:{

//                    rawMaterialTypeMassList = LocalRawMaterialTypeMassesStorage.getInstance().getRawMaterialTypeMassList();
//                    fullPathName = FileHelper.getSaveLocalFullPath(context.getApplicationContext(), "_RawMaterialTypeMassList", fileExtension);
//                    file = new File(fullPathName);
//                    if(isEnabledWriteToLocalStorage) isSuccesWriteToLocalStorage = saveToLocalFile(file, rawMaterialTypeMassList);

                    break;
                }
                case CREATE_RECYCLEDMATERIAL:{

//                    recycledMaterialList = LocalRecycLedMaterialsStorage.getInstance().getRecycledMaterialList();
//                    fullPathName = FileHelper.getSaveLocalFullPath(context.getApplicationContext(), "_RecycledMaterialList", fileExtension);
//                    file = new File(fullPathName);
//                    if(isEnabledWriteToLocalStorage) isSuccesWriteToLocalStorage = saveToLocalFile(file, recycledMaterialList);

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

    private <T> boolean saveToGlobalFile(File file, List<T> list, String[] globalPathElements) {
        boolean isSucces = true;

        for (int i = 0; i < globalPathElements.length; i++) {
            Log.e("glob", globalPathElements[i]);
        }

//        String ipAddress = null;
//        String portNumber = null;
//        String username = null;
//        String password = null;
//
//        if(globalPathElements[0].contains(":")){
//            ipAddress = globalPathElements[0].split(":")[0];
//            portNumber = globalPathElements[0].split(":")[1];
//        }
//        else {
//            ipAddress = globalPathElements[0].split(":")[0];
//        }
//
//        FTPClient client = new FTPClient();
//        FileInputStream fis = null;
//        try {
//            fos = new FileOutputStream(file);
//            if(list != null) {
//
//                writer = new FileWriter(fos.getFD());
//                writer.write(header + "\n\n");
//                for (int i = 0; i < list.size(); i++) {
//                    writer.write(list.get(i).toString() + "\n");
//                }
//                writer.flush();
//
//                client.connect("192.168.1.141", 21);
//
//                boolean b = client.login("raspberry", "pi");
//                Log.e("b", String.valueOf(b));
//                client.setFileType(FTP.ASCII_FILE_TYPE);
//                client.enterLocalPassiveMode();
//                client.sendCommand("OPTS UTF8 ON");
//                String filename = "/sdcard/Music/2023_04_03_22_16_RawMaterialList.txt";
//                fis = new FileInputStream(filename);
//                client.storeFile("/teszt1/2023_04_03_22_16_RawMaterialList.txt", fis);
//                fis.close();
//                client.logout();
//            }
//        }
//        catch (IOException ex){
//
//            ApplicationLogger.logging(LogLevel.FATAL, ex.getMessage());
//            sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);
//
//            isSucces = false;
//            return isSucces;
//        }
//
//        try {
//            writer.close();
//        } catch (IOException e) {
//
//            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
//            sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);
//
//            isSucces = false;
//            return isSucces;
//        }

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
            case CREATEFILE_FAILED:{
                message = Helper.createMessage(HandlerMessageIdentifiers.TRHEAD_INTERRUPTED, "Hiba történt!");
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

    public boolean getIsEnableWriteToLocalStorage(){
        return JSONFileHelper.getBoolean(context, "values.json", "IsEnableSaveLocalStorage");
    }

    private enum  CreateFileEnums{
        CREATEFILE_SUCCES,
        CREATEFILE_FAILED
    }

    public enum RunModes{
        CREATE_RAWMATERIAL,
        CREATE_RAWMATERIALTYPEMASS,
        CREATE_RECYCLEDMATERIAL
    }
}
