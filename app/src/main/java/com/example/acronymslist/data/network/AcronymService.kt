package com.example.acronymslist.data.network

import com.example.acronymslist.data.models.Acronym
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AcronymService {

    @GET("dictionary.py")
    suspend fun getAcronyms(@Query("sf") acronym: String): Response<List<Acronym>>

}