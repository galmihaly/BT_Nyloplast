package hu.logcontrol.wasteprogram.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Callable;

import hu.logcontrol.wasteprogram.enums.HandlerMessageIdentifiers;
import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.logger.ApplicationLogger;
import hu.logcontrol.wasteprogram.models.LocalRawMaterialsStorage;
import hu.logcontrol.wasteprogram.models.RawMaterial;
import hu.logcontrol.wasteprogram.taskmanager.CustomThreadPoolManager;

public class CreateTextFile implements Callable {

    private WeakReference<CustomThreadPoolManager> customThreadPoolManagerWeakReference;
    private List<RawMaterial> rawMaterialList;
    private Message message = null;

    private Context context;
    private Uri uri;

    private String fileName;
    private File textFile;

    private FileOutputStream fos;

    public CreateTextFile(Context context, Uri uri) {
        this.context = context.getApplicationContext();
        this.uri = uri;
    }

    public void setCustomThreadPoolManager(CustomThreadPoolManager customThreadPoolManager) {
        this.customThreadPoolManagerWeakReference = new WeakReference<>(customThreadPoolManager);
    }

    @Override
    public Object call() throws Exception {

        try {
            if (Thread.interrupted()) throw new InterruptedException();

            rawMaterialList = LocalRawMaterialsStorage.getInstance().getRawMaterialList();

            if(rawMaterialList != null) {

                fileName = ApplicationLogger.getDateTimeString() + ".txt";
                DocumentFile df = DocumentFile.fromTreeUri(context, uri);
                if(df != null) df.createFile("txt", fileName);

                Log.e("path", uri.getPath());
                Log.e("path", uri.toString());

                textFile = new File(context.getExternalFilesDir(uri.toString()), fileName);
                Log.e("filename", textFile.getName());

                fos = new FileOutputStream(textFile);
                fos.write("sfdjsdfhsdkjfhsdkjfhsdkjfhdsjkfhskfjs\n\n".getBytes());
                Log.e("size", String.valueOf(rawMaterialList.size()));
//                for (int i = 0; i < rawMaterialList.size(); i++) {
//                    fos.write("1".getBytes(StandardCharsets.UTF_8));
//                }

                fos.flush();
                fos.close();

                //LocalRawMaterialsStorage.getInstance().clearRawMaterialList();

                message = Helper.createMessage(HandlerMessageIdentifiers.TEXTFILE_ADD_TO_DIRECTORY_PATH_SUCCES, "Az fájl létrehozása és hozzádadása a mappaútvonalhoz sikerült!");

                if(customThreadPoolManagerWeakReference != null && customThreadPoolManagerWeakReference.get() != null) {
                    if(message != null) {
                        customThreadPoolManagerWeakReference.get().sendResultToPresenter(message);
                    }
                }
            }

        } catch (InterruptedException e) {

            e.printStackTrace();

            message = Helper.createMessage(HandlerMessageIdentifiers.TEXTFILE_ADD_TO_DIRECTORY_PATH_FAILED, "Az fájl létrehozása és hozzádadása a mappaútvonalhoz sikertelen!");

            if(customThreadPoolManagerWeakReference != null && customThreadPoolManagerWeakReference.get() != null) {
                if(message != null) {
                    customThreadPoolManagerWeakReference.get().sendResultToPresenter(message);
                }
            }
        }

        return null;
    }
}
