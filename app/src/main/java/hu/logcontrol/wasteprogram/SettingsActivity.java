package hu.logcontrol.wasteprogram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.helpers.ElementStateChangeHelper;
import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.interfaces.ISettingsView;
import hu.logcontrol.wasteprogram.presenters.ProgramPresenter;

public class SettingsActivity extends AppCompatActivity implements ISettingsView {

    private TextInputEditText settingSavePathTB;
    private MaterialButton editSettingsButton;
    private ImageButton settingsSaveButton;
    private ImageButton settingsBackButton;

    private String currentSavePath;
    private String newSavePath;

    private final String disableColor = "#B7C0C1";
    private final String enableColor = "#000000";

    private ProgramPresenter programPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
        programPresenter = new ProgramPresenter(this, getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // az elmentet útvonalat be kell tölteni a sharedpref fájlból

        if(settingSavePathTB != null){
            currentSavePath = Objects.requireNonNull(settingSavePathTB.getText()).toString();
        }

        if(editSettingsButton != null && settingSavePathTB != null){
            editSettingsButton.setOnClickListener(v -> {
                if(settingSavePathTB.isEnabled()){
                    ElementStateChangeHelper.disableSavePathEditor(settingSavePathTB, disableColor);
                }
                else{
                    ElementStateChangeHelper.enableSavePathEditor(settingSavePathTB, enableColor);
                    newSavePath = settingSavePathTB.getText().toString();
                }
            });
        }

        settingsBackButton.setOnClickListener(v -> {
            programPresenter.openActivityByEnum(ActivityEnums.MAIN_ACTIVITY);
        });

        if(settingSavePathTB != null){
            settingSavePathTB.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(settingsSaveButton != null){
                        settingSaveButton(EditButtonEnums.SAVE_BUTTON_ENABLED);

                        settingsSaveButton.setOnClickListener(v -> {

                            //TODO -> itt kell majd elmenteni az útvonalat
                            programPresenter.openActivityByEnum(ActivityEnums.MAIN_ACTIVITY);
                        });
                    }
                }

                @Override public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void initView(){
        settingSavePathTB = findViewById(R.id.settingSavePathTB);
        editSettingsButton = findViewById(R.id.editSettingsButton);
        settingsSaveButton = findViewById(R.id.settingsSaveButton);
        settingsBackButton = findViewById(R.id.settingsBackButton);

        if(settingSavePathTB != null) ElementStateChangeHelper.disableSavePathEditor(settingSavePathTB, disableColor);
        if(settingsSaveButton != null) settingSaveButton(EditButtonEnums.SAVE_BUTTON_DISABLED);

        hideNavigationBar();
    }

    @Override
    public void openActivityByIntent(Intent intent) {
        startActivity(intent);
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