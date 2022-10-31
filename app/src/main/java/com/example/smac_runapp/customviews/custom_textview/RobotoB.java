package com.example.smac_runapp.customviews.custom_textview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class RobotoB extends AppCompatTextView {
    public RobotoB(@NonNull Context context) {
        super(context);
        setFontsTextView();
    }

    public RobotoB(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFontsTextView();
    }

    public RobotoB(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFontsTextView();
    }

    private void setFontsTextView(){
        Typeface typeface = Utils.getRobotoBTypeface(getContext());
        setTypeface(typeface);
    }
}
