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

import com.google.android.material.textfield.TextInputLayout;

import hu.logcontrol.wasteprogram.helpers.Helper;

public class RawMaterialCreationActivity extends AppCompatActivity {

    private ConstraintLayout rawMatCountCL;
    private ConstraintLayout rawMatTypeCL;

    private EditText rawMatCountTextBox;
    private EditText rawMatTypeTextBox;

    private ImageButton addRawMatButton;
    private ImageButton deleteRawButton;
    private ImageButton backRawButton;

    private ImageButton enterButton_base_1;
    private ImageButton enterButton_base_2;

    private TextInputLayout textLayout_base_1;

    private final String disableColor = "#B7C0C1";
    private final String enableColor = "#000000";

    private boolean isSeged_1 = false;
    private boolean isSeged_2 = false;

    private boolean isFocus_1 = false;
    private boolean isFocus_2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_material_creation);
        initView();
     }


    @Override
    protected void onResume() {
        super.onResume();

        if(rawMatCountCL != null && rawMatCountTextBox != null && enterButton_base_1 != null && deleteRawButton != null){

            rawMatCountTextBox.addTextChangedListener(new TextWatcher() {

                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String watchedText = rawMatCountTextBox.getText().toString();

                    if(watchedText.equals("")){

                        enterButton_base_1.setEnabled(false);
                        enterButton_base_1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));

                        rawMatCountTextBox.requestFocus();
                        rawMatCountTextBox.setEnabled(true);
                        rawMatCountTextBox.setTextColor(Color.parseColor(enableColor));
                        rawMatTypeTextBox.setTextColor(Color.parseColor(enableColor));

                        rawMatTypeCL.setVisibility(View.INVISIBLE);
                        rawMatCountCL.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_red_background));

                        addRawMatButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_circle));
                        deleteRawButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_circle));
                    }
                    else {
                        enterButton_base_1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.enter_button_background));
                        enterButton_base_1.setEnabled(true);
                        deleteRawButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.delete_button_background));
                        deleteRawButton.setEnabled(true);

                        enterButton_base_1.setOnClickListener(v -> {
                            if(rawMatTypeTextBox != null){

                                rawMatTypeCL.setVisibility(View.VISIBLE);
                                rawMatCountCL.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));
                                enterButton_base_1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));

                                rawMatCountTextBox.setTextColor(Color.parseColor(disableColor));

                                isSeged_1 = true;
                                isFocus_1 = true;
                                rawMatTypeTextBox.setText("");
                            }
                        });

                        rawMatCountTextBox.setOnFocusChangeListener((v, hasFocus) -> {
                            if(isSeged_1){
                                Log.e("setOnFocusChangeListener", "beléptem ide");
                                rawMatCountTextBox.setEnabled(false);
                                enterButton_base_1.setEnabled(false);

                                enterButton_base_2.setEnabled(true);
                                rawMatTypeTextBox.setEnabled(true);
                            }
                        });

                        rawMatCountTextBox.setOnKeyListener((v, keyCode, event) -> {

                            if(keyCode == KeyEvent.KEYCODE_ENTER){
                                if(rawMatTypeTextBox != null){

                                    rawMatTypeCL.setVisibility(View.VISIBLE);
                                    rawMatCountCL.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));
                                    enterButton_base_1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));

                                    rawMatCountTextBox.setTextColor(Color.parseColor(disableColor));

                                    isSeged_1 = true;
                                }
                            }

                            return false;
                        });


                    }
                }

                @Override public void afterTextChanged(Editable s) {}
            });
        }

        if(rawMatTypeCL != null && rawMatTypeTextBox != null && enterButton_base_2 != null && addRawMatButton != null && deleteRawButton != null){

            rawMatTypeTextBox.addTextChangedListener(new TextWatcher() {

                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String watchedText = rawMatTypeTextBox.getText().toString();

                    if(watchedText.equals("")){

                        enterButton_base_2.setEnabled(false);
                        rawMatTypeTextBox.setEnabled(true);

                        rawMatTypeTextBox.setTextColor(Color.parseColor(enableColor));
                        enterButton_base_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));
                        rawMatTypeCL.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_red_background));
                        addRawMatButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_circle));

                        if(isFocus_1) rawMatTypeTextBox.requestFocus();
                    }
                    else {
                        enterButton_base_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.enter_button_background));
                        enterButton_base_2.setEnabled(true);
                        deleteRawButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.delete_button_background));


                        enterButton_base_2.setOnClickListener(v -> {
                            if(rawMatTypeTextBox != null){
                                rawMatTypeCL.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));
                                enterButton_base_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));
                                rawMatTypeTextBox.setTextColor(Color.parseColor(disableColor));

                                addRawMatButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_button_background));

                                isSeged_2 = true;
                                enterButton_base_2.setEnabled(false);
                                rawMatTypeTextBox.setEnabled(false);
                            }
                        });

                        rawMatTypeTextBox.setOnFocusChangeListener((v, hasFocus) -> {
                            if(isSeged_2){
                                rawMatTypeTextBox.setEnabled(false);
                                enterButton_base_2.setEnabled(false);
                            }
                        });

                        rawMatTypeTextBox.setOnKeyListener((v, keyCode, event) -> {

                            if(keyCode == KeyEvent.KEYCODE_ENTER){
                                if(rawMatTypeTextBox != null){
                                    rawMatTypeCL.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));
                                    enterButton_base_2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background_rectangle));
                                    rawMatTypeTextBox.setTextColor(Color.parseColor(disableColor));
                                    isSeged_2 = true;
                                    addRawMatButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_button_background));

                                    addRawMatButton.requestFocus();
                                }
                            }

                            return false;
                        });

                        addRawMatButton.setOnClickListener(v -> {
                            Intent intent = new Intent();

                            intent.putExtra("rawMatCountTextBox", rawMatCountTextBox.getText().toString());
                            intent.putExtra("rawMatTypeTextBox", rawMatTypeTextBox.getText().toString());

                            setResult(1, intent);

                            RawMaterialCreationActivity.super.onBackPressed();
                        });
                    }
                }

                @Override public void afterTextChanged(Editable s) {}
            });
        }

        if(deleteRawButton != null){
            deleteRawButton.setOnClickListener(v -> {
//                if(!rawMatTypeTextBox.getText().toString().equals("")){
//                    rawMatTypeTextBox.setText("");
//                }
//
//                if(!rawMatTypeTextBox.getText().toString().equals("")){
//                    rawMatCountTextBox.setText("");
//                }

                enterButton_base_2.setEnabled(false);
                rawMatTypeTextBox.setText("");

                enterButton_base_1.setEnabled(false);
                rawMatCountTextBox.setText("");
            });
        }

        if(backRawButton != null){
            backRawButton.setOnClickListener(view -> {
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
        rawMatCountTextBox = findViewById(R.id.rawMatCountTextBox);
        rawMatTypeTextBox = findViewById(R.id.rawMatTypeTextBox);

        rawMatCountTextBox.requestFocus();

        rawMatCountCL = findViewById(R.id.rawMatCountCL);
        rawMatTypeCL = findViewById(R.id.rawMatTypeCL);

        addRawMatButton = findViewById(R.id.addRawMatButton);
        deleteRawButton = findViewById(R.id.deleteRawButton);
        backRawButton = findViewById(R.id.backRawButton);

        enterButton_base_1 = findViewById(R.id.enterButton_base_1);
        enterButton_base_2 = findViewById(R.id.enterButton_base_2);

        textLayout_base_1 = findViewById(R.id.textLayout_base_1);

        hideNavigationBar();
    }

    private void hideNavigationBar(){
        Helper.hideNavigationBar(this);
    }
}