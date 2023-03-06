package hu.logcontrol.wasteprogram.interfaces;

import hu.logcontrol.wasteprogram.enums.ActivityEnums;

public interface IProgramPresenter {
    void initTaskManager();
    void openActivityByEnum(ActivityEnums activityEnum);
    void exitApplicationPresenter();
}
