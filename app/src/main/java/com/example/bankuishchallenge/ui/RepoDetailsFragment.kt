package com.example.bankuishchallenge.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.bankuishchallenge.R
import com.example.bankuishchallenge.data.remote.results.repodetails.RepoDetailsResult
import com.example.bankuishchallenge.data.repository.repo.RepoRemoteDataSource
import com.example.bankuishchallenge.data.repository.repo.RepoRepository
import com.example.bankuishchallenge.data.utils.Error
import com.example.bankuishchallenge.data.utils.Loaded
import com.example.bankuishchallenge.data.utils.Loading
import com.example.bankuishchallenge.data.utils.None
import com.example.bankuishchallenge.databinding.FragmentRepoDetailsBinding

/**
 * Fragment that displays the details of the selected repo.
 */

class RepoDetailsFragment: Fragment() {

    //View model with repoRepository as parameter
    //Initialize remoteDataSource
    private val viewModel: DetailViewModel by viewModels {
        val repoRemoteDataSource = RepoRemoteDataSource()
        val repoRepository = RepoRepository(repoRemoteDataSource)
        DetailViewModelFactory(repoRepository)
    }

    //Delegate to access fragment arguments as fragment instance
    private val navigationArgs: RepoDetailsFragmentArgs by navArgs()

    private var _binding: FragmentRepoDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRepoDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getRepoDetails()
        setObservers()
    }

    //Get repo details passing repoId to the viewModel
    private fun getRepoDetails() {
        //Take movieId passed from Repo List fragment
        val repoId = navigationArgs.repoId
        //Call getRepoDetails from viewModel passing movieId
        viewModel.getRepoDetails(repoId)
    }

    //Observers for LiveData properties
    private fun setObservers() {
        //Attach an observer on the repoDetails to listen for the data changes.
        //Use a when to update UI according operation state
        //Call  bindRepoDetails() and pass repoDetails as parameter
        // This will update the UI with the new repo details.
        viewModel.repoDetails.observe(viewLifecycleOwner) { operation ->
            //Bind Status image to show operation state accordingly
            val statusImageView = binding.statusImage
            val repoDetailsDataGroup = binding.repoDetailsData
            val progressBar = binding.progressBar

            when (operation) {
                is None -> {}
                is Loading -> {
                    repoDetailsDataGroup.visibility = View.GONE
                    statusImageView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                is Loaded -> {
                    statusImageView.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    // This will update the RepoDetailsFragment UI with the new repo clicked
                    val repoDetails = operation.data
                    bindRepoDetails(repoDetails)
                    repoDetailsDataGroup.visibility = View.VISIBLE
                }
                is Error -> {
                   repoDetailsDataGroup.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    statusImageView.setImageResource(R.drawable.ic_connection_error)
                    statusImageView.visibility = View.VISIBLE
                    showToastMsg(operation.error)
                }
            }
        }
    }

    // Declare and initialize all of the list item UI components
    private fun bindRepoDetails(repoDetails: RepoDetailsResult) {

        with(binding) {
            repoName.text = repoDetails.name
            repoAuthor.text = repoDetails.ownerDetails.author
            //Use substringBefore to leave only the date and dismiss the time
            repoCreationDate.text = repoDetails.createdAt.substringBefore(
                delimiter = 'T',
                missingDelimiterValue = getString(R.string.not_found)
            )
            repoDescription.text = repoDetails.description
            repoUrl.text = repoDetails.htmlUrl
            repoStargazersCount.text = repoDetails.stargazersCount.toString()
            //Use substringBefore to leave only the date and dismiss the time
            repoUpdatedDate.text = repoDetails.updatedAt.substringBefore(
                delimiter = 'T',
                missingDelimiterValue = getString(R.string.not_found)
            )
            //Load movie images with coil
            repoAvatarImage.load(repoDetails.ownerDetails.avatarUrl) {
                //Set image at error
                error(R.drawable.ic_broken_image)
            }
        }
    }
    //Show Toast Message
    private fun showToastMsg(message: String) {
        //Show toast
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
        toast.show()
    }
}