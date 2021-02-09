package com.news.app.Model

/*Model class for Everything object.
Response received from API when api call is made
on "everything" endpoint of the news api*/
data class Everything(
    val articles: List<ArticleX>,
)