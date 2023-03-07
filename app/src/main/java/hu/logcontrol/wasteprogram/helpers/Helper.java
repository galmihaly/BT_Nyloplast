package hu.logcontrol.wasteprogram.helpers;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {

    @SuppressLint("SimpleDateFormat")
    public static String getReadableTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        return sdf.format(new Date());
    }

}
