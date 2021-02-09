package com.news.app.Networking

import com.news.app.Model.Everything
import com.news.app.Model.News
import com.news.app.Model.Source
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/*APIService class is the interface which defines our API endpoints(Retrofit), their queries as
  well as their response types all at one place*/
interface ApiService {

    @GET("top-headlines/")
    suspend fun getNews(
        @Query(value = "country") country: String,
    ): Response<News>

    @GET("sources/")
    suspend fun getSources(): Response<Source>

    @GET("everything/")
    suspend fun searchNews(
        @Query(value = "q") query: String,
    ): Response<Everything>

}