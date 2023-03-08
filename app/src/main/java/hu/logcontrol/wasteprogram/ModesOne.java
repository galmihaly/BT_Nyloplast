package hu.logcontrol.wasteprogram;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import hu.logcontrol.wasteprogram.adapters.RawMaterialAdapter;
import hu.logcontrol.wasteprogram.enums.ActivityEnums;
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

    private RecyclerView recycleViewModesOneRV;

    private ConstraintLayout mainModesOneCL;

    private List<RawMaterial> rawMaterialListView;
    private RawMaterialAdapter rawMaterialAdapter;

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == 1){
                    Intent intent = result.getData();

                    String rawMatTypeTextBox = intent.getStringExtra("rawMatTypeTextBox");
                    String rawMatCountTextBox = intent.getStringExtra("rawMatCountTextBox");

                    RawMaterial rawMaterial = new RawMaterial(ApplicationLogger.getUTCDateTimeString(),rawMatTypeTextBox, rawMatCountTextBox);

                    programPresenter.addRawMaterialToAdapterList(rawMaterial);
                    hideNavigationBar();
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

        recycleViewModesOneRV = findViewById(R.id.recycleViewModesOneRV);
        recycleViewModesOneRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mainModesOneCL = findViewById(R.id.mainModesOneCL);

        hideNavigationBar();
    }

    private void hideNavigationBar(){
        Helper.hideNavigationBar(this);
    }
}