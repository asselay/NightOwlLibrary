package com.nightowl.library.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nightowl.library.data.model.ReservationSort
import com.nightowl.library.data.model.ThemeMode
import com.nightowl.library.data.model.UserPreferences
import com.nightowl.library.ui.components.AppTopBar
import com.nightowl.library.ui.components.RadioOptionRow
import com.nightowl.library.ui.theme.NightOwlPreview
import com.nightowl.library.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory)
) {
    val preferences by viewModel.preferences.collectAsStateWithLifecycle()

    SettingsContent(
        preferences = preferences,
        onThemeModeChange = viewModel::onThemeModeChange,
        onReservationSortChange = viewModel::onReservationSortChange,
        onBack = onBack
    )
}

@Composable
fun SettingsContent(
    preferences: UserPreferences,
    onThemeModeChange: (ThemeMode) -> Unit,
    onReservationSortChange: (ReservationSort) -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(title = "Settings", onBack = onBack)

        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingsSection(
                title = "App theme",
                description = "Choose how NightOwl looks."
            ) {
                ThemeMode.entries.forEach { mode ->
                    RadioOptionRow(
                        label = mode.label,
                        selected = preferences.themeMode == mode,
                        onClick = { onThemeModeChange(mode) }
                    )
                }
            }

            SettingsSection(
                title = "Reservations order",
                description = "How the My reservations list is sorted."
            ) {
                ReservationSort.entries.forEach { option ->
                    RadioOptionRow(
                        label = option.label,
                        selected = preferences.reservationSort == option,
                        onClick = { onReservationSortChange(option) }
                    )
                }
            }

            Text(
                text = "Your choices are saved on this device with DataStore and stay after you close and reopen the app.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    description: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = description, style = MaterialTheme.typography.bodySmall)
            content()
        }
    }
}

@PreviewLightDark
@Composable
private fun SettingsPreview() {
    NightOwlPreview {
        SettingsContent(
            preferences = UserPreferences(ThemeMode.DARK, ReservationSort.BY_ZONE),
            onThemeModeChange = {},
            onReservationSortChange = {},
            onBack = {}
        )
    }
}