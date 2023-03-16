package hu.logcontrol.wasteprogram.interfaces;

import android.content.Intent;

import hu.logcontrol.wasteprogram.enums.EditButtonEnums;

public interface IModesTwoView {
    void openActivityByIntent(Intent intent);
    void settingButton(EditButtonEnums editButtonEnum);
    void getMessageFromPresenter(String message);
}
