package hu.logcontrol.wasteprogram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import hu.logcontrol.wasteprogram.adapters.SettingViewPagerAdapter;
import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.fragments.GeneralSettingsFragment;
import hu.logcontrol.wasteprogram.fragments.UploadFileSettingsFragment;
import hu.logcontrol.wasteprogram.interfaces.GeneralListener;
import hu.logcontrol.wasteprogram.interfaces.IGeneralFragmentListener;
import hu.logcontrol.wasteprogram.interfaces.ISettingsView;
import hu.logcontrol.wasteprogram.interfaces.IUploadFileFragmentListener;
import hu.logcontrol.wasteprogram.interfaces.UploadFileListener;
import hu.logcontrol.wasteprogram.models.LocalEncryptedPreferences;
import hu.logcontrol.wasteprogram.presenters.ProgramPresenter;

public class SettingsActivity extends AppCompatActivity implements ISettingsView, GeneralListener, UploadFileListener {

    private ImageButton settingsSaveButton;
    private ImageButton settingsBackButton;

    private MaterialButton backDialogButton;
    private MaterialButton enterDialogButton;

    private ProgramPresenter programPresenter;

    private String resultGlobalPath;
    private String resultLocalPath;
    private String resultFileSeparatorCharachter;
    private String resultUsername;
    private String resultPassword;
    private String resultHostName;
    private String resultPortNumber;

    private boolean resultLocalCheckbox;
    private boolean isReadyLocalCheckbox = true;

    private boolean resultBarcodeCheckbox;
    private boolean isReadyBarcodeCheckbox = true;

    private boolean resultKeyBoardCheckbox;
    private boolean isReadyKeyBoardCheckbox = true;

    private String originalGlobalPath = null;
    private String originalLocalPath = null;
    private String originalFileSeparatorCharachter = null;
    private String originalUsername = null;
    private String originalPassword = null;
    private String originalHostName = null;
    private String originalPortNumber = null;

    private boolean originalLocalCheckbox;
    private boolean originalBarcodeCheckbox;
    private boolean originalKeyBoardCheckbox;

    private TabLayout settingTabLayout;
    private ViewPager2 settingViewPager;
    private SettingViewPagerAdapter settingViewPagerAdapter;
    private List<Fragment> fragmentList;

    private IUploadFileFragmentListener iUploadFileFragmentListener;
    private IGeneralFragmentListener iGeneralFragmentListener;

    private LocalEncryptedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
        initLocalPreferences();
        initPresenter();
        initStartingValuesOfView();
        initViewPager();
        initButtonsListeners();
    }

    private void initButtonsListeners() {

        if(settingsSaveButton != null){
            settingsSaveButton.setOnClickListener(v -> {

                if(iUploadFileFragmentListener != null){
                    if(isReadyLocalCheckbox){
                        resultLocalCheckbox = iUploadFileFragmentListener.getLocalSaveCheckBoxState();
                    }

                    resultGlobalPath = iUploadFileFragmentListener.getGlobalPath();
                    resultUsername = iUploadFileFragmentListener.getUsername();
                    resultPassword = iUploadFileFragmentListener.getPassword();
                    resultHostName = iUploadFileFragmentListener.getHostName();
                    resultPortNumber = iUploadFileFragmentListener.getPortNumber();

                    if((originalLocalCheckbox || resultLocalCheckbox) && !(originalLocalCheckbox && resultLocalCheckbox)){

                        originalLocalCheckbox = resultLocalCheckbox;
                        programPresenter.saveBooleanValueToSharedPreferencesFile("IsEnableSaveLocalStorage", originalLocalCheckbox);
                    }

                    if(resultLocalPath != null){
                        if(!originalLocalPath.equals(resultLocalPath)){

                            originalLocalPath = resultLocalPath;
                            programPresenter.saveStringValueToSharedPreferencesFile("LocalSavePath", resultLocalPath);
                        }
                    }

                    if(resultGlobalPath != null){
                        if(!originalGlobalPath.equals(resultGlobalPath)){

                            originalGlobalPath = resultGlobalPath;
                            programPresenter.saveStringValueToSharedPreferencesFile("GlobalSavePath", originalGlobalPath);
                        }
                    }

                    if(resultUsername != null){
                        if(!originalUsername.equals(resultUsername)){

                            originalUsername = resultUsername;
                            programPresenter.saveStringValueToSharedPreferencesFile("Username", originalUsername);
                        }
                    }

                    if(resultPassword != null){
                        if(!originalPassword.equals(resultPassword)){

                            originalPassword = resultPassword;
                            programPresenter.saveStringValueToSharedPreferencesFile("Password", originalPassword);
                        }
                    }

                    if(resultHostName != null){
                        if(!originalHostName.equals(resultHostName)){

                            originalHostName = resultHostName;
                            Log.e("originalHostName_", originalHostName);
                            programPresenter.saveStringValueToSharedPreferencesFile("HostName", originalHostName);
                        }
                    }

                    if(resultPortNumber != null){
                        if(!originalPortNumber.equals(resultPortNumber)){

                            originalPortNumber = resultPortNumber;
                            Log.e("originalPortNumber_", originalPortNumber);
                            programPresenter.saveStringValueToSharedPreferencesFile("PortNumber", originalPortNumber);
                        }
                    }
                }

                if(iGeneralFragmentListener != null){
                    if(isReadyBarcodeCheckbox){
                        resultBarcodeCheckbox = iGeneralFragmentListener.getBarcodeCheckBoxState();
                    }

                    if(isReadyKeyBoardCheckbox){
                        resultKeyBoardCheckbox = iGeneralFragmentListener.getKeyBoardCheckBoxState();
                    }
                }

                if((originalBarcodeCheckbox || resultBarcodeCheckbox) && !(originalBarcodeCheckbox && resultBarcodeCheckbox)){

                    originalBarcodeCheckbox = resultBarcodeCheckbox;
                    programPresenter.saveBooleanValueToSharedPreferencesFile("IsEnableBarcodeReaderMode", originalBarcodeCheckbox);
                }

                if((originalKeyBoardCheckbox || resultKeyBoardCheckbox) && !(originalKeyBoardCheckbox && resultKeyBoardCheckbox)){

                    originalKeyBoardCheckbox = resultKeyBoardCheckbox;
                    programPresenter.saveBooleanValueToSharedPreferencesFile("IsEnableKeyBoardOnTextBoxes", originalKeyBoardCheckbox);
                }

                if(resultFileSeparatorCharachter != null){
                    if(!originalFileSeparatorCharachter.equals(resultFileSeparatorCharachter)){

                        originalFileSeparatorCharachter = resultFileSeparatorCharachter;
                        programPresenter.saveStringValueToSharedPreferencesFile("FileSeparatorCharacter", originalFileSeparatorCharachter);
                    }
                }

                programPresenter.openActivityByEnum(ActivityEnums.MAIN_ACTIVITY);
            });
        }

        if(settingsBackButton != null){
            settingsBackButton.setOnClickListener(v -> {
                checkDataChangesInStorage();
            });
        }
    }

    private void checkDataChangesInStorage() {

        boolean a = false;
        String b = null;
        String c = null;
        String d = null;
        String e = null;
        String f = null;
        boolean g = false;
        boolean h = false;
        String i;
        String j;

        boolean isEnabled = true;

        g = iGeneralFragmentListener.getBarcodeCheckBoxState();
        h = iGeneralFragmentListener.getKeyBoardCheckBoxState();
        i = iGeneralFragmentListener.getFileSeparatorCharachter();

        if(iGeneralFragmentListener != null){
            if(originalBarcodeCheckbox == g && originalKeyBoardCheckbox == h && originalFileSeparatorCharachter.equals(i)){
                isEnabled = true;
            }
            else {
                isEnabled = false;
            }
        }

        if(iUploadFileFragmentListener != null){

            a = iUploadFileFragmentListener.getLocalSaveCheckBoxState();

            b = iUploadFileFragmentListener.getGlobalPath();
            j = iUploadFileFragmentListener.getLocalPath();
            c = iUploadFileFragmentListener.getUsername();
            d = iUploadFileFragmentListener.getPassword();
            e = iUploadFileFragmentListener.getHostName();
            f = iUploadFileFragmentListener.getPortNumber();

            if(
                    originalLocalCheckbox == a &&
                    originalGlobalPath.equals(b) &&
                    originalUsername.equals(c) &&
                    originalPassword.equals(d) &&
                    originalHostName.equals(e) &&
                    originalPortNumber.equals(f) &&
                    originalLocalPath.equals(j) &&
                    isEnabled)
            {
                isEnabled = true;
            }
            else {
                isEnabled = false;
            }
        }

        if(!isEnabled) { openSaveAlertDialog(); }
        else { programPresenter.openActivityByEnum(ActivityEnums.MAIN_ACTIVITY); }
    }

    private void openSaveAlertDialog() {
        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.alertdialog_view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        backDialogButton = dialog.findViewById(R.id.backDialogButton);
        enterDialogButton = dialog.findViewById(R.id.enterDialogButton);

        backDialogButton.setOnClickListener(v -> { dialog.dismiss(); });
        enterDialogButton.setOnClickListener(v -> { programPresenter.openActivityByEnum(ActivityEnums.MAIN_ACTIVITY); });

        dialog.show();
    }

    private void initView(){
        settingsSaveButton = findViewById(R.id.settingsSaveButton);
        settingsBackButton = findViewById(R.id.settingsBackButton);

        settingTabLayout = findViewById(R.id.settingTabLayout);
        settingViewPager = findViewById(R.id.settingViewPager);
    }

    private void initViewPager() {
        settingViewPagerAdapter = new SettingViewPagerAdapter(this);
        fragmentList = new ArrayList<>();
        fragmentList.add(new UploadFileSettingsFragment());
        fragmentList.add(new GeneralSettingsFragment());

        settingViewPagerAdapter.setFragmentList(fragmentList);
        settingViewPager.setAdapter(settingViewPagerAdapter);

        settingTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                settingViewPager.setCurrentItem(tab.getPosition());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        settingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(settingTabLayout != null){
                    settingTabLayout.getTabAt(position).select();
                }
            }
        });
    }

    private void initStartingValuesOfView() {

        if(preferences != null) {

            originalGlobalPath = preferences.getStringValueByKey("GlobalSavePath");
            originalLocalPath = preferences.getStringValueByKey("LocalSavePath");
            originalFileSeparatorCharachter = preferences.getStringValueByKey("FileSeparatorCharacter");
            originalUsername = preferences.getStringValueByKey("Username");
            originalPassword = preferences.getStringValueByKey("Password");
            originalHostName = preferences.getStringValueByKey("HostName");
            originalPortNumber = preferences.getStringValueByKey("PortNumber");

            originalBarcodeCheckbox = preferences.getBooleanValueByKey("IsEnableBarcodeReaderMode");
            originalLocalCheckbox = preferences.getBooleanValueByKey("IsEnableSaveLocalStorage");
            originalKeyBoardCheckbox = preferences.getBooleanValueByKey("IsEnableKeyBoardOnTextBoxes");
        }
    }

    private void initPresenter() {
        programPresenter = new ProgramPresenter(this, getApplicationContext());
        programPresenter.initTaskManager();
    }

    private void initLocalPreferences() {
        preferences = LocalEncryptedPreferences.getInstance(
                "values",
                MasterKeys.AES256_GCM_SPEC,
                getApplicationContext(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
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
        new Handler(Looper.getMainLooper()).post(() -> { Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show(); });
    }

    @Override
    public void sendBarcodeNextCheckBoxState(boolean state) {
        resultBarcodeCheckbox = state;
        isReadyBarcodeCheckbox = false;
    }

    @Override
    public void sendKeyboardCheckBox(boolean state) {
        resultKeyBoardCheckbox = state;
        isReadyKeyBoardCheckbox = false;
    }

    @Override
    public void sendFileSeparatorCharachter(String charachter) {
        if(charachter == null) return;
        resultFileSeparatorCharachter = charachter;
    }

    @Override
    public String getFileSeparatorCharacter() {
        if(originalFileSeparatorCharachter == null) return null;
        return originalFileSeparatorCharachter;
    }

    @Override
    public void sendLocalSavePath(String path) {
        resultLocalPath = path;
    }

    @Override
    public void sendLocalSaveCheckbox(boolean state) {
        resultLocalCheckbox = state;
        isReadyLocalCheckbox = false;
    }

    public void setUploadFileFragmentListener(IUploadFileFragmentListener iUploadFileFragmentListener) {
        this.iUploadFileFragmentListener = iUploadFileFragmentListener;
    }

    public void setGeneralFragmentListener(IGeneralFragmentListener iGeneralFragmentListener) {
        this.iGeneralFragmentListener = iGeneralFragmentListener;
    }
}