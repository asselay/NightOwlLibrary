package com.nightowl.library.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nightowl.library.data.model.Zone
import com.nightowl.library.data.repository.LibraryRepository
import com.nightowl.library.data.repository.ReservationRepository
import com.nightowl.library.nightOwlApplication
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ZoneUi(
    val zone: Zone,
    val totalSeats: Int,
    val freeSeats: Int
)

class ZonesViewModel(reservationRepository: ReservationRepository) : ViewModel() {

    val nightMode: StateFlow<Boolean> = LibraryRepository.nightMode

    val zones: StateFlow<List<ZoneUi>> = combine(
        reservationRepository.observeReservations(),
        LibraryRepository.nightMode
    ) { reservations, night ->
        val start = if (night) LibraryRepository.NIGHT_START_HOUR else LibraryRepository.DAY_START_HOUR
        val end = if (night) LibraryRepository.NIGHT_END_HOUR else LibraryRepository.DAY_END_HOUR
        LibraryRepository.zones
            .filter { !night || it.openAtNight }
            .map { zone ->
                ZoneUi(
                    zone = zone,
                    totalSeats = LibraryRepository.seats.count { it.zoneId == zone.id },
                    freeSeats = LibraryRepository.freeSeats(zone.id, start, end, reservations).size
                )
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onNightModeChange(enabled: Boolean) {
        LibraryRepository.setNightMode(enabled)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ZonesViewModel(nightOwlApplication().container.reservationRepository)
            }
        }
    }
}