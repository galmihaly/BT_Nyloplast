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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

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
    private boolean isEnableBarcodeReaderMode;

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

                    if(rawMaterialList != null) rawMaterialList.add(rawMaterial);

                    if(saveButton != null){
                        settingButton(EditButtonEnums.SAVE_BUTTON_ENABLED);

                        if(saveButton != null){
                            saveButton.setOnClickListener(view -> {
                                programPresenter.createFileFromRawMaterialList();
                            });
                        }
                    }

                    rawMaterialAdapter = new RawMaterialAdapter(getApplicationContext(), rawMaterialList,  this);
                    recycleViewModesOneRV.setAdapter(rawMaterialAdapter);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modes_one);
        initView();
        initPresenter();
        initButtons();
        initRefreshListener();
    }

    private void initRefreshListener() {
        onRefreshListener = () -> {
            if(isHaveToClearList){
                rawMaterialAdapter.clearRawMaterialList();
                settingButton(EditButtonEnums.SAVE_BUTTON_DISABLED);
                isHaveToClearList = false;
            }

            rawMaterialList = LocalRawMaterialsStorage.getInstance().getRawMaterialList();
            rawMaterialAdapter = new RawMaterialAdapter(getApplicationContext(), rawMaterialList,  this);
            recycleViewModesOneRV.setAdapter(rawMaterialAdapter);

            swipeRefreshLayout.setRefreshing(false);
        };
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    private void initButtons() {
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
        }

        rawMaterialList = LocalRawMaterialsStorage.getInstance().getRawMaterialList();
        if(saveButton != null){

            if(rawMaterialList.size() > 0){
                settingButton(EditButtonEnums.SAVE_BUTTON_ENABLED);

                saveButton.setOnClickListener(view -> {
                    programPresenter.createFileFromRawMaterialList();
                });
            }
        }

        rawMaterialAdapter = new RawMaterialAdapter(getApplicationContext(), rawMaterialList,  this);
        recycleViewModesOneRV.setAdapter(rawMaterialAdapter);
    }

    private void initPresenter() {
        programPresenter = new ProgramPresenter(this, getApplicationContext());
        programPresenter.initTaskManager();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(isEnableBarcodeReaderMode){
            if(keyCode == KeyEvent.KEYCODE_BUTTON_R1 || keyCode == KeyEvent.KEYCODE_BUTTON_L1){
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

        isEnableBarcodeReaderMode = JSONFileHelper.getBoolean(getApplicationContext(), "values.json", "IsEnableBarcodeReaderMode");
        separatorFromJSON = JSONFileHelper.getString(getApplicationContext(), "values.json", "FileSeparatorCharacter");
    }
}