package com.nightowl.library.ui.navigation

object Routes {
    const val CATALOG = "catalog"
    const val BOOK_DETAIL = "book/{bookId}"
    const val SEATS = "seats"
    const val RESERVE = "reserve/{seatId}"
    const val RESERVATIONS = "reservations"

    fun bookDetail(bookId: Int) = "book/$bookId"
    fun reserve(seatId: Int) = "reserve/$seatId"
}