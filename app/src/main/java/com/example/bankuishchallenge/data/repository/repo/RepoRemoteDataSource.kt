package com.example.bankuishchallenge.data.repository.repo

import com.example.bankuishchallenge.data.remote.GitHubApiClient
import com.example.bankuishchallenge.data.remote.results.searchrepo.SearchRepoResult
import com.example.bankuishchallenge.data.remote.results.repodetails.RepoDetailsResult
import com.example.bankuishchallenge.data.repository.RepositoryResult
import retrofit2.Response

//Const value to get only language:kotlin results
private const val QUERY_PARAMETER_LANGUAGE = "language:kotlin"



class RepoRemoteDataSource {

    companion object {
        const val ITEMS_PER_PAGE = 30}

    suspend fun getRepos(pageNumber: Int): RepositoryResult<SearchRepoResult> {

        //Use try and catch to handle exceptions
        try {
            //Call GitHubApiClient with retrofit service, call getRepos passing pageNumber
            //for pagination
            val response: Response<SearchRepoResult> =
                GitHubApiClient.retrofitService.getRepos(QUERY_PARAMETER_LANGUAGE,ITEMS_PER_PAGE,pageNumber)

            //Response body on SearchRepoResult
            val searchRepoResult = response.body()

            //Check response is successful AND response.body() not null
            return if (response.isSuccessful && searchRepoResult != null) {
                //Repository result has data
                RepositoryResult(
                    data = searchRepoResult,
                    error = null
                )

                //Else Repository result has error
            } else RepositoryResult(
                data = null,
                error = "Request to server was rejected"
            )

            //Handle exception and repository result has error
        } catch (e: Exception) {
            return RepositoryResult(
                data = null,
                error = e.message ?: "An Error has occurred",
            )
        }
    }

    suspend fun getRepoDetails(repoId: Int): RepositoryResult<RepoDetailsResult> {

        //Use try and catch to handle exceptions
        try {
            //Call  GitHubApiClient with retrofit service, call getRepoDetails passing repoId
            val response: Response<RepoDetailsResult> =
                GitHubApiClient.retrofitService.getRepoDetails(repoId)

            //Response body on movieDetails
            val repoDetails = response.body()

            //Check response is successful AND response.body() not null
            return if (response.isSuccessful && repoDetails != null) {
                //Repository result has data
                RepositoryResult(
                    data = repoDetails,
                    error = null
                 )
            } else {
                //Else Repository result has error
                RepositoryResult(
                    data = null,
                    error = "Request to server was rejected"
                )
            }
            //Handle exception and repository result has error
        } catch (e: Exception) {
            return RepositoryResult(
                data = null,
                error = e.message ?: "An Error has occurred"
            )
        }
    }



}