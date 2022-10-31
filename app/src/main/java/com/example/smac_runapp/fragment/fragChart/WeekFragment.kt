package com.example.smac_runapp.fragment.fragChart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.smac_runapp.R
import com.example.smac_runapp.customviews.MyCustomChart
import com.example.smac_runapp.databinding.FragmentDayBinding
import com.example.smac_runapp.databinding.FragmentMonthBinding
import com.example.smac_runapp.databinding.FragmentWeekBinding
import com.github.mikephil.charting.data.BarEntry

class WeekFragment : Fragment() {


    lateinit var mView: View
    private lateinit var mBinding: FragmentWeekBinding

    private lateinit var barEntriesList: ArrayList<BarEntry>
    private val lsAxis: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_week, container, false)
        mView = mBinding.root

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val setUpChart =
            activity?.let {
                MyCustomChart(
                    it.applicationContext,
                    mBinding.barChartWeek,
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

    private fun getBarChartData() {
        barEntriesList = ArrayList()
        barEntriesList.add(BarEntry(1f, 100f))
        barEntriesList.add(BarEntry(2f, 350f))
        barEntriesList.add(BarEntry(3f, 210f))
        barEntriesList.add(BarEntry(4f, 110f))
        barEntriesList.add(BarEntry(5f, 510f))
        barEntriesList.add(BarEntry(6f, 111f))
    }

    private fun setAxis() {
        lsAxis.add("")
        lsAxis.add("Week 1")
        lsAxis.add("Week 2")
        lsAxis.add("Week 3")
        lsAxis.add("Week 4")
        lsAxis.add("Week 5")
        lsAxis.add("Week 6")
    }


}