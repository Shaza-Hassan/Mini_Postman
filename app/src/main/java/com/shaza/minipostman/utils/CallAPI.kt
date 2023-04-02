package com.shaza.minipostman.utils

import android.os.AsyncTask
import com.shaza.minipostman.shared.HttpRequestType
import com.shaza.minipostman.shared.HttpResponse
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class CallAPI(
    requestURL: String,
    httpRequestType: HttpRequestType,
    headers: Map<String, String>?,
    body: String?,
    fileAsByteArray: ByteArray?,
    asyncResponse: HTTPCallback?
) : AsyncTask<String?, Void?, HttpResponse>() {
    var requestURL = ""
    var delegate: HTTPCallback? = null //Call back interface
    private var headers: Map<String, String>? = null
    private var res_code = 0
    var httpRequestType: HttpRequestType
    var body: String? = null
    var fileAsByteArray: ByteArray? = null

    init {
        delegate = asyncResponse //Assigning call back interface through constructor
        this.headers = headers
        this.requestURL = requestURL
        this.httpRequestType = httpRequestType
        this.body = body
        this.fileAsByteArray = fileAsByteArray
    }

    override fun onPreExecute() {
        delegate!!.processRunning()
    }

    override fun doInBackground(vararg params: String?): HttpResponse? {
        if (this.fileAsByteArray != null) {
            return uploadFile()
        } else {
            return performPostCall(requestURL)
        }
    }

    override fun onPostExecute(result: HttpResponse) {
        //super.onPostExecute(result);
        if (res_code == HttpURLConnection.HTTP_OK) {
            delegate!!.processFinish(result)
        } else {
            delegate!!.processFailed(result)
        }
    }

    private fun performPostCall(
        requestURL: String?,
    ): HttpResponse {
        val startTime = System.currentTimeMillis()

        val url: URL
        var httpResponse: HttpResponse
        var response: String? = null
        var error: String? = null
        var statusCode = 0
        var responseHeader: MutableMap<String, List<String>>? = null
        var queryParams: String? = null

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
            statusCode = conn.responseCode
            res_code = statusCode
            responseHeader = conn.headerFields
            queryParams = conn.url.query

            val pair = handleResponse(statusCode, conn, response, error)
            error = pair.first
            response = pair.second

        } catch (e: Exception) {
            error = e.message
            response = null

        } finally {
            val elapsedTime = System.currentTimeMillis() - startTime

            httpResponse = HttpResponse(
                httpRequestType = httpRequestType,
                url = this.requestURL,
                elapsedTime = elapsedTime,
                statusCode = statusCode,
                response = response,
                error = error,
                queryParams = queryParams,
                bodyRequest = body,
                requestHeaders = headers?.toString(),
                responseHeaders = responseHeader?.toString()
            )
        }
        return httpResponse
    }

    private fun handleResponse(
        statusCode: Int,
        conn: HttpURLConnection,
        response: String?,
        error: String?
    ): Pair<String?, String?> {
        var response1 = response
        var error1 = error
        when (statusCode) {
            HttpsURLConnection.HTTP_OK -> {
                var line: String?
                val br = BufferedReader(InputStreamReader(conn.inputStream))
                line = br.readLine()
                while (line != null) {
                    response1 += line
                    line = br.readLine()

                }
                error1 = null
            }
            else -> {
                var line: String?
                val br = BufferedReader(InputStreamReader(conn.errorStream))
                line = br.readLine()
                while (line != null) {
                    error1 += line
                    line = br.readLine()
                }
                response1 = null

            }
        }
        return Pair(error1, response1)
    }


    private fun uploadFile(): HttpResponse {

        val startTime = System.currentTimeMillis()

        val url: URL
        var httpResponse: HttpResponse
        var response: String? = null
        var error: String? = null
        var statusCode = 0
        var responseHeader: MutableMap<String, List<String>>? = null
        var queryParams: String? = null
        try {
            val lineEnd = "\r\n"
            val twoHyphens = "--"
            val boundary = "*****"
            var bytesRead: Int
            var bytesAvailable: Int
            var bufferSize: Int
            val maxBufferSize = 1 * 1024 * 1024
            url = URL(requestURL)
            val connection = url.openConnection() as HttpURLConnection

            // Allow Inputs &amp; Outputs.
            connection.doInput = true
            connection.doOutput = true
            connection.useCaches = false

            // Set HTTP method to POST.
            connection.requestMethod = "POST"
            connection.setRequestProperty("Connection", "Keep-Alive")
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=$boundary")

            val outputStream = DataOutputStream(connection.outputStream)
            outputStream.writeBytes(twoHyphens + boundary + lineEnd)
            outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"${this.fileAsByteArray}\"$lineEnd")
            outputStream.writeBytes(lineEnd)
            outputStream.write(fileAsByteArray)
            outputStream.writeBytes(lineEnd)
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)

            statusCode = connection.responseCode
            responseHeader = connection.headerFields
            outputStream.flush()
            outputStream.close()
            val pair = handleResponse(statusCode, connection, response, error)
            error = pair.first
            response = pair.second
        } catch (e: Exception) {
            error = e.message
            response = null
        } finally {
            val elapsedTime = System.currentTimeMillis() - startTime

            httpResponse = HttpResponse(
                httpRequestType = httpRequestType,
                url = this.requestURL,
                elapsedTime = elapsedTime,
                statusCode = statusCode,
                response = response,
                error = error,
                queryParams = queryParams,
                bodyRequest = body,
                requestHeaders = headers?.toString(),
                responseHeaders = responseHeader?.toString()
            )
        }
        return httpResponse
    }
}


