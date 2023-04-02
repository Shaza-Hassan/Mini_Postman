package com.shaza.minipostman.response.viewmodel

import android.os.Build
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.shaza.minipostman.response.view.ResponseFragment
import com.shaza.minipostman.shared.HttpResponse

class ResponseViewModel : ViewModel() {
    var response: HttpResponse? = null

    fun extractArgument(bundle: Bundle?) {
        bundle?.let { bundle ->
            response = when (Build.VERSION.SDK_INT) {
                Build.VERSION_CODES.TIRAMISU -> {
                    bundle.getParcelable(ResponseFragment.RESPONSE_KEY, HttpResponse::class.java)
                }
                else -> {
                    bundle.getParcelable(ResponseFragment.RESPONSE_KEY)
                }
            }
        }
    }
}