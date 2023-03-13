package hu.logcontrol.wasteprogram.presenters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

import hu.logcontrol.wasteprogram.MainActivity;
import hu.logcontrol.wasteprogram.ModesOne;
import hu.logcontrol.wasteprogram.ModesThree;
import hu.logcontrol.wasteprogram.ModesTwo;
import hu.logcontrol.wasteprogram.RawMaterialCreationActivity;
import hu.logcontrol.wasteprogram.RawMaterialTypeMassCreationActivity;
import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.enums.HandlerMessageIdentifiers;
import hu.logcontrol.wasteprogram.interfaces.IModesOneView;
import hu.logcontrol.wasteprogram.interfaces.IModesTwoView;
import hu.logcontrol.wasteprogram.interfaces.IProgramPresenter;
import hu.logcontrol.wasteprogram.interfaces.IMainView;
import hu.logcontrol.wasteprogram.logger.ApplicationLogger;
import hu.logcontrol.wasteprogram.logger.LogLevel;
import hu.logcontrol.wasteprogram.models.RawMaterial;
import hu.logcontrol.wasteprogram.models.RawMaterialTypeMass;
import hu.logcontrol.wasteprogram.taskmanager.CustomThreadPoolManager;
import hu.logcontrol.wasteprogram.taskmanager.PresenterThreadCallback;
import hu.logcontrol.wasteprogram.tasks.AddElementToList;
import hu.logcontrol.wasteprogram.tasks.CreateFile;

public class ProgramPresenter implements IProgramPresenter, PresenterThreadCallback {

    private IMainView iMainView;
    private IModesOneView iModesOneView;
    private IModesTwoView iModesTwoView;

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

    public ProgramPresenter(IModesTwoView iModesTwoView, Context context) {
        this.iModesTwoView = iModesTwoView;
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
        int requestCode = -1;

        switch (activityEnum){
            case MAIN_ACTIVITY:{
                intent = new Intent(context, MainActivity.class);
                break;
            }
            case MODES_ACTIVITY_ONE:{
                intent = new Intent(context, ModesOne.class);
                break;
            }
            case MODES_ACTIVITY_TWO:{
                intent = new Intent(context, ModesTwo.class);
                break;
            }
            case MODES_ACTIVITY_THREE:{
                intent = new Intent(context, ModesThree.class);
                break;
            }
            case RAW_MATERIAL_CREATION_ACTIVITY:{
                intent = new Intent(context, RawMaterialCreationActivity.class);
                break;
            }
            case RAW_MATERIAL_TYPEMASS_CREATION_ACTIVITY:{
                intent = new Intent(context, RawMaterialTypeMassCreationActivity.class);
                break;
            }
            case FOLDERPICKER_ACTIVITY:{
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                break;
            }
        }

        if(intent == null) return;
        if(iMainView != null) iMainView.openActivityByIntent(intent);
        if(iModesOneView != null) iModesOneView.openActivityByIntent(intent);
        if(iModesTwoView != null) iModesTwoView.openActivityByIntent(intent);
    }

    @Override
    public void exitApplicationPresenter() {
        if(iMainView != null) iMainView.exitApplication();
    }

    @Override
    public void addRawMaterialToAdapterList(RawMaterial rawMaterial) {

        try {
            ApplicationLogger.logging(LogLevel.INFORMATION, "A RawMaterial objetum hozzáadása az Adapter listájához elkezdődött.");

            AddElementToList callable = new AddElementToList(AddElementToList.RunModes.ADD_RAWMATERIAL, rawMaterial);
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
    public void addRawMaterialTypeMassToAdapterList(RawMaterialTypeMass rawMaterialTypeMass) {
        try {
            ApplicationLogger.logging(LogLevel.INFORMATION, "A RawMaterialTypeMass objetum hozzáadása az Adapter listájához elkezdődött.");

            AddElementToList callable = new AddElementToList(AddElementToList.RunModes.ADD_RAWMATERIALTYPEMASS, rawMaterialTypeMass);
            callable.setCustomThreadPoolManager(mCustomThreadPoolManager);
            mCustomThreadPoolManager.addCallableMethod(callable);

            ApplicationLogger.logging(LogLevel.INFORMATION, "A RawMaterialTypeMass objetum hozzáadása az Adapter listájához befejeződött.");
        }
        catch (Exception e){
            e.printStackTrace();
            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
        }
    }

    @Override
    public void createFileFromRawMaterialList(Uri uri) {
        try {
            ApplicationLogger.logging(LogLevel.INFORMATION, "A RawMaterial lista átmásolása txt fájlba elkezdődött.");

            CreateFile callable = new CreateFile(context, CreateFile.RunModes.CREATE_RAWMATERIAL_CSV, uri, RawMaterial.getCSVHeader(), "csv");
            callable.setCustomThreadPoolManager(mCustomThreadPoolManager);
            mCustomThreadPoolManager.addCallableMethod(callable);

            ApplicationLogger.logging(LogLevel.INFORMATION, "A RawMaterial lista átmásolása txt fájlba befejeződött.");
        }
        catch (Exception e){
            e.printStackTrace();
            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
        }
    }

    @Override
    public void createFileFromRawMaterialTypeMassList(Uri uri) {
        try {
            ApplicationLogger.logging(LogLevel.INFORMATION, "A RawMaterial lista átmásolása txt fájlba elkezdődött.");

            CreateFile callable = new CreateFile(context, CreateFile.RunModes.CREATE_RAWMATERIALTYPEMASS_CSV, uri, RawMaterialTypeMass.getCSVHeader(), "csv");
            callable.setCustomThreadPoolManager(mCustomThreadPoolManager);
            mCustomThreadPoolManager.addCallableMethod(callable);

            ApplicationLogger.logging(LogLevel.INFORMATION, "A RawMaterial lista átmásolása txt fájlba befejeződött.");
        }
        catch (Exception e){
            e.printStackTrace();
            ApplicationLogger.logging(LogLevel.FATAL, e.getMessage());
        }
    }

    @Override
    public void setSaveButtonState(EditButtonEnums editButtonEnum) {
        if(editButtonEnum == null) return;
        if(iModesOneView != null) iModesOneView.settingSaveButton(editButtonEnum);
        if(iModesTwoView != null) iModesTwoView.settingSaveButton(editButtonEnum);
    }

    @Override
    public void sendMessageToView(String message) {
        if(message == null) return;
        Log.e("pre", message);
        if(iModesOneView != null) iModesOneView.getMessageFromPresenter(message);
        if(iModesTwoView != null) iModesTwoView.getMessageFromPresenter(message);
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
                case HandlerMessageIdentifiers.RAWMATERIAL_LIST_ADD_ELEMENT_SUCCESS:{
                    ApplicationLogger.logging(LogLevel.INFORMATION, getWeakReferenceNotification(msg));
                    break;
                }
                case HandlerMessageIdentifiers.RAWMATERIAL_LIST_ADD_ELEMENT_FAILED:
                case HandlerMessageIdentifiers.TEXTFILE_ADD_TO_DIRECTORY_PATH_FAILED: {
                    ApplicationLogger.logging(LogLevel.ERROR, getWeakReferenceNotification(msg));
                    break;
                }
                case HandlerMessageIdentifiers.TEXTFILE_ADD_TO_DIRECTORY_PATH_SUCCES:{
                    ApplicationLogger.logging(LogLevel.INFORMATION, getWeakReferenceNotification(msg));

                    iProgramPresenterWeakReference.get().setSaveButtonState(EditButtonEnums.SAVE_BUTTON_DISABLED);
                    iProgramPresenterWeakReference.get().sendMessageToView("A fájl létrehozása sikeres!");
                    break;
                }
            }
        }

        private String getWeakReferenceNotification(Message message){
            Bundle bundle = message.getData();
            return bundle.getString(HandlerMessageIdentifiers.MESSAGE_BODY);
        }
    }
}
