package hu.logcontrol.wasteprogram.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import hu.logcontrol.wasteprogram.R;
import hu.logcontrol.wasteprogram.SettingsActivity;
import hu.logcontrol.wasteprogram.enums.ActivityEnums;
import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.helpers.JSONFileHelper;
import hu.logcontrol.wasteprogram.interfaces.IUploadFileFragmentListener;
import hu.logcontrol.wasteprogram.interfaces.IUploadFileSettingsFragment;
import hu.logcontrol.wasteprogram.interfaces.UploadFileListener;
import hu.logcontrol.wasteprogram.presenters.ProgramPresenter;

public class UploadFileSettingsFragment extends Fragment implements IUploadFileSettingsFragment, IUploadFileFragmentListener {

    private View view;

    private TextInputEditText settingsLocalSavePathTB;
    private TextInputEditText settingsGlobalSavePathTB;
    private ImageButton folderPickerButton;
    private CheckBox localSavePathCheckbox;

    private ConstraintLayout localSavePathCL;
    private ProgramPresenter programPresenter;

    private boolean resultLocalSaveBoolean;
    private String resultGlobalPath;
    private String resultLocalPath;

    private UploadFileListener uploadFileListener;

    private String path = null;

    @SuppressLint("SetTextI18n")
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent intent = result.getData();
                    if(intent == null) return;

                    path = Helper.formatPathString(intent.getData().getPathSegments());
                    Log.e("ActivityResultLauncher -> path", path);
                    if(localSavePathCheckbox.isChecked()){
                        if(path != null){
                            uploadFileListener.sendLocalSavePath(path);
                        }
                    }

                    if(settingsLocalSavePathTB != null){
                        settingsLocalSavePathTB.setText(path);
                    }
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_upload_file_settings, container, false);

        initListeners();

        settingsLocalSavePathTB = view.findViewById(R.id.settingsLocalSavePathTB);
        settingsGlobalSavePathTB = view.findViewById(R.id.settingsGlobalSavePathTB);
        folderPickerButton = view.findViewById(R.id.folderPickerButton);

        localSavePathCheckbox = view.findViewById(R.id.localSavePathCheckbox);

        localSavePathCL = view.findViewById(R.id.localSavePathCL);

        programPresenter = new ProgramPresenter(this, getContext());
        programPresenter.initTaskManager();

        if(settingsGlobalSavePathTB != null && settingsLocalSavePathTB != null && localSavePathCheckbox != null && localSavePathCL != null){

            boolean isExist = JSONFileHelper.isExist(getContext(), "values.json");
            if(isExist) {

                resultGlobalPath = JSONFileHelper.getString(getContext(), "values.json", "GlobalSavePath");
                if(resultGlobalPath != null) settingsGlobalSavePathTB.setText(resultGlobalPath);

                resultLocalPath = JSONFileHelper.getString(getContext(), "values.json", "LocalSavePath");
                if(resultLocalPath != null) settingsLocalSavePathTB.setText(resultLocalPath);

                resultLocalSaveBoolean = JSONFileHelper.getBoolean(getContext(), "values.json", "IsEnableSaveLocalStorage");
                localSavePathCheckbox.setChecked(resultLocalSaveBoolean);

                if(localSavePathCheckbox.isChecked()){
                    localSavePathCL.setVisibility(View.VISIBLE);
                }
                else{
                    localSavePathCL.setVisibility(View.INVISIBLE);
                }
            }
        }

        return view;
    }

    public void initListeners(){
        try {
            uploadFileListener = (UploadFileListener) getActivity();
            ((SettingsActivity) getActivity()).setActivityListener(UploadFileSettingsFragment.this);
        }
        catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(localSavePathCheckbox != null){
            localSavePathCheckbox.setOnClickListener(v -> {
                if(localSavePathCheckbox.isChecked()){

                    localSavePathCL.setVisibility(View.VISIBLE);
                    folderPickerButton.setEnabled(true);
                    uploadFileListener.sendLocalSaveCheckbox(true);

                    if(folderPickerButton != null){
                        if(localSavePathCheckbox.isChecked()){
                            folderPickerButton.setOnClickListener(view -> {
                                programPresenter.openActivityByEnum(ActivityEnums.FOLDERPICKER_ACTIVITY);
                            });
                        }
                    }
                }
                if(!localSavePathCheckbox.isChecked()){

                    localSavePathCL.setVisibility(View.INVISIBLE);
                    folderPickerButton.setEnabled(false);
                    uploadFileListener.sendLocalSaveCheckbox(false);
                }
            });
        }
    }

    @Override
    public void getMessageFromPresenter(String message) {
        if(message == null) return;
        new Handler(Looper.getMainLooper()).post(() -> { Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show(); });
    }

    @Override
    public void openActivityByIntent(Intent intent) {
        activityResultLauncher.launch(intent);
    }

    @Override
    public String getGlobalPath() {
        if(settingsGlobalSavePathTB == null && !settingsGlobalSavePathTB.getText().toString().equals("")) return null;
        return settingsGlobalSavePathTB.getText().toString();
    }
}