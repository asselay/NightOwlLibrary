package com.nightowl.library.data.model

enum class QuietLevel(val label: String) {
    SILENT("Silent"),
    QUIET("Quiet"),
    MODERATE("Moderate"),
    BUSY("Busy")
}

data class Seat(
    val id: Int,
    val name: String,
    val zoneId: Int
)

data class Zone(
    val id: Int,
    val name: String,
    val quietLevel: QuietLevel,
    val openAtNight: Boolean
)

data class ZoneWithSeats(
    val zone: Zone,
    val freeSeats: List<Seat>,
    val totalSeats: Int
)