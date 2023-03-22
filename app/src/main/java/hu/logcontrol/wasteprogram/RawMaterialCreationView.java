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

public class RawMaterialCreationView extends AppCompatActivity {

    private ConstraintLayout constraint_1;
    private ConstraintLayout constraint_2;

    private EditText textBox_1;
    private EditText textBox_2;

    private ImageButton addBut;
    private ImageButton deleteBut;
    private ImageButton backBut;

    private ImageButton enterBut_1;
    private ImageButton enterBut_2;

    private final String disableColor = "#B7C0C1";
    private final String enableColor = "#000000";

    private boolean isSeged_2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_material_creation);
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
                        addBut.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_circle));
                        deleteBut.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_circle));

                        if(enterBut_1.isEnabled()) enterBut_1.setEnabled(false);
                        if(!textBox_1.isEnabled()) textBox_1.setEnabled(true);
                        if(!textBox_1.isFocused()) textBox_1.requestFocus();
                        Log.e("vvv", "bbb");
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

                        Log.e("keyCode", String.valueOf(keyCode));

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

        if(constraint_2 != null && textBox_2 != null && enterBut_2 != null && addBut != null && deleteBut != null){

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
                    }
                    else {
                        enterBut_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.enter_button_background));
                        if(!enterBut_2.isEnabled()) enterBut_2.setEnabled(true);

                        enterBut_2.setOnClickListener(v -> {
                            constraint_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));
                            enterBut_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));
                            textBox_2.setTextColor(Color.parseColor(disableColor));

                            addBut.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_button_background));

                            isSeged_2 = true;
                            if(!addBut.isEnabled()) addBut.setEnabled(true);
                            if(textBox_2.isFocused()) textBox_2.clearFocus();
                            if(addBut.isFocused()) addBut.requestFocus();
                        });

                        textBox_2.setOnFocusChangeListener((v, hasFocus) -> {
                            if(!textBox_2.getText().toString().equals("")){
                                if(isSeged_2){
                                    if(textBox_2.isEnabled()) textBox_2.setEnabled(false);
                                    if(enterBut_2.isEnabled()) enterBut_2.setEnabled(false);
                                }
                            }
                        });

                        textBox_2.setOnKeyListener((v, keyCode, event) -> {
                            if(keyCode == KeyEvent.KEYCODE_ENTER){
                                if(event.getAction() == KeyEvent.ACTION_UP){

                                    if(!textBox_2.getText().toString().equals("")){
                                        constraint_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));
                                        enterBut_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));
                                        textBox_2.setTextColor(Color.parseColor(disableColor));

                                        addBut.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_button_background));

                                        isSeged_2 = true;
                                        if(!addBut.isEnabled()) addBut.setEnabled(true);
                                        if(textBox_2.isFocused()) textBox_2.clearFocus();
                                    }
                                }
                            }

                            return false;
                        });

                        addBut.setOnClickListener(v -> {
                            Intent intent = new Intent();

                            intent.putExtra("rawMatCountTextBox", textBox_1.getText().toString());
                            intent.putExtra("rawMatTypeTextBox", textBox_2.getText().toString());

                            setResult(1, intent);

                            RawMaterialCreationView.super.onBackPressed();
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

    private void initView() {
        textBox_1 = findViewById(R.id.countRMC_TB);
        if(textBox_1 != null){ textBox_1.setShowSoftInputOnFocus(false); }

        textBox_2 = findViewById(R.id.typeRMC_TB);
        if(textBox_2 != null){ textBox_2.setShowSoftInputOnFocus(false); }

        textBox_1.requestFocus();

        constraint_1 = findViewById(R.id.countRMC_CL);
        constraint_2 = findViewById(R.id.typeRMC_CL);

        addBut = findViewById(R.id.addRMCBut);
        addBut.setEnabled(false);

        deleteBut = findViewById(R.id.deleteRMCBut);
        deleteBut.setEnabled(false);

        backBut = findViewById(R.id.backRMCBut);

        enterBut_1 = findViewById(R.id.enterCountRMC_BUT);
        enterBut_2 = findViewById(R.id.enterTypeRMC_BUT);

        hideNavigationBar();
    }

    private void hideNavigationBar(){
        Helper.hideNavigationBar(this);
    }
}