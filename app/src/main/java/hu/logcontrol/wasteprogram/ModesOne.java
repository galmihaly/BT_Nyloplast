package hu.logcontrol.wasteprogram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.interfaces.IModesOneView;
import hu.logcontrol.wasteprogram.presenters.ProgramPresenter;

public class ModesOne extends AppCompatActivity implements IModesOneView {

    private ProgramPresenter programPresenter;
    private ImageButton addButton;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modes_one);
        initView();

        programPresenter = new ProgramPresenter(this, getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    @Override
    public void openActivityByIntent(Intent intent) {
        startActivity(intent);
    }

    private void initView() {

        addButton = findViewById(R.id.addButton);
        backButton = findViewById(R.id.backButton);
    }
}