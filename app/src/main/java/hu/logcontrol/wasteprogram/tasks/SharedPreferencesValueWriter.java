package hu.logcontrol.wasteprogram.tasks;

import android.content.Context;
import android.os.Message;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

import hu.logcontrol.wasteprogram.enums.HandlerMessageIdentifiers;
import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.models.LocalEncryptedPreferences;
import hu.logcontrol.wasteprogram.taskmanager.CustomThreadPoolManager;

public class SharedPreferencesValueWriter implements Callable {

    private WeakReference<CustomThreadPoolManager> customThreadPoolManagerWeakReference;

    private static final String FILE_NAME = "values.json";
    private static final String DEFAULT_FILE_NAME = "DefaultValues.json";

    private Context context;
    private String sharedPreferencesKeyValue;
    private boolean valueBoolean;
    private String valueString;
    private SharedPreferencesValueWriter.MODE mode;

    private Message message;

    private LocalEncryptedPreferences preferences;

    public SharedPreferencesValueWriter(Context context, String sharedPreferencesKeyValue, String valueString, MODE mode) {
        this.context = context;
        this.sharedPreferencesKeyValue = sharedPreferencesKeyValue;
        this.valueString = valueString;
        this.mode = mode;
    }

    public SharedPreferencesValueWriter(Context context, String sharedPreferencesKeyValue, boolean valueBoolean, MODE mode) {
        this.context = context;
        this.valueBoolean = valueBoolean;
        this.sharedPreferencesKeyValue = sharedPreferencesKeyValue;
        this.mode = mode;
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
                    context.getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            switch (mode) {
                case WRITE_STRING: {
                    preferences.replaceString(sharedPreferencesKeyValue, valueString);
                    sendMessageToPresenterHandler(JSONWriterEnums.WRITE_SUCCES);
                    break;
                }
                case WRITE_BOOLEAN: {
                    preferences.replaceBoolean(sharedPreferencesKeyValue, valueBoolean);
                    sendMessageToPresenterHandler(JSONWriterEnums.WRITE_SUCCES);
                    break;
                }
            }
        }
        catch (InterruptedException e) {

            e.printStackTrace();
            sendMessageToPresenterHandler(JSONWriterEnums.THREAD_INTERRUPTED);
        }

        return null;
    }

    private void sendMessageToPresenterHandler(JSONWriterEnums jsonWriterEnum){

        switch (jsonWriterEnum){
            case THREAD_INTERRUPTED:{
                message = Helper.createMessage(HandlerMessageIdentifiers.TEXTFILE_ADD_TO_DIRECTORY_PATH_FAILED, "A feldolgozó szál megszűnt létezni vagy nem jött létre a folyamat betöltésekor!");
                break;
            }
            case IOEXCEPTION:{
                message = Helper.createMessage(HandlerMessageIdentifiers.TEXTFILE_ADD_TO_DIRECTORY_PATH_FAILED, "Az érték mentése közben hiba lépett fel!");
                break;
            }
            case WRITE_SUCCES:{
                message = Helper.createMessage(HandlerMessageIdentifiers.WRITE_VAULE_SUCCES, "Az érték mentése a JSON fájlba sikerült!");
                break;
            }
            case READ_FAILED:{
                message = Helper.createMessage(HandlerMessageIdentifiers.READ_VAULES_FAILED, "A " + FILE_NAME  + " fájl olvasása közben hiba lépett fel!");
                break;
            }
            case FILECREATE_FAILED:{
                message = Helper.createMessage(HandlerMessageIdentifiers.FILECREATE_FAILED, "A " + FILE_NAME  + " fájl a '" + context.getFilesDir() + "' mappaútvonalon nem jött létre!");
                break;
            }
        }

        if(customThreadPoolManagerWeakReference != null && customThreadPoolManagerWeakReference.get() != null) {
            if(message != null) { customThreadPoolManagerWeakReference.get().sendResultToPresenter(message); }
        }
    }

    public enum MODE{
        WRITE_STRING,
        WRITE_BOOLEAN
    }

    private enum  JSONWriterEnums{
        THREAD_INTERRUPTED,
        IOEXCEPTION,
        WRITE_SUCCES,
        READ_FAILED,
        FILECREATE_FAILED,
    }
}
