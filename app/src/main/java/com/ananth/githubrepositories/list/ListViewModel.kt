package com.ananth.githubrepositories.list

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ananth.githubrepositories.model.Repository
import com.ananth.githubrepositories.network.RepoApi
import com.ananth.githubrepositories.network.UpdateWorker
import com.ananth.githubrepositories.network.getRepoApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.await
import java.lang.Exception
import java.lang.NullPointerException
import java.util.concurrent.TimeUnit

class ListViewModel(application: Application) : AndroidViewModel(application) {

    // This will hold the data to be shown in the list
    private val _repoList = MutableLiveData<List<Repository>>()
    // The external LiveData interface to the property is immutable, so only this class can modify
    val repoList: LiveData<List<Repository>>
        get() = _repoList

    // Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()
    // The Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {
        coroutineScope.launch {
            val repositoriesDeferred = getRepoApiService(application).getRepositories()
            try {
                val repositoriesResult = repositoriesDeferred.await()
                _repoList.value = repositoriesResult
                Log.d(TAG, "Received ${_repoList.value!!.size} items")
            } catch (e: NullPointerException) {
                e.printStackTrace()
            } catch (e: HttpException) {
                e.printStackTrace()
                when {
                    e.code() == 404 -> Toast.makeText(getApplication(),
                        "Not Found", Toast.LENGTH_LONG).show()
                    e.code() == 504 -> Toast.makeText(getApplication(),
                        "Please Check Network Connectivity", Toast.LENGTH_LONG).show()
                    else -> Toast.makeText(getApplication(),
                        "Something Is Not Right", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(getApplication(), "Something Is Not Right", Toast.LENGTH_LONG)
                    .show()
            }
        }
        setupWorkRequest()
    }

    private fun setupWorkRequest() {
        val workRequest = PeriodicWorkRequestBuilder<UpdateWorker>(
            20,
            TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(getApplication()).enqueue(workRequest)
    }
    companion object {
        val TAG = ListViewModel::class.java.simpleName
    }

    /**
     * Factory for constructing ForecastViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ListViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
