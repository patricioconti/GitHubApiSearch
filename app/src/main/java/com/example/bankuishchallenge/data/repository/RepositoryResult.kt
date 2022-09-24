package com.example.bankuishchallenge.data.repository

//Used to wrap remoteDataSource result
class RepositoryResult<T>(
    val data: T?,
    val error: String?)