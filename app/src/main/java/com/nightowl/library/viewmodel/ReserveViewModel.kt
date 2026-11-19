package com.nightowl.library.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nightowl.library.data.model.Seat
import com.nightowl.library.data.model.Zone
import com.nightowl.library.data.repository.LibraryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ReserveUiState(
    val studentName: String = "",
    val startHour: Int = 0,
    val endHour: Int = 1,
    val error: String? = null,
    val saved: Boolean = false
)

class ReserveViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val seatId: Int = checkNotNull(savedStateHandle.get<Int>("seatId"))

    val seat: Seat? = LibraryRepository.getSeat(seatId)
    val zone: Zone? = seat?.let { LibraryRepository.getZone(it.zoneId) }
    val nightMode: Boolean = LibraryRepository.nightMode.value
    val windowStart: Int = LibraryRepository.windowStart()
    val windowEnd: Int = LibraryRepository.windowEnd()

    private val _state = MutableStateFlow(
        ReserveUiState(startHour = windowStart, endHour = windowStart + 1)
    )
    val state: StateFlow<ReserveUiState> = _state.asStateFlow()

    fun onNameChange(value: String) {
        _state.update { it.copy(studentName = value, error = null) }
    }

    fun changeStart(delta: Int) {
        _state.update {
            val start = (it.startHour + delta).coerceIn(windowStart, windowEnd - 1)
            val end = if (it.endHour <= start) start + 1 else it.endHour
            it.copy(startHour = start, endHour = end, error = null)
        }
    }

    fun changeEnd(delta: Int) {
        _state.update {
            val end = (it.endHour + delta).coerceIn(it.startHour + 1, windowEnd)
            it.copy(endHour = end, error = null)
        }
    }

    fun submit() {
        val current = _state.value
        val error = LibraryRepository.reserve(
            seatId,
            current.studentName,
            current.startHour,
            current.endHour
        )
        if (error == null) {
            _state.update { it.copy(saved = true) }
        } else {
            _state.update { it.copy(error = error) }
        }
    }
}