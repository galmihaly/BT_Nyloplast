package hu.logcontrol.wasteprogram.interfaces;

import android.content.Intent;

import hu.logcontrol.wasteprogram.enums.EditButtonEnums;

public interface ISettingsView {
    void openActivityByIntent(Intent intent);
    void settingSaveButton(EditButtonEnums editButtonEnum);
    void getMessageFromPresenter(String message);
}
