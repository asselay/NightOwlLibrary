package com.nightowl.library.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nightowl.library.data.model.ReservationSort
import com.nightowl.library.data.model.ThemeMode
import com.nightowl.library.data.model.UserPreferences
import com.nightowl.library.data.preferences.UserPreferencesRepository
import com.nightowl.library.nightOwlApplication
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: UserPreferencesRepository
) : ViewModel() {

    val preferences: StateFlow<UserPreferences> = repository.preferences
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = UserPreferences()
        )

    fun onThemeModeChange(mode: ThemeMode) {
        viewModelScope.launch {
            repository.setThemeMode(mode)
        }
    }

    fun onReservationSortChange(sort: ReservationSort) {
        viewModelScope.launch {
            repository.setReservationSort(sort)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(nightOwlApplication().container.userPreferencesRepository)
            }
        }
    }
}