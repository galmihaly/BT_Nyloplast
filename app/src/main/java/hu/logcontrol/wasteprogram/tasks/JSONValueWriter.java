package hu.logcontrol.wasteprogram.tasks;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

import hu.logcontrol.wasteprogram.enums.HandlerMessageIdentifiers;
import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.taskmanager.CustomThreadPoolManager;

public class JSONValueWriter implements Callable {

    private WeakReference<CustomThreadPoolManager> customThreadPoolManagerWeakReference;

    private static final String FILE_NAME = "values.json";
    private static final String DEFAULT_FILE_NAME = "DefaultValues.json";

    private Context context;
    private String jsonIdValue;
    private boolean valueBoolean;
    private String valueString;
    private JSONValueWriter.MODE mode;

    private Message message;

    private File file;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;

    public JSONValueWriter(Context context, String jsonIdValue, String valueString, MODE mode) {
        this.context = context;
        this.jsonIdValue = jsonIdValue;
        this.valueString = valueString;
        this.mode = mode;
    }

    public JSONValueWriter(Context context, String jsonIdValue, boolean valueBoolean, MODE mode) {
        this.context = context;
        this.valueBoolean = valueBoolean;
        this.jsonIdValue = jsonIdValue;
        this.mode = mode;
    }

    public void setCustomThreadPoolManager(CustomThreadPoolManager customThreadPoolManager) {
        this.customThreadPoolManagerWeakReference = new WeakReference<>(customThreadPoolManager);
    }

    @Override
    public Object call() throws Exception {

        try {
            if (Thread.interrupted()) throw new InterruptedException();

            JSONObject jsonObject = getJSONObjectFromJSONFile(context, DEFAULT_FILE_NAME);
            jsonObject.remove(jsonIdValue);

            switch (mode){
                case WRITE_STRING:{
                    jsonObject.put(jsonIdValue, valueString);
                    break;
                }
                case WRITE_BOOLEAN:{
                    jsonObject.put(jsonIdValue, valueBoolean);
                    break;
                }
            }

            file = new File(context.getFilesDir() + File.separator + FILE_NAME);

            if(!file.exists()){
                file.createNewFile();
            }

            if(file.exists()){
                fileWriter = new FileWriter(file);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(formatJSONObjectString(jsonObject.toString()));
            }
        }
        catch (InterruptedException e) {

            e.printStackTrace();
            sendMessageToPresenterHandler(JSONWriterEnums.THREAD_INTERRUPTED);
        }
        catch (IOException e){

            e.printStackTrace();
            sendMessageToPresenterHandler(JSONWriterEnums.IOEXCEPTION);
        }
        finally {
            if(bufferedWriter != null) bufferedWriter.close();
            sendMessageToPresenterHandler(JSONWriterEnums.WRITE_SUCCES);
        }

        return null;
    }

    public static JSONObject getJSONObjectFromJSONFile(Context context, String fileName){

        JSONObject jsonObject = null;
        String seged = null;

        try{
            InputStream file = context.getApplicationContext().getAssets().open(fileName);
            byte[] fromArray = new byte[file.available()];
            file.read(fromArray);
            seged = new String(fromArray, StandardCharsets.UTF_8);
            file.close();

            jsonObject = new JSONObject(seged);
        }
        catch (Exception e){
            e.printStackTrace();

        }

        if(jsonObject == null) return null;

        return jsonObject;
    }

    private String formatJSONObjectString(String jsonObjectString){
        return jsonObjectString
                .replace("{", "{\n\t")
                .replace("}", "\n}")
                .replace(",", ",\n\t")
                .replace(":", ": ")
                .replace("\\", "");
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
        READ_FAILED
    }
}
