package com.shaza.minipostman.home.viewmodel

import androidx.lifecycle.ViewModel
import com.shaza.minipostman.home.model.Header
import com.shaza.minipostman.home.model.HttpRequestType
import com.shaza.minipostman.home.model.repo.HomeRepo
import com.shaza.minipostman.utils.HTTPCallback

class HomeViewModel : ViewModel() {
    val homeRepo:HomeRepo

    init {
        homeRepo = HomeRepo()
    }

    val headers = mutableListOf<Header>()
    var url :String= ""
    var body :String? = null
    var requestType = HttpRequestType.GET

    fun makeRequest(httpCallback: HTTPCallback){
        val headers = constructRequestHeaders()
        homeRepo.callAPI(url,requestType,body, headers, httpCallback)
    }

    private fun constructRequestHeaders(): MutableMap<String, String> {
        val headersMap = mutableMapOf<String, String>()
        for (header in headers) {
            if (!header.title.isNullOrEmpty() && !header.value.isNullOrEmpty()) {
                headersMap[header.title!!] = header.value!!
            }
        }
        return headersMap
    }
}