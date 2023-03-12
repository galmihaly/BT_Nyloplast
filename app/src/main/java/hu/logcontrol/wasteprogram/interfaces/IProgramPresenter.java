package hu.logcontrol.wasteprogram.interfaces;

import android.content.Context;
import android.net.Uri;

import java.util.List;

import hu.logcontrol.wasteprogram.adapters.RawMaterialAdapter;
import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.models.RawMaterial;
import hu.logcontrol.wasteprogram.models.RawMaterialTypeMass;

public interface IProgramPresenter {
    void initTaskManager();
    void openActivityByEnum(ActivityEnums activityEnum);
    void exitApplicationPresenter();
    void addRawMaterialToAdapterList(RawMaterial rawMaterial);
    void addRawMaterialTypeMassToAdapterList(RawMaterialTypeMass rawMaterialTypeMass);
    void createFileFromRawMaterialList(Uri uri);
    void createFileFromRawMaterialTypeMassList(Uri uri);
    void setSaveButtonState(EditButtonEnums editButtonEnum);
    void sendMessageToView(String message);
}
