package com.example.smac_runapp.presenter

import android.annotation.SuppressLint
import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.smac_runapp.R
import com.example.smac_runapp.models.RawData
import com.example.smac_runapp.models.Receive
import com.example.smac_runapp.utils.Utils
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class AwardPresenter(
    application: Application
) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    val countTitles = ObservableField(0)
    private val cal = Calendar.getInstance()
    val lsMonthChallenger = MutableLiveData<ArrayList<Receive>>()

    @OptIn(DelicateCoroutinesApi::class)
    fun getStepsByMonth() {
        countTitles.set(0)
        val cal = Calendar.getInstance()

        val daysInMonth = Utils.getNumOfMonth(cal[Calendar.YEAR], cal[Calendar.MONTH] + 1)
        // lấy thời gian
        cal[Calendar.DATE] = 1
        cal[Calendar.MONTH] = 0
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        val startTimeReadRequest = cal.timeInMillis
        // end time
        cal[Calendar.DATE] = daysInMonth + 1
        cal[Calendar.MONTH] = 11
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        val endTimeReadRequest = cal.timeInMillis

        GlobalScope.launch(Dispatchers.Main) {
            val rawData = async { Utils.getData(context, startTimeReadRequest, endTimeReadRequest) }
            handleRawDataByMonth(rawData.await())
        }
    }

    private fun handleRawDataByMonth(lsRawData: ArrayList<RawData>) {
        val lsChallenger = ArrayList<Receive>()
//        lsAxis.add("")
        var start = 0
        var end = 0
        // loop 12 tháng để tính tổng steps từng tháng
        for (i in 1..12) {
            val dayOfMonth = Utils.getNumOfMonth(cal[Calendar.YEAR], i)
            var totalMonth = 0f
            end += dayOfMonth
            if (end >= lsRawData.size) {
                end = lsRawData.size - 1
            }
            var day: Long = 0
            // tính tổng số bước trong 1 tháng
            for (j in start..end) {
                totalMonth += lsRawData[j].step
                if (totalMonth >= Utils.MAX_MONTH && day == 0L) {
                    day = lsRawData[j].time
                }
            }

            if (day != 0L) {
                countTitles.set(countTitles.get()!! + 1)
            }
            // add challenger
            val icon = R.drawable.huy_huong1
            val title = "${Utils.getNameOfMonth(i).toString()} Challenger"

            lsChallenger.add(
                Receive(
                    icon,
                    title,
                    day,
                    totalMonth.toInt().toString(),
                    Utils.MAX_MONTH.toString()
                )
            )
            //
            start = end + 1
        }
        this.lsMonthChallenger.postValue(lsChallenger)
    }


}