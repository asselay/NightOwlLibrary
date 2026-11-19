package com.nightowl.library.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nightowl.library.data.model.Book
import com.nightowl.library.data.repository.LibraryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class CatalogViewModel : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val books: StateFlow<List<Book>> = combine(
        LibraryRepository.books,
        _query
    ) { books, query ->
        filterBooks(books, query)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = filterBooks(LibraryRepository.books.value, "")
    )

    fun onQueryChange(value: String) {
        _query.value = value
    }

    private fun filterBooks(books: List<Book>, query: String): List<Book> {
        if (query.isBlank()) return books
        return books.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.author.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
        }
    }
}