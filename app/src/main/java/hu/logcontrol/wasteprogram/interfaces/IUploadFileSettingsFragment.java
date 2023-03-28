package hu.logcontrol.wasteprogram.interfaces;

import android.content.Intent;

public interface IUploadFileSettingsFragment {
    void getMessageFromPresenter(String message);
    void openActivityByIntent(Intent intent);
}
