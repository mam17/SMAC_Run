package com.example.smac_runapp.fragment.fragChart

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.smac_runapp.R
import com.example.smac_runapp.TAG
import com.example.smac_runapp.customviews.MyCustomChart
import com.example.smac_runapp.databinding.FragmentDayBinding
import com.example.smac_runapp.logger.Log
import com.example.smac_runapp.models.DataChart
import com.example.smac_runapp.presenter.DayPresenter
import com.example.smac_runapp.presenter.HomePresenter
import com.example.smac_runapp.utils.Utils
import com.example.smac_runapp.utils.Utils.dumpDataSet
import com.example.smac_runapp.utils.Utils.getAccount
import com.example.smac_runapp.utils.Utils.getTimeNow
import com.github.mikephil.charting.data.BarEntry
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import java.text.DateFormat
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class DayFragment : Fragment() {

    lateinit var mView: View
    private lateinit var mBinding: FragmentDayBinding
    val dataChar = MutableLiveData<DataChart>()
    private lateinit var dayPresenter: DayPresenter
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_day, container, false)

        mView = mBinding.root
        dayPresenter = ViewModelProvider(this)[DayPresenter::class.java]
        dayPresenter.getStepsByWeek()
        observerChart()
        return mView
    }

    private fun observerChart() {
        dayPresenter.dataChar.observe(viewLifecycleOwner) {
            displayChart(it.lsAxis, it.lsBarEntry)
        }
    }

    private fun displayChart(lsAxis: ArrayList<String>, lsBarEntries: ArrayList<BarEntry>) {
        MyCustomChart(context, mBinding.barChartDay, lsBarEntries, lsAxis, 4000f)
            .setUp()
    }
}