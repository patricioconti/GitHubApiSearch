package com.example.bankuishchallenge

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.bankuishchallenge.data.remote.results.repodetails.OwnerDetails
import com.example.bankuishchallenge.data.remote.results.repodetails.RepoDetailsResult
import com.example.bankuishchallenge.data.repository.RepositoryResult
import com.example.bankuishchallenge.data.repository.repo.RepoRepository
import com.example.bankuishchallenge.data.utils.Loaded
import com.example.bankuishchallenge.ui.DetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {
    //A JUnit Test Rule that swaps the background executor used by the Architecture Components
    // with a different one which executes each task synchronously.
    //To specify that LiveData objects should not call the main thread
    // we need to provide a specific test rule any time we are testing a LiveData object
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    //Execute coroutines at the main thread through runBolocking
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    //Mock properties
    @Mock
    private lateinit var repoRepository: RepoRepository

    private lateinit var viewModel: DetailViewModel

    //Run this fun before every test.
    //DetailViewModel is the SUT
    @Before
    fun setup() {
        viewModel = DetailViewModel(repoRepository)
    }

    //getRepoDetails fun from viewModel returns Current Details
    @Test
    fun getRepoDetails_returnsCurrentDetails() = runBlocking {
        //GIVEN

        //mockRepoDetails
        val mockRepoDetails =
            RepoDetailsResult(
                "name", "createdAt", "description",
                "htmlUrl", 5, OwnerDetails("avatarUrl", "author"),
                452, "updatedAt"
            )

        //Repository getRepoDetails(repoId) returns RepositoryResult class with the data.
        //mockRepositoryResultRepoDetails
        val mockRepositoryResultRepoDetails =
            RepositoryResult(data = mockRepoDetails, error = null)

        //mockRepoId
        val mockRepoId = 1234

        //Set repoRepository to always return mockRepositoryResultMovieDetails
        whenever(repoRepository.getRepoDetails(mockRepoId)).thenReturn(
            mockRepositoryResultRepoDetails
        )

        //WHEN
        //Call viewModel getRepoDetails(mockRepoId)
        viewModel.getRepoDetails(mockRepoId)

        //THEN
        //Now get LiveData repoDetails.value using LiveDataTestUtil
        val repoDetails = LiveDataTestUtil.getValue(viewModel.repoDetails)

        //Check repoDetails LiveData equals Loaded(mockRepositoryResultRepoDetails.data)
        //I had to override equals at Loaded class to compare it with repoDetails
        Assert.assertTrue(
            "repoDetails not equal to Loaded(mockRepositoryResultRepoDetails.data)",
            repoDetails == Loaded(mockRepositoryResultRepoDetails.data)
        )
    }
}