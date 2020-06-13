package com.ananth.githubrepositories.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ananth.githubrepositories.model.Repository
import com.ananth.githubrepositories.network.RepoApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.await
import java.lang.NullPointerException

class ListViewModel : ViewModel() {

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
            var repositoriesDeferred = RepoApi.retrofitService.getRepositories()
            try {
                val repositoriesResult = repositoriesDeferred.await()
                _repoList.value = repositoriesResult
                Log.d(TAG, "Received ${_repoList.value!!.size} items")
            } catch (e: NullPointerException) {
                Log.e(TAG, e.toString())
            }
        }
    }

    companion object {
        val TAG = ListViewModel::class.java.simpleName
    }
}
