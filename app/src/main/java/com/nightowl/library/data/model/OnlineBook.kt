package com.nightowl.library.data.model

data class OnlineBook(
    val workKey: String,
    val title: String,
    val author: String,
    val firstPublishYear: Int?,
    val coverUrl: String?,
    val editionCount: Int
)