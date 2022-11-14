package com.example.smac_runapp.interfaces

import androidx.fragment.app.Fragment

interface HomeInterface{

    fun replaceReceive(fragment: Fragment) {}
    fun permissionApproved(): Boolean {
        TODO("Not yet implemented")
    }

}