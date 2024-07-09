package hu.logcontrol.wasteprogram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.helpers.JSONFileHelper;
import hu.logcontrol.wasteprogram.interfaces.IMainView;
import hu.logcontrol.wasteprogram.models.LocalEncryptedPreferences;
import hu.logcontrol.wasteprogram.presenters.ProgramPresenter;
import java.time.Duration;

public class MainActivity extends AppCompatActivity implements IMainView {

    private ProgramPresenter programPresenter;

    private Button mode1;
    private Button mode2;
    private Button mode3;


    private ImageButton settingsButton;

    private LocalEncryptedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        startWrite();
        initSharedPreferenceFile();
        initButtons();
        programPresenter = new ProgramPresenter(this, getApplicationContext());
    }

    private void initButtons() {
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

        settingsButton = findViewById(R.id.settingsButton);
    }

    private void initSharedPreferenceFile() {
        preferences = LocalEncryptedPreferences.getInstance(
                "values",
                MasterKeys.AES256_GCM_SPEC,
                getApplicationContext(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        preferences.putString("GlobalSavePath", "");
        preferences.putString("LocalSavePath", "");
        preferences.putString("FileSeparatorCharacter", "pontosvessző");
        preferences.putString("Username", "");
        preferences.putString("Password", "");
        preferences.putString("HostName", "");
        preferences.putString("PortNumber", "21");

        preferences.putBoolean("IsEnableBarcodeReaderMode", false);
        preferences.putBoolean("IsEnableSaveLocalStorage", false);
        preferences.putBoolean("IsEnableKeyBoardOnTextBoxes", false);
    }

    @Override
    public void openActivityByIntent(Intent intent) {
        startActivity(intent);
    }
}