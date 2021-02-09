package com.news.app.Model

/*Model Class for Article Object
Article Object is a part of News Object
which further is the response which fetched is from our "top-headlines"
api endpoint*/
data class Article(
    val author: String,
    val title: String,
    val url: String,
    val urlToImage: String
)