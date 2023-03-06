package hu.logcontrol.wasteprogram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

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

public class RawMaterialCreationActivity extends AppCompatActivity implements IRawMaterialCreationView {

    private CardView rawMaterialTypeCV;
    private CardView rawMaterialCountCV;

    private ConstraintLayout rawMatCountCL;
    private ConstraintLayout rawMatTypeCL;

    private EditText rawMatCountTextBox;
    private EditText rawMatTypeTextBox;

    private Button addRawMatButton;

    private ProgramPresenter programPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_material_creation);
        initView();

        programPresenter = new ProgramPresenter(this, getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(addRawMatButton != null){
            addRawMatButton.setOnClickListener(view -> {

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

                        rawMatCountTextBox.requestFocus();

                        rawMatCountCL.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_red_background));

                        if(rawMaterialTypeCV != null){
                            hideRawMaterialTypeCV();
                            clearRawMatTypeTextBox();
                        }

                        if(addRawMatButton != null) setBackgroundAddRawMatButton(false);
                    }
                    else {

                        rawMatCountCL.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));

                        if(rawMatTypeTextBox != null) rawMatTypeTextBox.requestFocus();
                        if(rawMaterialTypeCV != null)showRawMaterialTypeCV();
                        if(addRawMatButton != null) setBackgroundAddRawMatButton(false);
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
                        setBackgroundAddRawMatButton(false);
                    }
                    else {
                        rawMatTypeCL.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));
                        setBackgroundAddRawMatButton(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

        Log.e("CL", String.valueOf(2));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("", String.valueOf(keyCode));
        switch (keyCode) {
            case KeyEvent.KEYCODE_A:
            {
                //your Action code
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        rawMaterialTypeCV = findViewById(R.id.rawMaterialTypeCV);
        hideRawMaterialTypeCV();

        rawMaterialCountCV = findViewById(R.id.rawMaterialCountCV);

        rawMatCountTextBox = findViewById(R.id.rawMatCountTextBox);
        rawMatTypeTextBox = findViewById(R.id.rawMatTypeTextBox);

        rawMatCountCL = findViewById(R.id.rawMatCountCL);
        rawMatTypeCL = findViewById(R.id.rawMatTypeCL);

        addRawMatButton = findViewById(R.id.addRawMatButton);
    }

    private void hideRawMaterialTypeCV() {
        if(rawMaterialTypeCV == null) return;
        rawMaterialTypeCV.setVisibility(View.INVISIBLE);
    }

    private void clearRawMatTypeTextBox(){
        if(rawMatTypeTextBox == null) return;
        if(!rawMatTypeTextBox.getText().toString().equals("")) rawMatTypeTextBox.setText("");
    }

    private void showRawMaterialTypeCV() {
        if(rawMaterialTypeCV == null) return;
        rawMaterialTypeCV.setVisibility(View.VISIBLE);
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
}