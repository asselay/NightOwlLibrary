package com.nightowl.library.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nightowl.library.data.model.Reservation
import com.nightowl.library.data.model.ZoneWithSeats
import com.nightowl.library.data.repository.LibraryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ZonesViewModel : ViewModel() {

    val nightMode: StateFlow<Boolean> = LibraryRepository.nightMode

    val zones: StateFlow<List<ZoneWithSeats>> = combine(
        LibraryRepository.reservations,
        LibraryRepository.nightMode
    ) { reservations, night ->
        buildZones(reservations, night)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = buildZones(
            LibraryRepository.reservations.value,
            LibraryRepository.nightMode.value
        )
    )

    fun onNightModeChange(enabled: Boolean) {
        LibraryRepository.setNightMode(enabled)
    }

    private fun buildZones(reservations: List<Reservation>, night: Boolean): List<ZoneWithSeats> {
        val start = if (night) LibraryRepository.NIGHT_START_HOUR else LibraryRepository.DAY_START_HOUR
        val end = if (night) LibraryRepository.NIGHT_END_HOUR else LibraryRepository.DAY_END_HOUR
        return LibraryRepository.zones
            .filter { !night || it.openAtNight }
            .map { zone ->
                ZoneWithSeats(
                    zone = zone,
                    freeSeats = LibraryRepository.freeSeats(zone.id, start, end, reservations),
                    totalSeats = LibraryRepository.seats.count { it.zoneId == zone.id }
                )
            }
    }
}