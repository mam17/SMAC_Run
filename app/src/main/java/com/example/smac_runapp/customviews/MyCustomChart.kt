package com.example.smac_runapp.customviews

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.smac_runapp.R
import com.example.smac_runapp.utils.Utils.getMaxValue
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.model.GradientColor

class MyCustomChart(
    private val context: Context?,
    private val barChart: BarChart,
    private val barEntriesList: ArrayList<BarEntry>,
    private val lsAxis: ArrayList<String>,
    private val maxYValue: Float
) {
    private lateinit var dataChart: BarData
    private lateinit var dataSet: BarDataSet


    fun setUp() {

        // set marker
        val mv = MyMarkerView(context!!, R.layout.my_marker)
        // gradient column
        val startColor = ContextCompat.getColor(context, R.color.gradient1)
        val endColor = ContextCompat.getColor(context, R.color.gradient3)
        val lsGradientColors: MutableList<GradientColor> = ArrayList()
        lsGradientColors.add(GradientColor(endColor, startColor))

        // data column
        dataSet = BarDataSet(barEntriesList, "").apply {
            setDrawValues(false)
            valueTextSize = 14f
            gradientColors = lsGradientColors
        }
        // width column
        dataChart = BarData(dataSet).apply {
            barWidth = 0.6f
        }
        // radius column
        val myBarChartRender = BorderChartBar(
            barChart,
            barChart.animator,
            barChart.viewPortHandler
        ).apply {
            setRadius(15)
        }
        // setup chart
        barChart.apply {
            data = dataChart
            legend.isEnabled = false
            description.isEnabled = false
            marker = mv
            isDoubleTapToZoomEnabled = false
//            renderer = myBarChartRender
            setBackgroundResource(R.drawable.bg_chartbar)
            setTouchEnabled(true)
            setScaleEnabled(true)
            setVisibleXRangeMaximum(7F)
        }
        // setup cot x ben trai
        barChart.axisLeft.apply {
            setDrawGridLines(false)
            setDrawAxisLine(true)
            setStartAtZero(true)
            textColor = Color.WHITE
            textSize = 12f
        }
        // setup cot y ben phai
        barChart.axisRight.apply {
            isEnabled = false
            setDrawGridLines(true)
        }

        var maxValue = maxYValue
        if (getMaxValue(barEntriesList) >= 4000f) {
            maxValue = getMaxValue(barEntriesList)
        }

        barChart.axisLeft.apply {
            setDrawGridLines(false)
            setDrawAxisLine(true)
            setStartAtZero(true)
            setAxisMaxValue(maxValue)
            valueFormatter = LargeValueFormatter()
            textColor = Color.WHITE
            textSize = 12f
        }

        // setup cot x
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = IndexAxisValueFormatter(lsAxis)
            textColor = Color.WHITE
            textSize = 12f
            setDrawAxisLine(false)
            setDrawGridLines(false)

        }
    }
}