package com.news.app.Networking.Repository

import com.news.app.Networking.ApiHelper

/*Repository Class which acts as mediator between ViewModel and Model(Data)
  It contains suspend function which are only allowed to be called from a
  coroutine or another suspend function */
class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getNews(country: String) = apiHelper.getNews(country)
    suspend fun getSources() = apiHelper.getSource()
    /*Query string is passed to this searchView() function as it accepts
    input as a parameter and makes api call accordingly*/
    suspend fun searchNews(q: String) = apiHelper.getSearchNews(q)
}