package hu.logcontrol.wasteprogram;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.List;

import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.helpers.JSONFileReaderHelper;
import hu.logcontrol.wasteprogram.interfaces.ISettingsView;
import hu.logcontrol.wasteprogram.models.LocalEncryptedPreferences;
import hu.logcontrol.wasteprogram.presenters.ProgramPresenter;

public class SettingsActivity extends AppCompatActivity implements ISettingsView {

    private TextInputEditText settingsLocalSavePathTB;
    private TextInputEditText settingsGlobalSavePathTB;
    private ImageButton settingsSaveButton;
    private ImageButton settingsBackButton;
    private ImageButton folderPickerButton;

    private CheckBox localSavePathCheckbox;
    private CheckBox settingBarcodeNextCheckBox;

    private ConstraintLayout localSavePathCL;

    private ProgramPresenter programPresenter;
    private LocalEncryptedPreferences preferences;

    private String resultString;
    private boolean resultBoolean;

    @SuppressLint("SetTextI18n")
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent intent = result.getData();
                    if(intent == null) return;

                    Log.e("getPath", intent.getData().getPath());
                    Log.e("getEncodedPath", intent.getData().getEncodedPath());
                    List<String> a = intent.getData().getPathSegments();

                    for (int i = 0; i < a.size(); i++) {
                        Log.e("getPathSegments", a.get(i));
                    }

                    String path = File.separator + "sdcard" + File.separator + intent.getData().getPath().split(":")[1];
                    programPresenter.saveStringValueToJSONFile("LocalSavePath", path);
                    hideNavigationBar();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = LocalEncryptedPreferences.getInstance(
                "values",
                MasterKeys.AES256_GCM_SPEC,
                this,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        Log.e("", String.valueOf(preferences.getIntValueByKey("values3")));

        initView();
        programPresenter = new ProgramPresenter(this, getApplicationContext());
        programPresenter.initTaskManager();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(settingsGlobalSavePathTB != null && settingsLocalSavePathTB != null && settingBarcodeNextCheckBox != null && localSavePathCheckbox != null){

            boolean isExist = JSONFileReaderHelper.existJSONFile(getApplicationContext(), "values.json");
            if(isExist) {

                resultString = JSONFileReaderHelper.getStringFromJSONFile(getApplicationContext(), "values.json", "GlobalSavePath");
                if(resultString != null) settingsGlobalSavePathTB.setText(resultString);

                resultString = JSONFileReaderHelper.getStringFromJSONFile(getApplicationContext(), "values.json", "LocalSavePath");
                if(resultString != null) settingsLocalSavePathTB.setText(resultString);

                resultBoolean = JSONFileReaderHelper.getBooleanFromJSONFile(getApplicationContext(), "values.json", "IsEnableBarcodeReaderMode");
                settingBarcodeNextCheckBox.setChecked(resultBoolean);

                resultBoolean = JSONFileReaderHelper.getBooleanFromJSONFile(getApplicationContext(), "values.json", "IsEnableSaveLocalStorage");
                localSavePathCheckbox.setChecked(resultBoolean);
            }
        }

        if(settingsLocalSavePathTB != null){
        }

        if(localSavePathCheckbox != null){
            localSavePathCheckbox.setOnClickListener(v -> {
                if(localSavePathCheckbox.isChecked()){

                    localSavePathCL.setVisibility(View.VISIBLE);
                    localSavePathCheckbox.setChecked(true);

                    programPresenter.saveBooleanValueToJSONFile("IsEnableSaveLocalStorage", true);

                    if(folderPickerButton != null){
                        folderPickerButton.setOnClickListener(view -> {
                            programPresenter.openActivityByEnum(ActivityEnums.FOLDERPICKER_ACTIVITY);
                        });
                    }
                }
                if(!localSavePathCheckbox.isChecked()){

                    localSavePathCL.setVisibility(View.INVISIBLE);
                    localSavePathCheckbox.setChecked(false);

                    programPresenter.saveBooleanValueToJSONFile("IsEnableSaveLocalStorage", false);
                }
            });
        }

        if(settingBarcodeNextCheckBox != null){
            settingBarcodeNextCheckBox.setOnClickListener(v -> {
                if(settingBarcodeNextCheckBox.isChecked()){

                    settingBarcodeNextCheckBox.setChecked(true);
                    programPresenter.saveBooleanValueToJSONFile("IsEnableBarcodeReaderMode", true);

                }
                if(!settingBarcodeNextCheckBox.isChecked()){

                    localSavePathCheckbox.setChecked(false);
                    programPresenter.saveBooleanValueToJSONFile("IsEnableBarcodeReaderMode", false);
                }
            });
        }

        settingsBackButton.setOnClickListener(v -> {
            programPresenter.openActivityByEnum(ActivityEnums.MAIN_ACTIVITY);
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hideNavigationBar();
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

    private void initView(){
        settingsLocalSavePathTB = findViewById(R.id.settingsLocalSavePathTB);
        settingsGlobalSavePathTB = findViewById(R.id.settingsGlobalSavePathTB);
        settingsSaveButton = findViewById(R.id.settingsSaveButton);
        settingsBackButton = findViewById(R.id.settingsBackButton);
        folderPickerButton = findViewById(R.id.folderPickerButton);

        localSavePathCheckbox = findViewById(R.id.localSavePathCheckbox);
        settingBarcodeNextCheckBox = findViewById(R.id.settingBarcodeNextCheckBox);

        localSavePathCL = findViewById(R.id.localSavePathCL);

        hideNavigationBar();
    }

    @Override
    public void openActivityByIntent(Intent intent) {
        activityResultLauncher.launch(intent);
    }

    @Override
    public void settingSaveButton(EditButtonEnums editButtonEnum) {
        if(settingsSaveButton == null) return;

        switch (editButtonEnum){
            case SAVE_BUTTON_ENABLED:{
                settingsSaveButton.setEnabled(true);
                settingsSaveButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.save_button_background));

                break;
            }
            case SAVE_BUTTON_DISABLED:{
                settingsSaveButton.setEnabled(false);
                settingsSaveButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_circle));

                break;
            }
        }
    }

    @Override
    public void getMessageFromPresenter(String message) {
        if(message == null) return;
        new Handler(Looper.getMainLooper()).post(() -> { Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show(); });
    }

    private void hideNavigationBar(){
        Helper.hideNavigationBar(this);
    }
}