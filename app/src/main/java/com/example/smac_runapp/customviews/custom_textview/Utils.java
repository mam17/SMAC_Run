package com.example.smac_runapp.customviews.custom_textview;

import android.content.Context;
import android.graphics.Typeface;

public class Utils {
    private static Typeface robotoBTypeface;
    private static Typeface robotoITypeface;
    private static Typeface robotoLTypeface;
    private static Typeface robotoMTypeface;
    private static Typeface robotoCTypeface;

    public static Typeface getRobotoBTypeface(Context context) {
        if (robotoBTypeface == null){
            robotoBTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
        }
        return robotoBTypeface;
    }

    public static Typeface getRobotoITypeface(Context context) {
        if (robotoITypeface == null){
            robotoITypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Italic.ttf");
        }
        return robotoITypeface;
    }

    public static Typeface getRobotoLTypeface(Context context) {
        if (robotoLTypeface == null){
            robotoLTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        }
        return robotoLTypeface;
    }

    public static Typeface getRobotoMTypeface(Context context) {
        if (robotoMTypeface == null){
            robotoMTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
        }
        return robotoMTypeface;
    }

    public static Typeface getRobotoCTypeface(Context context) {
        if (robotoCTypeface == null){
            robotoCTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Italic.ttf");
        }
        return robotoCTypeface;
    }
}
