package hu.logcontrol.wasteprogram.helpers;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JSONFileReaderHelper {

    public static String getStringFromJSONFile(Context context, String fileName, String dataMember){
        JSONObject jsonObject = null;
        String s = null;
        String result = null;
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
                result = jsonObject.getString(dataMember);
            }

        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            return null;
        }

        if(result == null) return null;

        return result;
    }

    public static boolean existJSONFile(Context context, String fileName){
        return new File(context.getFilesDir() + File.separator + fileName).exists();
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
