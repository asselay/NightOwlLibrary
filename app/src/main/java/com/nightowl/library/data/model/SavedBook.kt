package com.nightowl.library.data.model

enum class ReadingStatus(val label: String) {
    WANT_TO_READ("Want to read"),
    READING("Reading"),
    FINISHED("Finished")
}

data class SavedBook(
    val id: Int,
    val workKey: String,
    val title: String,
    val author: String,
    val coverUrl: String?,
    val firstPublishYear: Int?,
    val status: ReadingStatus,
    val note: String,
    val rating: Int,
    val savedAt: Long
)