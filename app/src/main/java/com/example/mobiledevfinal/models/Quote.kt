package com.example.mobiledevfinal.models

data class Quote(
    val _id: String,
    // The quotation text
    val content: String,
    // The full name of the author
    val author: String,
    // The `slug` of the quote author
    val authorSlug: String,
    // The length of quote (number of characters)
    val length: Int,
    // An array of tag names for this quote
    val tags: List<String>,
){
}