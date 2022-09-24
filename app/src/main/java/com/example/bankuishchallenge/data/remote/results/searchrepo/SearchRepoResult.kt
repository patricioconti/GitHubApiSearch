package com.example.bankuishchallenge.data.remote.results.searchrepo

import com.squareup.moshi.Json


//Result from GET search/repositories. It includes a list of repos.
data class SearchRepoResult(
    @Json(name = "incomplete_results") val incompleteResults: Boolean,
    @Json(name = "items") val items: List<Repo>,
    @Json(name = "total_count") val totalCount: Int
)