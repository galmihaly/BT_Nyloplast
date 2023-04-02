package hu.logcontrol.wasteprogram.tasks;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;

import hu.logcontrol.wasteprogram.enums.HandlerMessageIdentifiers;
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

    private String fileName;

    private FileOutputStream fos;
    private FileWriter writer;
    private File file;
    private String path;

    private boolean isSucces;

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

            path = JSONFileHelper.getString(context, "values.json", "LocalSavePath");

            switch (runMode){
                case CREATE_RAWMATERIAL:{

                    rawMaterialList = LocalRawMaterialsStorage.getInstance().getRawMaterialList();
                    fileName = ApplicationLogger.getDateTimeString() + "_RawMaterialList" + "." + fileExtension;
                    file = new File(path + File.separator + fileName);

                    isSucces = saveToFile(file, rawMaterialList);

                    break;
                }
                case CREATE_RAWMATERIALTYPEMASS:{

                    rawMaterialTypeMassList = LocalRawMaterialTypeMassesStorage.getInstance().getRawMaterialTypeMassList();
                    fileName = ApplicationLogger.getDateTimeString() + "_RawMaterialTypeMassList" + "." + fileExtension;
                    file = new File(path + File.separator + fileName);

                    isSucces = saveToFile(file, rawMaterialTypeMassList);

                    break;
                }
                case CREATE_RECYCLEDMATERIAL:{

                    recycledMaterialList = LocalRecycLedMaterialsStorage.getInstance().getRecycledMaterialList();
                    fileName = ApplicationLogger.getDateTimeString() + "_RecycledMaterialList" + "." + fileExtension;
                    file = new File(path + File.separator + fileName);

                    isSucces = saveToFile(file, recycledMaterialList);

                    break;
                }
            }

            if(isSucces){
                ApplicationLogger.logging(LogLevel.INFORMATION, "Az fájl létrehozása és hozzádadása a mappaútvonalhoz sikerült!");
                sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_SUCCES);
            }
        }
        catch (InterruptedException e) {

            e.printStackTrace();

            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
            sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_FAILED);
        }

        return null;
    }

    public <T> boolean saveToFile(File file, List<T> list) {

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
