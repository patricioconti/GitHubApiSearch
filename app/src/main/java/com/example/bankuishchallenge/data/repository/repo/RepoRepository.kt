package com.example.bankuishchallenge.data.repository.repo

import com.example.bankuishchallenge.data.remote.results.searchrepo.SearchRepoResult
import com.example.bankuishchallenge.data.remote.results.repodetails.RepoDetailsResult
import com.example.bankuishchallenge.data.repository.RepositoryResult


//Repository has only one source: remoteDataSource API.
class RepoRepository(private val remoteDataSource: RepoRemoteDataSource) {

    //getRepos from remoteDataSource
    suspend fun getRepos(pageNumber:Int): RepositoryResult<SearchRepoResult> {
        return this.remoteDataSource.getRepos(pageNumber)
}

    //getRepoDetails from remoteDataSource
    suspend fun getRepoDetails(repoId:Int): RepositoryResult<RepoDetailsResult> {
        return this.remoteDataSource.getRepoDetails(repoId)
    }
}