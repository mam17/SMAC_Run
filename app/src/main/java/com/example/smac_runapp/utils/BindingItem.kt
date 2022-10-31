package com.example.smac_runapp.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter

object BindingItem {
    @JvmStatic
    @BindingAdapter("android:loadImage")
    fun loadImage(img: ImageView, url: Int?) {
        if (url != null) {
            img.setImageResource(url)
        }
    }
}