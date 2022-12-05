package com.example.smac_runapp.presenter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.smac_runapp.utils.FitRequestCode
import com.example.smac_runapp.utils.Utils
import com.example.smac_runapp.utils.Utils.fitnessOptions
import com.example.smac_runapp.utils.Utils.getAccount
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import kotlinx.coroutines.*
import java.util.*

const val TAG = "HomePresenter"

class HomePresenter(
    application: Application
) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private var xFloat = 0

    val totalSteps = ObservableField<Int>()
    val process = MutableLiveData<Float>()


    @RequiresApi(Build.VERSION_CODES.O)
    fun checkPermission(activity: Activity) {
        totalSteps.set(0)
        process.value = 0f
        if (!GoogleSignIn.hasPermissions(getAccount(context), fitnessOptions)) {
            Log.e(
                TAG,
                "checkPermission: ${
                    GoogleSignIn.hasPermissions(
                        getAccount(context),
                        fitnessOptions
                    )
                }"
            )
            GoogleSignIn.requestPermissions(
                activity,
                FitRequestCode.GG_FIT_REQUEST_CODE.ordinal,
                getAccount(context),
                fitnessOptions
            )
        } else {
            recordingClient()
            getStepsToDay()
        }
    }

    private fun recordingClient() {
        Fitness.getRecordingClient(
            context.applicationContext,
            GoogleSignIn.getAccountForExtension(context.applicationContext, fitnessOptions)
        )
            .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
            .addOnSuccessListener {
                Log.i(TAG, "Subscription was successful!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "There was a problem subscribing ", e)
            }
    }

    private fun getStepsByCurrentDay() {
        Fitness.getHistoryClient(context.applicationContext, getAccount(context))
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { dataSet ->
                val total = when {
                    dataSet.isEmpty -> 0
                    else -> dataSet.dataPoints.first().getValue(Field.FIELD_STEPS).asInt()
                }
                xFloat++
                totalSteps.set(total)
                process.value = total.toFloat()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "There was a problem getting the step count.", e)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
     fun getStepsToDay() {
     //lấy step 1 tuần trc
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        val endTime = cal.timeInMillis

        cal.add(Calendar.DAY_OF_WEEK, -6)
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        val startTime = cal.timeInMillis

        Fitness.getHistoryClient(context, getAccount(context))
            .readData(Utils.getReadRequestData(startTime, endTime))
            .addOnSuccessListener {
                getStepsByCurrentDay()

            }
            .addOnFailureListener { e ->
                Log.d(TAG, "OnFailure()", e)
            }
    }



}