package hu.logcontrol.wasteprogram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
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

public class RawMaterialCreationView extends AppCompatActivity {

    private ConstraintLayout constraint_1;
    private ConstraintLayout constraint_2;
    private ConstraintLayout constraint_3;

    private EditText textBox_1;
    private EditText textBox_2;
    private EditText textBox_3;

    private ImageButton addBut;
    private ImageButton deleteBut;
    private ImageButton backBut;

    private ImageButton enterBut_1;
    private ImageButton enterBut_2;
    private ImageButton enterBut_3;

    private final String disableColor = "#B7C0C1";
    private final String enableColor = "#000000";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_material_creation);
        initView();
        initDrawables();
        initTextWatcher();

        addBut.setFocusableInTouchMode(true);
    }

    private void initTextWatcher() {


        if(constraint_1 != null && textBox_1 != null && enterBut_1 != null && deleteBut != null){

            textBox_1.addTextChangedListener(new TextWatcher() {

                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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

                @Override public void afterTextChanged(Editable s) {}
            });
        }

        if(constraint_2 != null && textBox_2 != null && enterBut_2 != null && deleteBut != null){

            textBox_2.addTextChangedListener(new TextWatcher() {

                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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

        if(constraint_3 != null && textBox_3 != null && enterBut_3 != null && addBut != null && deleteBut != null){

            textBox_3.addTextChangedListener(new TextWatcher() {

                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String watchedText = textBox_3.getText().toString();

                    if(watchedText.equals("")){

                        TextWatcherHelper.setElementsToBaseState(
                                constraint_3, textBox_3, true, enterBut_3,
                                false, enableColor, enterDisableBackground, clDisableBackground
                        );

                        isFirstGettingText = true;
                    }
                    else {
                        if(isFirstGettingText){

                            isFirstGettingText = false;
                            TextWatcherHelper.changeStateButton(enterBut_3, enterEnableBackground, true);

                            enterBut_3.setOnClickListener(v -> {

                                TextWatcherHelper.setLastElementsToFinishState(constraint_3, textBox_3, enterBut_3, addBut,
                                        disableColor, clEnableBackground, enterDisableBackground, addEnableBackground
                                );

                                isClickAddButton = true;
                            });

                            textBox_3.setOnKeyListener((v, keyCode, event) -> {
                                if(keyCode == KeyEvent.KEYCODE_ENTER){
                                    if(event.getAction() == KeyEvent.ACTION_UP){
                                        enterBut_3.callOnClick();
                                    }
                                }

                                return false;
                            });

                            addBut.setOnFocusChangeListener((view, hasFocus) -> {
                                if(hasFocus){
                                    addBut.setOnClickListener(v -> {
                                        Intent intent = new Intent();

                                        intent.putExtra("rawMatCountTextBox", textBox_1.getText().toString());
                                        intent.putExtra("rawMatTypeTextBox", textBox_2.getText().toString());
                                        intent.putExtra("rawMatContentTextBox", textBox_3.getText().toString());

                                        setResult(1, intent);

                                        RawMaterialCreationView.super.onBackPressed();
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
                isClickAddButton = false;
                textBox_1.setText("");
                textBox_2.setText("");
                textBox_3.setText("");

                isClickAddButton = false;

                constraint_2.setVisibility(View.INVISIBLE);
                constraint_3.setVisibility(View.INVISIBLE);

                if(!textBox_1.isEnabled()) textBox_1.setEnabled(true);
                if(!textBox_1.isFocused()) textBox_1.requestFocus();

                if(addBut.isEnabled()) addBut.setEnabled(false);
                if(deleteBut.isEnabled()) deleteBut.setEnabled(false);
            });
        }

        if(backBut != null){
            backBut.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), ModesOne.class);
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

    private void initView() {
        constraint_1 = findViewById(R.id.countRMC_CL);
        constraint_2 = findViewById(R.id.typeRMC_CL);
        constraint_3 = findViewById(R.id.commentRMC_CL);

        textBox_1 = findViewById(R.id.countRMC_TB);
        textBox_2 = findViewById(R.id.typeRMC_TB);
        textBox_3 = findViewById(R.id.commentRMC_TB);

        textBox_1.requestFocus();

        isEnableBarcodeReaderMode = JSONFileHelper.getBoolean(getApplicationContext(), "values.json", "IsEnableBarcodeReaderMode");
        isEnableKeyBoardOnTextBoxes = JSONFileHelper.getBoolean(getApplicationContext(), "values.json", "IsEnableKeyBoardOnTextBoxes");

        if(isEnableKeyBoardOnTextBoxes){
            if(textBox_1 != null){ textBox_1.setShowSoftInputOnFocus(true); }
            if(textBox_2 != null){ textBox_2.setShowSoftInputOnFocus(true); }
            if(textBox_3 != null){ textBox_3.setShowSoftInputOnFocus(true); }
        }
        else {
            if(textBox_1 != null){ textBox_1.setShowSoftInputOnFocus(false); }
            if(textBox_2 != null){ textBox_2.setShowSoftInputOnFocus(false); }
            if(textBox_3 != null){ textBox_3.setShowSoftInputOnFocus(false); }
        }

        addBut = findViewById(R.id.addRMCBut);
        deleteBut = findViewById(R.id.deleteRMCBut);
        backBut = findViewById(R.id.backRMCBut);

        addBut.setEnabled(false);
        deleteBut.setEnabled(false);

        enterBut_1 = findViewById(R.id.enterCountRMC_BUT);
        enterBut_2 = findViewById(R.id.enterTypeRMC_BUT);
        enterBut_3 = findViewById(R.id.enterCommentRMC_BUT);
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
}