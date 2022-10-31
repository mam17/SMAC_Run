package com.example.smac_runapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smac_runapp.R
import com.example.smac_runapp.adapter.ReceiveAdapter
import com.example.smac_runapp.adapter.TabLayoutAdapter
import com.example.smac_runapp.customviews.SpacesItemDecoration
import com.example.smac_runapp.databinding.FragmentHomeBinding
import com.example.smac_runapp.fragment.fragAwards.AwardFragment
import com.example.smac_runapp.interfaces.HomeBack
import com.example.smac_runapp.models.Receive
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment(private val goToHome: HomeBack) : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding
    private var myAdapter = ReceiveAdapter(arrayListOf(),0)
    private var lsReceive: ArrayList<Receive> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.seekbar.indicatorPositions = listOf(0F, 0.2F, 0.4F, 0.85F)
        mBinding.seekbar.indicatorText = listOf("0", "500", "1000", "4000")
        setupViewPager()
        setTabLayout()
        setReceive()
        setUpRcv()
        mBinding.viewAll.setOnClickListener {
            goToHome.replaceReceive(AwardFragment())
//            val fragmentManager = activity?.supportFragmentManager
//            fragmentManager?.beginTransaction()
//                ?.replace(R.id.frame, AwardFragment())
//                ?.addToBackStack(null)
//                ?.commit()
        }

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

}