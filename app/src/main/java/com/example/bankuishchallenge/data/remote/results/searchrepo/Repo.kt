package com.example.bankuishchallenge.data.remote.results.searchrepo

import com.squareup.moshi.Json

data class Repo(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "owner") val owner: Owner,
)