package com.nightowl.library.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nightowl.library.data.model.Reservation
import com.nightowl.library.data.model.Seat
import com.nightowl.library.data.model.Zone
import com.nightowl.library.data.model.formatHour
import com.nightowl.library.data.repository.LibraryRepository
import com.nightowl.library.data.repository.ReservationRepository
import com.nightowl.library.nightOwlApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReserveUiState(
    val studentName: String = "",
    val startHour: Int = 0,
    val endHour: Int = 1,
    val isEditMode: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val saved: Boolean = false
)

class ReserveViewModel(
    savedStateHandle: SavedStateHandle,
    private val reservationRepository: ReservationRepository
) : ViewModel() {

    private val seatId: Int = checkNotNull(savedStateHandle.get<Int>("seatId"))

    private val reservationId: Int = savedStateHandle.get<Int>("reservationId") ?: -1

    val seat: Seat? = LibraryRepository.getSeat(seatId)

    val zone: Zone? = seat?.let { LibraryRepository.getZone(it.zoneId) }

    val windowStart: Int = LibraryRepository.windowStart()

    val windowEnd: Int = LibraryRepository.windowEnd()

    private val _state = MutableStateFlow(
        ReserveUiState(
            startHour = windowStart,
            endHour = windowStart + 1,
            isEditMode = reservationId > 0
        )
    )
    val state: StateFlow<ReserveUiState> = _state.asStateFlow()

    init {
        if (reservationId > 0) {
            viewModelScope.launch {
                reservationRepository.getReservation(reservationId)?.let { reservation ->
                    _state.update {
                        it.copy(
                            studentName = reservation.studentName,
                            startHour = reservation.startHour,
                            endHour = reservation.endHour
                        )
                    }
                }
            }
        }
    }

    fun onNameChange(value: String) {
        _state.update { it.copy(studentName = value, error = null) }
    }

    fun onStartChange(value: Int) {
        _state.update { it.copy(startHour = value, error = null) }
    }

    fun onEndChange(value: Int) {
        _state.update { it.copy(endHour = value, error = null) }
    }

    fun submit() {
        val s = _state.value
        if (s.isSaving) return
        val currentSeat = seat
        val currentZone = zone
        val validationError = when {
            currentSeat == null -> "Seat not found."
            currentZone == null -> "Zone not found."
            s.studentName.isBlank() -> "Please enter your name."
            s.endHour <= s.startHour -> "The end time must be after the start time."
            s.startHour < windowStart || s.endHour > windowEnd ->
                "Choose a time between ${formatHour(windowStart)} and ${formatHour(windowEnd)}."
            LibraryRepository.nightMode.value && !currentZone.openAtNight ->
                "${currentZone.name} is closed at night."
            else -> null
        }
        if (validationError != null || currentSeat == null || currentZone == null) {
            _state.update { it.copy(error = validationError) }
            return
        }
        _state.update { it.copy(isSaving = true, error = null) }
        viewModelScope.launch {
            val conflict = reservationRepository.findConflict(
                seatId = currentSeat.id,
                startHour = s.startHour,
                endHour = s.endHour,
                excludeId = if (reservationId > 0) reservationId else 0
            )
            if (conflict != null) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        error = "${currentSeat.name} is already reserved ${conflict.timeRange}."
                    )
                }
                return@launch
            }
            if (reservationId > 0) {
                reservationRepository.updateReservation(
                    Reservation(
                        id = reservationId,
                        seatId = currentSeat.id,
                        seatName = currentSeat.name,
                        zoneName = currentZone.name,
                        studentName = s.studentName.trim(),
                        startHour = s.startHour,
                        endHour = s.endHour
                    )
                )
            } else {
                reservationRepository.addReservation(
                    seat = currentSeat,
                    zone = currentZone,
                    studentName = s.studentName.trim(),
                    startHour = s.startHour,
                    endHour = s.endHour
                )
            }
            _state.update { it.copy(isSaving = false, saved = true) }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ReserveViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    reservationRepository = nightOwlApplication().container.reservationRepository
                )
            }
        }
    }
}