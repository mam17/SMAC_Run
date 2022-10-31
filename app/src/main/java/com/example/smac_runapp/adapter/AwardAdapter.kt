package com.example.smac_runapp.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.smac_runapp.fragment.fragAwards.AwardFragment

class AwardAdapter(fragment: AwardFragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> AwardFragment()
            else -> AwardFragment()
        }
    }
}