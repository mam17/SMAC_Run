package com.example.smac_runapp.fragment.fragAwards

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.smac_runapp.R
import com.example.smac_runapp.adapter.ReceiveAdapter
import com.example.smac_runapp.databinding.AwardDialogBinding
import com.example.smac_runapp.databinding.FragmentAwardBinding
import com.example.smac_runapp.models.Receive
import com.example.smac_runapp.presenter.HomePresenter
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class AwardFragment : Fragment() {
    private var mBinding: FragmentAwardBinding? = null
    private val binding get() = mBinding!!
    private var myAdapterMonthChallenger = ReceiveAdapter(arrayListOf(), 1)
    private var myAdapterAccumulatingSteps = ReceiveAdapter(arrayListOf(), 1)
    private lateinit var homePresenter: HomePresenter
    private val lsMonthChallenger = ArrayList<Receive>()
    private val lsAccumulateCha = ArrayList<Receive>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAwardBinding.inflate(inflater, container, false)
        homePresenter = ViewModelProvider(requireActivity())[HomePresenter::class.java]
        mBinding!!.item = homePresenter
        observer()
        setUpRcv()
        showAlertDialog()
        return binding.root
    }

    private fun observer() {

        homePresenter.lsMonthChallenger.observe(viewLifecycleOwner) {
            lsMonthChallenger.addAll(it)
            myAdapterMonthChallenger.addData(it)
        }

        homePresenter.accumulateChallenger.observe(viewLifecycleOwner) {
            lsAccumulateCha.addAll(it)
            myAdapterAccumulatingSteps.addData(it)
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
}