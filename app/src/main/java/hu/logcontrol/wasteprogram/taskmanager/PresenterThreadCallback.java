package hu.logcontrol.wasteprogram.taskmanager;

import android.os.Message;

public interface PresenterThreadCallback {
    void sendResultToPresenter(Message message);
}
