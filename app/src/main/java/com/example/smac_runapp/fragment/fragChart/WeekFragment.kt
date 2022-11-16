package com.example.smac_runapp.fragment.fragChart

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.smac_runapp.R
import com.example.smac_runapp.TAG
import com.example.smac_runapp.customviews.MyCustomChart
import com.example.smac_runapp.databinding.FragmentWeekBinding
import com.example.smac_runapp.logger.Log
import com.example.smac_runapp.presenter.MonthPresenter
import com.example.smac_runapp.presenter.WeekPresenter
import com.example.smac_runapp.utils.Utils
import com.github.mikephil.charting.data.BarEntry
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class WeekFragment : Fragment() {


    lateinit var mView: View
    private lateinit var mBinding: FragmentWeekBinding
    lateinit var weekPresenter: WeekPresenter

    private lateinit var barEntriesList: ArrayList<BarEntry>
    private val lsAxis: ArrayList<String> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_week, container, false)
        weekPresenter = ViewModelProvider(this)[WeekPresenter::class.java]
        observerComponent()
        readData()
        mView = mBinding.root

        return mView
    }

    private fun observerComponent() {
//        weekPresenter.dataChar.observe(viewLifecycleOwner) {
//            displayChart(it.lsAxis, it.lsBarEntry)
//        }
    }

    private fun displayChart(
        lsAxis: ArrayList<String>,
        lsBarEntries: ArrayList<BarEntry>
    ) {
        MyCustomChart(context, mBinding.barChartWeek, lsBarEntries, lsAxis, 4000f)
            .setUp()
    }
    //Đọc tổng số bước hàng ngày hiện tại.
    private fun readData() {
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal[Calendar.HOUR_OF_DAY] = 23
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        val endTime = cal.timeInMillis

        cal.add(Calendar.DATE, -1)
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        val startTime = cal.timeInMillis

        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build()

        Fitness.getHistoryClient(this.requireActivity().applicationContext, GoogleSignIn.getAccountForExtension(this.requireActivity().applicationContext,
            Utils.fitnessOptions
        ))
            .readData(readRequest)
            .addOnSuccessListener { response ->
                for (dataSet in response.buckets.flatMap { it.dataSets }) {
                    for (dp in dataSet.dataPoints) {
                        for (field in dp.dataType.fields) {
                            val value = dp.getValue(field).asInt().toString()
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "There was an error reading data from Google Fit", e)
            }
    }

}