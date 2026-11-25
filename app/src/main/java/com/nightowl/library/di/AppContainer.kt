package com.nightowl.library.di

import android.content.Context
import com.nightowl.library.data.local.NightOwlDatabase
import com.nightowl.library.data.preferences.UserPreferencesRepository
import com.nightowl.library.data.remote.RetrofitClient
import com.nightowl.library.data.repository.OnlineBookRepository
import com.nightowl.library.data.repository.ReadingListRepository
import com.nightowl.library.data.repository.ReservationRepository

class AppContainer(context: Context) {

    private val database: NightOwlDatabase = NightOwlDatabase.getInstance(context)

    val onlineBookRepository: OnlineBookRepository = OnlineBookRepository(RetrofitClient.api)

    val reservationRepository: ReservationRepository = ReservationRepository(database.reservationDao())

    val readingListRepository: ReadingListRepository = ReadingListRepository(database.savedBookDao())

    val userPreferencesRepository: UserPreferencesRepository =
        UserPreferencesRepository(context.applicationContext)
}