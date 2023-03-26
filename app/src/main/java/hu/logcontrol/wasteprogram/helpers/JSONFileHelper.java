package hu.logcontrol.wasteprogram.helpers;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JSONFileHelper {

    public static String getString(Context context, String fileName, String dataMember){
        JSONObject jsonObject = null;
        String s = null;
        String result = null;
        try {

            File file = new File(context.getApplicationContext().getFilesDir() + File.separator + fileName);
            if(file.exists()){
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fileInputStream);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();

                while((s = br.readLine()) != null) sb.append(s);

                jsonObject = new JSONObject(sb.toString());
                result = jsonObject.getString(dataMember);
            }

        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            return null;
        }

        if(result == null) return null;

        return result;
    }

    public static boolean getBoolean(Context context, String fileName, String dataMember){
        JSONObject jsonObject = null;
        String s = null;
        boolean result = false;
        FileInputStream fileInputStream = null;
        try {

            File file = new File(context.getApplicationContext().getFilesDir() + File.separator + fileName);
            if(file.exists()){
                fileInputStream = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fileInputStream);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();

                while((s = br.readLine()) != null) sb.append(s);

                jsonObject = new JSONObject(sb.toString());
                result = jsonObject.getBoolean(dataMember);
            }

        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public static JSONObject getJSONObject(Context context, String fileName){

        JSONObject result = null;
        String s;
        FileInputStream fileInputStream;

        try {

            File file = new File(context.getApplicationContext().getFilesDir() + File.separator + fileName);
            if(file.exists()){
                fileInputStream = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fileInputStream);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();

                while((s = br.readLine()) != null) sb.append(s);

                result = new JSONObject(sb.toString());
            }

        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            return null;
        }

        return result;
    }

    public static boolean isExist(Context context, String fileName){
        return new File(context.getFilesDir() + File.separator + fileName).exists();
    }

    public static void initBaseJSONFile(Context context, String localBaseFileName, String localNewFileName){
        if(context == null || localBaseFileName == null || localNewFileName == null) return;

        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;
        try {

            InputStream is = context.getAssets().open(localBaseFileName);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String result = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(result);

            File file = new File(context.getApplicationContext().getFilesDir() + File.separator + localNewFileName);
            if(!file.exists()){ file.createNewFile(); }

            if(file.exists()){
                fileWriter = new FileWriter(file);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(formatJSONObjectString(jsonObject.toString()));
                bufferedWriter.close();
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static String formatJSONObjectString(String jsonObjectString){
        return jsonObjectString
                .replace("{", "{\n\t")
                .replace("}", "\n}")
                .replace(",", ",\n\t")
                .replace(":", ": ")
                .replace("\\", "");
    }
}
