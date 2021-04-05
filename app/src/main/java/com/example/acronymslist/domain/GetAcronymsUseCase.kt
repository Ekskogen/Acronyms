package com.example.acronymslist.domain

import com.example.acronymslist.data.models.LongForm
import com.example.acronymslist.data.network.Result

interface GetAcronymsUseCase {
    suspend fun execute(word: String): Result<List<LongForm>>
}