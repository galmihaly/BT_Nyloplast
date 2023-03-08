package hu.logcontrol.wasteprogram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.interfaces.IMainView;
import hu.logcontrol.wasteprogram.presenters.ProgramPresenter;

public class MainActivity extends AppCompatActivity implements IMainView {

    private ProgramPresenter programPresenter;

    private Button mode1;
    private Button mode2;
    private Button mode3;

    private ImageButton exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        programPresenter = new ProgramPresenter(this, getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mode1 != null){
            mode1.setOnClickListener(view -> {
                programPresenter.openActivityByEnum(ActivityEnums.ModesActivty_One);
            });
        }
        if(mode2 != null){
            mode2.setOnClickListener(view -> {
                programPresenter.openActivityByEnum(ActivityEnums.ModesActivity_Two);
            });
        }
        if(mode3 != null){
            mode3.setOnClickListener(view -> {
                programPresenter.openActivityByEnum(ActivityEnums.ModesActivity_Three);
            });
        }
        if(exitButton != null){
            exitButton.setOnClickListener(view -> {
                programPresenter.exitApplicationPresenter();
            });
        }
    }

    private void initView() {

        mode1 = findViewById(R.id.modeOne);
        mode2 = findViewById(R.id.modeTwo);
        mode3 = findViewById(R.id.modeThree);

        exitButton = findViewById(R.id.exitButton);

        Helper.hideNavigationBar(this);
    }

    @Override
    public void openActivityByIntent(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void exitApplication() {
        finishAffinity();
        System.exit(0);
    }
}