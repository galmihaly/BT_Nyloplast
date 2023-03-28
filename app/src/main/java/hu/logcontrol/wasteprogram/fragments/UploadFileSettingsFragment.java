package hu.logcontrol.wasteprogram.fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
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
import hu.logcontrol.wasteprogram.helpers.JSONFileHelper;
import hu.logcontrol.wasteprogram.interfaces.IUploadFileSettingsFragment;
import hu.logcontrol.wasteprogram.interfaces.TextBoxListener;
import hu.logcontrol.wasteprogram.interfaces.UploadFileListener;
import hu.logcontrol.wasteprogram.presenters.ProgramPresenter;

public class UploadFileSettingsFragment extends Fragment implements IUploadFileSettingsFragment {

    private View view;

    private TextInputEditText settingsLocalSavePathTB;
    private TextInputEditText settingsGlobalSavePathTB;
    private ImageButton settingsSaveButton;
    private ImageButton settingsBackButton;
    private ImageButton folderPickerButton;
    private CheckBox localSavePathCheckbox;

    private ConstraintLayout localSavePathCL;
    private ProgramPresenter programPresenter;

    private boolean resultBoolean;
    private String resultString;

    private UploadFileListener uploadFileListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_upload_file_settings, container, false);

        initUploadFileListener();

        settingsLocalSavePathTB = view.findViewById(R.id.settingsLocalSavePathTB);
        settingsGlobalSavePathTB = view.findViewById(R.id.settingsGlobalSavePathTB);
        settingsSaveButton = view.findViewById(R.id.settingsSaveButton);
        settingsBackButton = view.findViewById(R.id.settingsBackButton);
        folderPickerButton = view.findViewById(R.id.folderPickerButton);
        localSavePathCheckbox = view.findViewById(R.id.localSavePathCheckbox);
        localSavePathCL = view.findViewById(R.id.localSavePathCL);

        programPresenter = new ProgramPresenter(this, getContext());
        programPresenter.initTaskManager();

        if(settingsGlobalSavePathTB != null && settingsLocalSavePathTB != null && localSavePathCheckbox != null && localSavePathCL != null){

            boolean isExist = JSONFileHelper.isExist(getContext(), "values.json");
            if(isExist) {

                resultString = JSONFileHelper.getString(getContext(), "values.json", "GlobalSavePath");
                if(resultString != null) settingsGlobalSavePathTB.setText(resultString);

                resultString = JSONFileHelper.getString(getContext(), "values.json", "LocalSavePath");
                if(resultString != null) settingsLocalSavePathTB.setText(resultString);

                resultBoolean = JSONFileHelper.getBoolean(getContext(), "values.json", "IsEnableSaveLocalStorage");
                localSavePathCheckbox.setChecked(resultBoolean);

                if(localSavePathCheckbox.isChecked()){ localSavePathCL.setVisibility(View.VISIBLE); }
                else{ localSavePathCL.setVisibility(View.INVISIBLE); }
            }
        }

        return view;
    }

    public void initUploadFileListener(){
        try {
            uploadFileListener = (UploadFileListener) getActivity();
        }
        catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(settingsGlobalSavePathTB != null && settingsLocalSavePathTB != null && localSavePathCheckbox != null){

            boolean isExist = JSONFileHelper.isExist(getContext(), "values.json");
            if(isExist) {

                resultString = JSONFileHelper.getString(getContext(), "values.json", "GlobalSavePath");
                if(resultString != null) settingsGlobalSavePathTB.setText(resultString);

                resultString = JSONFileHelper.getString(getContext(), "values.json", "LocalSavePath");
                if(resultString != null) settingsLocalSavePathTB.setText(resultString);

                resultBoolean = JSONFileHelper.getBoolean(getContext(), "values.json", "IsEnableSaveLocalStorage");
                localSavePathCheckbox.setChecked(resultBoolean);

                if(localSavePathCheckbox.isChecked()){ localSavePathCL.setVisibility(View.VISIBLE); }
                else{ localSavePathCL.setVisibility(View.INVISIBLE); }
            }
        }

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
                    uploadFileListener.sendLocalSaveCheckbox(true);

                    if(folderPickerButton != null){
                        folderPickerButton.setOnClickListener(view -> {
                            programPresenter.openActivityByEnum(ActivityEnums.FOLDERPICKER_ACTIVITY);
                        });
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
}