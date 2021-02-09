package com.news.app.Model

//Model Class for News object
//News is the object received as response when api call is made on
//"top-headlines" endpoint
data class News(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int)