package hu.logcontrol.wasteprogram;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import hu.logcontrol.wasteprogram.adapters.RawMaterialAdapter;
import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.helpers.JSONFileHelper;
import hu.logcontrol.wasteprogram.interfaces.IModesOneView;
import hu.logcontrol.wasteprogram.logger.ApplicationLogger;
import hu.logcontrol.wasteprogram.models.LocalRawMaterialsStorage;
import hu.logcontrol.wasteprogram.models.RawMaterial;
import hu.logcontrol.wasteprogram.presenters.ProgramPresenter;

public class ModesOne extends AppCompatActivity implements IModesOneView {

    private ProgramPresenter programPresenter;

    private ImageButton addButton;
    private ImageButton backButton;
    private ImageButton saveButton;

    private ConstraintLayout mainModesOneCL;

    private RecyclerView recycleViewModesOneRV;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    private boolean isHaveToClearList = false;

    private RawMaterialAdapter rawMaterialAdapter;
    private List<RawMaterial> rawMaterialList;

    private String separatorFromJSON;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == 1){
                    Intent intent = result.getData();

                    if(intent == null) return;

                    String rawMatTypeTextBox = intent.getStringExtra("rawMatTypeTextBox");
                    String rawMatCountTextBox = intent.getStringExtra("rawMatCountTextBox");
                    String rawMatContentTextBox = intent.getStringExtra("rawMatContentTextBox");

                    RawMaterial rawMaterial = new RawMaterial(ApplicationLogger.getUTCDateTimeString(),rawMatTypeTextBox, rawMatCountTextBox, rawMatContentTextBox);

                    char c = Helper.getSeparator(separatorFromJSON);
                    if(c != 0) rawMaterial.setSeparator(c);

                    programPresenter.addRawMaterialToAdapterList(rawMaterial);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modes_one);
        initView();

        programPresenter = new ProgramPresenter(this, getApplicationContext());
        programPresenter.initTaskManager();

        rawMaterialList = LocalRawMaterialsStorage.getInstance().getRawMaterialList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(programPresenter != null){
            if(addButton != null){
                addButton.setOnClickListener(view -> {
                    programPresenter.openActivityByEnum(ActivityEnums.RAW_MATERIAL_CREATION_ACTIVITY);
                });
            }

            if(backButton != null){
                backButton.setOnClickListener(view -> {
                    programPresenter.openActivityByEnum(ActivityEnums.MAIN_ACTIVITY);
                });
            }

            if(saveButton != null){

                int sizeOfRawMaterialList = LocalRawMaterialsStorage.getInstance().getRawMaterialListSize();

                if(sizeOfRawMaterialList > 0){
                    settingButton(EditButtonEnums.SAVE_BUTTON_ENABLED);

                    saveButton.setOnClickListener(view -> {
                        programPresenter.createFileFromRawMaterialList();
                    });
                }
            }
        }

        onRefreshListener = () -> {
            if(isHaveToClearList){
                rawMaterialAdapter.clearRawMaterialList();
                settingButton(EditButtonEnums.SAVE_BUTTON_DISABLED);
                isHaveToClearList = false;
            }

            rawMaterialList = LocalRawMaterialsStorage.getInstance().getRawMaterialList();

            swipeRefreshLayout.setRefreshing(false);
        };
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        rawMaterialAdapter = new RawMaterialAdapter(getApplicationContext(), rawMaterialList,  this);
        recycleViewModesOneRV.setAdapter(rawMaterialAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BUTTON_R1 || keyCode == KeyEvent.KEYCODE_BUTTON_L1){
            if(event.getAction() == KeyEvent.ACTION_DOWN){
                addButton.callOnClick();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void openActivityByIntent(Intent intent) {
        activityResultLauncher.launch(intent);
    }

    @Override
    public void settingButton(EditButtonEnums editButtonEnum) {
        if(saveButton == null) return;

        switch (editButtonEnum){
            case SAVE_BUTTON_ENABLED:{

                saveButton.setEnabled(true);
                saveButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.save_button_background));

                break;
            }
            case SAVE_BUTTON_DISABLED:{

                saveButton.setEnabled(false);
                saveButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_circle));

                break;
            }
            case ADD_BUTTON_ENABLED:{

                addButton.setEnabled(true);
                addButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_button_background));

                break;
            }
            case ADD_BUTTON_DISABLED:{

                addButton.setEnabled(false);
                addButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_circle));

                break;
            }
        }
    }

    @Override
    public void getMessageFromPresenter(String message) {
        if(message == null) return;
        new Handler(Looper.getMainLooper()).post(() -> { Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show(); });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void clearRawMaterialList(String message) {
        if(message == null) return;
        if(swipeRefreshLayout == null) return;
        if(onRefreshListener == null) return;

        getMessageFromPresenter(message);

        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            isHaveToClearList = true;
            onRefreshListener.onRefresh();
        });
    }

    private void initView() {

        addButton = findViewById(R.id.addButton);
        backButton = findViewById(R.id.backButton);

        saveButton = findViewById(R.id.saveButton);
        settingButton(EditButtonEnums.SAVE_BUTTON_DISABLED);

        recycleViewModesOneRV = findViewById(R.id.recycleViewModesOneRV);
        recycleViewModesOneRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mainModesOneCL = findViewById(R.id.mainModesOneCL);
        swipeRefreshLayout = findViewById(R.id.swipeRefresLayoutModesOneRV);

        separatorFromJSON = JSONFileHelper.getString(getApplicationContext(), "values.json", "FileSeparatorCharacter");
    }
}