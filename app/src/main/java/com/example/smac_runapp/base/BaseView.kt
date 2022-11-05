package com.example.smac_runapp.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.util.logging.Logger

abstract class BaseView<T> {
    lateinit var mContext: Context
    lateinit var mActivity: AppCompatActivity
    var mView: T = TODO()

    fun BasePresenterForm(mContext: Context, mView: T) {
        this.mContext = mContext
        this.mView = mView
        mActivity = mContext as AppCompatActivity
        try {
            initData()
        } catch (e: Exception) {
            Logger.getLogger(this.javaClass.toString(), e.toString())
//            Logger.log(this.javaClass, e)
        }
    }

    protected abstract fun initData()

    fun onCancel() {}

    fun unSubscription() {}

}