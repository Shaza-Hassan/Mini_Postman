package com.shaza.minipostman.utils

import android.os.AsyncTask
import android.util.Log
import com.shaza.minipostman.shared.HttpRequestType
import com.shaza.minipostman.shared.HttpResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class CallAPI(
    requestURL: String,
    httpRequestType: HttpRequestType,
    headers: Map<String, String>?,
    body:String?,
    asyncResponse: HTTPCallback?
) : AsyncTask<String?, Void?, HttpResponse>() {
    var requestURL = ""
    var delegate: HTTPCallback? = null //Call back interface
    private var headers: Map<String, String>? = null
    private var res_code = 0
    var httpRequestType: HttpRequestType = HttpRequestType.GET
    var body:String? = null

    init {
        delegate = asyncResponse //Assigning call back interface through constructor
        this.headers = headers
        this.requestURL = requestURL
        this.httpRequestType = httpRequestType
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
            delegate!!.processFailed(result)
        }
    }

    fun performPostCall(
        requestURL: String?,
    ): HttpResponse {
        val startTime = System.currentTimeMillis()

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
            conn.requestMethod = httpRequestType.name

            headers?.forEach { (key, value) ->
                conn.setRequestProperty(key, value)
            }

            if (this.body != null) {
                conn.setRequestProperty("Content-Type", "application/json")
                val writer = OutputStreamWriter(conn.outputStream)
                writer.write(body.toString())
                writer.flush()
            }

            //Connect to our url
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
            val elapsedTime = System.currentTimeMillis() - startTime

            httpResponse = HttpResponse(
                httpRequestType = httpRequestType,
                url = requestURL!!,
                elapsedTime = elapsedTime,
                statusCode = responseCode,
                response = response,
                error = error,
                queryParams = conn.url.query,
                bodyRequest = body,
                requestHeaders = headers.toString(),
                responseHeaders = responseHeader.toString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            val elapsedTime = System.currentTimeMillis() - startTime

            httpResponse = HttpResponse(
                httpRequestType = HttpRequestType.GET,
                url = requestURL!!,
                elapsedTime = elapsedTime,
                statusCode = res_code,
                response = null,
                error = e.message,
                queryParams = null,
                bodyRequest = body,
                requestHeaders = headers.toString(),
                responseHeaders = null
            )

        }
        return httpResponse
    }
}


