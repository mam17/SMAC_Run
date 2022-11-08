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
import com.github.mikephil.charting.data.BarEntry
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class DayFragment : Fragment() {

    lateinit var mView: View
    private lateinit var mBinding: FragmentDayBinding

    private lateinit var barEntriesList: ArrayList<BarEntry>
    private val lsAxis: ArrayList<String> = ArrayList()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val setUpChart =
            activity?.let {
                MyCustomChart(
                    it.applicationContext,
                    mBinding.barChartDay,
                    barEntriesList,
                    lsAxis
                )
            }
        setUpChart?.setUp()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBarChartData()
        setAxis()

    }

    //Đọc tổng số bước hàng ngày hiện tại.
    private fun readData() {
//        val cal: Calendar = Calendar.getInstance()
//        val now = Date()
//        cal.time = now
//        val endtime: Long = cal.timeInMillis
//        cal.add(Calendar.DAY_OF_WEEK, -7)
//        val starttime: Long = cal.timeInMillis
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        val endtime = cal.timeInMillis

        cal.add(Calendar.DATE, -1)
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
                for (dataSet in response.buckets.flatMap { it.dataSets }) {
                    for (dp in dataSet.dataPoints) {
                        for (field in dp.dataType.fields) {
                            val value = dp.getValue(field).asInt().toString()
                            numSteps.text = value
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "There was an error reading data from Google Fit", e)
            }

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