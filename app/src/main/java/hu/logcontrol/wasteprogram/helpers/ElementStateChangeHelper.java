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

    public static void clearNextCardView(Context context, ConstraintLayout constraintLayout, int disableBackground, EditText editText, int enableColor){
        if(constraintLayout == null) return;
        if(editText == null) return;

        constraintLayout.setBackground(ContextCompat.getDrawable(context, disableBackground));
        editText.setText("");
        editText.setEnabled(false);
        editText.setTextColor(enableColor);
    }

    public static void visibleCardViewElement(CardView cardView){
        if(cardView == null) return;
        cardView.setVisibility(View.VISIBLE);
    }

    public static void inVisibleCardViewElement(CardView cardView){
        if(cardView == null) return;
        cardView.setVisibility(View.INVISIBLE);
    }

    // zöld színre vált a kártya, a textbox lezár és zsürke szöveg jelenik meg
    public static void setReadyStateElements(Context context, ConstraintLayout constraintLayout, int constraintLayoutBackground, EditText editText, String disableColor){
        if(context == null) return;
        if(constraintLayout == null) return;
        if(editText == null) return;

        constraintLayout.setBackground(ContextCompat.getDrawable(context, constraintLayoutBackground));
        editText.setEnabled(false);
        editText.setTextColor(Color.parseColor(disableColor));
        editText.clearFocus();
    }

    // piros színre vált a kártya, a textbox nyitva marad, a szöveg fekete színű lesz
    public static void setNotReadyStateElements(Context context, ConstraintLayout constraintLayout, int constraintLayoutBackground, EditText editText, String enableColor){
        if(context == null) return;
        if(constraintLayout == null) return;
        if(editText == null) return;

        constraintLayout.setBackground(ContextCompat.getDrawable(context, constraintLayoutBackground));
        editText.setEnabled(true);
        editText.setTextColor(Color.parseColor(enableColor));
        editText.requestFocus();
    }

    public static void enableButton(Context context, EditButtonEnums editButtonEnum, ImageButton button){
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

    public static void disableButton(Context context, EditButtonEnums editButtonEnum, ImageButton button){
        if(button == null) return;

        switch (editButtonEnum){
            case ADD_BUTTON_DISABLED:
            case DELETE_BUTTON_DISABLED: {

                button.setEnabled(false);
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

//    public static void enableSavePathEditor(TextInputEditText inputEditText, String enableColor){
//        if(inputEditText == null) return;
//
//        inputEditText.setEnabled(true);
//        inputEditText.setTextColor(Color.parseColor(enableColor));
//        inputEditText.requestFocus();
//    }
//
//    public static void disableSavePathEditor(TextInputEditText inputEditText, String disableColor){
//        if(inputEditText == null) return;
//
//        inputEditText.setEnabled(false);
//        inputEditText.setTextColor(Color.parseColor(disableColor));
//        inputEditText.clearFocus();
//    }
}
