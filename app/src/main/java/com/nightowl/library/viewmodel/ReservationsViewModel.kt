package com.nightowl.library.viewmodel

import androidx.lifecycle.ViewModel
import com.nightowl.library.data.model.Reservation
import com.nightowl.library.data.repository.LibraryRepository
import kotlinx.coroutines.flow.StateFlow

class ReservationsViewModel : ViewModel() {

    val reservations: StateFlow<List<Reservation>> = LibraryRepository.reservations

    fun cancel(id: Int) {
        LibraryRepository.cancelReservation(id)
    }
}