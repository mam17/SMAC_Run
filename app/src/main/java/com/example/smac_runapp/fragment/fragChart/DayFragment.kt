package com.example.smac_runapp.fragment.fragChart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.smac_runapp.R
import com.example.smac_runapp.TAG
import com.example.smac_runapp.customviews.MyCustomChart
import com.example.smac_runapp.databinding.FragmentDayBinding
import com.example.smac_runapp.logger.Log
import com.example.smac_runapp.presenter.HomePresenter
import com.example.smac_runapp.utils.Utils.getAccount
import com.example.smac_runapp.utils.Utils.getTimeNow
import com.github.mikephil.charting.data.BarEntry
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class DayFragment : Fragment() {

    lateinit var mView: View
    private lateinit var mBinding: FragmentDayBinding
    private lateinit var viewModel: HomePresenter

    private lateinit var barEntriesList: ArrayList<BarEntry>
    private val lsAxis: ArrayList<String> = ArrayList()

    private var xFloat = 0f
    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_day, container, false)
        mView = mBinding.root

        return mView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBarChartData()
        setAxis()
        readData()
    }

    //Đọc tổng số bước hàng ngày hiện tại.
    private fun readData() {
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        val endtime = cal.timeInMillis

        cal.add(Calendar.DAY_OF_WEEK, -7)
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        val starttime = cal.timeInMillis

        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(starttime, endtime, TimeUnit.MILLISECONDS)
            .build()

        Fitness.getHistoryClient(this.requireActivity().applicationContext, GoogleSignIn.getAccountForExtension(this.requireActivity().applicationContext, fitnessOptions))
            .readData(readRequest)
            .addOnSuccessListener { response ->
                for (bucket in response.buckets){
                    //convert days in bucket to milliseconds
                    val days = bucket.getStartTime(TimeUnit.MILLISECONDS)
                    //convert milliseconds to date
                    val stepsDate = Date(days)
                    // add day vao ls
                    lsAxis.add(stepsDate.toString().substring(0, 4))
                    android.util.Log.e(TAG, "accessGoogleFit: $stepsDate")
                    xFloat++
                    for (dataSet in bucket.dataSets) {
                        barEntriesList.add(BarEntry(xFloat, dumpDataSet(dataSet)))
                        android.util.Log.e(TAG, "accessGoogleFit: ${dumpDataSet(dataSet)}")
                    }
                }
                getStepsByCurrentDay(lsAxis, barEntriesList)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "There was an error reading data from Google Fit", e)
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

    private fun getStepsByCurrentDay(lsAxis: java.util.ArrayList<String>, barEntriesList: ArrayList<BarEntry>) {


        android.util.Log.i(TAG, "getStepsByWeek: ${getTimeNow()}")

        Fitness.getHistoryClient(requireActivity(), getAccount(requireContext()))
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { dataSet ->
//                val days = getStartTime(TimeUnit.MILLISECONDS)
                val total = when {
                    dataSet.isEmpty -> 0
                    else -> dataSet.dataPoints.first().getValue(Field.FIELD_STEPS).asInt()
                }
                xFloat++
                lsAxis.add(getTimeNow().toString().substring(0, 4))
                barEntriesList.add(BarEntry(xFloat, total.toFloat()))
                displayChart(lsAxis, barEntriesList)
            }
            .addOnFailureListener { e ->
                android.util.Log.e(TAG, "There was a problem getting the step count.", e)
            }
    }

    private fun displayChart(lsAxis: java.util.ArrayList<String>, barEntriesList: ArrayList<BarEntry>) {
        MyCustomChart(context, mBinding.barChartDay, barEntriesList, lsAxis, 4000f)
            .setUp()
    }

    private fun getBarChartData() {
        barEntriesList = ArrayList()
        barEntriesList.add(BarEntry(1f, 100f))
        barEntriesList.add(BarEntry(2f, 350f))
        barEntriesList.add(BarEntry(3f, 210f))
        barEntriesList.add(BarEntry(4f, 110f))
        barEntriesList.add(BarEntry(5f, 320f))
        barEntriesList.add(BarEntry(6f, 330f))
        barEntriesList.add(BarEntry(7f, 330f))
    }

    private fun setAxis() {
        lsAxis.add("")
        lsAxis.add("Mon")
        lsAxis.add("Tue")
        lsAxis.add("Wed")
        lsAxis.add("Thu")
        lsAxis.add("Fri")
        lsAxis.add("Sat")
        lsAxis.add("Sun")
    }
}