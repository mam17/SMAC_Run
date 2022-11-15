package com.example.smac_runapp.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.smac_runapp.R
import com.example.smac_runapp.TAG
import com.example.smac_runapp.adapter.ReceiveAdapter
import com.example.smac_runapp.adapter.TabLayoutAdapter
import com.example.smac_runapp.customviews.MySeekBar
import com.example.smac_runapp.customviews.SpacesItemDecoration
import com.example.smac_runapp.databinding.FragmentHomeBinding
import com.example.smac_runapp.fragment.fragAwards.AwardFragment
import com.example.smac_runapp.interfaces.HomeInterface
import com.example.smac_runapp.logger.Log
import com.example.smac_runapp.models.Receive
import com.example.smac_runapp.models.customView.ReceiveSeekBar
import com.example.smac_runapp.presenter.HomePresenter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class HomeFragment(private val goToHome: HomeInterface) : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding
    private var myAdapter = ReceiveAdapter(arrayListOf(),0)
    private var lsIconReceive = ArrayList<ReceiveSeekBar>()
    private var lsReceive: ArrayList<Receive> = ArrayList()
    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
        .build()

    private var fragment: Fragment? = null
    private lateinit var homePresenter: HomePresenter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)

        homePresenter = ViewModelProvider(this)[HomePresenter::class.java]
        mBinding.presenter = homePresenter
        homePresenter.checkPermission(requireActivity())
        observerComponent()
        return mBinding.root
    }

    private fun observerComponent() {
        homePresenter.process.observe(viewLifecycleOwner) {
            mBinding.seekbar.progress = it / 4500f
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSeekbar()
        setupViewPager()
        setTabLayout()
        setReceive()
        setUpRcv()
        readData()
        replaceBack()
        seekBarOnClick()
        setupMySeekBar()
    }

    private fun seekBarOnClick() {
        mBinding.seekbar.setOnClickListener(object : MySeekBar.OnClickBitmapReceive {
            override fun clickItem(index: Int) {
                /**
                 * 1, neu isDisable = false => CLICK
                 * 2, update trạng thái của lsIconReceive[index] isisDisable = true
                 * 3, truyền ls mơi update vào MySeekBar => update lại view
                 */
                if (!lsIconReceive[index].isDisable) {
                    lsIconReceive[index].isDisable = !lsIconReceive[index].isDisable
                    Log.e("CLICKED  ", "clickItem: $index")

                    // Xử lí logic click button ....

                    updateSeekBar(lsIconReceive)
                }
            }
        })

    }

    /**
     * Update lại seekbar
     */
    private fun updateSeekBar(lsIconReceive: ArrayList<ReceiveSeekBar>) {
        mBinding.seekbar.indicatorBitmapReceive = lsIconReceive
        mBinding.seekbar.invalidate()
    }

    /**
     * Add vị trí của indicator
     * Add text
     */
    private fun setupMySeekBar() {
        addIconReceive()
        mBinding.seekbar.indicatorPositions = listOf(0F, 0.20F, 0.40F, 0.85F)
        mBinding.seekbar.indicatorText = listOf("0", "500", "1000", "4000")
        mBinding.seekbar.indicatorBitmapReceive = lsIconReceive
    }

    private fun addIconReceive() {
        lsIconReceive.add(ReceiveSeekBar(0, true))
        lsIconReceive.add(ReceiveSeekBar(R.drawable.receive1, false))
        lsIconReceive.add(ReceiveSeekBar(R.drawable.receive2, false))
        lsIconReceive.add(ReceiveSeekBar(R.drawable.receive3, false))
    }

    private fun setupSeekbar() {
        mBinding.seekbar.indicatorPositions = listOf(0F, 0.2F, 0.4F, 0.85F)
        mBinding.seekbar.indicatorText = listOf("0", "500", "1000", "4000")
    }

    private fun setUpRcv() {
        mBinding.rcv.apply {
            adapter = myAdapter
            addItemDecoration(SpacesItemDecoration(10))
            setHasFixedSize(true)
        }
    }

    private fun setReceive() {

        lsReceive.add(Receive(R.drawable.huy_chuong2, "Spectacular Breakout","17/10/2022", true))
        lsReceive.add(Receive(R.drawable.huy_huong1, "October Challenger","17/10/2022", true))
        lsReceive.add(Receive(R.drawable.huy_chuong3, "Step to Mars ","17/10/2022", true))
        lsReceive.add(Receive(R.drawable.huy_chuong4, "August Challenger","17/10/2022", true))
        lsReceive.add(Receive(R.drawable.huy_chuong2, "Spectacular Breakout","17/10/2022", true))
        lsReceive.add(Receive(R.drawable.huy_huong1, "October Challenger","17/10/2022",true))
        lsReceive.add(Receive(R.drawable.huy_chuong3, "Step to Mars ","17/10/2022",true))
        lsReceive.add(Receive(R.drawable.huy_chuong4, "August Challenger","17/10/2022", true))

        myAdapter.addData(lsReceive)
    }

    private fun setTabLayout() {
        TabLayoutMediator(
            mBinding.layoutTab, mBinding.viewpage
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = resources.getString(R.string.day)
                }
                1 -> {
                    tab.text = resources.getString(R.string.week)
                }
                2 -> {
                    tab.text = resources.getString(R.string.month)
                }
            }
        }.attach()
    }

    private fun setupViewPager() {
        val adapter = activity?.let { TabLayoutAdapter(it) }
        mBinding.viewpage.adapter = adapter
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

        Fitness.getHistoryClient(this.requireActivity().applicationContext, GoogleSignIn.getAccountForExtension(this.requireActivity().applicationContext, fitnessOptions))
            .readData(readRequest)
            .addOnSuccessListener { response ->
                for (dataSet in response.buckets.flatMap { it.dataSets }) {
                    for (dp in dataSet.dataPoints) {
                        for (field in dp.dataType.fields) {
                            val value = dp.getValue(field).asInt().toString()
                            numSteps.text = value
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "There was an error reading data from Google Fit", e)
            }
    }

    private fun replaceBack() {
        mBinding.viewAll.setOnClickListener {
            goToHome.replaceReceive(AwardFragment())
        }
    }
}