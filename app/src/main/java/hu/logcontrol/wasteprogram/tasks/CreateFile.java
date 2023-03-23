package hu.logcontrol.wasteprogram.tasks;

import android.content.Context;
import android.os.Message;

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
import hu.logcontrol.wasteprogram.helpers.JSONFileReaderHelper;
import hu.logcontrol.wasteprogram.logger.ApplicationLogger;
import hu.logcontrol.wasteprogram.models.LocalRawMaterialTypeMassesStorage;
import hu.logcontrol.wasteprogram.models.LocalRawMaterialsStorage;
import hu.logcontrol.wasteprogram.models.LocalRecycLedMaterialsStorage;
import hu.logcontrol.wasteprogram.models.RawMaterial;
import hu.logcontrol.wasteprogram.models.RawMaterialTypeMass;
import hu.logcontrol.wasteprogram.models.RecycledMaterial;
import hu.logcontrol.wasteprogram.taskmanager.CustomThreadPoolManager;

public class CreateFile implements Callable {

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
    private DocumentFile documentFile;
    private FileWriter writer = null;

    private File file;

    private LocalRawMaterialsStorage localRawMatStorage;
    private LocalRawMaterialTypeMassesStorage localTypeMassStorage;
    private LocalRecycLedMaterialsStorage localRecMatsStorage;

    public CreateFile(Context context, RunModes runMode, String header, String fileExtension) {
        this.context = context;
        this.runMode = runMode;
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
                case CREATE_RAWMATERIAL:{

                    localRawMatStorage = LocalRawMaterialsStorage.getInstance();
                    rawMaterialList = localRawMatStorage.getRawMaterialList();

                    fileName = ApplicationLogger.getDateTimeString() + "_RawMaterialList" + "." + fileExtension;
                    file = new File(getPathFromJSONFile() + File.separator + fileName);
                    fos = new FileOutputStream(file);

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
                case CREATE_RAWMATERIALTYPEMASS:{

                    localTypeMassStorage = LocalRawMaterialTypeMassesStorage.getInstance();
                    rawMaterialTypeMassList = localTypeMassStorage.getRawMaterialTypeMassList();

                    fileName = ApplicationLogger.getDateTimeString() + "_RawMaterialTypeMassList" + "." + fileExtension;
                    file = new File(getPathFromJSONFile() + File.separator + fileName);
                    fos = new FileOutputStream(file);

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
                case CREATE_RECYCLEDMATERIAL:{

                    localRecMatsStorage = LocalRecycLedMaterialsStorage.getInstance();
                    recycledMaterialList = localRecMatsStorage.getRecycledMaterialList();

                    fileName = ApplicationLogger.getDateTimeString() + "_RecycledMaterialList" + "." + fileExtension;
                    file = new File(getPathFromJSONFile() + File.separator + fileName);
                    fos = new FileOutputStream(file);

                    if(rawMaterialTypeMassList != null) {

                        writer = new FileWriter(fos.getFD());
                        writer.write(header + "\n\n");
                        for (int i = 0; i < rawMaterialTypeMassList.size(); i++) {
                            writer.write(rawMaterialTypeMassList.get(i).toString() + "\n");
                        }
                        writer.flush();
                    }

                    localRecMatsStorage.clearRecycledMaterialList();
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

    private String getPathFromJSONFile(){
        return JSONFileReaderHelper.getStringFromJSONFile(context, "values.json", "LocalSavePath");
    }

    private void sendMessageToPresenterHandler(CreateFileEnums createFileEnum){

        switch (createFileEnum){
            case CREATEFILE_THREAD_INTERRUPTED:{
                message = Helper.createMessage(HandlerMessageIdentifiers.TEXTFILE_ADD_TO_DIRECTORY_PATH_FAILED, "A feldolgozó szál megszűnt létezni vagy nem jött létre a folyamat betöltésekor!");
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
        CREATE_RAWMATERIAL,
        CREATE_RAWMATERIALTYPEMASS,
        CREATE_RECYCLEDMATERIAL
    }
}
