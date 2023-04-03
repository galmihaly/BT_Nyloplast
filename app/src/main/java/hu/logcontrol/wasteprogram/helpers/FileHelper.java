package hu.logcontrol.wasteprogram.helpers;

import android.content.Context;

import java.io.File;

import hu.logcontrol.wasteprogram.logger.ApplicationLogger;

public class FileHelper {

    public static String[] getSaveLocalFullPath(Context context, String listType, String fileExtension){

        String[] results = null;

        String fileName = ApplicationLogger.getDateTimeString() + listType + "." + fileExtension;
        String path = JSONFileHelper.getString(context, "values.json", "LocalSavePath");

        if(path != null){
            results = new String[2];
            results[0] = path;
            results[1] = fileName;
        }

        if(results == null) return null;
        return results;
    }

    public static String[] getSaveGlobalFullPath(Context applicationContext) {

        String[] results = null;

        String globalSavePath = JSONFileHelper.getString(applicationContext, "values.json", "GlobalSavePath");
        String username = JSONFileHelper.getString(applicationContext, "values.json", "Username");
        String password = JSONFileHelper.getString(applicationContext, "values.json", "Password");

        if(globalSavePath != null && username != null && password != null){

            results = new String[3];
            results[0] = globalSavePath;
            results[1] = username;
            results[2] = password;
        }

        if(results == null) return null;
        return results;
    }
}
