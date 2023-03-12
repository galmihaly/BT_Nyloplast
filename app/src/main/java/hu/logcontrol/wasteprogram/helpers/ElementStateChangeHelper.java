package hu.logcontrol.wasteprogram.helpers;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import hu.logcontrol.wasteprogram.R;
import hu.logcontrol.wasteprogram.enums.EditButtonEnums;

public class ElementStateChangeHelper {

    public static void disableCurrentElements(Context context, ConstraintLayout constraintLayout, CardView cardView, EditText textBox, int background){
        if(constraintLayout == null) return;
        if(cardView == null) return;
        if(textBox == null) return;

        constraintLayout.setBackground(ContextCompat.getDrawable(context, background));
        cardView.setVisibility(View.INVISIBLE);
        textBox.setText("");
        textBox.setEnabled(false);
    }

    public static void setEnableButton(Context context, EditButtonEnums editButtonEnum, ImageButton button){
        if(button == null) return;

        switch (editButtonEnum){
            case ADD_BUTTON_ENABLED:{

                button.setEnabled(true);
                button.setBackground(ContextCompat.getDrawable(context, R.drawable.add_button_background));

                break;
            }
            case DELETE_BUTTON_ENABLED:{

                button.setEnabled(true);
                button.setBackground(ContextCompat.getDrawable(context, R.drawable.delete_button_background));

                break;
            }
            case ENTER_BUTTON_ENABLED:{

                button.setEnabled(true);
                button.setBackground(ContextCompat.getDrawable(context, R.drawable.enter_button_background));

                break;
            }
        }
    }

    public static void setDisableButton(Context context, EditButtonEnums editButtonEnum, ImageButton button){
        if(button == null) return;

        switch (editButtonEnum){
            case ADD_BUTTON_DISABLED:
            case DELETE_BUTTON_DISABLED: {

                button.setEnabled(true);
                button.setBackground(ContextCompat.getDrawable(context, R.drawable.disable_button_background_circle));
                break;
            }
            case ENTER_BUTTON_DISABLED:{

                button.setEnabled(false);
                button.setBackground(ContextCompat.getDrawable(context, R.drawable.disable_button_background_rectangle));
                break;
            }
        }
    }

    public static void setReadyStateElements(Context context, ConstraintLayout cl, EditText tB, String color, int background) {
        if(context == null) return;
        if(cl == null) return;
        if(tB == null) return;
        if(color == null) return;

        cl.setBackground(ContextCompat.getDrawable(context, background));
        tB.setEnabled(false);
        tB.setTextColor(Color.parseColor(color));
    }

    public static void enableNextElements(CardView cv, EditText tB) {
        if(cv == null) return;
        if(tB == null) return;

        cv.setVisibility(View.VISIBLE);
        tB.setEnabled(true);
        tB.requestFocus();
    }

    public static void setEnablePreviousElements(Context context, ConstraintLayout cl, EditText tB) {
        if(context == null) return;
        if(cl == null) return;
        if(tB == null) return;

        cl.setBackground(ContextCompat.getDrawable(context, R.drawable.cardview_red_background));
        tB.setEnabled(true);
    }
}
