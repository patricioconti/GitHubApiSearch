package com.example.bankuishchallenge

import com.example.bankuishchallenge.data.remote.GitHubApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class GitHubApiServiceTests : BaseTest() {

    private lateinit var service: GitHubApiService

    @Before
    fun setup() {

        //MockWebServer has a function called url() that specifies which URL we want to intercept
        //The url() function takes a string that represents that fake URL and
        // it returns an HttpUrl object.
        val url = mockWebServer.url("/")

        //Create an instance of the GitHubApiService in the same way that was done in the GitHubApiService
        service = Retrofit.Builder()

            //This specifies to our API service that we want to route requests to our MockWebServer
            .baseUrl(url)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .build()
            .create(GitHubApiService::class.java)
    }

    // MockWebServer as a fake API that returns data that we created, and as such,
    // we need to explicitly tell MockWebServer what to return before a request is made
    @Test
    fun api_service_getRepos() {

        // The function takes a file from test resources and turns it into a fake API response.
        enqueue("search_repos.json")

        //getRepos() is a suspend function, and it must be called from a coroutine scope
        runBlocking {
            //Call service.getRepos() and pass parameters
            val apiResponse = service.getRepos("language:kotlin",30,1)
            //Let's make sure that getRepos() response is not null
            TestCase.assertNotNull(apiResponse.body())
            //Make sure that the list is not empty (remember that there is a list of items).
            TestCase.assertTrue("The list was empty", apiResponse.body()!!.items.isNotEmpty())
            //Assert that the name for the first repo equals the value of the repo from the corresponding list item
            TestCase.assertEquals(
                "The name for first repo in the list did not match",
                "kotlin",
                apiResponse.body()!!.items.first().name
            )
            TestCase.assertEquals(
                "The author for the last repo in the list did not match",
                "mockk",
                apiResponse.body()!!.items.last().owner.author
            )
        }
    }
}