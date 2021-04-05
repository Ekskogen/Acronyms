package com.example.acronymslist.di

import com.example.acronymslist.data.network.AcronymService
import com.example.acronymslist.data.repositories.AcronymRepository
import com.example.acronymslist.data.repositories.AcronymRepositoryImpl
import com.example.acronymslist.domain.GetAcronymsUseCase
import com.example.acronymslist.domain.GetAcronymsUseCaseImpl
import com.example.acronymslist.ui.main.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    factory { HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY } }
    single { provideRetrofit(get()) }
    single { provideOkHttpClient(get()) }
    single { provideAcronymServiceApi(get()) }

    single { AcronymRepositoryImpl(get()) as AcronymRepository }

    factory { GetAcronymsUseCaseImpl(get()) as GetAcronymsUseCase }

    viewModel { MainViewModel(get()) }
}

val BASE_URL = "http://www.nactem.ac.uk/software/acromine/"

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideOkHttpClient(httpLogger: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient()
        .newBuilder()
        .addInterceptor(httpLogger)
        .build()
}

fun provideAcronymServiceApi(retrofit: Retrofit): AcronymService = retrofit.create(AcronymService::class.java)
