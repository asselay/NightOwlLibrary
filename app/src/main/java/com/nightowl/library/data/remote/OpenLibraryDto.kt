package com.nightowl.library.data.remote

import com.google.gson.annotations.SerializedName
import com.nightowl.library.data.model.OnlineBook

data class SearchResponseDto(
    @SerializedName("numFound") val numFound: Int = 0,
    @SerializedName("docs") val docs: List<BookDocDto> = emptyList()
)

data class BookDocDto(
    @SerializedName("key") val key: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("author_name") val authorNames: List<String>? = null,
    @SerializedName("first_publish_year") val firstPublishYear: Int? = null,
    @SerializedName("cover_i") val coverId: Int? = null,
    @SerializedName("edition_count") val editionCount: Int? = null
)

fun BookDocDto.toOnlineBook(): OnlineBook? {
    val workKey = key ?: return null
    val bookTitle = title ?: return null
    return OnlineBook(
        workKey = workKey,
        title = bookTitle,
        author = authorNames?.firstOrNull() ?: "Unknown author",
        firstPublishYear = firstPublishYear,
        coverUrl = coverId?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" },
        editionCount = editionCount ?: 0
    )
}