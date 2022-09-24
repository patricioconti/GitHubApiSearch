package com.example.bankuishchallenge.data.remote.results.searchrepo

import com.squareup.moshi.Json

data class Owner(
    @Json(name = "login") val author: String,
)