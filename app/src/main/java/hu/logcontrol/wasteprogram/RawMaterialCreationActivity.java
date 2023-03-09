package hu.logcontrol.wasteprogram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.helpers.Helper;
import hu.logcontrol.wasteprogram.presenters.ProgramPresenter;

public class RawMaterialCreationActivity extends AppCompatActivity {

    private CardView rawMaterialTypeCV;
    private CardView rawMaterialCountCV;

    private ConstraintLayout rawMatCountCL;
    private ConstraintLayout rawMatTypeCL;

    private EditText rawMatCountTextBox;
    private EditText rawMatTypeTextBox;

    private ImageButton addRawMatButton;
    private ImageButton deleteRawButton;
    private ImageButton backRawButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_material_creation);
        initView();
     }

    @Override
    protected void onResume() {
        super.onResume();

        if(rawMatTypeTextBox.getText().toString().equals("") && rawMatCountTextBox.getText().toString().equals("")) {
            addRawMatButton.setEnabled(false);
            deleteRawButton.setEnabled(false);
        }

        if(backRawButton != null){
            backRawButton.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), ModesOne.class);
                startActivity(intent);
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

                        settingDeleteAndAddButtons(EditButtonEnums.DELETE_BUTTON_DISABLED);
                    }
                    else {

                        rawMatCountCL.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));
                        rawMatCountTextBox.setEnabled(false);
                        rawMatCountTextBox.setTextColor(Color.parseColor("#B7C0C1"));

                        if(rawMaterialTypeCV != null) {
                            rawMaterialTypeCV.setVisibility(View.VISIBLE);
                            rawMatTypeTextBox.setEnabled(true);
                            rawMatTypeTextBox.requestFocus();
                        }

                        settingDeleteAndAddButtons(EditButtonEnums.DELETE_BUTTON_ENABLED);

                        if(deleteRawButton != null){
                            if(deleteRawButton.isEnabled()){
                                deleteRawButton.setOnClickListener(view -> {
                                    if(rawMaterialTypeCV != null){
                                        rawMatCountTextBox.setEnabled(true);
                                        rawMatCountTextBox.requestFocus();
                                        rawMatTypeTextBox.setText("");
                                        rawMatCountTextBox.setText("");
                                        rawMaterialTypeCV.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        }
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

                        settingDeleteAndAddButtons(EditButtonEnums.ADD_BUTTON_DISABLED);
                    }
                    else {
                        rawMatTypeCL.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_green_background));
                        rawMatTypeTextBox.setEnabled(false);
                        rawMatTypeTextBox.setTextColor(Color.parseColor("#B7C0C1"));

                        settingDeleteAndAddButtons(EditButtonEnums.ADD_BUTTON_ENABLED);

                        if(addRawMatButton != null){
                            if(addRawMatButton.isEnabled()){
                                addRawMatButton.setOnClickListener(view -> {
                                    Intent intent = new Intent();
                                    intent.putExtra("rawMatTypeTextBox", rawMatTypeTextBox.getText().toString());
                                    intent.putExtra("rawMatCountTextBox", rawMatCountTextBox.getText().toString());
                                    setResult(1, intent);

                                    RawMaterialCreationActivity.super.onBackPressed();
                                });
                            }
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    private void settingDeleteAndAddButtons(EditButtonEnums editButtonEnum){
        switch (editButtonEnum){
            case ADD_BUTTON_ENABLED:{

                addRawMatButton.setEnabled(true);
                addRawMatButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_button_background));

                break;
            }
            case ADD_BUTTON_DISABLED:{

                addRawMatButton.setEnabled(false);
                addRawMatButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background));

                break;
            }
            case DELETE_BUTTON_ENABLED:{

                deleteRawButton.setEnabled(true);
                deleteRawButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.delete_button_background));

                break;
            }
            case DELETE_BUTTON_DISABLED:{

                deleteRawButton.setEnabled(false);
                deleteRawButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disable_button_background));

                break;
            }
        }
    }

    private void openDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alapanyag Hozzáadás")
                .setMessage("Biztosan hozzá szeretnéd adni az alapanyag listához?")
                .setNegativeButton("Nem", (dialogInterface, i) -> {
                    hideNavigationBar();
                    dialogInterface.dismiss();
                })
                .setPositiveButton("Igen", (dialogInterface, i) -> {

                    Intent intent = new Intent();
                    intent.putExtra("rawMatTypeTextBox", rawMatTypeTextBox.getText().toString());
                    intent.putExtra("rawMatCountTextBox", rawMatCountTextBox.getText().toString());
                    setResult(1, intent);

                    RawMaterialCreationActivity.super.onBackPressed();
                });

        AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        alertDialog.show();
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
        deleteRawButton = findViewById(R.id.deleteRawButton);
        backRawButton = findViewById(R.id.backRawButton);

        hideNavigationBar();
    }

    private void hideNavigationBar(){
        Helper.hideNavigationBar(this);
    }
}