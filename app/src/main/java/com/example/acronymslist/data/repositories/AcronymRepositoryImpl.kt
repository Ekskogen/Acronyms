package com.example.acronymslist.data.repositories

import android.util.Log
import com.example.acronymslist.data.models.Acronym
import com.example.acronymslist.data.network.AcronymService
import com.example.acronymslist.data.network.NoInternetException
import com.example.acronymslist.data.network.Result
import java.lang.Exception
import java.net.UnknownHostException

class AcronymRepositoryImpl(val service: AcronymService): AcronymRepository {

    override suspend fun getAcronyms(word: String): Result<Acronym> {
        return try {
            val response = service.getAcronyms(word)
            if(response.isSuccessful) {
                response.body()?.firstOrNull()?.let {
                    Result.Success(it)
                } ?: Result.Success(Acronym("", listOf()))
            } else {
                Result.Failure()
            }
        } catch (e: Exception) {
            if(e is UnknownHostException) Result.Failure(NoInternetException)
            else Result.Failure(e)
        }
    }

}