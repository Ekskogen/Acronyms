package com.example.acronymslist.data.repositories

import com.example.acronymslist.data.models.Acronym
import com.example.acronymslist.data.network.Result

interface AcronymRepository {
    suspend fun getAcronyms(word: String): Result<Acronym>
}