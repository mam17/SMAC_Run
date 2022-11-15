package com.example.smac_runapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.smac_runapp.databinding.ActivityMainBinding
import com.example.smac_runapp.fragment.EventFragment
import com.example.smac_runapp.fragment.HomeFragment
import com.example.smac_runapp.fragment.fragAwards.AwardFragment
import com.example.smac_runapp.interfaces.HomeInterface
import com.example.smac_runapp.logger.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.material.snackbar.Snackbar

const val TAG = "StepCounter"

enum class FitActionRequestCode {
    SUBSCRIBE,
    READ_DATA
}
class MainActivity : AppCompatActivity(), HomeInterface {
    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
        .build()

    private val runningQOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    private lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        reFragment(HomeFragment(this))

        checkPermissionsAndRun(FitActionRequestCode.SUBSCRIBE)

        mBinding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> reFragment(HomeFragment(this))
                R.id.action_event -> reFragment(EventFragment(this))
                else -> {
                }
            }
            true
        }

        mBinding.backToolbar.back.setOnClickListener {
            val f = supportFragmentManager.findFragmentById(R.id.frame)
            reFragment(HomeFragment(this))
        }

//        mBinding.backToolbar.noti.setOnClickListener{
//            val intent = Intent(this, MainActivity2::class.java)
//            startActivity(intent)
//        }
    }

    private fun reFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransient = fragmentManager.beginTransaction()

        fragmentTransient.replace(R.id.frame, fragment)
        fragmentTransient.commit()
    }

    override fun replaceReceive(fragment: Fragment) {
        super.replaceReceive(fragment)
        reFragment(AwardFragment())
    }
    //Kiểm tra xem người dùng đã đăng nhập chưa và nếu có, thực thi chức năng được chỉ định
    //Nếu người dùng là chưa đăng nhập, bắt đầu quy trình đăng nhập, chỉ định hàm sau đăng nhập để thực thi
    private fun checkPermissionsAndRun(fitActionRequestCode: FitActionRequestCode) {
        if (permissionApproved()) {
            fitSignIn(fitActionRequestCode)
        } else {
            requestRuntimePermissions(fitActionRequestCode)
        }
    }

    private fun fitSignIn(requestCode: FitActionRequestCode) {
        if (oAuthPermissionsApproved()) {
            performActionForRequestCode(requestCode)
        } else {
            requestCode.let {
                GoogleSignIn.requestPermissions(
                    this,
                    requestCode.ordinal,
                    getGoogleAccount(), fitnessOptions
                )
            }
        }
    }

    // Xử lý lệnh gọi lại từ luồng đăng nhập OAuth, thực hiện chức năng đăng nhập.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            RESULT_OK -> {
                val postSignInAction = FitActionRequestCode.values()[requestCode]
                postSignInAction.let {
                    performActionForRequestCode(postSignInAction)
                }
            }
            else -> oAuthErrorMsg(requestCode, resultCode)
        }
    }

    private fun performActionForRequestCode(requestCode: FitActionRequestCode) =
        when (requestCode) {
            FitActionRequestCode.READ_DATA -> readData()
            FitActionRequestCode.SUBSCRIBE -> subscribe()
        }

    private fun readData() {
        TODO("Not yet implemented")
    }

    private fun oAuthErrorMsg(requestCode: Int, resultCode: Int) {
        val message = """
            There was an error signing into Fit. Check the troubleshooting section of the README
            for potential issues.
            Request code was: $requestCode
            Result code was: $resultCode
        """.trimIndent()
        Log.e(TAG, message)
    }

    private fun oAuthPermissionsApproved() =
        GoogleSignIn.hasPermissions(getGoogleAccount(), fitnessOptions)

    private fun getGoogleAccount() = GoogleSignIn.getAccountForExtension(this, fitnessOptions)

    //Ghi lại dữ liệu bước bằng cách yêu cầu đăng ký dữ liệu bước nền
    private fun subscribe() {
        // To create a subscription, invoke the Recording API. As soon as the subscription is
        // active, fitness data will start recording.
        Fitness.getRecordingClient(this, getGoogleAccount())
            .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, "Successfully subscribed!")
                } else {
                    Log.w(TAG, "There was a problem subscribing.", task.exception)
                }
            }
    }

    override fun permissionApproved(): Boolean {
        val approved = if (runningQOrLater) {
            PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            )
        } else {
            true
        }
        return approved
    }

    private fun requestRuntimePermissions(requestCode: FitActionRequestCode) {
        val shouldProvideRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            )

        requestCode.let {
            if (shouldProvideRationale) {
                Log.i(TAG, "Displaying permission rationale to provide additional context.")
                Snackbar.make(
                    mBinding.frame,
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.ok) {
                        // Request permission
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                            requestCode.ordinal
                        )
                    }
                    .show()
            } else {
                Log.i(TAG, "Requesting permission")

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    requestCode.ordinal
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when {
            grantResults.isEmpty() -> {
                // If user interaction was interrupted, the permission request.
                // is cancelled and you receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.")
            }
            grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                // Permission was granted.
                val fitActionRequestCode = FitActionRequestCode.values()[requestCode]
                fitSignIn(fitActionRequestCode)
            }
            else -> {

                Snackbar.make(
                    mBinding.frame,
                    R.string.permission_denied_explanation,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.settings) {
                        // Build intent that displays the App settings screen.
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts(
                            "package",
                            BuildConfig.APPLICATION_ID, null
                        )
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    .show()
            }
        }
    }
}