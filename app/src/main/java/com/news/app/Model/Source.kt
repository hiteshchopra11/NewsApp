package com.news.app.Model

/*Model Class for Sources object
Sources is the object received as response when api call is made on
"sources" endpoint*/
data class Source(
    val sources: List<SourceX>,
    val status: String
)