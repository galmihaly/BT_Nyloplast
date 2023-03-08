package hu.logcontrol.wasteprogram.interfaces;

import android.content.Context;
import android.net.Uri;

import java.util.List;

import hu.logcontrol.wasteprogram.adapters.RawMaterialAdapter;
import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.models.RawMaterial;

public interface IProgramPresenter {
    void initTaskManager();
    void openActivityByEnum(ActivityEnums activityEnum);
    void exitApplicationPresenter();
    void addRawMaterialToAdapterList(RawMaterial rawMaterial);
    void createTextFileFromRawMaterialList(Uri uri);
}
