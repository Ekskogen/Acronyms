package com.example.acronymslist.data.models

data class LongForm(val lf: String, val freq: Int, val since: Int, val vars: List<LongForm>? = null)