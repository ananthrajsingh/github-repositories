package com.ananth.githubrepositories.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ananth.githubrepositories.model.Repository

class ListViewModel : ViewModel() {


    private val _repoList = MutableLiveData<List<Repository>>()
    // The external LiveData interface to the property is immutable, so only this class can modify
    val repoList: LiveData<List<Repository>>
        get() = _repoList
}
