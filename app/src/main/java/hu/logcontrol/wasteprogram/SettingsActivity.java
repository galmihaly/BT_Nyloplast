package hu.logcontrol.wasteprogram;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import androidx.viewpager2.widget.ViewPager2;

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

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import hu.logcontrol.wasteprogram.adapters.SettingViewPagerAdapter;
import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.fragments.GeneralSettingsFragment;
import hu.logcontrol.wasteprogram.fragments.UploadFileSettingsFragment;
import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.helpers.JSONFileHelper;
import hu.logcontrol.wasteprogram.interfaces.GeneralListener;
import hu.logcontrol.wasteprogram.interfaces.ISettingsView;
import hu.logcontrol.wasteprogram.interfaces.UploadFileListener;
import hu.logcontrol.wasteprogram.models.LocalEncryptedPreferences;
import hu.logcontrol.wasteprogram.presenters.ProgramPresenter;

public class SettingsActivity extends AppCompatActivity implements ISettingsView, GeneralListener, UploadFileListener {

    private TextInputEditText settingsLocalSavePathTB;
    private TextInputEditText settingsGlobalSavePathTB;
    private ImageButton settingsSaveButton;
    private ImageButton settingsBackButton;
    private ImageButton folderPickerButton;

    private CheckBox localSavePathCheckbox;
    private CheckBox settingBarcodeNextCheckBox;
    private CheckBox settingKeyboardCheckBox;

    private ConstraintLayout localSavePathCL;

    private ProgramPresenter programPresenter;
    private LocalEncryptedPreferences preferences;

    private String resultString;
    private boolean resultBoolean;

    private TabLayout settingTabLayout;
    private ViewPager2 settingViewPager;

    private String path = null;


    @SuppressLint("SetTextI18n")
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent intent = result.getData();
                    if(intent == null) return;

                    path = Helper.formatPathString(intent.getData().getPathSegments());

                    if(settingsLocalSavePathTB != null){
                        settingsLocalSavePathTB.setText(path);
                    }
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

        initView();
        programPresenter = new ProgramPresenter(this, getApplicationContext());
        programPresenter.initTaskManager();

        if(settingsGlobalSavePathTB != null && settingsLocalSavePathTB != null && settingBarcodeNextCheckBox != null && localSavePathCheckbox != null && settingKeyboardCheckBox != null){

            boolean isExist = JSONFileHelper.isExist(getApplicationContext(), "values.json");
            if(isExist) {

                resultString = JSONFileHelper.getString(getApplicationContext(), "values.json", "GlobalSavePath");
                if(resultString != null) settingsGlobalSavePathTB.setText(resultString);

                resultString = JSONFileHelper.getString(getApplicationContext(), "values.json", "LocalSavePath");
                if(resultString != null) settingsLocalSavePathTB.setText(resultString);

                resultBoolean = JSONFileHelper.getBoolean(getApplicationContext(), "values.json", "IsEnableBarcodeReaderMode");
                settingBarcodeNextCheckBox.setChecked(resultBoolean);

                resultBoolean = JSONFileHelper.getBoolean(getApplicationContext(), "values.json", "IsEnableSaveLocalStorage");
                localSavePathCheckbox.setChecked(resultBoolean);

                resultBoolean = JSONFileHelper.getBoolean(getApplicationContext(), "values.json", "IsEnableKeyBoardOnTextBoxes");
                settingKeyboardCheckBox.setChecked(resultBoolean);

                if(localSavePathCheckbox.isChecked()){ localSavePathCL.setVisibility(View.VISIBLE); }
                else{ localSavePathCL.setVisibility(View.INVISIBLE); }
            }
        }

        SettingViewPagerAdapter settingViewPagerAdapter = new SettingViewPagerAdapter(this);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new UploadFileSettingsFragment());
        fragmentList.add(new GeneralSettingsFragment());
        settingViewPagerAdapter.setFragmentList(fragmentList);

        settingViewPager.setAdapter(settingViewPagerAdapter);

        settingTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                settingViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        settingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                settingTabLayout.getTabAt(position).select();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(folderPickerButton != null){
            if(localSavePathCheckbox.isChecked()){
                folderPickerButton.setOnClickListener(view -> {
                    programPresenter.openActivityByEnum(ActivityEnums.FOLDERPICKER_ACTIVITY);
                });
            }
        }

        if(localSavePathCheckbox != null){
            localSavePathCheckbox.setOnClickListener(v -> {
                if(localSavePathCheckbox.isChecked()){

                    localSavePathCL.setVisibility(View.VISIBLE);
                    folderPickerButton.setEnabled(true);

                    if(folderPickerButton != null){
                        folderPickerButton.setOnClickListener(view -> {
                            programPresenter.openActivityByEnum(ActivityEnums.FOLDERPICKER_ACTIVITY);
                        });
                    }
                }
                if(!localSavePathCheckbox.isChecked()){

                    localSavePathCL.setVisibility(View.INVISIBLE);
                    folderPickerButton.setEnabled(false);
                }
            });
        }

        if(settingKeyboardCheckBox != null){
            settingKeyboardCheckBox.setOnClickListener(v -> {
                if(settingKeyboardCheckBox.isChecked()){

                    settingKeyboardCheckBox.setChecked(true);
                }
                if(!settingKeyboardCheckBox.isChecked()){

                    settingKeyboardCheckBox.setChecked(false);
                }
            });
        }

        if(settingBarcodeNextCheckBox != null){
            settingBarcodeNextCheckBox.setOnClickListener(v -> {
                if(settingBarcodeNextCheckBox.isChecked()){

                    settingBarcodeNextCheckBox.setChecked(true);
                }
                if(!settingBarcodeNextCheckBox.isChecked()){

                    settingBarcodeNextCheckBox.setChecked(false);
                }
            });
        }

        if(settingsSaveButton != null || settingBarcodeNextCheckBox != null || localSavePathCheckbox != null){
            settingsSaveButton.setOnClickListener(v -> {
                if(localSavePathCheckbox.isChecked()){
                    programPresenter.saveBooleanValueToJSONFile("IsEnableSaveLocalStorage", true);
                }
                if(!localSavePathCheckbox.isChecked()){
                    programPresenter.saveBooleanValueToJSONFile("IsEnableSaveLocalStorage", false);
                }
                if(settingBarcodeNextCheckBox.isChecked()){
                    programPresenter.saveBooleanValueToJSONFile("IsEnableBarcodeReaderMode", true);

                }
                if(!settingBarcodeNextCheckBox.isChecked()){
                    programPresenter.saveBooleanValueToJSONFile("IsEnableBarcodeReaderMode", false);
                }
                if(settingKeyboardCheckBox.isChecked()){
                    programPresenter.saveBooleanValueToJSONFile("IsEnableKeyBoardOnTextBoxes", true);
                }
                if(!settingKeyboardCheckBox.isChecked()){
                    programPresenter.saveBooleanValueToJSONFile("IsEnableKeyBoardOnTextBoxes", false);
                }
                if(path != null){
                    if(!path.equals("")){
                        programPresenter.saveStringValueToJSONFile("LocalSavePath", path);
                    }
                }

                programPresenter.openActivityByEnum(ActivityEnums.MAIN_ACTIVITY);
            });
        }

        if(settingsBackButton != null){
            settingsBackButton.setOnClickListener(v -> {
                programPresenter.openActivityByEnum(ActivityEnums.MAIN_ACTIVITY);
            });
        }
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
        settingsSaveButton = findViewById(R.id.settingsSaveButton);
        settingsBackButton = findViewById(R.id.settingsBackButton);

        settingTabLayout = findViewById(R.id.settingTabLayout);
        settingViewPager = findViewById(R.id.settingViewPager);

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
        new Handler(Looper.getMainLooper()).post(() -> { Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show(); });
    }

    private void hideNavigationBar(){
        Helper.hideNavigationBar(this);
    }

    @Override
    public void sendBarcodeNextCheckBoxState(boolean state) {
        Log.e("sendBarcodeNextCheckBoxState", String.valueOf(state));
    }

    @Override
    public void sendKeyboardCheckBox(boolean state) {
        Log.e("sendBarcodeKeyboardCheckBox", String.valueOf(state));
    }

    @Override
    public void sendGlobalSavePath(String path) {
        Log.e("sendGlobalSavePath", path);
    }

    @Override
    public void sendLocalSavePath(String path) {
        Log.e("sendLocalSavePath", path);
    }

    @Override
    public void sendLocalSaveCheckbox(boolean state) {
        Log.e("sendLocalSaveCheckbox", String.valueOf(state));
    }

    @Override
    public void sendLocalSaveConstraintState(boolean state) {
        Log.e("sendLocalSaveConstraintState", String.valueOf(state));
    }
}