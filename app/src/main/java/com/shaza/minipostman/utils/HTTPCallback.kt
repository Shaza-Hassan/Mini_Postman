package com.shaza.minipostman.utils

import com.shaza.minipostman.shared.HttpResponse

interface HTTPCallback {
    fun processRunning()
    fun processFinish(output: HttpResponse?)
    fun processFailed(responseCode: Int, output: HttpResponse?)
}