package com.shaza.minipostman.shared

import com.shaza.minipostman.home.model.HttpRequestType

data class HttpResponse(
    val httpRequestType: HttpRequestType,
    val url:String,
    val responseCode: Int,
    val elapsedTime: Long,
    val response: String?,
    val error:String?,
    val queryParams: String?,
    val bodyRequest: String?,
    val requestHeaders:Map<String,String>?,
    val responseHeaders:Map<String,MutableList<String>>?
    )