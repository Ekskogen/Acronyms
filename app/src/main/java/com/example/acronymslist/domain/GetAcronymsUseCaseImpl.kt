package com.example.acronymslist.domain

import com.example.acronymslist.data.models.LongForm
import com.example.acronymslist.data.network.Result
import com.example.acronymslist.data.repositories.AcronymRepository

class GetAcronymsUseCaseImpl(val repository: AcronymRepository): GetAcronymsUseCase {

    override suspend fun execute(word: String): Result<List<LongForm>> {
        val response = repository.getAcronyms(word)
        if(response is Result.Failure<*>)
            return Result.Failure(response.throwable)

        val acronym = (response as Result.Success).result
        return Result.Success(acronym.lfs)
    }
}