package hu.logcontrol.wasteprogram.helpers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    @SuppressLint("SimpleDateFormat")
    public static String getReadableTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        return sdf.format(new Date());
    }
}
