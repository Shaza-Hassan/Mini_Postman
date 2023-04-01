package com.shaza.minipostman.history.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shaza.minipostman.history.model.HistoryRepo
import com.shaza.minipostman.shared.HttpResponse

class HistoryViewModel : ViewModel() {
    val repo: HistoryRepo = HistoryRepo()

    val requests = MutableLiveData<MutableList<HttpResponse>>()

    fun getAllRequests(context: Context){
        requests.value = repo.getAllRequests(context)
    }

    fun getAllGETRequests(context: Context){
        requests.value = repo.getAllGetRequests(context)
    }

    fun getAllPostRequests(context: Context){
        requests.value = repo.getAllPostRequests(context)
    }

    fun getAllRequestsSortedByTime(context: Context){
        requests.value = repo.getAllRequestsSortedByTime(context)
    }

    fun getAllSuccessRequests(context: Context){
        requests.value = repo.getAllSucceedRequests(context)
    }

    fun getAllFailedRequests(context: Context){
        requests.value = repo.getAllFailedRequests(context)
    }

}