package hu.logcontrol.wasteprogram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import hu.logcontrol.wasteprogram.adapters.SettingViewPagerAdapter;
import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.fragments.GeneralSettingsFragment;
import hu.logcontrol.wasteprogram.fragments.UploadFileSettingsFragment;
import hu.logcontrol.wasteprogram.helpers.JSONFileHelper;
import hu.logcontrol.wasteprogram.interfaces.GeneralListener;
import hu.logcontrol.wasteprogram.interfaces.IGeneralFragmentListener;
import hu.logcontrol.wasteprogram.interfaces.ISettingsView;
import hu.logcontrol.wasteprogram.interfaces.IUploadFileFragmentListener;
import hu.logcontrol.wasteprogram.interfaces.UploadFileListener;
import hu.logcontrol.wasteprogram.presenters.ProgramPresenter;

public class SettingsActivity extends AppCompatActivity implements ISettingsView, GeneralListener, UploadFileListener {

    private ImageButton settingsSaveButton;
    private ImageButton settingsBackButton;

    private ProgramPresenter programPresenter;

    private String resultGlobalPath;
    private String resultLocalPath;
    private String resultFileSeparatorCharachter;
    private String resultUsername;
    private String resultPassword;

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

    private boolean originalLocalCheckbox;
    private boolean originalBarcodeCheckbox;
    private boolean originalKeyBoardCheckbox;

    private TabLayout settingTabLayout;
    private ViewPager2 settingViewPager;
    private SettingViewPagerAdapter settingViewPagerAdapter;
    private List<Fragment> fragmentList;

    private IUploadFileFragmentListener iUploadFileFragmentListener;
    private IGeneralFragmentListener iGeneralFragmentListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
        initPresenter();
        initStartingValuesOfView();
        initViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(settingsSaveButton != null){
            settingsSaveButton.setOnClickListener(v -> {

                if(iUploadFileFragmentListener != null){
                    if(isReadyLocalCheckbox){
                        resultLocalCheckbox = iUploadFileFragmentListener.getLocalSaveCheckBoxState();
                    }

                    resultGlobalPath = iUploadFileFragmentListener.getGlobalPath();
                    Log.e("resultGlobalPath", resultGlobalPath);
                    resultUsername = iUploadFileFragmentListener.getUsername();
                    Log.e("resultUsername", resultUsername);
                    resultPassword = iUploadFileFragmentListener.getPassword();
                    Log.e("resultPassword", resultPassword);
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
                    programPresenter.saveBooleanValueToJSONFile("IsEnableBarcodeReaderMode", originalBarcodeCheckbox);
                }

                if((originalLocalCheckbox || resultLocalCheckbox) && !(originalLocalCheckbox && resultLocalCheckbox)){
                    originalLocalCheckbox = resultLocalCheckbox;
                    Log.e("resultLocalCheckbox_programPresenter", String.valueOf(resultLocalCheckbox));
                    programPresenter.saveBooleanValueToJSONFile("IsEnableSaveLocalStorage", originalLocalCheckbox);
                }

                if((originalKeyBoardCheckbox || resultKeyBoardCheckbox) && !(originalKeyBoardCheckbox && resultKeyBoardCheckbox)){
                    originalKeyBoardCheckbox = resultKeyBoardCheckbox;
                    programPresenter.saveBooleanValueToJSONFile("IsEnableKeyBoardOnTextBoxes", originalKeyBoardCheckbox);
                }

                if(originalLocalPath != null  && resultLocalPath != null){
                    if(!originalLocalPath.equals(resultLocalPath)){
                        originalLocalPath = resultLocalPath;
                        programPresenter.saveStringValueToJSONFile("LocalSavePath", resultLocalPath);
                    }
                }

                if(originalFileSeparatorCharachter != null && resultFileSeparatorCharachter != null){
                    if(originalFileSeparatorCharachter != resultFileSeparatorCharachter){
                        originalFileSeparatorCharachter = resultFileSeparatorCharachter;
                        programPresenter.saveStringValueToJSONFile("FileSeparatorCharacter", originalFileSeparatorCharachter);
                    }
                }

                if(originalGlobalPath != null && resultGlobalPath != null){
                    if(!originalGlobalPath.equals(resultGlobalPath)){
                        originalGlobalPath = resultGlobalPath;
                        programPresenter.saveStringValueToJSONFile("GlobalSavePath", resultGlobalPath);
                    }
                }

                if(originalUsername != null && resultUsername != null){
                    if(!originalUsername.equals(resultUsername)){
                        originalUsername = resultUsername;
                        programPresenter.saveStringValueToJSONFile("Username", resultUsername);
                    }
                }

                if(originalPassword != null && resultPassword != null){
                    if(!originalPassword.equals(resultPassword)){
                        originalPassword = resultPassword;
                        programPresenter.saveStringValueToJSONFile("Password", resultPassword);
                    }
                }
            });
        }

        if(settingsBackButton != null){
            settingsBackButton.setOnClickListener(v -> {
                programPresenter.openActivityByEnum(ActivityEnums.MAIN_ACTIVITY);
            });
        }
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
        boolean isExist = JSONFileHelper.isExist(getApplicationContext(), "values.json");
        if(isExist) {
            originalGlobalPath = JSONFileHelper.getString(getApplicationContext(), "values.json", "GlobalSavePath");
            originalLocalPath = JSONFileHelper.getString(getApplicationContext(), "values.json", "LocalSavePath");
            originalFileSeparatorCharachter = JSONFileHelper.getString(getApplicationContext(), "values.json", "FileSeparatorCharacter");
            originalUsername = JSONFileHelper.getString(getApplicationContext(), "values.json", "Username");
            originalPassword = JSONFileHelper.getString(getApplicationContext(), "values.json", "Password");

            originalBarcodeCheckbox = JSONFileHelper.getBoolean(getApplicationContext(), "values.json", "IsEnableBarcodeReaderMode");
            originalLocalCheckbox = JSONFileHelper.getBoolean(getApplicationContext(), "values.json", "IsEnableSaveLocalStorage");
            originalKeyBoardCheckbox = JSONFileHelper.getBoolean(getApplicationContext(), "values.json", "IsEnableKeyBoardOnTextBoxes");
        }
    }

    private void initPresenter() {
        programPresenter = new ProgramPresenter(this, getApplicationContext());
        programPresenter.initTaskManager();
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