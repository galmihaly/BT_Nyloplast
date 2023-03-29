package hu.logcontrol.wasteprogram.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import hu.logcontrol.wasteprogram.R;
import hu.logcontrol.wasteprogram.SettingsActivity;
import hu.logcontrol.wasteprogram.helpers.JSONFileHelper;
import hu.logcontrol.wasteprogram.interfaces.GeneralListener;
import hu.logcontrol.wasteprogram.interfaces.IGeneralFragmentListener;
import hu.logcontrol.wasteprogram.interfaces.IGeneralSettingFragment;
import hu.logcontrol.wasteprogram.presenters.ProgramPresenter;

public class GeneralSettingsFragment extends Fragment implements IGeneralSettingFragment, IGeneralFragmentListener {

    private View view;

    private CheckBox settingBarcodeNextCheckBox;
    private CheckBox settingKeyboardCheckBox;

    private ProgramPresenter programPresenter;

    private boolean resultBarcodeCheckbox;
    private boolean resultKeyBoardCheckbox;

    private GeneralListener generalListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_general, container, false);

        initListeners();

        settingBarcodeNextCheckBox = view.findViewById(R.id.settingBarcodeNextCheckBox);
        settingKeyboardCheckBox = view.findViewById(R.id.settingKeyboardCheckBox);

        programPresenter = new ProgramPresenter(this, getContext());
        programPresenter.initTaskManager();

        if(settingBarcodeNextCheckBox != null && settingKeyboardCheckBox != null){

            boolean isExist = JSONFileHelper.isExist(getContext(), "values.json");
            if(isExist) {

                resultBarcodeCheckbox = JSONFileHelper.getBoolean(getContext(), "values.json", "IsEnableBarcodeReaderMode");
                settingBarcodeNextCheckBox.setChecked(resultBarcodeCheckbox);

                resultKeyBoardCheckbox = JSONFileHelper.getBoolean(getContext(), "values.json", "IsEnableKeyBoardOnTextBoxes");
                settingKeyboardCheckBox.setChecked(resultKeyBoardCheckbox);
            }
        }

        return view;
    }

    public void initListeners(){
        try {
            generalListener = (SettingsActivity) getActivity();
            ((SettingsActivity) getActivity()).setGeneralFragmentListener(GeneralSettingsFragment.this);
        }
        catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(settingBarcodeNextCheckBox != null && settingKeyboardCheckBox != null){

            boolean isExist = JSONFileHelper.isExist(getContext(), "values.json");
            if(isExist) {

                resultBarcodeCheckbox = JSONFileHelper.getBoolean(getContext(), "values.json", "IsEnableBarcodeReaderMode");
                settingBarcodeNextCheckBox.setChecked(resultBarcodeCheckbox);

                resultKeyBoardCheckbox = JSONFileHelper.getBoolean(getContext(), "values.json", "IsEnableKeyBoardOnTextBoxes");
                settingKeyboardCheckBox.setChecked(resultKeyBoardCheckbox);

            }
        }

        if(settingKeyboardCheckBox != null){
            settingKeyboardCheckBox.setOnClickListener(v -> {
                if(settingKeyboardCheckBox.isChecked()){

                    settingKeyboardCheckBox.setChecked(true);
                    generalListener.sendKeyboardCheckBox(true);
                }
                else {
                    settingKeyboardCheckBox.setChecked(false);
                    generalListener.sendKeyboardCheckBox(false);
                }
            });
        }

        if(settingBarcodeNextCheckBox != null){
            settingBarcodeNextCheckBox.setOnClickListener(v -> {
                if(settingBarcodeNextCheckBox.isChecked()){

                    settingBarcodeNextCheckBox.setChecked(true);
                    generalListener.sendBarcodeNextCheckBoxState(true);
                }
                else {
                    settingBarcodeNextCheckBox.setChecked(false);
                    generalListener.sendBarcodeNextCheckBoxState(false);
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
    public boolean getBarcodeCheckBoxState() {
        if(settingBarcodeNextCheckBox.isChecked()) { return true; }
        return false;
    }

    @Override
    public boolean getKeyBoardCheckBoxState() {
        if(settingKeyboardCheckBox.isChecked()) { return true; }
        return false;
    }
}