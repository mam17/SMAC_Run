package com.example.smac_runapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smac_runapp.models.Receive

const val DB_NAME = "Steps_Challenge"
const val DB_VERSION = 1

@Database(entities = [Receive::class], version = DB_VERSION, exportSchema = true)
abstract class ChallengerDatabase : RoomDatabase() {
    abstract val challengerDao: ChallengerDAO

    companion object {
        @Volatile
        private var INSTANCE: ChallengerDatabase? = null

        fun getInstanceDB(context: Context): ChallengerDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        ChallengerDatabase::class.java,
                        DB_NAME
                    ).build()

                }
                return instance
            }
        }
    }

}
