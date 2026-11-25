package com.nightowl.library.data.model

enum class ThemeMode(val label: String) {
    SYSTEM("Follow system"),
    LIGHT("Light"),
    DARK("Dark")
}

enum class ReservationSort(val label: String) {
    BY_TIME("By time"),
    BY_ZONE("By zone")
}

data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val reservationSort: ReservationSort = ReservationSort.BY_TIME
)