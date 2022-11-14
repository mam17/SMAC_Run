package com.example.smac_runapp.fragment.fragChart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.smac_runapp.R
import com.example.smac_runapp.customviews.MyCustomChart
import com.example.smac_runapp.databinding.FragmentMonthBinding
import com.github.mikephil.charting.data.BarEntry


class MonthFragment : Fragment() {

    lateinit var mView: View
    private lateinit var mBinding: FragmentMonthBinding

    private lateinit var barEntriesList: ArrayList<BarEntry>
    private val lsAxis: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_month, container, false)
        mView = mBinding.root

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val setUpChart =
            activity?.let {
                MyCustomChart(
                    it.applicationContext,
                    mBinding.barChartMonth,
                    barEntriesList,
                    lsAxis,
                    4000f
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
        barEntriesList.add(BarEntry(1f, 1000f))
        barEntriesList.add(BarEntry(2f, 3500f))
        barEntriesList.add(BarEntry(3f, 2100f))
        barEntriesList.add(BarEntry(4f, 1100f))
        barEntriesList.add(BarEntry(5f, 3200f))
        barEntriesList.add(BarEntry(6f, 3300f))
        barEntriesList.add(BarEntry(7f, 3300f))
        barEntriesList.add(BarEntry(8f, 3300f))
        barEntriesList.add(BarEntry(9f, 3030f))
        barEntriesList.add(BarEntry(10f, 3300f))
        barEntriesList.add(BarEntry(11f, 2303f))
        barEntriesList.add(BarEntry(12f, 1010f))
    }

    private fun setAxis() {
        lsAxis.add("")
        lsAxis.add("M1")
        lsAxis.add("M2")
        lsAxis.add("M3")
        lsAxis.add("M4")
        lsAxis.add("M5")
        lsAxis.add("M6")
        lsAxis.add("M7")
        lsAxis.add("M8")
        lsAxis.add("M9")
        lsAxis.add("M10")
        lsAxis.add("M11")
        lsAxis.add("M12")
    }

}