package com.nightowl.library.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nightowl.library.data.model.OnlineBook
import com.nightowl.library.data.repository.OnlineBookRepository
import com.nightowl.library.data.repository.ReadingListRepository
import com.nightowl.library.nightOwlApplication
import java.io.IOException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed interface DiscoverUiState {
    data object Loading : DiscoverUiState
    data class Success(val books: List<OnlineBook>) : DiscoverUiState
    data class Error(val message: String) : DiscoverUiState
    data object Empty : DiscoverUiState
}

class DiscoverViewModel(
    private val onlineBookRepository: OnlineBookRepository,
    private val readingListRepository: ReadingListRepository
) : ViewModel() {

    private val _query = MutableStateFlow(DEFAULT_QUERY)
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<DiscoverUiState>(DiscoverUiState.Loading)
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    val savedKeys: StateFlow<Set<String>> = readingListRepository.observeSavedKeys()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    private var searchJob: Job? = null

    init {
        search()
    }

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun search() {
        val currentQuery = _query.value.trim()
        if (currentQuery.isBlank()) {
            _uiState.value = DiscoverUiState.Empty
            return
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.value = DiscoverUiState.Loading
            onlineBookRepository.searchBooks(currentQuery)
                .onSuccess { books ->
                    _uiState.value = if (books.isEmpty()) {
                        DiscoverUiState.Empty
                    } else {
                        DiscoverUiState.Success(books)
                    }
                }
                .onFailure { error ->
                    _uiState.value = DiscoverUiState.Error(error.toUserMessage())
                }
        }
    }

    fun retry() {
        search()
    }

    fun saveBook(book: OnlineBook) {
        viewModelScope.launch {
            readingListRepository.saveBook(book)
        }
    }

    private fun Throwable.toUserMessage(): String = when (this) {
        is IOException -> "No internet connection. Check your network and try again."
        is HttpException -> "Server error (${code()}). Please try again later."
        else -> "Something went wrong: ${message ?: "unknown error"}"
    }

    companion object {
        private const val DEFAULT_QUERY = "programming"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val container = nightOwlApplication().container
                DiscoverViewModel(
                    onlineBookRepository = container.onlineBookRepository,
                    readingListRepository = container.readingListRepository
                )
            }
        }
    }
}