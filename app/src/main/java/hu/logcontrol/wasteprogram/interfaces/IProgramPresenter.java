package hu.logcontrol.wasteprogram.interfaces;

import android.content.Context;
import android.net.Uri;

import java.util.List;

import hu.logcontrol.wasteprogram.adapters.RawMaterialAdapter;
import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.enums.ViewButtons;
import hu.logcontrol.wasteprogram.models.RawMaterial;
import hu.logcontrol.wasteprogram.models.RawMaterialTypeMass;
import hu.logcontrol.wasteprogram.models.RecycledMaterial;

public interface IProgramPresenter {
    void initTaskManager();
    void openActivityByEnum(ActivityEnums activityEnum);
    void addRawMaterialToAdapterList(RawMaterial rawMaterial);
    void addRawMaterialTypeMassToAdapterList(RawMaterialTypeMass rawMaterialTypeMass);
    void addRecycledMaterialToAdapterList(RecycledMaterial recycledMaterial);

    void createFileFromRawMaterialList();
    void createFileFromRawMaterialTypeMassList();
    void createFileFromRecycledMaterialTypeMassList();

    void setSaveButtonState(EditButtonEnums editButtonEnum);
    void sendMessageToView(String message);
    void sendMessageToSettingsView(String message);
    void saveBooleanValueToSharedPreferencesFile(String jsonIdValue, boolean value);
    void saveStringValueToSharedPreferencesFile(String jsonIdValue, String value);

    void clearRawMaterialList(String message);
}
