package hu.logcontrol.wasteprogram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;

import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.helpers.ElementStateChangeHelper;
import hu.logcontrol.wasteprogram.helpers.Helper;

public class RecycledMaterialCreationActivity extends AppCompatActivity {

    private ScrollView recMatScrollView;

    private CardView typeRecMatCV;
    private CardView storageBoxIdentifierCV2;
    private CardView massDataCV2;

    private ConstraintLayout typeRecMatCL;
    private ConstraintLayout storageBoxIdentifierCL2;
    private ConstraintLayout massDataCL2;

    private EditText typeRecMatTextBox;
    private EditText storageBoxIdentifierTextBox2;
    private EditText massDataTextBox2;

    private ImageButton addRecMatButton;
    private ImageButton deleteRecMatButton;
    private ImageButton backRecMatButton;

    private ImageButton enterButton_rec_1;
    private ImageButton enterButton_rec_2;
    private ImageButton enterButton_rec_3;

    private String disableColor = "#B7C0C1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycled_material_creation);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String text1 = typeRecMatTextBox.getText().toString();
        String text2 = storageBoxIdentifierTextBox2.getText().toString();
        String text3 = massDataTextBox2.getText().toString();


        if(text1.equals("") && text2.equals("") && text3.equals(""))
        {
            addRecMatButton.setEnabled(false);
            deleteRecMatButton.setEnabled(false);
        }

        if(backRecMatButton != null){
            backRecMatButton.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), ModesThree.class);
                startActivity(intent);
            });
        }

        if(typeRecMatCV != null && typeRecMatTextBox != null && typeRecMatCL != null){
            typeRecMatTextBox.addTextChangedListener(new TextWatcher() {

                @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String data = typeRecMatTextBox.getText().toString();
                    if(data.equals("")){

                        typeRecMatCL.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_red_background));
                        typeRecMatTextBox.setTextColor(Color.parseColor("#000000"));
                        ElementStateChangeHelper.setDisableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_rec_1);

//                        ElementStateChangeHelper.disableCurrentElements(getApplicationContext(), typeRecMatCL, storageBoxIdentifierCV2, storageBoxIdentifierTextBox2, R.drawable.cardview_red_background);
//                        ElementStateChangeHelper.setDisableButton(getApplicationContext(), EditButtonEnums.DELETE_BUTTON_DISABLED, deleteRecMatButton);
                    }
                    else {
//                        ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), typeRecMatCL, typeRecMatTextBox, disableColor, R.drawable.cardview_green_background);
//                        ElementStateChangeHelper.setEnableButton(getApplicationContext(), EditButtonEnums.DELETE_BUTTON_ENABLED, deleteRecMatButton);
//                        ElementStateChangeHelper.enableNextElements(storageBoxIdentifierCV2, storageBoxIdentifierTextBox2);

                        ElementStateChangeHelper.setEnableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_ENABLED, enterButton_rec_1);

                        enterButton_rec_1.setOnClickListener(view -> {
                            ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), typeRecMatCL, typeRecMatTextBox, disableColor, R.drawable.cardview_green_background);
                            ElementStateChangeHelper.enableNextElements(storageBoxIdentifierCV2, storageBoxIdentifierTextBox2);
                            ElementStateChangeHelper.setDisableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_rec_1);
                            ElementStateChangeHelper.setEnableButton(getApplicationContext(), EditButtonEnums.DELETE_BUTTON_ENABLED, deleteRecMatButton);
                            addRecMatButton.setEnabled(true);

                            hideNavigationBar();
                        });

                        hideNavigationBar();
                    }
                }

                @Override public void afterTextChanged(Editable editable) {}
            });
        }

        if(storageBoxIdentifierCV2 != null && storageBoxIdentifierTextBox2 != null && storageBoxIdentifierCL2 != null){
            storageBoxIdentifierTextBox2.addTextChangedListener(new TextWatcher() {

                @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String data = storageBoxIdentifierTextBox2.getText().toString();
                    if(data.equals("")){

                        storageBoxIdentifierCL2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_red_background));
                        storageBoxIdentifierTextBox2.setTextColor(Color.parseColor("#000000"));
                        ElementStateChangeHelper.setDisableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_rec_2);
                    }
                    else {

                        ElementStateChangeHelper.setEnableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_ENABLED, enterButton_rec_2);

                        enterButton_rec_2.setOnClickListener(view -> {
                            ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), storageBoxIdentifierCL2, storageBoxIdentifierTextBox2, disableColor, R.drawable.cardview_green_background);
                            ElementStateChangeHelper.enableNextElements(massDataCV2, massDataTextBox2);
                            ElementStateChangeHelper.setDisableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_rec_2);
                            ElementStateChangeHelper.setEnableButton(getApplicationContext(), EditButtonEnums.DELETE_BUTTON_ENABLED, deleteRecMatButton);
                            addRecMatButton.setEnabled(true);

                            hideNavigationBar();
                        });

                        hideNavigationBar();
                    }
                }

                @Override public void afterTextChanged(Editable editable) {}
            });
        }

        if(massDataCV2 != null && massDataTextBox2 != null && massDataCL2 != null) {
            massDataTextBox2.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String data = massDataTextBox2.getText().toString();
                    if (data.equals("")) {

                        massDataCL2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cardview_red_background));
                        massDataTextBox2.setTextColor(Color.parseColor("#000000"));
                        ElementStateChangeHelper.setDisableButton(getApplicationContext(), EditButtonEnums.ADD_BUTTON_DISABLED, addRecMatButton);
                        ElementStateChangeHelper.setDisableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_rec_3);
                    } else {
                        ElementStateChangeHelper.setEnableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_ENABLED, enterButton_rec_3);

                        enterButton_rec_3.setOnClickListener(view -> {
                            ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), massDataCL2, massDataTextBox2, disableColor, R.drawable.cardview_green_background);
                            ElementStateChangeHelper.setEnableButton(getApplicationContext(), EditButtonEnums.ADD_BUTTON_ENABLED, addRecMatButton);
                            ElementStateChangeHelper.setDisableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_rec_3);
                            addRecMatButton.setEnabled(true);

                            hideNavigationBar();
                        });
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }

        if(addRecMatButton != null){
            addRecMatButton.setOnClickListener(view -> {

                Intent intent = new Intent();

                intent.putExtra("typeRecMatTextBox", typeRecMatTextBox.getText().toString());
                intent.putExtra("storageBoxIdentifierTextBox2", storageBoxIdentifierTextBox2.getText().toString());
                intent.putExtra("massDataTextBox2", massDataTextBox2.getText().toString());

                setResult(1, intent);

                RecycledMaterialCreationActivity.super.onBackPressed();
            });
        }

        deleteRecMatButton.setOnClickListener(view -> {
            if(typeRecMatCV != null){
                typeRecMatTextBox.setEnabled(true);
                typeRecMatTextBox.requestFocus();
                typeRecMatTextBox.setText("");

                if(recMatScrollView != null) recMatScrollView.fullScroll(ScrollView.FOCUS_UP);

                hideNavigationBar();
            }
        });
    }

    public void initView(){

        recMatScrollView = findViewById(R.id.recMatScrollView);

        typeRecMatCV = findViewById(R.id.typeRecMatCV);
        storageBoxIdentifierCV2 = findViewById(R.id.storageBoxIdentifierCV2);
        massDataCV2 = findViewById(R.id.massDataCV2);

        storageBoxIdentifierCV2.setVisibility(View.INVISIBLE);
        massDataCV2.setVisibility(View.INVISIBLE);

        typeRecMatCL = findViewById(R.id.typeRecMatCL);
        storageBoxIdentifierCL2 = findViewById(R.id.storageBoxIdentifierCL2);
        massDataCL2 = findViewById(R.id.massDataCL2);

        typeRecMatTextBox = findViewById(R.id.typeRecMatTextBox);
        storageBoxIdentifierTextBox2 = findViewById(R.id.storageBoxIdentifierTextBox2);
        massDataTextBox2 = findViewById(R.id.massDataTextBox2);

        addRecMatButton = findViewById(R.id.addRecMatButton);
        deleteRecMatButton = findViewById(R.id.deleteRecMatButton);
        backRecMatButton = findViewById(R.id.backRecMatButton);

        enterButton_rec_1 = findViewById(R.id.enterButton_rec_1);
        enterButton_rec_2 = findViewById(R.id.enterButton_rec_2);
        enterButton_rec_3 = findViewById(R.id.enterButton_rec_3);

        hideNavigationBar();
    }

    private void hideNavigationBar(){
        Helper.hideNavigationBar(this);
    }
}