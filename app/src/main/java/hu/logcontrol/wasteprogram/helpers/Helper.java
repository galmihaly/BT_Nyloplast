package hu.logcontrol.wasteprogram.helpers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import hu.logcontrol.wasteprogram.enums.HandlerMessageIdentifiers;

public class Helper {


    public static Message createMessage(int id, String dataString) {
        Bundle bundle = new Bundle();
        bundle.putString(HandlerMessageIdentifiers.MESSAGE_BODY, dataString);
        Message message = new Message();
        message.what = id;
        message.setData(bundle);

        return message;
    }

    public static void hideNavigationBar(AppCompatActivity appCompatActivity){
        View decorView = appCompatActivity.getWindow().getDecorView();

        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(flags);
    }

    @SuppressLint("SdCardPath")
    public static String formatPathString(List<String> path) {
        if(path == null) return null;

        StringBuilder sb = new StringBuilder();

        String[] pathSegmens = path.get(1).split(":");

        if(pathSegmens.length == 0){ return sb.append("").toString(); }
        if(pathSegmens.length == 1){ return sb.append("/sdcard").toString(); }

        return sb
                .append("/sdcard")
                .append("/")
                .append(pathSegmens[1])
                .toString();
    }

    public static char getSeparator(String separatorFromJSON){
        if(separatorFromJSON == null) return 0;

        char c = 0;
        switch (separatorFromJSON){
            case "vessző":{ c = ','; break; }
            case "pontosvessző":{ c = ';'; break; }
            case "tabulátor":{ c = (char)9; break; }
        }

        if(c == 0) return 0;
        return c;
    }
}
