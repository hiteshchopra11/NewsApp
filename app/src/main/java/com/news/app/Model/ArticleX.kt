package com.news.app.Model

/*Model Class for ArticleX Object
Article Object is a part of Everything Object
which further is the response which fetched is from our "everything"
api endpoint*/
data class ArticleX(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val title: String,
    val url: String,
    val urlToImage: String
)