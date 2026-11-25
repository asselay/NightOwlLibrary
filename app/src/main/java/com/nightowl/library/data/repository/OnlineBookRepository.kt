package com.nightowl.library.data.repository

import com.nightowl.library.data.model.OnlineBook
import com.nightowl.library.data.remote.OpenLibraryApi
import com.nightowl.library.data.remote.toOnlineBook
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OnlineBookRepository(private val api: OpenLibraryApi) {

    suspend fun searchBooks(query: String): Result<List<OnlineBook>> = withContext(Dispatchers.IO) {
        try {
            val response = api.searchBooks(query = query.trim())
            val books = response.docs
                .mapNotNull { it.toOnlineBook() }
                .distinctBy { it.workKey }
            Result.success(books)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}