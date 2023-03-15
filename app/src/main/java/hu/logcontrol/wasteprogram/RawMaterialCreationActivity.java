package hu.logcontrol.wasteprogram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputLayout;

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

    private ImageButton enterButton_base_1;
    private ImageButton enterButton_base_2;

    private TextInputLayout textLayout_base_1;

    private final String disableColor = "#B7C0C1";
    private final String enableColor = "#000000";

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

                        ElementStateChangeHelper.visibleCardViewElement(rawMaterialCountCV);
                        ElementStateChangeHelper.inVisibleCardViewElement(rawMaterialTypeCV);

                        ElementStateChangeHelper.setNotReadyStateElements(getApplicationContext(), rawMatCountCL, R.drawable.cardview_red_background, rawMatCountTextBox, enableColor);
                        ElementStateChangeHelper.clearNextCardView(getApplicationContext(), rawMatTypeCL, R.drawable.cardview_red_background ,rawMatTypeTextBox, R.color.black);

                        ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_base_1); // enter button lezárása
                        ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.DELETE_BUTTON_DISABLED, deleteRawButton);
                        ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ADD_BUTTON_DISABLED, addRawMatButton);
                    }
                    else {

                        ElementStateChangeHelper.enableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_ENABLED, enterButton_base_1);

                        enterButton_base_1.setOnClickListener(view -> {
                            ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), rawMatCountCL, R.drawable.cardview_green_background, rawMatCountTextBox, disableColor); // jelenlegi elemek késszé tétele

                            ElementStateChangeHelper.visibleCardViewElement(rawMaterialTypeCV);
                            ElementStateChangeHelper.setNotReadyStateElements(getApplicationContext(), rawMatTypeCL, R.drawable.cardview_red_background, rawMatTypeTextBox, enableColor); // következő elemek láthatóvá tétele

                            ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_base_1); // enter button lezárása
                            ElementStateChangeHelper.enableButton(getApplicationContext(), EditButtonEnums.DELETE_BUTTON_ENABLED, deleteRawButton);
                            hideNavigationBar();
                        });

                        rawMatCountTextBox.setOnKeyListener((view, l, keyEvent) -> {
                            if(l == 66){
                                if(enterButton_base_1 != null){
                                    if(enterButton_base_1.isEnabled()){
                                        enterButton_base_1.callOnClick();
                                    }
                                }
                            }
                            return false;
                        });
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

                        ElementStateChangeHelper.inVisibleCardViewElement(rawMaterialTypeCV);
                        ElementStateChangeHelper.setNotReadyStateElements(getApplicationContext(), rawMatTypeCL, R.drawable.cardview_red_background, rawMatTypeTextBox, enableColor);
                        ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_base_2); // enter button lezárása
                    }
                    else {

                        ElementStateChangeHelper.enableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_ENABLED, enterButton_base_2);

                        enterButton_base_2.setOnClickListener(view -> {
                            ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), rawMatTypeCL, R.drawable.cardview_green_background, rawMatTypeTextBox, disableColor); // jelenlegi elemek késszé tétele
                            ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_base_2); // enter button lezárása

                            ElementStateChangeHelper.enableButton(getApplicationContext(), EditButtonEnums.ADD_BUTTON_ENABLED, addRawMatButton);
                            hideNavigationBar();
                        });

                        rawMatTypeTextBox.setOnKeyListener((view, k, keyEvent) -> {
                            if(k == 66){
                                if(enterButton_base_2 != null){
                                    if(enterButton_base_2.isEnabled()){
                                        enterButton_base_2.callOnClick();
                                    }
                                }
                            }
                            return false;
                        });

                        hideNavigationBar();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

        if(addRawMatButton != null){
            addRawMatButton.setOnClickListener(view -> {

                Intent intent = new Intent();

                intent.putExtra("rawMatTypeTextBox", rawMatTypeTextBox.getText().toString());
                intent.putExtra("rawMatCountTextBox", rawMatCountTextBox.getText().toString());

                setResult(1, intent);

                RawMaterialCreationActivity.super.onBackPressed();
            });
        }

        if(deleteRawButton != null){
            deleteRawButton.setOnClickListener(view -> {
                if(rawMaterialCountCV != null){
                    rawMatCountTextBox.setText("");
                    textLayout_base_1.requestFocus();
                    hideNavigationBar();
                }
            });
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == 66){
            Log.e("", String.valueOf(keyCode));
        }

        return super.onKeyDown(keyCode, event);
    }

    public void startEnterButton(){


        if(enterButton_base_2 != null){
            if(enterButton_base_2.isEnabled()) {
                enterButton_base_2.setOnClickListener(view -> {
                    ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), rawMatTypeCL, R.drawable.cardview_green_background, rawMatTypeTextBox, disableColor); // jelenlegi elemek késszé tétele
                    ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_base_2); // enter button lezárása

                    ElementStateChangeHelper.enableButton(getApplicationContext(), EditButtonEnums.ADD_BUTTON_ENABLED, addRawMatButton);
                    hideNavigationBar();
                });
            }
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

        enterButton_base_1 = findViewById(R.id.enterButton_base_1);
        enterButton_base_2 = findViewById(R.id.enterButton_base_2);

        textLayout_base_1 = findViewById(R.id.textLayout_base_1);

        hideNavigationBar();
    }

    private void hideNavigationBar(){
        Helper.hideNavigationBar(this);
    }
}