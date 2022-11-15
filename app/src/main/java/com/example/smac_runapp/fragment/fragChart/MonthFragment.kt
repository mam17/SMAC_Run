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
import com.example.smac_runapp.customviews.MyCustomChart
import com.example.smac_runapp.databinding.FragmentMonthBinding
import com.example.smac_runapp.presenter.HomePresenter
import com.example.smac_runapp.presenter.MonthPresenter
import com.github.mikephil.charting.data.BarEntry


class MonthFragment : Fragment() {

    lateinit var mView: View
    private lateinit var mBinding: FragmentMonthBinding
    private lateinit var monthPresenter: MonthPresenter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_month, container, false)
        monthPresenter = ViewModelProvider(this)[MonthPresenter::class.java]
        monthPresenter.getStepsByMonth()
        observerComponent()
        mView = mBinding.root

        return mView
    }

    private fun observerComponent() {
        monthPresenter.dataChar.observe(viewLifecycleOwner) {
            displayChart(it.lsAxis, it.lsBarEntry)
        }
    }

    private fun displayChart(
        lsAxis: ArrayList<String>,
        lsBarEntries: ArrayList<BarEntry>
    ) {
        MyCustomChart(context, mBinding.barChartMonth, lsBarEntries, lsAxis, 4000f)
            .setUp()
    }

}