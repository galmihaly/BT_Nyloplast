package hu.logcontrol.wasteprogram.interfaces;

import android.content.Intent;

public interface IMainView {
    void openActivityByIntent(Intent intent);
    void exitApplication();
}
