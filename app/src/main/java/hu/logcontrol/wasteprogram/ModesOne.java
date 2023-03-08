package hu.logcontrol.wasteprogram;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import org.w3c.dom.Document;

import java.util.ArrayList;
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

    private String fileName;

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

                Log.e("", String.valueOf(result.getResultCode()));
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent intent = result.getData();

                    if(intent == null) return;

                    Uri uri = intent.getData();
                    Log.e("uripath", uri.getPath());
                    programPresenter.createTextFileFromRawMaterialList(uri);
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

        rawMaterialListView = LocalRawMaterialsStorage.getInstance().getRawMaterialList();
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
                    programPresenter.openActivityByEnum(ActivityEnums.RawMaterialCreationActivity);
                });
            }
            if(backButton != null){
                backButton.setOnClickListener(view -> {
                    programPresenter.openActivityByEnum(ActivityEnums.MAIN_ACTIVITY);
                });
            }
        }

        rawMaterialAdapter = new RawMaterialAdapter(rawMaterialListView);
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

    private void settingSaveButton(EditButtonEnums editButtonEnum) {
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

    private void hideNavigationBar(){
        Helper.hideNavigationBar(this);
    }
}