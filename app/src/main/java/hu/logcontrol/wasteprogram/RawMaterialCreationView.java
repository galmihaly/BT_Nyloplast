package hu.logcontrol.wasteprogram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.helpers.JSONFileHelper;

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

    private boolean isEnableBarcodeReaderMode = false;
    private boolean isEnableKeyBoardOnTextBoxes = false;
    private boolean isFirstGettingText = true;

    private boolean isClickAddButton = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_material_creation);
        initView();
        initTextWatcher();

        addBut.setFocusableInTouchMode(true);

        isEnableBarcodeReaderMode = JSONFileHelper.getBoolean(getApplicationContext(), "values.json", "IsEnableBarcodeReaderMode");
    }

    private void initTextWatcher() {
        if(constraint_1 != null && textBox_1 != null && enterBut_1 != null && deleteBut != null){

            textBox_1.addTextChangedListener(new TextWatcher() {

                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String watchedText = textBox_1.getText().toString();

                    if(watchedText.equals("")){

                        enterBut_1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));
                        textBox_1.setTextColor(Color.parseColor(enableColor));
                        constraint_1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_red_background));

                        textBox_2.setTextColor(Color.parseColor(enableColor));
                        constraint_2.setVisibility(View.INVISIBLE);

                        textBox_3.setTextColor(Color.parseColor(enableColor));
                        constraint_3.setVisibility(View.INVISIBLE);

                        addBut.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_circle));
                        deleteBut.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_circle));

                        if(enterBut_1.isEnabled()) enterBut_1.setEnabled(false);
                        if(!textBox_1.isEnabled()) textBox_1.setEnabled(true);
                        if(!textBox_1.isFocused()) textBox_1.requestFocus();

                        isFirstGettingText = true;
                    }
                    else {
                        if(isFirstGettingText){

                            isFirstGettingText = false;

                            enterBut_1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.enter_button_background));
                            deleteBut.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.delete_button_background));

                            if(!enterBut_1.isEnabled())enterBut_1.setEnabled(true);
                            if(!deleteBut.isEnabled())deleteBut.setEnabled(true);

                            enterBut_1.setOnClickListener(v -> {
                                setStateFirstEdittext();
                            });

                            textBox_1.setOnKeyListener((v, keyCode, event) -> {
                                if(keyCode == KeyEvent.KEYCODE_ENTER){
                                    if(event.getAction() == KeyEvent.ACTION_UP){
                                        setStateFirstEdittext();
                                    }
                                }

                                if(isEnableBarcodeReaderMode){
                                    if(keyCode == KeyEvent.KEYCODE_BUTTON_R1 || keyCode == KeyEvent.KEYCODE_BUTTON_L1){
                                        if(event.getAction() == KeyEvent.ACTION_UP){
                                            setStateFirstEdittext();
                                        }
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

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String watchedText = textBox_2.getText().toString();

                    if(watchedText.equals("")){

                        enterBut_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));
                        constraint_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_red_background));
                        textBox_2.setTextColor(Color.parseColor(enableColor));

                        if(enterBut_2.isEnabled()) enterBut_2.setEnabled(false);
                        if(!textBox_2.isEnabled())textBox_2.setEnabled(true);
                        if(!textBox_2.isFocused()) textBox_2.requestFocus();

                        isFirstGettingText = true;
                    }
                    else {
                        if(isFirstGettingText){

                            isFirstGettingText = false;

                            enterBut_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.enter_button_background));
                            deleteBut.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.delete_button_background));

                            if(!enterBut_2.isEnabled())enterBut_2.setEnabled(true);
                            if(!deleteBut.isEnabled())deleteBut.setEnabled(true);

                            enterBut_2.setOnClickListener(v -> {
                                setStateSecondEdittext();
                            });

                            textBox_2.setOnKeyListener((v, keyCode, event) -> {
                                if(keyCode == KeyEvent.KEYCODE_ENTER){
                                    if(event.getAction() == KeyEvent.ACTION_UP){
                                        setStateSecondEdittext();
                                    }
                                }

                                if(isEnableBarcodeReaderMode){
                                    if(keyCode == KeyEvent.KEYCODE_BUTTON_R1 || keyCode == KeyEvent.KEYCODE_BUTTON_L1){
                                        if(event.getAction() == KeyEvent.ACTION_UP){
                                            setStateSecondEdittext();
                                        }
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

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String watchedText = textBox_3.getText().toString();

                    if(watchedText.equals("")){

                        enterBut_3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));
                        constraint_3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_red_background));
                        textBox_3.setTextColor(Color.parseColor(enableColor));

                        if(enterBut_3.isEnabled()) enterBut_3.setEnabled(false);
                        if(!textBox_3.isEnabled())textBox_3.setEnabled(true);
                        if(!textBox_3.isFocused()) textBox_3.requestFocus();

                        isFirstGettingText = true;
                    }
                    else {
                        if(isFirstGettingText){

                            isFirstGettingText = false;

                            enterBut_3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.enter_button_background));
                            if(!enterBut_3.isEnabled()) enterBut_3.setEnabled(true);

                            enterBut_3.setOnClickListener(v -> {
                                setStateThirdEdittext();
                                isClickAddButton = true;
                            });

                            textBox_3.setOnKeyListener((v, keyCode, event) -> {
                                if(keyCode == KeyEvent.KEYCODE_ENTER){
                                    if(event.getAction() == KeyEvent.ACTION_UP){
                                        setStateThirdEdittext();
                                        isClickAddButton = true;
                                    }
                                }

                                if(isEnableBarcodeReaderMode){
                                    if(keyCode == KeyEvent.KEYCODE_BUTTON_R1 || keyCode == KeyEvent.KEYCODE_BUTTON_L1){
                                        if(event.getAction() == KeyEvent.ACTION_UP){
                                            if(!isClickAddButton){
                                                setStateThirdEdittext();
                                                isClickAddButton = true;
                                            }
                                        }
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

        if(keyCode == KeyEvent.KEYCODE_BUTTON_L1){
            if(event.getAction() == KeyEvent.ACTION_DOWN){
                if(isClickAddButton) addBut.callOnClick();
            }
        }

        return super.onKeyUp(keyCode, event);
    }

    private void setStateFirstEdittext(){
        if(!textBox_1.getText().toString().equals("")){
            constraint_2.setVisibility(View.VISIBLE);
            constraint_1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));
            textBox_1.setTextColor(Color.parseColor(disableColor));

            enterBut_1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));
            textBox_2.setText("");

            if(textBox_1.isEnabled()) textBox_1.setEnabled(false);
            if(enterBut_1.isEnabled()) enterBut_1.setEnabled(false);

            if(!textBox_2.isFocused()) textBox_2.requestFocus();
        }
    }

    private void setStateSecondEdittext(){
        if(!textBox_2.getText().toString().equals("")){
            constraint_3.setVisibility(View.VISIBLE);
            constraint_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));
            textBox_2.setTextColor(Color.parseColor(disableColor));

            enterBut_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));
            textBox_3.setText("");

            if(textBox_2.isEnabled()) textBox_2.setEnabled(false);
            if(enterBut_2.isEnabled()) enterBut_2.setEnabled(false);

            if(!textBox_3.isFocused()) textBox_3.requestFocus();
        }
    }

    private void setStateThirdEdittext() {
        if(!textBox_3.getText().toString().equals("")){
            constraint_3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));
            enterBut_3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));
            textBox_3.setTextColor(Color.parseColor(disableColor));

            addBut.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_button_background));

            if(!addBut.isEnabled()) addBut.setEnabled(true);
            if(textBox_3.isFocused()) textBox_3.clearFocus();

            if(textBox_3.isEnabled()) textBox_3.setEnabled(false);
            if(enterBut_3.isEnabled()) enterBut_3.setEnabled(false);

            isClickAddButton = true;
            addBut.requestFocus(View.FOCUS_DOWN);
        }
    }

    private void initView() {
        constraint_1 = findViewById(R.id.countRMC_CL);
        constraint_2 = findViewById(R.id.typeRMC_CL);
        constraint_3 = findViewById(R.id.commentRMC_CL);

        textBox_1 = findViewById(R.id.countRMC_TB);
        textBox_2 = findViewById(R.id.typeRMC_TB);
        textBox_3 = findViewById(R.id.commentRMC_TB);

        isEnableKeyBoardOnTextBoxes = JSONFileHelper.getBoolean(getApplicationContext(), "values.json", "IsEnableKeyBoardOnTextBoxes");

        if(isEnableKeyBoardOnTextBoxes){
            if(textBox_1 != null){ textBox_1.setShowSoftInputOnFocus(false); }
            if(textBox_2 != null){ textBox_2.setShowSoftInputOnFocus(false); }
            if(textBox_3 != null){ textBox_3.setShowSoftInputOnFocus(false); }
        }

        textBox_1.requestFocus();

        addBut = findViewById(R.id.addRMCBut);
        deleteBut = findViewById(R.id.deleteRMCBut);
        backBut = findViewById(R.id.backRMCBut);

        addBut.setEnabled(false);
        deleteBut.setEnabled(false);

        enterBut_1 = findViewById(R.id.enterCountRMC_BUT);
        enterBut_2 = findViewById(R.id.enterTypeRMC_BUT);
        enterBut_3 = findViewById(R.id.enterCommentRMC_BUT);
    }
}