package hu.logcontrol.wasteprogram.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Spinner;
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

    private String resultFileSeparator;

    private ProgramPresenter programPresenter;

    private boolean resultBarcodeCheckbox;
    private boolean resultKeyBoardCheckbox;

    private GeneralListener generalListener;

    private ArrayAdapter<String> arrayAdapter;
    private AutoCompleteTextView fileSeparatorACTV;
    private final String[] fileSeparators = new String[] {"vessző", "pontosvessző", "tabulátor"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_general, container, false);

        settingBarcodeNextCheckBox = view.findViewById(R.id.settingBarcodeNextCheckBox);
        settingKeyboardCheckBox = view.findViewById(R.id.settingKeyboardCheckBox);
        fileSeparatorACTV = view.findViewById(R.id.fileSeparatorACTV);

        initListeners();
        initPresenter();
        initFileSeparatorsDropDownMenu();
        initDefaultValues();
        isCheckingCheckboxes();

        return view;
    }

    private void isCheckingCheckboxes() {
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

    private void initDefaultValues() {
        if(settingBarcodeNextCheckBox != null && settingKeyboardCheckBox != null && fileSeparatorACTV != null && arrayAdapter != null){

            boolean isExist = JSONFileHelper.isExist(getContext(), "values.json");
            if(isExist) {

                resultBarcodeCheckbox = JSONFileHelper.getBoolean(getContext(), "values.json", "IsEnableBarcodeReaderMode");
                settingBarcodeNextCheckBox.setChecked(resultBarcodeCheckbox);

                resultKeyBoardCheckbox = JSONFileHelper.getBoolean(getContext(), "values.json", "IsEnableKeyBoardOnTextBoxes");
                settingKeyboardCheckBox.setChecked(resultKeyBoardCheckbox);

                int resultPosition = -1;
                resultFileSeparator = JSONFileHelper.getString(getContext(), "values.json", "FileSeparatorCharacter");

                for (int i = 0; i < fileSeparators.length; i++) {
                    if(fileSeparators[i].equals(resultFileSeparator)) resultPosition = i;
                }

                if(resultPosition != -1){ fileSeparatorACTV.setText(arrayAdapter.getItem(resultPosition), false); }
            }
        }
    }

    private void initPresenter() {
        programPresenter = new ProgramPresenter(this, getContext());
        programPresenter.initTaskManager();
    }

    private void initFileSeparatorsDropDownMenu() {

        arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.fileseparator_dropdownmenu_item, fileSeparators);

        if(fileSeparatorACTV != null){
            fileSeparatorACTV.setAdapter(arrayAdapter);

            int resultPosition = -1;
            String c = generalListener.getFileSeparatorCharacter();
            for (int i = 0; i < fileSeparators.length; i++) {
                if(fileSeparators[i].equals(c)) resultPosition = i;
                break;
            }

            if(resultPosition != -1){
                fileSeparatorACTV.setText(arrayAdapter.getItem(resultPosition), false);
            }

            fileSeparatorACTV.setOnItemClickListener((parent, view, position, id) -> {
                generalListener.sendFileSeparatorCharachter(arrayAdapter.getItem(position));
            });
        }
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

    @Override
    public String getFileSeparatorCharachter() {
//        if(fileSeparatorACTV == null) return null;
        return fileSeparatorACTV.getText().toString();
    }
}