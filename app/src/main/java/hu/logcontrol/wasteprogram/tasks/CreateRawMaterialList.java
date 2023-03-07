package hu.logcontrol.wasteprogram.tasks;

import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;

import hu.logcontrol.wasteprogram.adapters.RawMaterialAdapter;
import hu.logcontrol.wasteprogram.enums.HandlerMessageIdentifiers;
import hu.logcontrol.wasteprogram.helpers.Helper;
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

    public CreateRawMaterialList(RawMaterial rawMaterial, List<RawMaterial> rawMaterialList) {
        this.rawMaterial = rawMaterial;
        this.rawMaterialList = rawMaterialList;
    }

    @Override
    public Object call() throws Exception {
        try {
            if (Thread.interrupted()) throw new InterruptedException();

            for (int i = 0; i < rawMaterialList.size(); i++) {
                Log.e("task", rawMaterialList.get(i).getDate());
                Log.e("task", rawMaterialList.get(i).getMaterialType());
                Log.e("task", rawMaterialList.get(i).getDoseNumber());
            }

            if(rawMaterialList != null) {
                rawMaterialList.add(rawMaterial);

                message = Helper.createMessage(HandlerMessageIdentifiers.ADAPTER_CREATED, "Az adapter lista létrehozása sikerült!");
                message.obj = rawMaterialList;
            }

            if(customThreadPoolManagerWeakReference != null && customThreadPoolManagerWeakReference.get() != null) {
                if(message != null) {
                    customThreadPoolManagerWeakReference.get().sendResultToPresenter(message);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
