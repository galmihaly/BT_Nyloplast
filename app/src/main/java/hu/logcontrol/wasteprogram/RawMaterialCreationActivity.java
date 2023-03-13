package hu.logcontrol.wasteprogram;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.helpers.ElementStateChangeHelper;
import hu.logcontrol.wasteprogram.helpers.Helper;

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

    private String disableColor = "#B7C0C1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_material_creation);
        initView();
     }

    @Override
    protected void onResume() {
        super.onResume();

        hideNavigationBar();

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
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String rawMatCount = rawMatCountTextBox.getText().toString();

                    if(rawMatCount.equals("")){

                        ElementStateChangeHelper.disableCurrentElements(getApplicationContext(), rawMatCountCL, rawMaterialTypeCV, rawMatTypeTextBox, R.drawable.cardview_red_background);
                        ElementStateChangeHelper.setDisableButton(getApplicationContext(), EditButtonEnums.DELETE_BUTTON_DISABLED, deleteRawButton);
                    }
                    else {

                        ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), rawMatCountCL, rawMatCountTextBox, disableColor, R.drawable.cardview_green_background);
                        ElementStateChangeHelper.setEnableButton(getApplicationContext(), EditButtonEnums.DELETE_BUTTON_ENABLED, deleteRawButton);
                        ElementStateChangeHelper.enableNextElements(rawMaterialTypeCV, rawMatTypeTextBox);

                        if(deleteRawButton != null){
                            if(deleteRawButton.isEnabled()){
                                deleteRawButton.setOnClickListener(view -> {
                                    if(rawMaterialTypeCV != null){
                                        rawMatCountTextBox.setEnabled(true);
                                        rawMatCountTextBox.requestFocus();
                                        rawMatCountTextBox.setText("");

                                        rawMaterialTypeCV.setVisibility(View.INVISIBLE);
                                        rawMatTypeTextBox.setText("");
                                    }
                                });
                            }
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {}
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

                        ElementStateChangeHelper.setEnablePreviousElements(getApplicationContext(), rawMatTypeCL, rawMatCountTextBox);
                        ElementStateChangeHelper.setDisableButton(getApplicationContext(), EditButtonEnums.ADD_BUTTON_DISABLED, addRawMatButton);
                    }
                    else {

                        ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), rawMatTypeCL, rawMatTypeTextBox, disableColor, R.drawable.cardview_green_background);
                        ElementStateChangeHelper.setEnableButton(getApplicationContext(), EditButtonEnums.ADD_BUTTON_ENABLED, addRawMatButton);

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

//    private void openDialog() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Alapanyag Hozzáadás")
//                .setMessage("Biztosan hozzá szeretnéd adni az alapanyag listához?")
//                .setNegativeButton("Nem", (dialogInterface, i) -> {
//                    hideNavigationBar();
//                    dialogInterface.dismiss();
//                })
//                .setPositiveButton("Igen", (dialogInterface, i) -> {
//
//                    Intent intent = new Intent();
//                    intent.putExtra("rawMatTypeTextBox", rawMatTypeTextBox.getText().toString());
//                    intent.putExtra("rawMatCountTextBox", rawMatCountTextBox.getText().toString());
//                    setResult(1, intent);
//
//                    RawMaterialCreationActivity.super.onBackPressed();
//                });
//
//        AlertDialog alertDialog = builder.create();
//        Window window = alertDialog.getWindow();
//        alertDialog.show();
//    }

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