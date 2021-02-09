package com.news.app.di.module


import com.news.app.BuildConfig
import com.news.app.Networking.ApiHelperImpl
import com.news.app.Networking.ApiService
import com.news.app.Networking.AuthInterceptor
import com.news.app.Networking.ApiHelper
import com.news.app.Networking.Repository.MainRepository
import com.news.app.ViewModel.MainViewModel
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { AuthInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get(), BuildConfig.API_URL) }
    single { provideApiService(get()) }
    single<ApiHelper> { ApiHelperImpl(get()) }
}

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    API_URL: String
): Retrofit =
    Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(API_URL)
        .client(okHttpClient)
        .build()

private fun provideOkHttpClient(authInterceptor: AuthInterceptor) = if (BuildConfig.DEBUG) {
    OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()
} else OkHttpClient
    .Builder()
    .build()

private fun provideApiService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)


val repoModule = module {
    single { MainRepository(get()) }
}

val viewModelModule = module {
    viewModel {
        MainViewModel(get())
    }
}