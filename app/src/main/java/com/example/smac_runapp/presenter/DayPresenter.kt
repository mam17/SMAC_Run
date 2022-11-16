package com.example.smac_runapp.presenter

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableArrayList
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
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class DayPresenter(
    application: Application
) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    val dataChar = MutableLiveData<DataChart>()
    private val lsAxis = ObservableArrayList<String>()
    private val lsBarEntry = ObservableArrayList<BarEntry>()
    private var xFloat = 0

    private fun getStepsByCurrentDay(lsXAxis: ArrayList<String>, lsBarEntry: ArrayList<BarEntry>) {
        Fitness.getHistoryClient(context.applicationContext, Utils.getAccount(context))
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { dataSet ->
                val total = when {
                    dataSet.isEmpty -> 0
                    else -> dataSet.dataPoints.first().getValue(Field.FIELD_STEPS).asInt()
                }
                xFloat++
                lsXAxis.add(Utils.getTimeNow().toString().substring(0, 4))
                lsBarEntry.add(BarEntry(xFloat.toFloat(), total.toFloat()))
                val barEntry = DataChart(lsXAxis, lsBarEntry)
                dataChar.value = barEntry
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "There was a problem getting the step count.", e)
            }
    }
    @RequiresApi(Build.VERSION_CODES.O)
     fun getStepsByWeek() {
        val lsXAxis = ArrayList<String>()
        val lsBarEntry = ArrayList<BarEntry>()

        lsXAxis.add("")
//        //lấy step 1 tuần trc
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        val endTime = cal.timeInMillis

        cal.add(Calendar.DAY_OF_WEEK, -6)
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        val startTime = cal.timeInMillis

        Fitness.getHistoryClient(context, Utils.getAccount(context))
            .readData(Utils.getReadRequestData(startTime, endTime))
            .addOnSuccessListener { response ->
                for (bucket in response.buckets) {
                    //convert days in bucket to milliseconds
                    val days = bucket.getStartTime(TimeUnit.MILLISECONDS)
                    //convert milliseconds to date
                    val stepsDate = Date(days)
                    // add day vao ls
                    lsXAxis.add(stepsDate.toString().substring(0, 4))
                    Log.e(TAG, "accessGoogleFit: $stepsDate")
                    xFloat++
                    for (dataSet in bucket.dataSets) {
                        lsBarEntry.add(
                            BarEntry(
                                xFloat.toFloat(),
                                dumpDataSet(dataSet)
                            )
                        )
                        Log.e(
                            TAG,
                            "accessGoogleFit: ${dumpDataSet(dataSet)}"
                        )
                    }
                }
                getStepsByCurrentDay(lsXAxis, lsBarEntry)
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "OnFailure()", e)
            }
    }

    private fun dumpDataSet(dataSet: DataSet): Float {
        var totalSteps = 0f
        for (dp in dataSet.dataPoints) {
            for (field in dp.dataType.fields) {
                totalSteps += dp.getValue(field).asInt()
            }
        }
        return totalSteps
    }

}