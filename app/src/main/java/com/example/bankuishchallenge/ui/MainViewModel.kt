package com.example.bankuishchallenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.bankuishchallenge.data.remote.results.searchrepo.Repo
import com.example.bankuishchallenge.data.repository.repo.RepoPagingSource
import com.example.bankuishchallenge.data.repository.repo.RepoRemoteDataSource
import com.example.bankuishchallenge.data.repository.repo.RepoRepository
import kotlinx.coroutines.flow.Flow


class MainViewModel(repository: RepoRepository) : ViewModel() {

    //Initialize pagingSource passing repository as parameter.
    private val pagingSource = RepoPagingSource(repository)


    //Initiate viewModel calling getPagedRepos() to have the list of repos when viewModel
    //is created.
   init {
       getPagedRepos()
    }

    //Use Flow PagingData to getPagedRepos
    fun getPagedRepos(): Flow<PagingData<Repo>> {

        //Pager for Paging library
        return Pager(
            //The PagingConfig have two params, the items per page size and the enablePlaceHolders.
            config = PagingConfig(pageSize = RepoRemoteDataSource.ITEMS_PER_PAGE, enablePlaceholders = false),
            //Define pagingSourceFactory, which is going to be pagingSource
            pagingSourceFactory = { pagingSource }
            //as flow
        ).flow
            .cachedIn(viewModelScope)
    }
}