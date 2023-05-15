package com.example.smac_runapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "receive")
data class Receive(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val img: Int,
    val name: String,
    val date: Long,
    val progress: Float,
    val max: Float
)