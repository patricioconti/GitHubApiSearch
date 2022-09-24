package com.example.bankuishchallenge.data.remote


import com.example.bankuishchallenge.BuildConfig
import com.example.bankuishchallenge.data.remote.results.searchrepo.SearchRepoResult
import com.example.bankuishchallenge.data.remote.results.repodetails.RepoDetailsResult
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

//Create Moshi object
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//Logging Interceptor for debugging
val httpClient = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    .build()

//Compiler for creating and compiling Retrofit object
//With this, Retrofit can get a JSON response and show it as a string
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    //Base URL for web service
    .baseUrl(BuildConfig.API_URL)
    .client(httpClient)
    .build()


interface GitHubApiService {

    //Indicate retrofit that is a GET request and that the extreme is search/repositories
    @GET("search/repositories")
    //Query "q" for kotlin:language
    //Query "per_page" to define items per page
    //Query "page" to use pagination

    suspend fun getRepos(
        @Query("q") query:String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Response<SearchRepoResult>

    //Indicate retrofit that is a GET request and that the extreme is repositories/{repo_id}
    @GET("repositories/{repo_id}")
    //To get repo details, I need to pass path for repo_id
    suspend fun getRepoDetails(
        @Path("repo_id") repoId: Int,
         ): Response<RepoDetailsResult>

}
//Expose the service to the rest of the app using object declaration.
object GitHubApiClient {
    val retrofitService: GitHubApiService by lazy {
        retrofit.create(GitHubApiService::class.java)
    }
}