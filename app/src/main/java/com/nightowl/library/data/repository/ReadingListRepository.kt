package com.nightowl.library.data.repository

import com.nightowl.library.data.local.SavedBookDao
import com.nightowl.library.data.local.SavedBookEntity
import com.nightowl.library.data.local.toEntity
import com.nightowl.library.data.local.toSavedBook
import com.nightowl.library.data.model.OnlineBook
import com.nightowl.library.data.model.ReadingStatus
import com.nightowl.library.data.model.SavedBook
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReadingListRepository(private val dao: SavedBookDao) {

    fun observeSavedBooks(): Flow<List<SavedBook>> =
        dao.observeAll().map { list -> list.map { it.toSavedBook() } }

    fun observeSavedKeys(): Flow<Set<String>> =
        dao.observeSavedKeys().map { it.toSet() }

    suspend fun saveBook(book: OnlineBook) {
        dao.insert(
            SavedBookEntity(
                workKey = book.workKey,
                title = book.title,
                author = book.author,
                coverUrl = book.coverUrl,
                firstPublishYear = book.firstPublishYear,
                status = ReadingStatus.WANT_TO_READ.name,
                note = "",
                rating = 0,
                savedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun updateBook(book: SavedBook) {
        dao.update(book.toEntity())
    }

    suspend fun deleteBook(book: SavedBook) {
        dao.delete(book.toEntity())
    }
}