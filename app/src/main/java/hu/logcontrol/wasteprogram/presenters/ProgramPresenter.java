package hu.logcontrol.wasteprogram.presenters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

import hu.logcontrol.wasteprogram.MainActivity;
import hu.logcontrol.wasteprogram.ModesOne;
import hu.logcontrol.wasteprogram.ModesThree;
import hu.logcontrol.wasteprogram.ModesTwo;
import hu.logcontrol.wasteprogram.RawMaterialCreationActivity;
import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.interfaces.IModesOneView;
import hu.logcontrol.wasteprogram.interfaces.IProgramPresenter;
import hu.logcontrol.wasteprogram.interfaces.IMainView;
import hu.logcontrol.wasteprogram.logger.ApplicationLogger;
import hu.logcontrol.wasteprogram.logger.LogLevel;
import hu.logcontrol.wasteprogram.taskmanager.CustomThreadPoolManager;
import hu.logcontrol.wasteprogram.taskmanager.PresenterThreadCallback;

public class ProgramPresenter implements IProgramPresenter, PresenterThreadCallback {

    private IMainView iMainView;
    private IModesOneView iModesOneView;

    private Context context;
    private CustomThreadPoolManager mCustomThreadPoolManager;
    private ProgramHandler programHandler;

    public ProgramPresenter(IMainView iMainView, Context context) {
        this.iMainView = iMainView;
        this.context = context.getApplicationContext();
    }

    public ProgramPresenter(IModesOneView iModesOneView, Context context) {
        this.iModesOneView = iModesOneView;
        this.context = context;
    }

    @Override
    public void initTaskManager() {
        try {
            ApplicationLogger.logging(LogLevel.INFORMATION, "A feladatkezelő létrehozása megkezdődött.");

            programHandler = new ProgramHandler(Looper.myLooper(), this);
            mCustomThreadPoolManager = CustomThreadPoolManager.getsInstance();
            mCustomThreadPoolManager.setPresenterCallback(this);

            ApplicationLogger.logging(LogLevel.INFORMATION, "A feladatkezelő létrehozása befejeződött.");
        }
        catch (Exception e){
            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
        }
    }

    @Override
    public void openActivityByEnum(ActivityEnums activityEnum) {
        if(activityEnum == null) return;

        Intent intent = null;

        switch (activityEnum){
            case MAIN_ACTIVITY:{
                intent = new Intent(context, MainActivity.class);
                break;
            }
            case ModesActivty_One:{
                intent = new Intent(context, ModesOne.class);
                break;
            }
            case ModesActivity_Two:{
                intent = new Intent(context, ModesTwo.class);
                break;
            }
            case ModesActivity_Three:{
                intent = new Intent(context, ModesThree.class);
                break;
            }
            case RawMaterialCreationActivity:{
                intent = new Intent(context, RawMaterialCreationActivity.class);
                break;
            }
        }

        if(intent == null) return;
        if(iMainView != null) iMainView.openActivityByIntent(intent);
        if(iModesOneView != null) iModesOneView.openActivityByIntent(intent);
    }

    @Override
    public void exitApplicationPresenter() {
        if(iMainView != null) iMainView.exitApplication();
    }


    @Override
    public void sendResultToPresenter(Message message) {
        if(programHandler == null) return;
        programHandler.handleMessage(message);
    }


    private static class ProgramHandler extends Handler {

        private WeakReference<IProgramPresenter> iProgramPresenterWeakReference;

        public ProgramHandler(Looper looper, IProgramPresenter iProgramPresenter) {
            super(looper);
            this.iProgramPresenterWeakReference = new WeakReference<>(iProgramPresenter);
        }

        // Ui-ra szánt üzenetet kezelejük itt
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

            }
        }
    }
}
