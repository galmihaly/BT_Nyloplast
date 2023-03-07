package hu.logcontrol.wasteprogram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import hu.logcontrol.wasteprogram.interfaces.IRawMaterialCreationView;
import hu.logcontrol.wasteprogram.presenters.ProgramPresenter;

public class RawMaterialCreationActivity extends AppCompatActivity {

    private CardView rawMaterialTypeCV;
    private CardView rawMaterialCountCV;

    private ConstraintLayout rawMatCountCL;
    private ConstraintLayout rawMatTypeCL;

    private EditText rawMatCountTextBox;
    private EditText rawMatTypeTextBox;

    private Button addRawMatButton;
    private Button backRawMatButton;

    private ProgramPresenter programPresenter;

    private boolean hideSwitch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_material_creation);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(addRawMatButton != null){
            addRawMatButton.setOnClickListener(view -> {
                Intent intent = new Intent();
                intent.putExtra("rawMatTypeTextBox", rawMatTypeTextBox.getText().toString());
                intent.putExtra("rawMatCountTextBox", rawMatCountTextBox.getText().toString());
                setResult(1, intent);

                RawMaterialCreationActivity.super.onBackPressed();
            });
        }

        if(backRawMatButton != null){
            backRawMatButton.setOnClickListener(view -> {
                if(rawMaterialTypeCV != null){
                    rawMatCountTextBox.setEnabled(true);
                    rawMatCountTextBox.requestFocus();
                    rawMatTypeTextBox.setText("");
                    rawMatCountTextBox.setText("");
                    rawMaterialTypeCV.setVisibility(View.INVISIBLE);
                }
            });
        }

        if(rawMaterialCountCV != null && rawMatCountTextBox != null && rawMatCountCL != null){

            rawMatCountTextBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String rawMatCount = rawMatCountTextBox.getText().toString();

                    if(rawMatCount.equals("")){

                        rawMatCountCL.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_red_background));

                        if(rawMaterialTypeCV != null){
                            rawMaterialTypeCV.setVisibility(View.INVISIBLE);
                            rawMatTypeTextBox.setText("");
                            rawMatTypeTextBox.setEnabled(false);
                        }

                        if(addRawMatButton != null) setBackgroundAddRawMatButton(false);
                        if(backRawMatButton != null) setBackgroundBackRawMatButton(false);
                    }
                    else {

                        rawMatCountCL.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));
                        rawMatCountTextBox.setEnabled(false);

                        if(rawMaterialTypeCV != null) {
                            rawMaterialTypeCV.setVisibility(View.VISIBLE);
                            rawMatTypeTextBox.setEnabled(true);
                            rawMatTypeTextBox.requestFocus();
                        }
                        if(backRawMatButton != null) setBackgroundBackRawMatButton(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

        if(rawMaterialTypeCV != null && rawMatTypeTextBox != null && rawMatTypeCL != null){

            rawMatTypeTextBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String rawMatType = rawMatTypeTextBox.getText().toString();

                    if(rawMatType.equals("")){

                        rawMatTypeCL.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_red_background));
                        rawMatCountTextBox.setEnabled(true);
                        setBackgroundAddRawMatButton(false);
                    }
                    else {
                        rawMatTypeCL.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));
                        rawMatTypeTextBox.setEnabled(false);
                        setBackgroundAddRawMatButton(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    private void initView() {
        rawMaterialTypeCV = findViewById(R.id.rawMaterialTypeCV);
        rawMaterialTypeCV.setVisibility(View.INVISIBLE);

        rawMaterialCountCV = findViewById(R.id.rawMaterialCountCV);

        rawMatCountTextBox = findViewById(R.id.rawMatCountTextBox);
        rawMatTypeTextBox = findViewById(R.id.rawMatTypeTextBox);

        rawMatCountCL = findViewById(R.id.rawMatCountCL);
        rawMatTypeCL = findViewById(R.id.rawMatTypeCL);

        addRawMatButton = findViewById(R.id.addRawMatButton);
        backRawMatButton = findViewById(R.id.backRawMatButton);
    }

    private void setBackgroundAddRawMatButton(boolean value){
        if(addRawMatButton == null) return;

        if(value){
            addRawMatButton.setEnabled(true);
            addRawMatButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));
        }
        else
        {
            addRawMatButton.setEnabled(false);
            addRawMatButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_red_background));
        }
    }

    private void setBackgroundBackRawMatButton(boolean value) {
        if(backRawMatButton == null) return;

        if(value){
            backRawMatButton.setEnabled(true);
            backRawMatButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));
        }
        else
        {
            backRawMatButton.setEnabled(false);
            backRawMatButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_red_background));
        }
    }
}