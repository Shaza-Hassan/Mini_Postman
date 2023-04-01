package com.shaza.minipostman.utils

import android.os.AsyncTask
import android.util.Log
import com.shaza.minipostman.home.model.HttpRequestType
import com.shaza.minipostman.shared.HttpResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

class PostRequest (
    requestURL: String,
    headers: Map<String, String>?,
    body:String?,
    asyncResponse: HTTPCallback?
) : AsyncTask<String?, Void?, HttpResponse>() {
    var requestURL = ""
    var delegate: HTTPCallback? = null //Call back interface
    var body:String?
    var headers: Map<String, String>? = null
    var res_code = 0

    init {
        delegate = asyncResponse //Assigning call back interfacethrough constructor
        this.headers = headers
        this.requestURL = requestURL
        this.body = body
    }

    override fun onPreExecute() {
        delegate!!.processRunning()
    }

    override fun doInBackground(vararg params: String?): HttpResponse? {

        return performPostCall(requestURL)
    }

    override fun onPostExecute(result: HttpResponse) {
        //super.onPostExecute(result);
        if (res_code == HttpURLConnection.HTTP_OK) {
            delegate!!.processFinish(result)
        } else {
            delegate!!.processFailed(res_code, result)
        }
    }

    fun performPostCall(
        requestURL: String?,
    ): HttpResponse {
        Log.v("HTTP Request URL", requestURL.toString())
        val url: URL
        var httpResponse: HttpResponse
        var response: String? = ""
        var error: String? = ""

        try {
            url = URL(requestURL)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.readTimeout = 30000
            conn.connectTimeout = 60000
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json");
            headers?.forEach { (key, value) ->
                conn.setRequestProperty(key, value)
            }
            //Connect to our url
            if (this.body != null) {
                val writer = OutputStreamWriter(conn.outputStream)
                writer.write(body.toString())
                writer.flush()
            }
            conn.connect()
            val responseCode: Int = conn.responseCode
            val responseHeader = conn.headerFields
            res_code = responseCode
            when (responseCode) {
                HttpsURLConnection.HTTP_OK -> {
                    var line: String?
                    val br = BufferedReader(InputStreamReader(conn.inputStream))
                    line = br.readLine()
                    while (line != null) {
                        response += line
                        line = br.readLine()

                    }
                    error = null
                }
                else -> {
                    var line: String?
                    val br = BufferedReader(InputStreamReader(conn.errorStream))
                    line = br.readLine()
                    while (line != null) {
                        error += line
                        line = br.readLine()
                    }
                    response = null

                }
            }
            httpResponse = HttpResponse(
                httpRequestType = HttpRequestType.GET,
                url = requestURL!!,
                responseCode = responseCode,
                response = response,
                error = error,
                queryParams = conn.url.query,
                bodyRequest = body,
                requestHeaders = headers,
                responseHeaders = responseHeader
            )
        } catch (e: Exception) {
            e.printStackTrace()
            httpResponse = HttpResponse(
                httpRequestType = HttpRequestType.GET,
                url = requestURL!!,
                responseCode = res_code,
                response = null,
                error = e.message,
                queryParams = null,
                bodyRequest = body,
                requestHeaders = headers,
                responseHeaders = null
            )

        }
        return httpResponse
    }
}
