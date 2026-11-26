package com.nightowl.library.ui.navigation

object Routes {
    const val CATALOG = "catalog"
    const val BOOK_DETAIL = "book/{bookId}"
    const val DISCOVER = "discover"
    const val ZONES = "zones"
    const val RESERVE = "reserve/{seatId}?reservationId={reservationId}"
    const val MY_RESERVATIONS = "reservations"
    const val READING_LIST = "reading_list"
    const val SETTINGS = "settings"

    fun bookDetail(bookId: Int) = "book/$bookId"
    fun reserve(seatId: Int) = "reserve/$seatId"
    fun editReservation(seatId: Int, reservationId: Int) = "reserve/$seatId?reservationId=$reservationId"
}