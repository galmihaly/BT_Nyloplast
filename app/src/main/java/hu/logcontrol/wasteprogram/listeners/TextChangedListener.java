package hu.logcontrol.wasteprogram.listeners;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import hu.logcontrol.wasteprogram.R;
import hu.logcontrol.wasteprogram.enums.EditButtonEnums;

public class TextChangedListener implements TextWatcher {

    private Context context;
    private ConstraintLayout cl;
    private EditText firstTextBox;
    private EditText secondTextBox;
    private CardView firstCardView;
    private CardView secondCardView;

    private ImageButton addButton;
    private ImageButton deleteButton;

    public TextChangedListener(Context context, ConstraintLayout cl, EditText firstTextBox, EditText secondTextBox, CardView firstCardView, CardView secondCardView, ImageButton addButton, ImageButton deleteButton) {
        this.context = context;
        this.cl = cl;
        this.firstTextBox = firstTextBox;
        this.secondTextBox = secondTextBox;
        this.firstCardView = firstCardView;
        this.secondCardView = secondCardView;
        this.addButton = addButton;
        this.deleteButton = deleteButton;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String rawMatCount = firstTextBox.getText().toString();

        if(rawMatCount.equals("")){

            cl.setBackground(ContextCompat.getDrawable(context, R.drawable.cardview_red_background));

            if(firstCardView != null){
                firstCardView.setVisibility(View.INVISIBLE);
                firstTextBox.setText("");
                firstTextBox.setEnabled(false);
            }

            settingDeleteAndAddButtons(EditButtonEnums.DELETE_BUTTON_DISABLED);
        }
        else {

            cl.setBackground(ContextCompat.getDrawable(context, R.drawable.cardview_green_background));
            secondTextBox.setEnabled(false);
            secondTextBox.setTextColor(Color.parseColor("#B7C0C1"));

            if(secondCardView != null) {
                secondCardView.setVisibility(View.VISIBLE);
                secondTextBox.setEnabled(true);
                secondTextBox.requestFocus();
            }

            settingDeleteAndAddButtons(EditButtonEnums.DELETE_BUTTON_ENABLED);

            if(deleteButton != null){
                if(deleteButton.isEnabled()){
                    deleteButton.setOnClickListener(view -> {
                        if(firstCardView != null){
                            firstTextBox.setEnabled(true);
                            firstTextBox.requestFocus();
                            firstTextBox.setText("");

                            secondTextBox.setText("");
                            secondCardView.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void settingDeleteAndAddButtons(EditButtonEnums editButtonEnum){
        switch (editButtonEnum){
            case ADD_BUTTON_ENABLED:{

                addButton.setEnabled(true);
                addButton.setBackground(ContextCompat.getDrawable(context, R.drawable.add_button_background));

                break;
            }
            case ADD_BUTTON_DISABLED:{

                addButton.setEnabled(false);
                addButton.setBackground(ContextCompat.getDrawable(context, R.drawable.disable_button_background_circle));

                break;
            }
            case DELETE_BUTTON_ENABLED:{

                deleteButton.setEnabled(true);
                deleteButton.setBackground(ContextCompat.getDrawable(context, R.drawable.delete_button_background));

                break;
            }
            case DELETE_BUTTON_DISABLED:{

                deleteButton.setEnabled(false);
                deleteButton.setBackground(ContextCompat.getDrawable(context, R.drawable.disable_button_background_circle));

                break;
            }
        }
    }
}
