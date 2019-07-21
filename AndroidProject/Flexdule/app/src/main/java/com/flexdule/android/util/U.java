package com.flexdule.android.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class U {

    public static void changeBackgroundColor(Drawable background, String rgbColor) {
        if (background instanceof ShapeDrawable) {
            // cast to 'ShapeDrawable'
            ShapeDrawable shapeDrawable = (ShapeDrawable) background;
            shapeDrawable.getPaint().setColor(Color.parseColor("#" + rgbColor));
        } else if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(Color.parseColor("#" + rgbColor));
        } else if (background instanceof ColorDrawable) {
            // alpha value may need to be set again after this call
            ColorDrawable colorDrawable = (ColorDrawable) background;
            colorDrawable.setColor(Color.parseColor("#" + rgbColor));
        }
    }

    public static void toast(String msg, Context context){
        toast(msg,context, Toast.LENGTH_LONG);
    }

    public static void toast(String msg, Context context, int length ){
        Toast toast = Toast.makeText(context, msg, length);
        View view = toast.getView();

        //Gets the actual oval background of the Toast then sets the colour filter
        view.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

        //Gets the TextView from the Toast so it can be editted
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
//        text.setTypeface(text.getTypeface(), Typeface.BOLD);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        toast.show();
    }


}
