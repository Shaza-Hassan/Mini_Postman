package com.shaza.minipostman.home.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shaza.minipostman.home.model.Header
import com.shaza.minipostman.home.model.repo.HomeRepo
import com.shaza.minipostman.shared.HttpRequestType
import com.shaza.minipostman.shared.HttpResponse
import com.shaza.minipostman.utils.CheckInternetConnection
import com.shaza.minipostman.utils.HTTPCallback
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors

class HomeViewModel : ViewModel() {
    val homeRepo: HomeRepo = HomeRepo()

    val headers = mutableListOf<Header>()
    var url: String = ""
    var body: String? = null
    var requestType = HttpRequestType.GET
    val noInternetConnection = MutableLiveData<Boolean>()
    val navigateToResultScreen = MutableLiveData<Boolean>()
    var httpResponse : HttpResponse? = null
    var fileAsByteArray: ByteArray? = null

    fun makeRequest(httpCallback: HTTPCallback, context: Context) {
        if (CheckInternetConnection.isInternetAvailable(context)) {
            val headers = constructRequestHeaders()
            homeRepo.callAPI(url, requestType, body, fileAsByteArray, headers, httpCallback)
        } else {
            noInternetConnection.value = true
        }
    }

    private fun constructRequestHeaders(): MutableMap<String, String>? {
        val headersMap = mutableMapOf<String, String>()
        for (header in headers) {
            if (!header.title.isNullOrEmpty() && !header.value.isNullOrEmpty()) {
                headersMap[header.title!!] = header.value!!
            }
        }
        return if (headersMap.isEmpty()) null else headersMap
    }

    fun addRequestToDB(context: Context, httpResponse: HttpResponse) {
        this.httpResponse = httpResponse
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            homeRepo.saveRequestInDB(httpResponse, context)
            handler.post {
                navigateToResultScreen.value = true
            }
        }
    }

    fun compressImage(photoUri: Uri?, context: Context) {
        var thumbnail: Bitmap? = null
        val contentResolver: ContentResolver = context.contentResolver
        try {
            val source: ImageDecoder.Source =
                ImageDecoder.createSource(contentResolver, photoUri!!)
            thumbnail = ImageDecoder.decodeBitmap(source)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val bytes = ByteArrayOutputStream()
        thumbnail?.compress(Bitmap.CompressFormat.JPEG, 70, bytes)

        fileAsByteArray = bytes.toByteArray()

    }
}