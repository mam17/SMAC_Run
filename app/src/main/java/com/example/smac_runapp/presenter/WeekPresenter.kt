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

    val dataChart = MutableLiveData<DataChart>()
    private val lsAxis = ObservableArrayList<String>()
    private val lsBarEntry = ObservableArrayList<BarEntry>()
    private val cal = Calendar.getInstance()
    private val currentYear = cal[Calendar.YEAR]
    init {
        lsAxis.add("")
    }

    @SuppressLint("SimpleDateFormat")
     fun getStartEndOFWeek() {
        
        val currentMonth = cal[Calendar.MONTH]
        cal[Calendar.DATE] = 1
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        val startTime = cal.timeInMillis
        // end time
        val day = Utils.getNumOfMonth(cal[Calendar.YEAR], currentMonth)

        cal[Calendar.DATE] = day
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        val endTime = cal.timeInMillis
        
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val lsRawData = async { Utils.getData(context, startTime, endTime) }
                handlerRawDataByWeekOfMonth(lsRawData.await())
            } catch (e: Exception) {
                android.util.Log.e(TAG, "getStepsByWeek: err :${e}")
            }
        }
    }

    private fun handlerRawDataByWeekOfMonth(lsRawData: ArrayList<RawData>) {
        val lsAxis = ArrayList<String>()
        val lsBarEntry = ArrayList<BarEntry>()
        lsAxis.add("")

        var day = 6
        var start = 0
        for (i in 0..4) {
            var totalWeek = 0f
            if (day >= lsRawData.size) {
                day = lsRawData.size - 1
            }
            for (j in start..day) {
                totalWeek += lsRawData[j].step
            }
            val time = Utils.convertTimeRequestToShort(lsRawData[start].time, lsRawData[day].time)
            android.util.Log.e(TAG, "handlerRawDataByWeekOfMonth: time $time")
            lsAxis.add(time)
//            lsAxis.add((i + 1).toString())
            lsBarEntry.add(BarEntry(i + 2f, totalWeek))
            dataChart.postValue(DataChart(lsAxis, lsBarEntry))

            start = day + 1
            day += 7

        }
    }


    private fun getTotalStepWeek(startTime: Long, endTime: Long, i: Int) {
        var weely = 0f
        Fitness.getHistoryClient(context.applicationContext,getAccount(context))
            .readData(getReadRequestData(startTime , endTime))
            .addOnSuccessListener { response ->
                // 1 tuần
                //convert milliseconds to date
                val stepsDate = Date(response.buckets[0].getStartTime(TimeUnit.MILLISECONDS))
                for (bucket in response.buckets) {
                    var totalDay = 0f
                    for (dataSet in bucket.dataSets) {
                        totalDay +=
                            Utils.dumpDataSet(dataSet)
                    }
                    weely += totalDay
                }

                lsAxis.add(stepsDate.toString().substring(4, 7))
                lsBarEntry.add(BarEntry(i.toFloat(), weely))
                dataChart.value = DataChart(lsAxis, lsBarEntry)
            }
            .addOnFailureListener { e -> Log.d(TAG, "OnFailure()", e) }
    }
}