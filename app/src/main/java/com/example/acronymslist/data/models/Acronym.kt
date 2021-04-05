package com.example.acronymslist.data.models

import com.google.gson.annotations.SerializedName

data class Acronym(
    val sf: String,
    val lfs: List<LongForm>
)