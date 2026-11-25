package com.nightowl.library.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nightowl.library.data.model.Reservation
import com.nightowl.library.data.model.ReservationSort
import com.nightowl.library.data.preferences.UserPreferencesRepository
import com.nightowl.library.data.repository.ReservationRepository
import com.nightowl.library.nightOwlApplication
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReservationsViewModel(
    private val reservationRepository: ReservationRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val sort: StateFlow<ReservationSort> = userPreferencesRepository.preferences
        .map { it.reservationSort }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ReservationSort.BY_TIME
        )

    val reservations: StateFlow<List<Reservation>> = combine(
        reservationRepository.observeReservations(),
        sort
    ) { list, currentSort ->
        when (currentSort) {
            ReservationSort.BY_TIME -> list.sortedWith(compareBy({ it.startHour }, { it.zoneName }))
            ReservationSort.BY_ZONE -> list.sortedWith(compareBy({ it.zoneName }, { it.startHour }))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSortChange(newSort: ReservationSort) {
        viewModelScope.launch {
            userPreferencesRepository.setReservationSort(newSort)
        }
    }

    fun cancel(id: Int) {
        viewModelScope.launch {
            reservationRepository.deleteReservation(id)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val container = nightOwlApplication().container
                ReservationsViewModel(
                    reservationRepository = container.reservationRepository,
                    userPreferencesRepository = container.userPreferencesRepository
                )
            }
        }
    }
}