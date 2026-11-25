package com.nightowl.library.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nightowl.library.data.model.ReadingStatus
import com.nightowl.library.data.model.SavedBook

@Entity(
    tableName = "saved_books",
    indices = [Index(value = ["workKey"], unique = true)]
)
data class SavedBookEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val workKey: String,
    val title: String,
    val author: String,
    val coverUrl: String?,
    val firstPublishYear: Int?,
    val status: String,
    val note: String,
    val rating: Int,
    val savedAt: Long
)

fun SavedBookEntity.toSavedBook(): SavedBook = SavedBook(
    id = id,
    workKey = workKey,
    title = title,
    author = author,
    coverUrl = coverUrl,
    firstPublishYear = firstPublishYear,
    status = ReadingStatus.entries.firstOrNull { it.name == status } ?: ReadingStatus.WANT_TO_READ,
    note = note,
    rating = rating,
    savedAt = savedAt
)

fun SavedBook.toEntity(): SavedBookEntity = SavedBookEntity(
    id = id,
    workKey = workKey,
    title = title,
    author = author,
    coverUrl = coverUrl,
    firstPublishYear = firstPublishYear,
    status = status.name,
    note = note,
    rating = rating,
    savedAt = savedAt
)