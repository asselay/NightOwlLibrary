package com.nightowl.library.data.model

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val category: String,
    val description: String,
    val aisle: String,
    val shelf: String,
    val isAvailable: Boolean = true
)