package com.news.app.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.news.app.Networking.Repository.MainRepository
import com.news.app.Utils.Resource
import kotlinx.coroutines.Dispatchers

//ViewModel which acts as a mediator between view(UI) and Model(Repository and Model Classes)
class MainViewModel(
    private val mainRepository: MainRepository,
) : ViewModel() {
    fun getUsers(country: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getNews(country)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun searchNews(q: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.searchNews(q)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getSources() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getSources()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}