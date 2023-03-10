package hu.logcontrol.wasteprogram;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hu.logcontrol.wasteprogram.adapters.RawMaterialAdapter;
import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.helpers.Helper;
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

    private RecyclerView recycleViewModesOneRV;

    private ConstraintLayout mainModesOneCL;

    private List<RawMaterial> rawMaterialListView;
    private RawMaterialAdapter rawMaterialAdapter;

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == 1){
                    Intent intent = result.getData();

                    if(intent == null) return;

                    String rawMatTypeTextBox = intent.getStringExtra("rawMatTypeTextBox");
                    String rawMatCountTextBox = intent.getStringExtra("rawMatCountTextBox");

                    RawMaterial rawMaterial = new RawMaterial(ApplicationLogger.getUTCDateTimeString(),rawMatTypeTextBox, rawMatCountTextBox);

                    programPresenter.addRawMaterialToAdapterList(rawMaterial);
                    hideNavigationBar();
                }

                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent intent = result.getData();
                    if(intent == null) return;

                    programPresenter.createTextFileFromRawMaterialList(intent.getData());
                    hideNavigationBar();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modes_one);
        initView();
        startCamera();

        programPresenter = new ProgramPresenter(this, getApplicationContext());
        programPresenter.initTaskManager();

        rawMaterialListView = LocalRawMaterialsStorage.getInstance().getRawMaterialList();
    }

    public void startCamera() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.e("", "Jelenleg rendelkezik írási joggal!");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 786);
        }
    }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
            for (int r : grantResults) {
                if (r == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Engedély elutasítva!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (requestCode == 786) {
                Toast.makeText(this, "Engedély elfogadva!", Toast.LENGTH_SHORT).show();
            }

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    @Override
    protected void onResume() {
        super.onResume();

        if(mainModesOneCL != null){
            mainModesOneCL.setOnClickListener(view -> {
                hideNavigationBar();
            });
        }

        if(saveButton != null && rawMaterialListView != null){
            if(rawMaterialListView.size() > 0){
                settingSaveButton(EditButtonEnums.SAVE_BUTTON_ENABLED);

                saveButton.setOnClickListener(view -> {
                    programPresenter.openActivityByEnum(ActivityEnums.FOLDERPICKER_ACTIVITY);
                });
            }
        }

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

        rawMaterialAdapter = new RawMaterialAdapter(getApplicationContext(), rawMaterialListView, this);
        recycleViewModesOneRV.setAdapter(rawMaterialAdapter);
    }

    @Override
    public void openActivityByIntent(Intent intent) {
        activityResultLauncher.launch(intent);
    }

    private void initView() {

        addButton = findViewById(R.id.addButton);
        backButton = findViewById(R.id.backButton);

        saveButton = findViewById(R.id.saveButton);
        settingSaveButton(EditButtonEnums.SAVE_BUTTON_DISABLED);

        recycleViewModesOneRV = findViewById(R.id.recycleViewModesOneRV);
        recycleViewModesOneRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mainModesOneCL = findViewById(R.id.mainModesOneCL);

        hideNavigationBar();
    }

    @Override
    public void settingSaveButton(EditButtonEnums editButtonEnum) {
        if(saveButton == null) return;

        switch (editButtonEnum){
            case SAVE_BUTTON_ENABLED:{

                saveButton.setEnabled(true);
                saveButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.save_button_background));

                break;
            }
            case SAVE_BUTTON_DISABLED:{

                saveButton.setEnabled(false);
                saveButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background));

                break;
            }
        }
    }

    @Override
    public void getMessageFromPresenter(String message) {
        if(message == null) return;
        new Handler(Looper.getMainLooper()).post(() -> { Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show(); });
    }

    private void hideNavigationBar(){
        Helper.hideNavigationBar(this);
    }
}