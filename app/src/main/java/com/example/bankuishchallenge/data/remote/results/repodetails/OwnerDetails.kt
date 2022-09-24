package com.example.bankuishchallenge.data.remote.results.repodetails

import com.squareup.moshi.Json

data class OwnerDetails(
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name = "login") val author: String,
)