package com.news.app.Networking

import com.news.app.Model.Everything
import com.news.app.Model.Source
import retrofit2.Response

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getNews(country: String) =
        apiService.getNews(country)

    override suspend fun getSource(): Response<Source> = apiService.getSources()

    override suspend fun getSearchNews( q: String): Response<Everything> =
        apiService.searchNews(q)
}