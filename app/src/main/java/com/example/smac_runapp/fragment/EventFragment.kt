package com.example.smac_runapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smac_runapp.databinding.FragmentEventBinding
import com.example.smac_runapp.interfaces.HomeInterface


class EventFragment(private val goHome: HomeInterface) : Fragment() {
    private lateinit var mBinding: FragmentEventBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentEventBinding.inflate(inflater, container, false)
        return mBinding.root
    }

}