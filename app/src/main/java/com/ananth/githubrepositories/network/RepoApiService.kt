package com.ananth.githubrepositories.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.ananth.githubrepositories.model.Repository
import com.ananth.githubrepositories.util.Constant
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


/**
 * Helps finding whether the phone has network connections
 */
fun hasNetwork(context: Context): Boolean? {
    var isConnected: Boolean? = false // Initial Value
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    if (activeNetwork != null && activeNetwork.isConnected)
        isConnected = true
    return isConnected
}

/**
 * Builds the http client with the capability to hold cache. This will help in showing user
 * some data even when there is not network connection.
 */
fun getOkHttpClient(context: Context): OkHttpClient {
    val cacheSize = (5 * 1024 * 1024).toLong()
    val cache = Cache(context.cacheDir, cacheSize)
    return OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor { chain ->
            var request = chain.request()
            request = if (hasNetwork(context)!!)
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5)
                    .build()
            else
                request.newBuilder().header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                    .build()
            chain.proceed(request)
        }
        .build()
}

/**
 * Build the Moshi object that Retrofit will be using, adding the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Retrofit object to assist in talking to the server.
 * @deprecated This implementation doesn't support caching.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constant.BASE_URL)
    .build()

/**
 * Provides us with the [RepoApiService] over which we can do our desired operations.
 */
fun getRepoApiService(context: Context): RepoApiService {
    /*
     * Retrofit builder to build a retrofit object using a Moshi converter with the Moshi
     * object.
     */
    val retrofit =  Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(getOkHttpClient(context))
        .baseUrl(Constant.BASE_URL)
        .build()
    return retrofit.create(RepoApiService::class.java)
}

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
 * @deprecated This implementation doesn't support caching.
 */
object RepoApi {
    val retrofitService : RepoApiService by lazy { retrofit.create(RepoApiService::class.java) }
}