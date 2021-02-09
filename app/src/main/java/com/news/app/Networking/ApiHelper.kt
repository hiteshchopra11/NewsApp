package com.news.app.Networking

import com.news.app.Model.Everything
import com.news.app.Model.News
import com.news.app.Model.Source
import retrofit2.Response

interface ApiHelper {
    suspend fun getNews(country: String): Response<News>
    suspend fun getSource(): Response<Source>
    suspend fun getSearchNews(q: String): Response<Everything>
}