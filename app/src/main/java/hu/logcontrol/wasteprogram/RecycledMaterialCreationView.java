package hu.logcontrol.wasteprogram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import hu.logcontrol.wasteprogram.helpers.JSONFileHelper;
import hu.logcontrol.wasteprogram.helpers.TextWatcherHelper;
import hu.logcontrol.wasteprogram.models.LocalEncryptedPreferences;

public class RecycledMaterialCreationView extends AppCompatActivity {

    private ConstraintLayout constraint_1;
    private ConstraintLayout constraint_2;
    private ConstraintLayout constraint_3;
    private ConstraintLayout constraint_4;

    private EditText textBox_1;
    private EditText textBox_2;
    private EditText textBox_3;
    private EditText textBox_4;

    private ImageButton addBut;
    private ImageButton deleteBut;
    private ImageButton backBut;

    private ImageButton enterBut_1;
    private ImageButton enterBut_2;
    private ImageButton enterBut_3;
    private ImageButton enterBut_4;

    private String disableColor = "#B7C0C1";
    private String enableColor = "#000000";

    private boolean isEnableKeyBoardOnTextBoxes = false;
    private boolean isFirstGettingText = true;

    private boolean isClickAddButton = false;

    private Drawable enterEnableBackground;
    private Drawable enterDisableBackground;
    private Drawable deleteEnableBackground;
    private Drawable disableBackground;
    private Drawable clEnableBackground;
    private Drawable clDisableBackground;
    private Drawable addEnableBackground;

    private boolean isEnableBarcodeReaderMode;

    private LocalEncryptedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycled_material_creation);

        initLocalPreferences();
        initView();
        initDrawables();
        initTextWatcher();

        addBut.setFocusableInTouchMode(true);
    }

    public void initTextWatcher(){

        if(constraint_1 != null && textBox_1 != null && enterBut_1 != null && deleteBut != null){

            textBox_1.addTextChangedListener(new TextWatcher() {

                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String watchedText = textBox_1.getText().toString();

                    if(watchedText.equals("")){

                        TextWatcherHelper.setElementsToBaseState(constraint_1, textBox_1, true, enterBut_1, false, enableColor, enterDisableBackground, clDisableBackground);
                        TextWatcherHelper.changeStateButton(addBut, disableBackground, false);
                        TextWatcherHelper.changeStateButton(deleteBut, disableBackground, false);

                        isFirstGettingText = true;
                    }
                    else {
                        if(isFirstGettingText){

                            isFirstGettingText = false;

                            TextWatcherHelper.changeStateButton(enterBut_1, enterEnableBackground, true);
                            TextWatcherHelper.changeStateButton(deleteBut, deleteEnableBackground, true);

                            enterBut_1.setOnClickListener(v -> {
                                TextWatcherHelper.setElementsToFinishState(constraint_1, constraint_2, textBox_1, textBox_2, enterBut_1, disableColor, clEnableBackground, enterDisableBackground);
                            });

                            textBox_1.setOnKeyListener((v, keyCode, event) -> {
                                if(keyCode == KeyEvent.KEYCODE_ENTER){
                                    if(event.getAction() == KeyEvent.ACTION_UP){
                                        enterBut_1.callOnClick();
                                    }
                                }

                                return false;
                            });
                        }
                    }
                }

                @Override public void afterTextChanged(Editable s) {
                }
            });
        }

        if(constraint_2 != null && textBox_2 != null && enterBut_2 != null && deleteBut != null){

            textBox_2.addTextChangedListener(new TextWatcher() {

                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String watchedText = textBox_2.getText().toString();

                    if(watchedText.equals("")){

                        TextWatcherHelper.setElementsToBaseState(constraint_2, textBox_2, true, enterBut_2, false, enableColor, enterDisableBackground, clDisableBackground);
                        isFirstGettingText = true;
                    }
                    else {
                        if(isFirstGettingText){

                            isFirstGettingText = false;
                            TextWatcherHelper.changeStateButton(enterBut_2, enterEnableBackground, true);

                            enterBut_2.setOnClickListener(v -> {
                                TextWatcherHelper.setElementsToFinishState(constraint_2, constraint_3, textBox_2, textBox_3, enterBut_2, disableColor, clEnableBackground, enterDisableBackground);
                            });

                            textBox_2.setOnKeyListener((v, keyCode, event) -> {
                                if(keyCode == KeyEvent.KEYCODE_ENTER){
                                    if(event.getAction() == KeyEvent.ACTION_UP){
                                        enterBut_2.callOnClick();
                                    }
                                }

                                return false;
                            });
                        }
                    }

                }

                @Override public void afterTextChanged(Editable s) {}
            });
        }

        if(constraint_3 != null && textBox_3 != null && enterBut_3 != null && deleteBut != null){

            textBox_3.addTextChangedListener(new TextWatcher() {

                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String watchedText = textBox_3.getText().toString();

                    if(watchedText.equals("")){

                        TextWatcherHelper.setElementsToBaseState(constraint_3, textBox_3, true, enterBut_3, false, enableColor, enterDisableBackground, clDisableBackground);
                        isFirstGettingText = true;
                    }
                    else {
                        if(isFirstGettingText){

                            isFirstGettingText = false;
                            TextWatcherHelper.changeStateButton(enterBut_3, enterEnableBackground, true);

                            enterBut_3.setOnClickListener(v -> {
                                TextWatcherHelper.setElementsToFinishState(constraint_3, constraint_4, textBox_3, textBox_4, enterBut_3, disableColor, clEnableBackground, enterDisableBackground);
                            });

                            textBox_3.setOnKeyListener((v, keyCode, event) -> {
                                if(keyCode == KeyEvent.KEYCODE_ENTER){
                                    if(event.getAction() == KeyEvent.ACTION_UP){
                                        enterBut_3.callOnClick();
                                    }
                                }

                                return false;
                            });
                        }
                    }

                }

                @Override public void afterTextChanged(Editable s) {}
            });
        }

        if(constraint_4 != null && textBox_4 != null && enterBut_4 != null && addBut != null && deleteBut != null){

            textBox_4.addTextChangedListener(new TextWatcher() {

                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String watchedText = textBox_4.getText().toString();

                    if(watchedText.equals("")){

                        TextWatcherHelper.setElementsToBaseState(
                                constraint_4, textBox_4, true, enterBut_4,
                                false, enableColor, enterDisableBackground, clDisableBackground
                        );

                        isFirstGettingText = true;
                    }
                    else {
                        if(isFirstGettingText){

                            isFirstGettingText = false;
                            TextWatcherHelper.changeStateButton(enterBut_4, enterEnableBackground, true);

                            enterBut_4.setOnClickListener(v -> {
                                TextWatcherHelper.setLastElementsToFinishState(constraint_4, textBox_4, enterBut_4, addBut,
                                        disableColor, clEnableBackground, enterDisableBackground, addEnableBackground
                                );

                                isClickAddButton = true;
                            });


                            textBox_4.setOnKeyListener((v, keyCode, event) -> {
                                if(keyCode == KeyEvent.KEYCODE_ENTER){
                                    if(event.getAction() == KeyEvent.ACTION_UP){
                                        enterBut_4.callOnClick();
                                    }
                                }

                                return false;
                            });

                            addBut.setOnFocusChangeListener((view, hasFocus) -> {
                                if(hasFocus){
                                    addBut.setOnClickListener(v -> {
                                        Intent intent = new Intent();

                                        intent.putExtra("typeRecMatTextBox", textBox_1.getText().toString());
                                        intent.putExtra("storageBoxIdentifierTextBox2", textBox_2.getText().toString());
                                        intent.putExtra("massDataTextBox2", textBox_3.getText().toString());
                                        intent.putExtra("commentDataTextBox2", textBox_4.getText().toString());

                                        setResult(1, intent);

                                        RecycledMaterialCreationView.super.onBackPressed();
                                    });
                                }
                            });
                        }
                    }
                }

                @Override public void afterTextChanged(Editable s) {}
            });
        }

        if(deleteBut != null){
            deleteBut.setOnClickListener(v -> {
                textBox_1.setText("");
                textBox_2.setText("");
                textBox_3.setText("");
                textBox_4.setText("");

                constraint_2.setVisibility(View.INVISIBLE);
                constraint_3.setVisibility(View.INVISIBLE);
                constraint_4.setVisibility(View.INVISIBLE);

                isClickAddButton = false;

                if(!textBox_1.isEnabled()) textBox_1.setEnabled(true);
                if(!textBox_1.isFocused()) textBox_1.requestFocus();

                if(addBut.isEnabled()) addBut.setEnabled(false);
                if(deleteBut.isEnabled()) deleteBut.setEnabled(false);
            });
        }

        if(backBut != null){
            backBut.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), ModesThree.class);
                startActivity(intent);
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(isEnableBarcodeReaderMode){
            if(keyCode == KeyEvent.KEYCODE_BUTTON_R1 || keyCode == KeyEvent.KEYCODE_BUTTON_L1){
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    if(isClickAddButton) addBut.callOnClick();
                }
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    public void initView(){

        constraint_1 = findViewById(R.id.typeRecMatCL);
        constraint_2 = findViewById(R.id.storageBoxIdentifierCL2);
        constraint_3 = findViewById(R.id.massDataCL2);
        constraint_4 = findViewById(R.id.commentDataCL2);

        textBox_1 = findViewById(R.id.typeRecMatTextBox);
        textBox_2 = findViewById(R.id.storageBoxIdentifierTextBox2);
        textBox_3 = findViewById(R.id.massDataTextBox2);
        textBox_4 = findViewById(R.id.commentDataTextBox2);

        textBox_1.requestFocus();

        if(preferences != null){
            isEnableBarcodeReaderMode = preferences.getBooleanValueByKey("IsEnableBarcodeReaderMode");
            isEnableKeyBoardOnTextBoxes = preferences.getBooleanValueByKey("IsEnableKeyBoardOnTextBoxes");
        }

        if(isEnableKeyBoardOnTextBoxes){
            if(textBox_1 != null){ textBox_1.setShowSoftInputOnFocus(true); }
            if(textBox_2 != null){ textBox_2.setShowSoftInputOnFocus(true); }
            if(textBox_3 != null){ textBox_3.setShowSoftInputOnFocus(true); }
            if(textBox_4 != null){ textBox_4.setShowSoftInputOnFocus(true); }
        }
        else {
            if(textBox_1 != null){ textBox_1.setShowSoftInputOnFocus(false); }
            if(textBox_2 != null){ textBox_2.setShowSoftInputOnFocus(false); }
            if(textBox_3 != null){ textBox_3.setShowSoftInputOnFocus(false); }
            if(textBox_4 != null){ textBox_4.setShowSoftInputOnFocus(false); }
        }

        addBut = findViewById(R.id.addRecMatButton);
        deleteBut = findViewById(R.id.deleteRecMatButton);
        backBut = findViewById(R.id.backRecMatButton);

        addBut.setEnabled(false);
        deleteBut.setEnabled(false);

        enterBut_1 = findViewById(R.id.enterButton_rec_1);
        enterBut_2 = findViewById(R.id.enterButton_rec_2);
        enterBut_3 = findViewById(R.id.enterButton_rec_3);
        enterBut_4 = findViewById(R.id.enterButton_rec_4);
    }

    private void initDrawables() {
        enterEnableBackground = ContextCompat.getDrawable(getApplicationContext(), R.drawable.enter_button_background);
        enterDisableBackground = ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle);
        deleteEnableBackground = ContextCompat.getDrawable(getApplicationContext(), R.drawable.delete_button_background);
        clEnableBackground = ContextCompat.getDrawable(getApplicationContext(), R.drawable.constraint_green_background);
        clDisableBackground = ContextCompat.getDrawable(getApplicationContext(), R.drawable.constraint_red_background);
        addEnableBackground = ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_button_background);
        disableBackground = ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_circle);
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
}