package com.example.smac_runapp.db

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.smac_runapp.models.Receive

@Dao
interface ChallengerDAO {
    @WorkerThread
    @Query("SELECT * FROM receive WHERE name LIKE :name LIMIT 1")
    suspend fun findByName(name: String): Receive

    @WorkerThread
    @Query("SELECT * FROM receive")
    suspend fun getAll(): List<Receive>

    @WorkerThread
    @Insert
    suspend fun insertAll(vararg receive: Receive)

    @WorkerThread
    @Insert
    suspend fun insert(receive: Receive)

    @WorkerThread
    @Query("Delete FROM receive")
    suspend fun delete()
}