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
import android.widget.ScrollView;

import hu.logcontrol.wasteprogram.helpers.Helper;

public class RawMaterialTypeMassCreationView extends AppCompatActivity {

    private ScrollView massDataScrollView;

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

    private boolean isSeged_2 = false;
    private boolean isSeged_3 = false;
    private boolean isSeged_4 = false;

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

                        textBox_4.setTextColor(Color.parseColor(enableColor));
                        constraint_4.setVisibility(View.INVISIBLE);

                        addBut.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_circle));
                        deleteBut.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_circle));

                        if(enterBut_1.isEnabled()) enterBut_1.setEnabled(false);
                        if(!textBox_1.isEnabled()) textBox_1.setEnabled(true);
                        if(!textBox_1.isFocused()) textBox_1.requestFocus();
                    }
                    else {
                        enterBut_1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.enter_button_background));
                        deleteBut.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.delete_button_background));

                        if(!enterBut_1.isEnabled())enterBut_1.setEnabled(true);
                        if(!deleteBut.isEnabled())deleteBut.setEnabled(true);
                    }

                    enterBut_1.setOnClickListener(v -> {

                        constraint_2.setVisibility(View.VISIBLE);
                        constraint_1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));

                        textBox_1.setTextColor(Color.parseColor(disableColor));

                        enterBut_1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));

                        textBox_2.setText("");
                    });

                    textBox_1.setOnFocusChangeListener((v, hasFocus) -> {
                        if(!textBox_1.getText().toString().equals("")){
                            textBox_1.setEnabled(false);
                            enterBut_1.setEnabled(false);

                            isSeged_2 = false;
                        }
                    });

                    textBox_1.setOnKeyListener((v, keyCode, event) -> {

                        if(keyCode == KeyEvent.KEYCODE_ENTER){
                            if(event.getAction() == KeyEvent.ACTION_UP){

                                if(!textBox_1.getText().toString().equals("")){
                                    constraint_2.setVisibility(View.VISIBLE);
                                    constraint_1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));

                                    textBox_1.setTextColor(Color.parseColor(disableColor));

                                    enterBut_1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));

                                    textBox_2.setText("");
                                }
                            }
                        }

                        return false;
                    });
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

                        enterBut_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));
                        textBox_2.setTextColor(Color.parseColor(enableColor));
                        constraint_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_red_background));

                        if(enterBut_2.isEnabled()) enterBut_2.setEnabled(false);
                        if(!textBox_2.isEnabled()) textBox_2.setEnabled(true);
                        if(!textBox_2.isFocused()) textBox_2.requestFocus();
                        Log.e("vvv", "bbb");
                    }
                    else {
                        enterBut_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.enter_button_background));
                        deleteBut.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.delete_button_background));

                        if(!enterBut_2.isEnabled())enterBut_2.setEnabled(true);
                        if(!deleteBut.isEnabled())deleteBut.setEnabled(true);
                    }

                    enterBut_2.setOnClickListener(v -> {

                        constraint_3.setVisibility(View.VISIBLE);
                        constraint_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));

                        textBox_2.setTextColor(Color.parseColor(disableColor));

                        enterBut_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));

                        textBox_3.setText("");
                    });

                    textBox_2.setOnFocusChangeListener((v, hasFocus) -> {
                        if(!textBox_2.getText().toString().equals("")){
                            textBox_2.setEnabled(false);
                            enterBut_2.setEnabled(false);

                            isSeged_3 = false;
                        }
                    });

                    textBox_2.setOnKeyListener((v, keyCode, event) -> {

                        if(keyCode == KeyEvent.KEYCODE_ENTER){
                            if(event.getAction() == KeyEvent.ACTION_UP){

                                if(!textBox_2.getText().toString().equals("")){
                                    constraint_3.setVisibility(View.VISIBLE);
                                    constraint_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));

                                    textBox_2.setTextColor(Color.parseColor(disableColor));

                                    enterBut_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));

                                    textBox_3.setText("");
                                }
                            }
                        }

                        return false;
                    });
                }

                @Override public void afterTextChanged(Editable s) {
                }
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

                        enterBut_3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));
                        textBox_3.setTextColor(Color.parseColor(enableColor));
                        constraint_3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_red_background));

                        if(enterBut_3.isEnabled()) enterBut_3.setEnabled(false);
                        if(!textBox_3.isEnabled()) textBox_3.setEnabled(true);
                        if(!textBox_3.isFocused()) textBox_3.requestFocus();
                        Log.e("vvv", "bbb");
                    }
                    else {
                        enterBut_3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.enter_button_background));
                        deleteBut.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.delete_button_background));

                        if(!enterBut_3.isEnabled())enterBut_3.setEnabled(true);
                        if(!deleteBut.isEnabled())deleteBut.setEnabled(true);
                    }

                    enterBut_3.setOnClickListener(v -> {

                        constraint_4.setVisibility(View.VISIBLE);
                        constraint_3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));

                        textBox_3.setTextColor(Color.parseColor(disableColor));

                        enterBut_3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));

                        textBox_4.setText("");
                    });

                    textBox_3.setOnFocusChangeListener((v, hasFocus) -> {
                        if(!textBox_3.getText().toString().equals("")){
                            textBox_3.setEnabled(false);
                            enterBut_3.setEnabled(false);

                            isSeged_4 = false;
                        }
                    });

                    textBox_3.setOnKeyListener((v, keyCode, event) -> {

                        if(keyCode == KeyEvent.KEYCODE_ENTER){
                            if(event.getAction() == KeyEvent.ACTION_UP){

                                if(!textBox_3.getText().toString().equals("")){
                                    constraint_4.setVisibility(View.VISIBLE);
                                    constraint_3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));

                                    textBox_3.setTextColor(Color.parseColor(disableColor));

                                    enterBut_3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));

                                    textBox_4.setText("");
                                }
                            }
                        }

                        return false;
                    });
                }

                @Override public void afterTextChanged(Editable s) {
                }
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

                        enterBut_4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));
                        constraint_4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_red_background));
                        textBox_4.setTextColor(Color.parseColor(enableColor));

                        if(enterBut_4.isEnabled()) enterBut_4.setEnabled(false);
                        if(!textBox_4.isEnabled())textBox_4.setEnabled(true);
                        if(!textBox_4.isFocused()) textBox_4.requestFocus();
                    }
                    else {
                        enterBut_4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.enter_button_background));
                        if(!enterBut_4.isEnabled()) enterBut_4.setEnabled(true);

                        enterBut_4.setOnClickListener(v -> {
                            constraint_4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));
                            enterBut_4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));
                            textBox_4.setTextColor(Color.parseColor(disableColor));

                            addBut.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_button_background));

                            isSeged_4 = true;
                            if(!addBut.isEnabled()) addBut.setEnabled(true);
                            if(textBox_4.isFocused()) textBox_4.clearFocus();
                            if(!addBut.isFocused()) addBut.requestFocus();
                        });

                        textBox_4.setOnFocusChangeListener((v, hasFocus) -> {
                            if(!textBox_4.getText().toString().equals("")){
                                if(isSeged_4){
                                    if(textBox_4.isEnabled()) textBox_4.setEnabled(false);
                                    if(enterBut_4.isEnabled()) enterBut_4.setEnabled(false);
                                }
                            }
                        });

                        textBox_4.setOnKeyListener((v, keyCode, event) -> {
                            if(keyCode == KeyEvent.KEYCODE_ENTER){
                                if(event.getAction() == KeyEvent.ACTION_UP){

                                    if(!textBox_4.getText().toString().equals("")){
                                        constraint_4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));
                                        enterBut_4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));
                                        textBox_4.setTextColor(Color.parseColor(disableColor));

                                        addBut.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_button_background));

                                        isSeged_4 = true;
                                        if(!addBut.isEnabled()) addBut.setEnabled(true);
                                        if(textBox_4.isFocused()) textBox_4.clearFocus();
                                        if(!addBut.isFocused()) addBut.requestFocus();
                                    }
                                }
                            }

                            return false;
                        });

                        addBut.setOnClickListener(v -> {
                            Intent intent = new Intent();

                            intent.putExtra("typeMassCountTextBox", textBox_1.getText().toString());
                            intent.putExtra("typeMassTypeTextBox", textBox_2.getText().toString());
                            intent.putExtra("storageBoxIdentifierTextBox", textBox_3.getText().toString());
                            intent.putExtra("massDataTextBox", textBox_4.getText().toString());

                            setResult(1, intent);

                            RawMaterialTypeMassCreationView.super.onBackPressed();
                        });
                    }
                }

                @Override public void afterTextChanged(Editable s) {
                }
            });
        }

        if(deleteBut != null){
            deleteBut.setOnClickListener(v -> {
                textBox_1.setText("");
                isSeged_2 = false;
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
        constraint_1 = findViewById(R.id.typeMassCountCL);
        constraint_2 = findViewById(R.id.typeMassTypeCL);
        constraint_3 = findViewById(R.id.storageBoxIdentifierCL);
        constraint_4 = findViewById(R.id.massDataCL);

        textBox_1 = findViewById(R.id.typeMassCountTextBox);
        textBox_2 = findViewById(R.id.typeMassTypeTextBox);
        textBox_3 = findViewById(R.id.storageBoxIdentifierTextBox);
        textBox_4 = findViewById(R.id.massDataTextBox);

        if(textBox_1 != null){ textBox_1.setShowSoftInputOnFocus(false); }
        if(textBox_2 != null){ textBox_2.setShowSoftInputOnFocus(false); }
        if(textBox_3 != null){ textBox_3.setShowSoftInputOnFocus(false); }
        if(textBox_4 != null){ textBox_4.setShowSoftInputOnFocus(false); }

        addBut = findViewById(R.id.addRMTMC_BUT);
        deleteBut = findViewById(R.id.deleteRMTMC_BUT);
        backBut = findViewById(R.id.backRMTMC_BUT);

        addBut.setEnabled(false);
        deleteBut.setEnabled(false);

        enterBut_1 = findViewById(R.id.enterButton_type_1);
        enterBut_2 = findViewById(R.id.enterButton_type_2);
        enterBut_3 = findViewById(R.id.enterButton_type_3);
        enterBut_4 = findViewById(R.id.enterButton_type_4);

        //textLayout_typeMass_1 = findViewById(R.id.textLayout_typeMass_1);

        hideNavigationBar();
    }

    private void hideNavigationBar(){
        Helper.hideNavigationBar(this);
    }
}