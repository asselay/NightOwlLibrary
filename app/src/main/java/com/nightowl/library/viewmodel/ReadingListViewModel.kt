package com.nightowl.library.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nightowl.library.data.model.ReadingStatus
import com.nightowl.library.data.model.SavedBook
import com.nightowl.library.data.repository.ReadingListRepository
import com.nightowl.library.nightOwlApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReadingListViewModel(
    private val repository: ReadingListRepository
) : ViewModel() {

    private val _filter = MutableStateFlow<ReadingStatus?>(null)
    val filter: StateFlow<ReadingStatus?> = _filter.asStateFlow()

    private val _editingBook = MutableStateFlow<SavedBook?>(null)
    val editingBook: StateFlow<SavedBook?> = _editingBook.asStateFlow()

    val books: StateFlow<List<SavedBook>> = combine(
        repository.observeSavedBooks(),
        _filter
    ) { books, status ->
        if (status == null) books else books.filter { it.status == status }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onFilterChange(status: ReadingStatus?) {
        _filter.value = status
    }

    fun startEditing(book: SavedBook) {
        _editingBook.value = book
    }

    fun stopEditing() {
        _editingBook.value = null
    }

    fun saveChanges(book: SavedBook, status: ReadingStatus, note: String, rating: Int) {
        viewModelScope.launch {
            repository.updateBook(book.copy(status = status, note = note.trim(), rating = rating))
            _editingBook.value = null
        }
    }

    fun delete(book: SavedBook) {
        viewModelScope.launch {
            repository.deleteBook(book)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ReadingListViewModel(nightOwlApplication().container.readingListRepository)
            }
        }
    }
}