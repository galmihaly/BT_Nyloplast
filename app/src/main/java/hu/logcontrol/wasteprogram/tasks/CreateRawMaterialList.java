package hu.logcontrol.wasteprogram.tasks;

import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;

import hu.logcontrol.wasteprogram.adapters.RawMaterialAdapter;
import hu.logcontrol.wasteprogram.enums.HandlerMessageIdentifiers;
import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.models.LocalRawMaterialsStorage;
import hu.logcontrol.wasteprogram.models.RawMaterial;
import hu.logcontrol.wasteprogram.taskmanager.CustomThreadPoolManager;

public class CreateRawMaterialList implements Callable {

    private WeakReference<CustomThreadPoolManager> customThreadPoolManagerWeakReference;
    private RawMaterial rawMaterial;
    private List<RawMaterial> rawMaterialList;

    private Message message = null;

    public void setCustomThreadPoolManager(CustomThreadPoolManager customThreadPoolManager) {
        this.customThreadPoolManagerWeakReference = new WeakReference<>(customThreadPoolManager);
    }

    public CreateRawMaterialList(RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    @Override
    public Object call() throws Exception {
        try {
            if (Thread.interrupted()) throw new InterruptedException();

            rawMaterialList = LocalRawMaterialsStorage.getInstance().getRawMaterialList();

            if(rawMaterialList != null) {
                rawMaterialList.add(rawMaterial);
                message = Helper.createMessage(HandlerMessageIdentifiers.RAWMATERIAL_LIST_ADD_ELEMENT_SUCCESS, "Az RawMaterial elem hozzáadása a listához sikerült!");
            }

            if(customThreadPoolManagerWeakReference != null && customThreadPoolManagerWeakReference.get() != null) {
                if(message != null) {
                    customThreadPoolManagerWeakReference.get().sendResultToPresenter(message);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();

            message = Helper.createMessage(HandlerMessageIdentifiers.RAWMATERIAL_LIST_ADD_ELEMENT_FAILED, "Az RawMaterial elem hozzáadása a listához sikertelen!");


            if(customThreadPoolManagerWeakReference != null && customThreadPoolManagerWeakReference.get() != null) {
                if(message != null) {
                    customThreadPoolManagerWeakReference.get().sendResultToPresenter(message);
                }
            }
        }

        return null;
    }
}
