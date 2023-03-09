package hu.logcontrol.wasteprogram.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.core.graphics.PathUtils;
import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
    private DocumentFile documentFile;
    private FileWriter writer = null;

    private LocalRawMaterialsStorage localStorage;

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

            localStorage = LocalRawMaterialsStorage.getInstance();
            rawMaterialList = localStorage.getRawMaterialList();

            if(rawMaterialList != null) {

                fileName = ApplicationLogger.getDateTimeString() + ".csv";
                documentFile = DocumentFile.fromTreeUri(context, uri);
                if(documentFile != null) documentFile.createFile("csv", fileName);

                fos = new FileOutputStream("/sdcard/" + uri.getPath().split(":")[1] + File.separator + fileName);

                try {
                    writer = new FileWriter(fos.getFD());
                    writer.write("TimeStamp;RawMaterialType;RawMaterialCount\n\n");
                    for (int i = 0; i < rawMaterialList.size(); i++) {
                        writer.write(rawMaterialList.get(i).toString() + "\n");
                    }
                    writer.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    localStorage.clearRawMaterialList();
                    if(writer != null) writer.close();

                    message = Helper.createMessage(HandlerMessageIdentifiers.TEXTFILE_ADD_TO_DIRECTORY_PATH_SUCCES, "Az fájl létrehozása és hozzádadása a mappaútvonalhoz sikerült!");
                }
            }

        } catch (InterruptedException e) {

            e.printStackTrace();
            message = Helper.createMessage(HandlerMessageIdentifiers.TEXTFILE_ADD_TO_DIRECTORY_PATH_FAILED, "Az fájl létrehozása és hozzádadása a mappaútvonalhoz sikertelen!");
        }

        if(customThreadPoolManagerWeakReference != null && customThreadPoolManagerWeakReference.get() != null) {
            if(message != null) {
                customThreadPoolManagerWeakReference.get().sendResultToPresenter(message);
            }
        }

        return null;
    }
}
