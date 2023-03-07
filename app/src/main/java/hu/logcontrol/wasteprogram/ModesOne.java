package hu.logcontrol.wasteprogram;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import hu.logcontrol.wasteprogram.adapters.RawMaterialAdapter;
import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.interfaces.IModesOneView;
import hu.logcontrol.wasteprogram.models.RawMaterial;
import hu.logcontrol.wasteprogram.presenters.ProgramPresenter;

public class ModesOne extends AppCompatActivity implements IModesOneView {

    private ProgramPresenter programPresenter;
    private ImageButton addButton;
    private ImageButton backButton;

    private RecyclerView recycleViewModesOneRV;

    private RawMaterialAdapter rawMaterialAdapter;
    private List<RawMaterial> rawMaterialListView = new ArrayList<>();

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == 1){
                    Intent intent = result.getData();

                    String rawMatTypeTextBox = intent.getStringExtra("rawMatTypeTextBox");
                    String rawMatCountTextBox = intent.getStringExtra("rawMatCountTextBox");

                    RawMaterial rawMaterial = new RawMaterial(Helper.getReadableTime(),rawMatTypeTextBox, rawMatCountTextBox);

                    programPresenter.addRawMaterialToAdapterList(rawMaterial, rawMaterialListView);
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(programPresenter != null){
            if(addButton != null){
                addButton.setOnClickListener(view -> {
                    Log.e("", String.valueOf(rawMaterialListView.size()));
                    programPresenter.openActivityByEnum(ActivityEnums.RawMaterialCreationActivity);
                });
            }
            if(backButton != null){
                backButton.setOnClickListener(view -> {
                    programPresenter.openActivityByEnum(ActivityEnums.MAIN_ACTIVITY);
                });
            }
        }
    }

    @Override
    public void openActivityByIntent(Intent intent) {
        activityResultLauncher.launch(intent);
    }

    @Override
    public void getAdapterFromPresenter(List<RawMaterial> rawMaterialList) {
        if(recycleViewModesOneRV == null) return;

        RawMaterialAdapter rawMaterialAdapter = new RawMaterialAdapter(getApplicationContext(), rawMaterialList);
        recycleViewModesOneRV.setAdapter(rawMaterialAdapter);
        recycleViewModesOneRV.setLayoutManager(new LinearLayoutManager(this));

    }

    private void initView() {

        addButton = findViewById(R.id.addButton);
        backButton = findViewById(R.id.backButton);

        recycleViewModesOneRV = findViewById(R.id.recycleViewModesOneRV);
    }
}