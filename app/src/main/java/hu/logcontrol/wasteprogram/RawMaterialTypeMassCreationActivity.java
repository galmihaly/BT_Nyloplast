package hu.logcontrol.wasteprogram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;

import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.helpers.ElementStateChangeHelper;
import hu.logcontrol.wasteprogram.helpers.Helper;

public class RawMaterialTypeMassCreationActivity extends AppCompatActivity {

    private ScrollView massDataScrollView;

    private CardView typeMassCountCV;
    private CardView typeMassTypeCV;
    private CardView storageBoxIdentifierCV;
    private CardView massDataCV;

    private ConstraintLayout typeMassCountCL;
    private ConstraintLayout typeMassTypeCL;
    private ConstraintLayout storageBoxIdentifierCL;
    private ConstraintLayout massDataCL;

    private EditText typeMassCountTextBox;
    private EditText typeMassTypeTextBox;
    private EditText storageBoxIdentifierTextBox;
    private EditText massDataTextBox;

    private ImageButton addRawMatTypeMassButton;
    private ImageButton deleteTypeMassButton;
    private ImageButton backTypeMassButton;
    private ImageButton enterButton;

    private String disableColor = "#B7C0C1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_material_type_mass_creation);
        hideNavigationBar();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(typeMassTypeTextBox.getText().toString().equals("") && typeMassCountTextBox.getText().toString().equals("") &&
            storageBoxIdentifierTextBox.getText().toString().equals("") && massDataTextBox.getText().toString().equals(""))
        {
            addRawMatTypeMassButton.setEnabled(false);
            deleteTypeMassButton.setEnabled(false);
        }

        if(backTypeMassButton != null){
            backTypeMassButton.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), ModesTwo.class);
                startActivity(intent);
            });
        }

        if(typeMassCountCV != null && typeMassCountTextBox != null && typeMassCountCL != null){
            typeMassCountTextBox.addTextChangedListener(new TextWatcher() {

                @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    hideNavigationBar();
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String data = typeMassCountTextBox.getText().toString();
                    if(data.equals("")){

                        ElementStateChangeHelper.disableCurrentElements(getApplicationContext(), typeMassCountCL, typeMassTypeCV, typeMassTypeTextBox, R.drawable.cardview_red_background);
                        ElementStateChangeHelper.setDisableButton(getApplicationContext(), EditButtonEnums.DELETE_BUTTON_DISABLED, deleteTypeMassButton);
                    }
                    else {
                        ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), typeMassCountCL, typeMassCountTextBox, disableColor, R.drawable.cardview_green_background);
                        ElementStateChangeHelper.setEnableButton(getApplicationContext(), EditButtonEnums.DELETE_BUTTON_ENABLED, deleteTypeMassButton);
                        ElementStateChangeHelper.enableNextElements(typeMassTypeCV, typeMassTypeTextBox);
                    }
                }

                @Override public void afterTextChanged(Editable editable) {}
            });
        }

        if(typeMassTypeCV != null && typeMassTypeTextBox != null && typeMassTypeCL != null){
            typeMassTypeTextBox.addTextChangedListener(new TextWatcher() {

                @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String data = typeMassTypeTextBox.getText().toString();
                    if(data.equals("")){

                        ElementStateChangeHelper.disableCurrentElements(getApplicationContext(), typeMassTypeCL, storageBoxIdentifierCV, storageBoxIdentifierTextBox, R.drawable.cardview_red_background);
                        ElementStateChangeHelper.setDisableButton(getApplicationContext(), EditButtonEnums.DELETE_BUTTON_DISABLED, deleteTypeMassButton);
                    }
                    else {
                        ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), typeMassTypeCL, typeMassTypeTextBox, disableColor, R.drawable.cardview_green_background);
                        ElementStateChangeHelper.setEnableButton(getApplicationContext(), EditButtonEnums.DELETE_BUTTON_ENABLED, deleteTypeMassButton);
                        ElementStateChangeHelper.enableNextElements(storageBoxIdentifierCV, storageBoxIdentifierTextBox);
                    }
                }

                @Override public void afterTextChanged(Editable editable) {}
            });
        }

        if(storageBoxIdentifierCV != null && storageBoxIdentifierTextBox != null && storageBoxIdentifierCL != null){
            storageBoxIdentifierTextBox.addTextChangedListener(new TextWatcher() {

                @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String data = storageBoxIdentifierTextBox.getText().toString();
                    if(data.equals("")){

                        ElementStateChangeHelper.disableCurrentElements(getApplicationContext(), storageBoxIdentifierCL, massDataCV, massDataTextBox, R.drawable.cardview_red_background);
                        ElementStateChangeHelper.setDisableButton(getApplicationContext(), EditButtonEnums.DELETE_BUTTON_DISABLED, deleteTypeMassButton);
                    }
                    else {
                        ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), storageBoxIdentifierCL, storageBoxIdentifierTextBox, disableColor, R.drawable.cardview_green_background);
                        ElementStateChangeHelper.setEnableButton(getApplicationContext(), EditButtonEnums.DELETE_BUTTON_ENABLED, deleteTypeMassButton);
                        ElementStateChangeHelper.enableNextElements(massDataCV, massDataTextBox);
                    }
                }

                @Override public void afterTextChanged(Editable editable) {}
            });
        }

        if(massDataCV != null && massDataTextBox != null && massDataCL != null){
            massDataTextBox.addTextChangedListener(new TextWatcher() {

                @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String data = massDataTextBox.getText().toString();
                    if(data.equals("")){

                        massDataCL.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_red_background));
                        massDataTextBox.setTextColor(Color.parseColor("#000000"));
                        ElementStateChangeHelper.setDisableButton(getApplicationContext(), EditButtonEnums.ADD_BUTTON_DISABLED, addRawMatTypeMassButton);
                        ElementStateChangeHelper.setDisableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton);
                    }
                    else{
                        ElementStateChangeHelper.setEnableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_ENABLED, enterButton);

                        enterButton.setOnClickListener(view -> {
                            ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), massDataCL, massDataTextBox, disableColor, R.drawable.cardview_green_background);
                            ElementStateChangeHelper.setEnableButton(getApplicationContext(), EditButtonEnums.ADD_BUTTON_ENABLED, addRawMatTypeMassButton);
                            ElementStateChangeHelper.setDisableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton);
                            addRawMatTypeMassButton.setEnabled(true);

                            hideNavigationBar();
                        });
                    }
                }

                @Override public void afterTextChanged(Editable editable) {}
            });
        }

        if(addRawMatTypeMassButton != null){
            addRawMatTypeMassButton.setOnClickListener(view -> {

                Intent intent = new Intent();

                intent.putExtra("typeMassCountTextBox", typeMassCountTextBox.getText().toString());
                intent.putExtra("typeMassTypeTextBox", typeMassTypeTextBox.getText().toString());
                intent.putExtra("storageBoxIdentifierTextBox", storageBoxIdentifierTextBox.getText().toString());
                intent.putExtra("massDataTextBox", massDataTextBox.getText().toString());

                setResult(1, intent);

                RawMaterialTypeMassCreationActivity.super.onBackPressed();
            });
        }

        deleteTypeMassButton.setOnClickListener(view -> {
            if(typeMassCountCV != null){
                typeMassCountTextBox.setEnabled(true);
                typeMassCountTextBox.requestFocus();
                typeMassCountTextBox.setText("");

                storageBoxIdentifierCV.setVisibility(View.INVISIBLE);
                storageBoxIdentifierTextBox.setText("");

                if(massDataScrollView != null) massDataScrollView.fullScroll(ScrollView.FOCUS_UP);

                hideNavigationBar();
            }
        });
    }

    public void initView(){

        massDataScrollView = findViewById(R.id.massDataScrollView);

        typeMassCountCV = findViewById(R.id.typeMassCountCV);
        typeMassTypeCV = findViewById(R.id.typeMassTypeCV);
        storageBoxIdentifierCV = findViewById(R.id.storageBoxIdentifierCV);
        massDataCV = findViewById(R.id.massDataCV);

        typeMassTypeCV.setVisibility(View.INVISIBLE);
        storageBoxIdentifierCV.setVisibility(View.INVISIBLE);
        massDataCV.setVisibility(View.INVISIBLE);

        typeMassCountCL = findViewById(R.id.typeMassCountCL);
        typeMassTypeCL = findViewById(R.id.typeMassTypeCL);
        storageBoxIdentifierCL = findViewById(R.id.storageBoxIdentifierCL);
        massDataCL = findViewById(R.id.massDataCL);

        typeMassCountTextBox = findViewById(R.id.typeMassCountTextBox);
        typeMassTypeTextBox = findViewById(R.id.typeMassTypeTextBox);
        storageBoxIdentifierTextBox = findViewById(R.id.storageBoxIdentifierTextBox);
        massDataTextBox = findViewById(R.id.massDataTextBox);

        addRawMatTypeMassButton = findViewById(R.id.addRawMatTypeMassButton);
        deleteTypeMassButton = findViewById(R.id.deleteTypeMassButton);
        backTypeMassButton = findViewById(R.id.backTypeMassButton);
        enterButton = findViewById(R.id.enterButton);

        hideNavigationBar();
    }

    private void hideNavigationBar(){
        Helper.hideNavigationBar(this);
    }
}