package com.news.app.Model

/*Model Class for SourceX Object
SourceX Object is a part of Sources Object
which further is the response which fetched is from our "sources"
api endpoint*/
data class SourceX(
    val category: String,
    val country: String,
    val description: String,
    val id: String,
    val language: String,
    val name: String,
    val url: String
)