package hu.logcontrol.wasteprogram.helpers;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import hu.logcontrol.wasteprogram.R;

public class TextWatcherHelper {

    public static void changeStateButton(ImageButton imageButton, Drawable drawable, boolean isEnabled){
        if(imageButton == null) return;
        if(drawable == null) return;

        imageButton.setBackground(drawable);
        imageButton.setEnabled(isEnabled);
    }

    public static void setElementsToBaseState(ConstraintLayout cl, EditText editText, boolean edittextEnabled, ImageButton imageButton, boolean imgButEnabled, String enableColor, Drawable imageBackground, Drawable constraintBackground){
        imageButton.setBackground(imageBackground);
        editText.setTextColor(Color.parseColor(enableColor));
        cl.setBackground(constraintBackground);

        imageButton.setEnabled(imgButEnabled);
        editText.setEnabled(edittextEnabled);
        editText.requestFocus();
    }

    public static void setElementsToFinishState(ConstraintLayout cl_1, ConstraintLayout cl_2, EditText tb_1, EditText tb_2, ImageButton imageButton, Drawable constraintBackground, Drawable enterButtonBackground){
        cl_2.setVisibility(View.VISIBLE);
        cl_1.setBackground(constraintBackground);

        tb_1.setEnabled(false);

        imageButton.setBackground(enterButtonBackground);
        imageButton.setEnabled(false);

        tb_2.setText("");
        if(!tb_2.isFocused()) tb_2.requestFocus();
    }

    public static void setLastElementsToFinishState(ConstraintLayout cl, EditText tb, ImageButton imageButton_1, ImageButton imageButton_2, Drawable clBackground, Drawable imageButton1Background, Drawable imageButton2Background){
        cl.setBackground(clBackground);

        tb.setEnabled(false);

        imageButton_1.setBackground(imageButton1Background);
        imageButton_1.setEnabled(false);

        imageButton_2.setBackground(imageButton2Background);
        imageButton_2.setEnabled(true);
        imageButton_2.setFocusableInTouchMode(true);

        if(!imageButton_2.isFocused()) imageButton_2.requestFocus(View.FOCUS_DOWN);
    }
}
