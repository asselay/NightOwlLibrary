package com.nightowl.library.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nightowl.library.data.model.Book
import com.nightowl.library.data.repository.LibraryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class BookDetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val bookId: Int = checkNotNull(savedStateHandle.get<Int>("bookId"))

    val book: StateFlow<Book?> = LibraryRepository.books
        .map { list -> list.firstOrNull { it.id == bookId } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LibraryRepository.getBook(bookId)
        )
}