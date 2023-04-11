package com.shaza.minipostman.home.model.repo

import android.content.Context
import com.shaza.minipostman.shared.HttpRequestType
import com.shaza.minipostman.shared.HttpResponse
import com.shaza.minipostman.shared.RequestDB
import com.shaza.minipostman.utils.CallAPI
import com.shaza.minipostman.utils.HTTPCallback

class HomeRepo {

    fun callAPI(
        url: String,
        httpRequestType: HttpRequestType,
        body: String?,
        fileAsByteArray: ByteArray?,
        headers: MutableMap<String, String>?,
        httpCallback: HTTPCallback
    ) {
        val callAPI =
            CallAPI(requestURL = url, httpRequestType, headers, body, fileAsByteArray, httpCallback)
        callAPI.makeRequest()
    }

    fun saveRequestInDB(httpResponse: HttpResponse, context: Context) {
        RequestDB(context).addRequestInDB(httpResponse)
    }
}