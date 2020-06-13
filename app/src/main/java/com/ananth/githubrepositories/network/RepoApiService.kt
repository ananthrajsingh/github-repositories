package com.ananth.githubrepositories.network

import com.ananth.githubrepositories.model.Repository
import com.ananth.githubrepositories.util.Constant
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

/**
 * Build the Moshi object that Retrofit will be using, adding the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Retrofit object to assist in talking to the server.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constant.BASE_URL)
    .build()

/**
 * Public interface to expose the [getRepositories] method
 */
interface RepoApiService {
    /**
     * Returns a Coroutine [Call] [List] of [Repository] which can be fetched with await() if
     * in a Coroutine scope.
     */
    @GET(Constant.REPOSITORIES_ENDPOINT)
    fun getRepositories(): Call<List<Repository>>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object RepoApi {
    val retrofitService : RepoApiService by lazy { retrofit.create(RepoApiService::class.java) }
}