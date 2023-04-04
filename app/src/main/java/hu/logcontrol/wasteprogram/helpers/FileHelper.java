package hu.logcontrol.wasteprogram.helpers;

import android.content.Context;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.File;

import hu.logcontrol.wasteprogram.logger.ApplicationLogger;
import hu.logcontrol.wasteprogram.models.LocalEncryptedPreferences;

public class FileHelper {

    public static String[] getSavedLocalFullPath(Context context, String listType, String fileExtension){

        String[] results = null;

        LocalEncryptedPreferences preferences = LocalEncryptedPreferences.getInstance(
                "values",
                MasterKeys.AES256_GCM_SPEC,
                context.getApplicationContext(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        String fileName = ApplicationLogger.getDateTimeString() + listType + "." + fileExtension;
        String path = preferences.getStringValueByKey("LocalSavePath");

        if(path != null){
            results = new String[2];
            results[0] = path;
            results[1] = fileName;
        }

        if(results == null) return null;
        return results;
    }

    public static String[] getSavedGlobalFullPath(Context context) {

        String[] results = null;

        LocalEncryptedPreferences preferences = LocalEncryptedPreferences.getInstance(
                "values",
                MasterKeys.AES256_GCM_SPEC,
                context.getApplicationContext(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        String hostName = preferences.getStringValueByKey("HostName");
        String portNumber = preferences.getStringValueByKey("PortNumber");
        String username = preferences.getStringValueByKey("Username");
        String password = preferences.getStringValueByKey("Password");
        String globalSavePath = preferences.getStringValueByKey("GlobalSavePath");

//        Log.e("hostName+", hostName);
//        Log.e("portNumber+", portNumber);
//        Log.e("userName+", username);
//        Log.e("password+", password);
//        Log.e("globalSavePath+", globalSavePath);

        if(globalSavePath != null && username != null && password != null){

            results = new String[5];
            results[0] = hostName;
            results[1] = portNumber;
            results[2] = username;
            results[3] = password;
            results[4] = globalSavePath;
        }

        if(results == null) return null;
        return results;
    }
}
