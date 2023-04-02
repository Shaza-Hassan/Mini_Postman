package com.shaza.minipostman.shared

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HttpResponse(
    val httpRequestType: HttpRequestType,
    val url: String,
    val statusCode: Int,
    val elapsedTime: Long,
    val response: String?,
    val error: String?,
    val queryParams: String?,
    val bodyRequest: String?,
    val requestHeaders: String?,
    val responseHeaders: String?
) : Parcelable