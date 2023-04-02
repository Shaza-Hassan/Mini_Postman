package com.shaza.minipostman.home.model.repo

import android.content.Context
import com.shaza.minipostman.shared.HttpRequestType
import com.shaza.minipostman.shared.HttpResponse
import com.shaza.minipostman.utils.CallAPI
import com.shaza.minipostman.utils.HTTPCallback
import com.shaza.minipostman.utils.PostmanSqlite

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
        callAPI.execute()
    }

    fun saveRequestInDB(httpResponse: HttpResponse, context: Context) {
        PostmanSqlite.getInstance(context).addRequestInDB(httpResponse)
    }
}