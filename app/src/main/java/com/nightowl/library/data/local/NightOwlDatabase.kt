package com.nightowl.library.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ReservationEntity::class, SavedBookEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NightOwlDatabase : RoomDatabase() {

    abstract fun reservationDao(): ReservationDao

    abstract fun savedBookDao(): SavedBookDao

    companion object {
        @Volatile
        private var instance: NightOwlDatabase? = null

        fun getInstance(context: Context): NightOwlDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    NightOwlDatabase::class.java,
                    "nightowl.db"
                ).build().also { instance = it }
            }
    }
}