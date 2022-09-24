package com.example.bankuishchallenge.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankuishchallenge.data.remote.results.repodetails.RepoDetailsResult
import com.example.bankuishchallenge.data.repository.repo.RepoRepository
import com.example.bankuishchallenge.data.utils.Error
import com.example.bankuishchallenge.data.utils.Loaded
import com.example.bankuishchallenge.data.utils.Loading
import com.example.bankuishchallenge.data.utils.None
import com.example.bankuishchallenge.data.utils.OperationState
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: RepoRepository,
) : ViewModel() {

    // MutableLiveData which stores repoDetails from a repo.
    // Use Operation State sealed class as wrapper.
    // With this we can define operation states and observe them at the fragment.
    private val _repoDetails = MutableLiveData<OperationState<RepoDetailsResult>>(None())

    // The external immutable LiveData for repoDetails from a repo
    val repoDetails: LiveData<OperationState<RepoDetailsResult>> = _repoDetails

    fun getRepoDetails(repoId: Int) {

        //Set status to LOADING
        _repoDetails.value = Loading()

        viewModelScope.launch {
            //Call getRepoDetails from repository
            val result = repository.getRepoDetails(repoId)

            //If data result is not null
            if (result.data != null) {
                //Store repoDetails Data from repository on _repoDetails using Loaded operationState
                _repoDetails.value = Loaded(result.data)
            } else {
                val errorMessage = result.error ?: "An error has occurred"
                //When an error, Set operationState to Error() and pass errorMessage
                _repoDetails.value = Error(error = errorMessage)
            }
        }
    }
}