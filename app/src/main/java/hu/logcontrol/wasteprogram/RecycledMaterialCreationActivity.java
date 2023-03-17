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

import com.google.android.material.textfield.TextInputLayout;

import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.helpers.ElementStateChangeHelper;
import hu.logcontrol.wasteprogram.helpers.Helper;

public class RecycledMaterialCreationActivity extends AppCompatActivity {

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

    private TextInputLayout textLayout_rectMat_1;

    private String disableColor = "#B7C0C1";
    private String enableColor = "#000000";

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

                        ElementStateChangeHelper.visibleCardViewElement(typeRecMatCV);
                        ElementStateChangeHelper.inVisibleCardViewElement(storageBoxIdentifierCV2);

                        ElementStateChangeHelper.setNotReadyStateElements(getApplicationContext(), typeRecMatCL, R.drawable.cardview_red_background, typeRecMatTextBox, enableColor);
                        ElementStateChangeHelper.clearNextCardView(getApplicationContext(), storageBoxIdentifierCL2, R.drawable.cardview_red_background ,storageBoxIdentifierTextBox2, R.color.black);

                        ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_rec_1); // enter button lezárása
                        ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.DELETE_BUTTON_DISABLED, deleteRecMatButton);
                        ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ADD_BUTTON_DISABLED, addRecMatButton);
                        hideNavigationBar();
                    }
                    else {

                        ElementStateChangeHelper.enableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_ENABLED, enterButton_rec_1);

                        enterButton_rec_1.setOnClickListener(view -> {
                            ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), typeRecMatCL, R.drawable.cardview_green_background, typeRecMatTextBox, disableColor); // jelenlegi elemek késszé tétele

                            ElementStateChangeHelper.visibleCardViewElement(storageBoxIdentifierCV2);
                            ElementStateChangeHelper.setNotReadyStateElements(getApplicationContext(), storageBoxIdentifierCL2, R.drawable.cardview_red_background, storageBoxIdentifierTextBox2, enableColor); // következő elemek láthatóvá tétele

                            ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_rec_1); // enter button lezárása
                            ElementStateChangeHelper.enableButton(getApplicationContext(), EditButtonEnums.DELETE_BUTTON_ENABLED, deleteRecMatButton);
                            hideNavigationBar();
                        });

                        typeRecMatTextBox.setOnKeyListener((view, l, keyEvent) -> {
                            if(l == 66){
                                if(enterButton_rec_1 != null){
                                    if(enterButton_rec_1.isEnabled()){
                                        enterButton_rec_1.callOnClick();
                                    }
                                }
                            }
                            return false;
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

                        ElementStateChangeHelper.inVisibleCardViewElement(massDataCV2);

                        ElementStateChangeHelper.setNotReadyStateElements(getApplicationContext(), storageBoxIdentifierCL2, R.drawable.cardview_red_background, storageBoxIdentifierTextBox2, enableColor);
                        ElementStateChangeHelper.clearNextCardView(getApplicationContext(), massDataCL2, R.drawable.cardview_red_background ,massDataTextBox2, R.color.black);

                        ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_rec_2); // enter button lezárása
                        hideNavigationBar();
                    }
                    else {

                        ElementStateChangeHelper.enableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_ENABLED, enterButton_rec_2);

                        enterButton_rec_2.setOnClickListener(view -> {
                            ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), storageBoxIdentifierCL2, R.drawable.cardview_green_background, storageBoxIdentifierTextBox2, disableColor); // jelenlegi elemek késszé tétele

                            ElementStateChangeHelper.visibleCardViewElement(massDataCV2);
                            ElementStateChangeHelper.setNotReadyStateElements(getApplicationContext(), massDataCL2, R.drawable.cardview_red_background, massDataTextBox2, enableColor); // következő elemek láthatóvá tétele

                            ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_rec_2); // enter button lezárása
                            hideNavigationBar();
                        });

                        storageBoxIdentifierTextBox2.setOnKeyListener((view, l, keyEvent) -> {
                            if(l == 66){
                                if(enterButton_rec_2 != null){
                                    if(enterButton_rec_2.isEnabled()){
                                        enterButton_rec_2.callOnClick();
                                    }
                                }
                            }
                            return false;
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

                        ElementStateChangeHelper.inVisibleCardViewElement(massDataCV2);
                        ElementStateChangeHelper.setNotReadyStateElements(getApplicationContext(), massDataCL2, R.drawable.cardview_red_background, massDataTextBox2, enableColor);
                        ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_rec_3); // enter button lezárása
                        hideNavigationBar();
                    } else {
                        ElementStateChangeHelper.enableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_ENABLED, enterButton_rec_3);

                        enterButton_rec_3.setOnClickListener(view -> {
                            ElementStateChangeHelper.setReadyStateElements(getApplicationContext(), massDataCL2, R.drawable.cardview_green_background, massDataTextBox2, disableColor); // jelenlegi elemek késszé tétele
                            ElementStateChangeHelper.disableButton(getApplicationContext(), EditButtonEnums.ENTER_BUTTON_DISABLED, enterButton_rec_3); // enter button lezárása

                            ElementStateChangeHelper.enableButton(getApplicationContext(), EditButtonEnums.ADD_BUTTON_ENABLED, addRecMatButton);
                            hideNavigationBar();
                        });

                        massDataTextBox2.setOnKeyListener((view, l, keyEvent) -> {
                            if(l == 66){
                                if(enterButton_rec_3 != null){
                                    if(enterButton_rec_3.isEnabled()){
                                        enterButton_rec_3.callOnClick();
                                    }
                                }
                            }
                            return false;
                        });

                        hideNavigationBar();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {}
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
                typeRecMatTextBox.setText("");
                textLayout_rectMat_1.requestFocus();
                hideNavigationBar();
            }
        });
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

        textLayout_rectMat_1 = findViewById(R.id.textLayout_rectMat_1);

        hideNavigationBar();
    }

    private void hideNavigationBar(){
        Helper.hideNavigationBar(this);
    }
}