package hu.logcontrol.wasteprogram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.helpers.JSONFileHelper;
import hu.logcontrol.wasteprogram.interfaces.IMainView;
import hu.logcontrol.wasteprogram.presenters.ProgramPresenter;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

public class MainActivity extends AppCompatActivity implements IMainView {

    private ProgramPresenter programPresenter;

    private Button mode1;
    private Button mode2;
    private Button mode3;

    private ImageButton exitButton;
    private ImageButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        startWrite();

        programPresenter = new ProgramPresenter(this, getApplicationContext());

        if(!JSONFileHelper.isExist(getApplicationContext(), "values.json")) {
            programPresenter.initBaseJSONFile("values.json");
        }

//        Executor executor = Executors.newSingleThreadExecutor();
//        executor.execute(() -> {
//            String z = "";
//            try {
//
//                String url = "smb://172.16.1.5/public/Misi/text2.txt";
//                Log.e("url", url);
//
//                NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
//                        null, "Galmihaly", "Kutyaszar123!");
//                SmbFile sfile = new SmbFile(url, auth);
//
//                if (!sfile.exists()) {
//                    sfile.createNewFile();
//                    Log.e("message", "Created the file for you!!!!");
//                    z = "Created the file for you!!!!";
//                } else {
//                    Log.e("message", "Already exists at the specified location!!!!");
//                    z = "Already exists at the specified location!!!!";
//                }
//            } catch (Exception ex) {
//                // TODO: handle exception
//                z = ex.getMessage().toString();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mode1 != null){
            mode1.setOnClickListener(view -> {
                programPresenter.openActivityByEnum(ActivityEnums.MODES_ACTIVITY_ONE);
            });
        }
        if(mode2 != null){
            mode2.setOnClickListener(view -> {
                programPresenter.openActivityByEnum(ActivityEnums.MODES_ACTIVITY_TWO);
            });
        }
        if(mode3 != null){
            mode3.setOnClickListener(view -> {
                programPresenter.openActivityByEnum(ActivityEnums.MODES_ACTIVITY_THREE);
            });
        }
        if(exitButton != null){
            exitButton.setOnClickListener(view -> {
                programPresenter.exitApplicationPresenter();
            });
        }
        if(settingsButton != null){
            settingsButton.setOnClickListener(view -> {
                programPresenter.openActivityByEnum(ActivityEnums.SETTINGS_ACTIVITY);
            });
        }
    }

    public void startWrite() {
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

    private void initView() {

        mode1 = findViewById(R.id.modeOne);
        mode2 = findViewById(R.id.modeTwo);
        mode3 = findViewById(R.id.modeThree);

        exitButton = findViewById(R.id.exitButton);
        settingsButton = findViewById(R.id.settingsButton);
    }

    @Override
    public void openActivityByIntent(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void exitApplication() {
        onDestroy();
    }

    @Override
    protected void onDestroy() {

        finishAndRemoveTask();
        System.exit(0);
        super.onDestroy();
    }
}