package com.example.smac_runapp.presenter

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.smac_runapp.models.DataChart
import com.example.smac_runapp.utils.Utils
import com.github.mikephil.charting.data.BarEntry
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.*
import java.util.concurrent.TimeUnit

class WeekPresenter(
    application: Application
) : AndroidViewModel(application) {

}