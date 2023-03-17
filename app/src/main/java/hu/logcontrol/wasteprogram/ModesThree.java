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

import hu.logcontrol.wasteprogram.adapters.RecycledMaterialAdapter;
import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.interfaces.IModesThreeView;
import hu.logcontrol.wasteprogram.logger.ApplicationLogger;
import hu.logcontrol.wasteprogram.models.LocalRecycLedMaterialsStorage;
import hu.logcontrol.wasteprogram.models.RecycledMaterial;
import hu.logcontrol.wasteprogram.presenters.ProgramPresenter;

public class ModesThree extends AppCompatActivity implements IModesThreeView {

    private ConstraintLayout mainModesThreeCL;

    private ImageButton addButton_3;
    private  ImageButton saveButton_3;
    private ImageButton backButton_3;
    private RecyclerView recycleViewModesThreeRV;

    private ProgramPresenter programPresenter;
    private List<RecycledMaterial> recycledMaterialList;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == 1){
                    Intent intent = result.getData();

                    if(intent == null) return;

                    String typeRecMatTextBox = intent.getStringExtra("typeRecMatTextBox");
                    String storageBoxIdentifierTextBox2 = intent.getStringExtra("storageBoxIdentifierTextBox2");
                    String massDataTextBox2 = intent.getStringExtra("massDataTextBox2");

                    RecycledMaterial recycledMaterial = new RecycledMaterial(ApplicationLogger.getUTCDateTimeString(),typeRecMatTextBox, storageBoxIdentifierTextBox2, massDataTextBox2);

                    programPresenter.addRecycledMaterialToAdapterList(recycledMaterial);
                    hideNavigationBar();
                }

                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent intent = result.getData();
                    if(intent == null) return;

                    programPresenter.createFileFromRecycledMaterialTypeMassList(intent.getData());
                    hideNavigationBar();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modes_three);
        initView();

        programPresenter = new ProgramPresenter(this, getApplicationContext());
        programPresenter.initTaskManager();

        recycledMaterialList = LocalRecycLedMaterialsStorage.getInstance().getRecycledMaterialList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        hideNavigationBar();

        if(mainModesThreeCL != null){
            mainModesThreeCL.setOnClickListener(view -> {
                hideNavigationBar();
            });
        }

        if(saveButton_3 != null && recycledMaterialList != null){
            if(recycledMaterialList.size() > 0){
                settingButton(EditButtonEnums.SAVE_BUTTON_ENABLED);

                saveButton_3.setOnClickListener(view -> {
                    programPresenter.openActivityByEnum(ActivityEnums.FOLDERPICKER_ACTIVITY);
                });
            }
        }

        if(programPresenter != null){
            if(addButton_3 != null){
                addButton_3.setOnClickListener(view -> {
                    programPresenter.openActivityByEnum(ActivityEnums.RECYCLED_MATERIAL_TYPEMASS_CREATION_ACTIVITY);
                });
            }
            if(backButton_3 != null){
                backButton_3.setOnClickListener(view -> {
                    programPresenter.openActivityByEnum(ActivityEnums.MAIN_ACTIVITY);
                });
            }
        }

        RecycledMaterialAdapter recycledMaterialAdapter = new RecycledMaterialAdapter(getApplicationContext(), recycledMaterialList, this);
        recycleViewModesThreeRV.setAdapter(recycledMaterialAdapter);
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
        if(saveButton_3 == null) return;

        switch (editButtonEnum){
            case SAVE_BUTTON_ENABLED:{

                saveButton_3.setEnabled(true);
                saveButton_3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.save_button_background));

                break;
            }
            case SAVE_BUTTON_DISABLED:{

                saveButton_3.setEnabled(false);
                saveButton_3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_circle));

                break;
            }
            case ADD_BUTTON_ENABLED:{

                addButton_3.setEnabled(true);
                addButton_3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_button_background));

                break;
            }
            case ADD_BUTTON_DISABLED:{

                addButton_3.setEnabled(false);
                addButton_3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_circle));

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
        addButton_3 = findViewById(R.id.addButton_3);
        backButton_3 = findViewById(R.id.backButton_3);

        saveButton_3 = findViewById(R.id.saveButton_3);
        settingButton(EditButtonEnums.SAVE_BUTTON_DISABLED);

        recycleViewModesThreeRV = findViewById(R.id.recycleViewModesThreeRV);
        recycleViewModesThreeRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mainModesThreeCL = findViewById(R.id.mainModesThreeCL);

        hideNavigationBar();
    }

    private void hideNavigationBar(){
        Helper.hideNavigationBar(this);
    }
}