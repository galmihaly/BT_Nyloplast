package hu.logcontrol.wasteprogram;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import hu.logcontrol.wasteprogram.adapters.RawMaterialTypeMassAdapter;
import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.interfaces.IModesTwoView;
import hu.logcontrol.wasteprogram.logger.ApplicationLogger;
import hu.logcontrol.wasteprogram.models.LocalRawMaterialTypeMassesStorage;
import hu.logcontrol.wasteprogram.models.RawMaterialTypeMass;
import hu.logcontrol.wasteprogram.presenters.ProgramPresenter;

public class ModesTwo extends AppCompatActivity implements IModesTwoView {

    private ConstraintLayout mainModesTwoCL;

    private ImageButton addButton_2;
    private  ImageButton saveButton_2;
    private ImageButton backButton_2;
    private RecyclerView recycleViewModesTwoRV;

    private ProgramPresenter programPresenter;
    private List<RawMaterialTypeMass> rawMaterialTypeMassList;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == 1){
                    Intent intent = result.getData();

                    if(intent == null) return;

                    String typeMassCountTextBox = intent.getStringExtra("typeMassCountTextBox");
                    String typeMassTypeTextBox = intent.getStringExtra("typeMassTypeTextBox");
                    String storageBoxIdentifierTextBox = intent.getStringExtra("storageBoxIdentifierTextBox");
                    String massDataTextBox = intent.getStringExtra("massDataTextBox");

                    RawMaterialTypeMass rawMaterialTypeMass = new RawMaterialTypeMass(ApplicationLogger.getUTCDateTimeString(),typeMassCountTextBox, typeMassTypeTextBox, storageBoxIdentifierTextBox, massDataTextBox);

                    programPresenter.addRawMaterialTypeMassToAdapterList(rawMaterialTypeMass);
                    hideNavigationBar();
                }

                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent intent = result.getData();
                    if(intent == null) return;

                    programPresenter.createFileFromRawMaterialTypeMassList(intent.getData());
                    hideNavigationBar();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modes_two);
        initView();

        programPresenter = new ProgramPresenter(this, getApplicationContext());
        programPresenter.initTaskManager();

        rawMaterialTypeMassList = LocalRawMaterialTypeMassesStorage.getInstance().getRawMaterialTypeMassList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        hideNavigationBar();

        if(mainModesTwoCL != null){
            mainModesTwoCL.setOnClickListener(view -> {
                hideNavigationBar();
            });
        }

        if(saveButton_2 != null && rawMaterialTypeMassList != null){
            if(rawMaterialTypeMassList.size() > 0){
                settingButton(EditButtonEnums.SAVE_BUTTON_ENABLED);

                saveButton_2.setOnClickListener(view -> {
                    programPresenter.openActivityByEnum(ActivityEnums.FOLDERPICKER_ACTIVITY);
                });
            }
        }

        if(programPresenter != null){
            if(addButton_2 != null){
                addButton_2.setOnClickListener(view -> {
                    programPresenter.openActivityByEnum(ActivityEnums.RAW_MATERIAL_TYPEMASS_CREATION_ACTIVITY);
                });
            }
            if(backButton_2 != null){
                backButton_2.setOnClickListener(view -> {
                    programPresenter.openActivityByEnum(ActivityEnums.MAIN_ACTIVITY);
                });
            }
        }

        RawMaterialTypeMassAdapter rawMaterialTypeMassAdapter = new RawMaterialTypeMassAdapter(getApplicationContext(), rawMaterialTypeMassList, this);
        recycleViewModesTwoRV.setAdapter(rawMaterialTypeMassAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideNavigationBar();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideNavigationBar();
    }

    @Override
    public void openActivityByIntent(Intent intent) {
        activityResultLauncher.launch(intent);
    }

    @Override
    public void settingButton(EditButtonEnums editButtonEnum) {
        if(saveButton_2 == null) return;

        switch (editButtonEnum){
            case SAVE_BUTTON_ENABLED:{

                saveButton_2.setEnabled(true);
                saveButton_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.save_button_background));

                break;
            }
            case SAVE_BUTTON_DISABLED:{

                saveButton_2.setEnabled(false);
                saveButton_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_circle));

                break;
            }
            case ADD_BUTTON_ENABLED:{

                addButton_2.setEnabled(true);
                addButton_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_button_background));

                break;
            }
            case ADD_BUTTON_DISABLED:{

                addButton_2.setEnabled(false);
                addButton_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_circle));

                break;
            }
        }
    }

    @Override
    public void getMessageFromPresenter(String message) {
        if(message == null) return;
        new Handler(Looper.getMainLooper()).post(() -> { Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show(); });
    }

    private void initView(){
        addButton_2 = findViewById(R.id.addButton_2);
        backButton_2 = findViewById(R.id.backButton_2);

        saveButton_2 = findViewById(R.id.saveButton_2);
        settingButton(EditButtonEnums.SAVE_BUTTON_DISABLED);

        recycleViewModesTwoRV = findViewById(R.id.recycleViewModesTwoRV);
        recycleViewModesTwoRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mainModesTwoCL = findViewById(R.id.mainModesTwoCL);

        hideNavigationBar();
    }

    private void hideNavigationBar(){
        Helper.hideNavigationBar(this);
    }
}