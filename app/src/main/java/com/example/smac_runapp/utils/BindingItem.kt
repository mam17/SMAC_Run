package com.example.smac_runapp.utils

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.smac_runapp.models.Receive

object BindingItem {
    @JvmStatic
    @BindingAdapter("android:loadImage")
    fun loadImage(img: ImageView, url: Int?) {
        if (url != null) {
            img.setImageResource(url)
        }
    }

    @JvmStatic
    @BindingAdapter("android:showProgress")
    fun showProgress(img: ImageView, item: Receive) {
        val progressAfter = item.progress.toInt()
        val maxAfter = item.max.toInt()
        if (progressAfter / maxAfter < 1) {
            img.alpha = 0.3f
        }
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter("android:showText")
    fun showText(tv: TextView, item: Receive) {
        val progressAfter = item.progress.toInt()
        val maxAfter = item.max.toInt()
        if (progressAfter / maxAfter < 1) {
            tv.text = "${item.progress}k/${item.max}k"
        } else {
            tv.text = item.date.toString()
        }
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter("android:countTitlesMonthChallenger")
    fun countTitlesMonthChallenger(tv: TextView, count: Int) {
        tv.text = "$count/12 Titles"
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter("android:countTitlesAccumulateChallenger")
    fun countTitlesAccumulateChallenger(tv: TextView, count: Int) {
        tv.text = "$count/9 Titles"
    }

}