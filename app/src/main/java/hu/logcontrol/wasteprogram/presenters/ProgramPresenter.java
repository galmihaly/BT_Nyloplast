package hu.logcontrol.wasteprogram.presenters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import hu.logcontrol.wasteprogram.MainActivity;
import hu.logcontrol.wasteprogram.ModesOne;
import hu.logcontrol.wasteprogram.ModesThree;
import hu.logcontrol.wasteprogram.ModesTwo;
import hu.logcontrol.wasteprogram.RawMaterialCreationActivity;
import hu.logcontrol.wasteprogram.adapters.RawMaterialAdapter;
import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.enums.HandlerMessageIdentifiers;
import hu.logcontrol.wasteprogram.interfaces.IModesOneView;
import hu.logcontrol.wasteprogram.interfaces.IProgramPresenter;
import hu.logcontrol.wasteprogram.interfaces.IMainView;
import hu.logcontrol.wasteprogram.logger.ApplicationLogger;
import hu.logcontrol.wasteprogram.logger.LogLevel;
import hu.logcontrol.wasteprogram.models.RawMaterial;
import hu.logcontrol.wasteprogram.taskmanager.CustomThreadPoolManager;
import hu.logcontrol.wasteprogram.taskmanager.PresenterThreadCallback;
import hu.logcontrol.wasteprogram.tasks.CreateRawMaterialList;

public class ProgramPresenter implements IProgramPresenter, PresenterThreadCallback {

    private IMainView iMainView;
    private IModesOneView iModesOneView;

    private Context context;
    private CustomThreadPoolManager mCustomThreadPoolManager;
    private ProgramHandler programHandler;

    private List<RawMaterial> rawMaterialList;

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
    public void addRawMaterialToAdapterList(RawMaterial rawMaterial, List<RawMaterial> rawMaterialList) {

        rawMaterialList.add(rawMaterial);
        sendAdapterToView(rawMaterialList);

//        for (int i = 0; i < rawMaterialList.size(); i++) {
//            Log.e("presenter", rawMaterialList.get(i).getDate());
//            Log.e("presenter", rawMaterialList.get(i).getMaterialType());
//            Log.e("presenter", rawMaterialList.get(i).getDoseNumber());
//        }

        try {
            ApplicationLogger.logging(LogLevel.INFORMATION, "A RawMaterial objetum hozzáadása az Adapter listájához elkezdődött.");

            CreateRawMaterialList callable = new CreateRawMaterialList(rawMaterial, rawMaterialList);
            callable.setCustomThreadPoolManager(mCustomThreadPoolManager);
            mCustomThreadPoolManager.addCallableMethod(callable);

            ApplicationLogger.logging(LogLevel.INFORMATION, "A RawMaterial objetum hozzáadása az Adapter listájához befejeződött.");
        }
        catch (Exception e){
            e.printStackTrace();
            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
        }
    }

    @Override
    public void sendAdapterToView(List<RawMaterial> rawMaterialList) {
        if(iModesOneView == null) return;
        iModesOneView.getAdapterFromPresenter(rawMaterialList);
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
                case HandlerMessageIdentifiers.ADAPTER_CREATED:{

                    if(msg.obj instanceof List){
                        iProgramPresenterWeakReference.get().sendAdapterToView((List) msg.obj);
                    }
                    break;
                }
            }
        }
    }
}
