package com.example.smac_runapp.fragment.fragAwards

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.smac_runapp.R
import com.example.smac_runapp.adapter.ReceiveAdapter
import com.example.smac_runapp.databinding.AwardDialogBinding
import com.example.smac_runapp.databinding.FragmentAwardBinding
import com.example.smac_runapp.models.RawData
import com.example.smac_runapp.models.Receive
import com.example.smac_runapp.presenter.AwardPresenter
import com.example.smac_runapp.presenter.DayPresenter
import com.example.smac_runapp.presenter.HomePresenter
import com.example.smac_runapp.utils.Utils
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class AwardFragment : Fragment() {

    private var mBinding: FragmentAwardBinding? = null
    private val binding get() = mBinding!!
    private var myAdapterMonthChallenger = ReceiveAdapter(arrayListOf(), 1)
    private var myAdapterAccumulatingSteps = ReceiveAdapter(arrayListOf(), 1)
    private val lsChallenger = ArrayList<Receive>()
    private lateinit var awardPresenter: AwardPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_award, container, false
        )
        awardPresenter = ViewModelProvider(this)[AwardPresenter::class.java]
        awardPresenter.getStepsByMonth()
        setUpRcv()
        showAlertDialog()
        observer()
        return binding.root
    }

    private fun observer() {
        awardPresenter.lsMonthChallenger.observe(viewLifecycleOwner) {
            lsChallenger.addAll(it)
            myAdapterMonthChallenger.addData(it)
        }
    }

    private fun setUpRcv() {
        binding.rcv.apply {
            adapter = myAdapterMonthChallenger
            setHasFixedSize(true)
        }
        //
        binding.rcv2.apply {
            adapter = myAdapterAccumulatingSteps
            setHasFixedSize(true)
        }
        myAdapterAccumulatingSteps.addData(addLsAccumulatingSteps())
    }

    private fun showAlertDialog() {
        val dialog = activity?.let { Dialog(it) }
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val binding =
            DataBindingUtil.inflate<AwardDialogBinding>(
                LayoutInflater.from(activity?.applicationContext),
                R.layout.award_dialog,
                null,
                false
            )
        dialog?.setContentView(binding.root)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()
        binding.btnShare.setOnClickListener { _ ->
            dialog?.dismiss()
        }
        binding.btnClose.setOnClickListener { _ ->
            dialog?.dismiss()
        }


    }

    private fun addLsAccumulatingSteps(): ArrayList<Receive> {
        val lsAccumulatingSteps = ArrayList<Receive>()
        lsAccumulatingSteps.add(
            Receive(
                R.drawable.huy_huong1,
                Utils.lsAccumulateChallenger[0],
                0,
                "20",
                "120"
            )
        )
        lsAccumulatingSteps.add(
            Receive(
                R.drawable.huy_huong1,
                Utils.lsAccumulateChallenger[1],
                0,
                "10",
                "120"
            )
        )

        lsAccumulatingSteps.add(
            Receive(
                R.drawable.huy_huong1,
                Utils.lsAccumulateChallenger[2],
                1669827600776,
                "120",
                "120"
            )
        )
        lsAccumulatingSteps.add(
            Receive(
                R.drawable.huy_huong1,
                Utils.lsAccumulateChallenger[3],
                0,
                "20",
                "120"
            )
        )

        lsAccumulatingSteps.add(
            Receive(
                R.drawable.huy_huong1,
                Utils.lsAccumulateChallenger[4],
                1669827600776,
                "120",
                "120"
            )
        )
        lsAccumulatingSteps.add(
            Receive(
                R.drawable.huy_huong1,
                Utils.lsAccumulateChallenger[5],
                1669827600776,
                "120",
                "120"
            )
        )

        lsAccumulatingSteps.add(
            Receive(
                R.drawable.huy_huong1,
                Utils.lsAccumulateChallenger[6],
                0,
                "20",
                "120"
            )
        )
        lsAccumulatingSteps.add(
            Receive(
                R.drawable.huy_huong1,
                Utils.lsAccumulateChallenger[7],
                1669827600776,
                "120",
                "120"
            )
        )

        lsAccumulatingSteps.add(
            Receive(
                R.drawable.huy_huong1,
                Utils.lsAccumulateChallenger[8],
                0,
                "10",
                "120"
            )
        )
        return lsAccumulatingSteps
    }
}