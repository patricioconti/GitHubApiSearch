package com.example.bankuishchallenge.data.remote.results.repodetails

import com.example.bankuishchallenge.data.remote.results.searchrepo.Owner
import com.squareup.moshi.Json

//Result from GET repositories/{repo_id}
data class RepoDetailsResult(
    @Json(name = "name") val name: String,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "description") val description: String,
    @Json(name = "html_url") val htmlUrl: String,
    @Json(name = "id") val id: Int,
    @Json(name = "owner") val ownerDetails: OwnerDetails,
    @Json(name = "stargazers_count") val stargazersCount: Int,
    @Json(name = "updated_at") val updatedAt: String
)