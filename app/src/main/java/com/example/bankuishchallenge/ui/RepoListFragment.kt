package com.example.bankuishchallenge.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.bankuishchallenge.R
import com.example.bankuishchallenge.data.repository.repo.RepoRemoteDataSource
import com.example.bankuishchallenge.data.repository.repo.RepoRepository
import com.example.bankuishchallenge.databinding.FragmentRepoListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RepoListFragment : Fragment() {

    private var _binding: FragmentRepoListBinding? = null
    private val binding get() = _binding!!

    //View model with remoteDataSource and repository as parameters
    private val viewModel: MainViewModel by viewModels {
        val remoteDataSource = RepoRemoteDataSource()
        val repository = RepoRepository(remoteDataSource)
        MainViewModelFactory(repository)
    }



    //pagedListAdapter will be the recyclerview adapter to enable paging.
    private lateinit var pagedListAdapter: RepoPagedListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRepoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapters()
        setListeners()
        collectRepos()
        loadStateListener()
    }

    //Adapters for Recyclerview
    private fun setAdapters() {

        //Adapter for recyclerview by default (pagingListAdapter)
        pagedListAdapter = RepoPagedListAdapter { repo ->
            //Capture data of repoClicked, pass repo.id argument to RepoDetails Fragment
            //Navigate to the repo Details Fragment
            val action =
                RepoListFragmentDirections.actionRepoListFragmentToRepoDetailsFragment(repo.id)
            this.findNavController().navigate(action)
        }

        //Default recyclerView adapter is pagedListAdapter
        binding.recyclerView.adapter = pagedListAdapter
    }

    private fun setListeners() {
        //Retry button after an error, to reload movies
        binding.retryButton.setOnClickListener { pagedListAdapter.retry() }
    }

    //Use coroutines to collect pagingData Flow
    private fun collectRepos() {
        viewLifecycleOwner.lifecycleScope.launch {
            //We use collectLatest on the pagingData Flow so that collection on previous pagingData emissions
            // is canceled when a new pagingData instance is emitted.
            viewModel.getPagedRepos().collectLatest { pagingData ->
                //We notify the adapter of changes with submitData() instead of submitList().
                pagedListAdapter.submitData(pagingData)
            }
        }
    }

    //Use coroutines to collect loadState Flow
    private fun loadStateListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            pagedListAdapter.loadStateFlow.collectLatest { loadState ->

                with(binding) {
                    //Progress Bar is visible when LoadState.Loading
                    progressBar.isVisible = loadState.refresh is LoadState.Loading
                    //Retry Button is visible when LoadState.Error
                    retryButton.isVisible = loadState.refresh is LoadState.Error
                    //Status Image is visible when LoadState.Error
                    statusImage.isVisible = loadState.refresh is LoadState.Error
                    //If LoadState.Error show toast error
                    if (loadState.refresh is LoadState.Error) {
                        val errorMsg = getString(R.string.error_message)
                        showToastError(errorMsg)
                    }
                }
            }
        }
    }

    //Show Error Message Toast
    private fun showToastError(error: String) {
        //Show toast
        val toast = Toast.makeText(requireContext(), error, Toast.LENGTH_LONG)
        toast.show()
    }




}