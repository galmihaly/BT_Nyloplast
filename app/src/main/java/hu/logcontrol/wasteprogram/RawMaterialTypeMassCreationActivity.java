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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.google.android.material.textfield.TextInputLayout;

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

    private ImageButton enterButton_type_1;
    private ImageButton enterButton_type_2;
    private ImageButton enterButton_type_3;
    private ImageButton enterButton_type_4;

    private TextInputLayout textLayout_typeMass_1;

    private final String disableColor = "#B7C0C1";
    private final String enableColor = "#000000";

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

                        ElementStateChangeHelper.visibleCardViewElement(typeMassCountCV);
                        ElementStateChangeHelper.inVisibleCardViewElement(typeMassTypeCV);

                        ElementStateChangeHelper.setNotReadyStateElements(getApplicationContext(), typeMassCountCL, R.drawable.cardview_red_background, typeMassCountTextBox, enableColor);
                        ElementStateChangeHelper.clearNextCardView(getApplicationContext(), typeMassTypeCL, R.drawable.cardview_red_background ,typeMassTypeTextBox, R.color.black);

                        ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_type_1); // enter button lezárása
                        ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.DELETE_BUTTON_DISABLED, deleteTypeMassButton);
                        ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ADD_BUTTON_DISABLED, addRawMatTypeMassButton);
                    }
                    else {
                        ElementStateChangeHelper.enableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_ENABLED, enterButton_type_1);

                        enterButton_type_1.setOnClickListener(view -> {
                            ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), typeMassCountCL, R.drawable.cardview_green_background, typeMassCountTextBox, disableColor); // jelenlegi elemek késszé tétele

                            ElementStateChangeHelper.visibleCardViewElement(typeMassTypeCV);
                            ElementStateChangeHelper.setNotReadyStateElements(getApplicationContext(), typeMassTypeCL, R.drawable.cardview_red_background, typeMassTypeTextBox, enableColor); // következő elemek láthatóvá tétele

                            ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_type_1); // enter button lezárása
                            ElementStateChangeHelper.enableButton(getApplicationContext(), EditButtonEnums.DELETE_BUTTON_ENABLED, deleteTypeMassButton);
                            hideNavigationBar();
                        });

                        typeMassCountTextBox.setOnKeyListener((view, l, keyEvent) -> {
                            if(l == 66){
                                if(enterButton_type_1 != null){
                                    if(enterButton_type_1.isEnabled()){
                                        enterButton_type_1.callOnClick();
                                    }
                                }
                            }
                            return false;
                        });
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

                        ElementStateChangeHelper.inVisibleCardViewElement(storageBoxIdentifierCV);

                        ElementStateChangeHelper.setNotReadyStateElements(getApplicationContext(), typeMassTypeCL, R.drawable.cardview_red_background, typeMassTypeTextBox, enableColor);
                        ElementStateChangeHelper.clearNextCardView(getApplicationContext(), storageBoxIdentifierCL, R.drawable.cardview_red_background ,storageBoxIdentifierTextBox, R.color.black);

                        ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_type_2); // enter button lezárása
                        hideNavigationBar();
                    }
                    else {
                        ElementStateChangeHelper.enableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_ENABLED, enterButton_type_2);

                        enterButton_type_2.setOnClickListener(view -> {
                            ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), typeMassTypeCL, R.drawable.cardview_green_background, typeMassTypeTextBox, disableColor); // jelenlegi elemek késszé tétele

                            ElementStateChangeHelper.visibleCardViewElement(storageBoxIdentifierCV);
                            ElementStateChangeHelper.setNotReadyStateElements(getApplicationContext(), storageBoxIdentifierCL, R.drawable.cardview_red_background, storageBoxIdentifierTextBox, enableColor); // következő elemek láthatóvá tétele

                            ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_type_2); // enter button lezárása
                            hideNavigationBar();
                        });

                        typeMassTypeTextBox.setOnKeyListener((view, l, keyEvent) -> {
                            if(l == 66){
                                if(enterButton_type_2 != null){
                                    if(enterButton_type_2.isEnabled()){
                                        enterButton_type_2.callOnClick();
                                    }
                                }
                            }
                            return false;
                        });

                        hideNavigationBar();
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

                        ElementStateChangeHelper.inVisibleCardViewElement(massDataCV);

                        ElementStateChangeHelper.setNotReadyStateElements(getApplicationContext(), storageBoxIdentifierCL, R.drawable.cardview_red_background, storageBoxIdentifierTextBox, enableColor);
                        ElementStateChangeHelper.clearNextCardView(getApplicationContext(), massDataCL, R.drawable.cardview_red_background ,massDataTextBox, R.color.black);

                        ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_type_3); // enter button lezárása
                        hideNavigationBar();
                    }
                    else {
                        ElementStateChangeHelper.enableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_ENABLED, enterButton_type_3);

                        enterButton_type_3.setOnClickListener(view -> {
                            ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), storageBoxIdentifierCL, R.drawable.cardview_green_background, storageBoxIdentifierTextBox, disableColor); // jelenlegi elemek késszé tétele

                            ElementStateChangeHelper.visibleCardViewElement(massDataCV);
                            ElementStateChangeHelper.setNotReadyStateElements(getApplicationContext(), massDataCL, R.drawable.cardview_red_background, massDataTextBox, enableColor); // következő elemek láthatóvá tétele

                            ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_type_3); // enter button lezárása
                            hideNavigationBar();
                        });

                        storageBoxIdentifierTextBox.setOnKeyListener((view, l, keyEvent) -> {
                            if(l == 66){
                                if(enterButton_type_3 != null){
                                    if(enterButton_type_3.isEnabled()){
                                        enterButton_type_3.callOnClick();
                                    }
                                }
                            }
                            return false;
                        });

                        hideNavigationBar();
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

                        ElementStateChangeHelper.inVisibleCardViewElement(massDataCV);
                        ElementStateChangeHelper.setNotReadyStateElements(getApplicationContext(), massDataCL, R.drawable.cardview_red_background, massDataTextBox, enableColor);
                        ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_type_4); // enter button lezárása
                    }
                    else{
                        ElementStateChangeHelper.enableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_ENABLED, enterButton_type_4);

                        enterButton_type_4.setOnClickListener(view -> {
                            ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), massDataCL, R.drawable.cardview_green_background, massDataTextBox, disableColor); // jelenlegi elemek késszé tétele
                            ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_type_4); // enter button lezárása

                            ElementStateChangeHelper.enableButton(getApplicationContext(), EditButtonEnums.ADD_BUTTON_ENABLED, addRawMatTypeMassButton);
                            hideNavigationBar();
                        });

                        massDataTextBox.setOnKeyListener((view, l, keyEvent) -> {
                            if(l == 66){
                                if(enterButton_type_4 != null){
                                    if(enterButton_type_4.isEnabled()){
                                        enterButton_type_4.callOnClick();
                                    }
                                }
                            }
                            return false;
                        });

                        hideNavigationBar();
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
                typeMassCountTextBox.setText("");
                textLayout_typeMass_1.requestFocus();
                hideNavigationBar();
            }
        });
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

    public void initView(){

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

        enterButton_type_1 = findViewById(R.id.enterButton_type_1);
        enterButton_type_2 = findViewById(R.id.enterButton_type_2);
        enterButton_type_3 = findViewById(R.id.enterButton_type_3);
        enterButton_type_4 = findViewById(R.id.enterButton_type_4);

        textLayout_typeMass_1 = findViewById(R.id.textLayout_typeMass_1);

        hideNavigationBar();
    }

    private void hideNavigationBar(){
        Helper.hideNavigationBar(this);
    }
}