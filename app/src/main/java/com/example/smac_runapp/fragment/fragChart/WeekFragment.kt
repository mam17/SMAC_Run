package com.example.smac_runapp.fragment.fragChart

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.smac_runapp.R
import com.example.smac_runapp.TAG
import com.example.smac_runapp.customviews.MyCustomChart
import com.example.smac_runapp.databinding.FragmentWeekBinding
import com.example.smac_runapp.logger.Log
import com.example.smac_runapp.presenter.WeekPresenter
import com.example.smac_runapp.utils.Utils
import com.github.mikephil.charting.data.BarEntry
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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
        weekPresenter.getStepsByWeekOfMonth()
//        fake()
        mView = mBinding.root

        return mView
    }

    private fun observerComponent() {
        weekPresenter.dataChartByWeekOfMonth.observe(viewLifecycleOwner) {
            displayChart(it.lsAxis, it.lsBarEntry)
        }
    }

    private fun displayChart(
        lsAxis: ArrayList<String>,
        lsBarEntries: ArrayList<BarEntry>
    ) {
        MyCustomChart(context, mBinding.barChartWeek, lsBarEntries, lsAxis, 4000f)
            .setUp()
    }

}