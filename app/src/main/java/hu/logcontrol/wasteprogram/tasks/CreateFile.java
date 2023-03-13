package hu.logcontrol.wasteprogram.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Message;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;

import hu.logcontrol.wasteprogram.enums.HandlerMessageIdentifiers;
import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.logger.ApplicationLogger;
import hu.logcontrol.wasteprogram.models.LocalRawMaterialTypeMassesStorage;
import hu.logcontrol.wasteprogram.models.LocalRawMaterialsStorage;
import hu.logcontrol.wasteprogram.models.RawMaterial;
import hu.logcontrol.wasteprogram.models.RawMaterialTypeMass;
import hu.logcontrol.wasteprogram.taskmanager.CustomThreadPoolManager;

public class CreateFile implements Callable {

    private WeakReference<CustomThreadPoolManager> customThreadPoolManagerWeakReference;
    private List<RawMaterial> rawMaterialList;
    private List<RawMaterialTypeMass> rawMaterialTypeMassList;
    private Message message = null;

    private Context context;
    private RunModes runMode;
    private Uri uri;
    private String header;
    private String fileExtension;

    private String fileName;

    private FileOutputStream fos;
    private DocumentFile documentFile;
    private FileWriter writer = null;

    private LocalRawMaterialsStorage localRawMatStorage;
    private LocalRawMaterialTypeMassesStorage localTypeMassStorage;

    public CreateFile(Context context, RunModes runMode, Uri uri, String header, String fileExtension) {
        this.context = context;
        this.runMode = runMode;
        this.uri = uri;
        this.header = header;
        this.fileExtension = fileExtension;
    }

    public void setCustomThreadPoolManager(CustomThreadPoolManager customThreadPoolManager) {
        this.customThreadPoolManagerWeakReference = new WeakReference<>(customThreadPoolManager);
    }

    @Override
    public Object call() throws Exception {

        try {
            if (Thread.interrupted()) throw new InterruptedException();

            switch (runMode){
                case CREATE_RAWMATERIAL_CSV:{

                    localRawMatStorage = LocalRawMaterialsStorage.getInstance();
                    rawMaterialList = localRawMatStorage.getRawMaterialList();

                    fileName = ApplicationLogger.getDateTimeString() + "_RawMaterialList" + "." + fileExtension;
                    documentFile = DocumentFile.fromTreeUri(context, uri);
                    if(documentFile != null) documentFile.createFile(fileExtension, fileName);

                    fos = new FileOutputStream(getPathFromUri() + File.separator + fileName);

                    if(rawMaterialList != null) {

                        writer = new FileWriter(fos.getFD());
                        writer.write(header + "\n\n");
                        for (int i = 0; i < rawMaterialList.size(); i++) {
                            writer.write(rawMaterialList.get(i).toString() + "\n");
                        }
                        writer.flush();
                    }

                    localRawMatStorage.clearRawMaterialList();

                    break;
                }
                case CREATE_RAWMATERIALTYPEMASS_CSV:{

                    localTypeMassStorage = LocalRawMaterialTypeMassesStorage.getInstance();
                    rawMaterialTypeMassList = localTypeMassStorage.getRawMaterialTypeMassList();

                    fileName = ApplicationLogger.getDateTimeString() + "_RawMaterialTypeMassList" + "." + fileExtension;
                    documentFile = DocumentFile.fromTreeUri(context, uri);
                    if(documentFile != null) documentFile.createFile(fileExtension, fileName);

                    fos = new FileOutputStream(getPathFromUri() + File.separator + fileName);

                    if(rawMaterialTypeMassList != null) {

                        writer = new FileWriter(fos.getFD());
                        writer.write(header + "\n\n");
                        for (int i = 0; i < rawMaterialTypeMassList.size(); i++) {
                            writer.write(rawMaterialTypeMassList.get(i).toString() + "\n");
                        }
                        writer.flush();
                    }

                    localTypeMassStorage.clearRawMaterialTypeMassList();
                    break;
                }
            }
        }
        catch (InterruptedException e) {

            e.printStackTrace();
            sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_THREAD_INTERRUPTED);
        }
        catch (IOException e){

            e.printStackTrace();
            sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_IOEXCEPTION);
        }
        finally {

            if(writer != null) writer.close();
            sendMessageToPresenterHandler(CreateFileEnums.CREATEFILE_SUCCES);
        }

        return null;
    }

    @SuppressLint("SdCardPath")
    private String getPathFromUri(){
        return "/sdcard/" + this.uri.getPath().split(":")[1];
    }

    private void sendMessageToPresenterHandler(CreateFileEnums createFileEnum){

        switch (createFileEnum){
            case CREATEFILE_THREAD_INTERRUPTED:{
                message = Helper.createMessage(HandlerMessageIdentifiers.TEXTFILE_ADD_TO_DIRECTORY_PATH_FAILED, "Az fájl létrehozása és hozzádadása a mappaútvonalhoz sikertelen!");
                break;
            }
            case CREATEFILE_IOEXCEPTION:{
                message = Helper.createMessage(HandlerMessageIdentifiers.TEXTFILE_ADD_TO_DIRECTORY_PATH_FAILED, "Az fájl írása közben hiba lépett fel!");
                break;
            }
            case CREATEFILE_SUCCES:{
                message = Helper.createMessage(HandlerMessageIdentifiers.TEXTFILE_ADD_TO_DIRECTORY_PATH_SUCCES, "Az fájl létrehozása és hozzádadása a mappaútvonalhoz sikerült!");
                break;
            }
        }

        if(customThreadPoolManagerWeakReference != null && customThreadPoolManagerWeakReference.get() != null) {
            if(message != null) { customThreadPoolManagerWeakReference.get().sendResultToPresenter(message); }
        }
    }

    private enum  CreateFileEnums{
        CREATEFILE_THREAD_INTERRUPTED,
        CREATEFILE_IOEXCEPTION,
        CREATEFILE_SUCCES
    }

    public enum RunModes{
        CREATE_RAWMATERIAL_CSV,
        CREATE_RAWMATERIALTYPEMASS_CSV
    }
}
