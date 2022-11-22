package com.example.smac_runapp.presenter

import android.annotation.SuppressLint
import android.app.Application
import android.icu.util.Calendar.*
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.smac_runapp.logger.Log
import com.example.smac_runapp.models.DataChart
import com.example.smac_runapp.models.RawData
import com.example.smac_runapp.utils.Utils
import com.example.smac_runapp.utils.Utils.getAccount
import com.example.smac_runapp.utils.Utils.getReadRequestData
import com.github.mikephil.charting.data.BarEntry
import com.google.android.gms.fitness.Fitness
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class WeekPresenter(
    application: Application
) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    val dataChartByWeekOfMonth = MutableLiveData<DataChart>()
    private val lsAxis = ObservableArrayList<String>()
    private val cal = Calendar.getInstance()
    private val currentMonth = cal[Calendar.MONTH] + 1

    init {
        lsAxis.add("")
    }

    fun getStepsByWeekOfMonth() {
        // lấy thời gian
        val cal = Calendar.getInstance()

        val day = Utils.getNumOfMonth(cal[Calendar.YEAR], currentMonth)
        cal[Calendar.DATE] = day
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        val endTime = cal.timeInMillis
        // start time
        cal[Calendar.DATE] = 1
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        val startTime = cal.timeInMillis


        GlobalScope.launch(Dispatchers.Main) {
            try {
                val lsRawData = async { Utils.getData(context, startTime, endTime) }
                handlerRawDataByWeekOfMonth(lsRawData.await())
            } catch (e: Exception) {
                Log.e(TAG, "getStepsByWeek: err :${e}")
            }
        }
    }

    private fun handlerRawDataByWeekOfMonth(lsRawData: ArrayList<RawData>) {
        val lsAxis = ArrayList<String>()
        val lsBarEntry = ArrayList<BarEntry>()

        var day = 6
        var start = 0
        // lặp 1 tháng, tính tổng 1 tuần
        for (i in 0..4) {
            var totalWeek = 0f
            if (day >= lsRawData.size) {
                day = lsRawData.size - 1
            }
            // tỉnh tổng steps từng tuần
            for (j in start..day) {
                totalWeek += lsRawData[j].step
            }
            val time = Utils.convertTimeRequestToShort(lsRawData[start].time, lsRawData[day].time)
            lsAxis.add(time)
            lsBarEntry.add(BarEntry((i).toFloat(), totalWeek))

            start = day + 1
            day += 7
        }

        lsBarEntry.forEachIndexed { index, barEntry ->
            Log.e(TAG, "handlerRawDataByWeekOfMonth: ${barEntry.y} --- ${lsAxis[index]}")
        }
        dataChartByWeekOfMonth.postValue(DataChart(lsAxis, lsBarEntry))
    }

}