package com.shaza.minipostman.shared

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import com.shaza.minipostman.shared.HttpRequestType.GET
import com.shaza.minipostman.shared.HttpRequestType.POST

object RequestTableData {
    const val REQUEST_TABLE = "request_table"
    private const val COLUMN_ID = "_id"
    private const val COLUMN_URL = "url"
    private const val COLUMN_REQUEST_TYPE = "type"
    private const val COLUMN_STATUS_CODE = "status_code"
    private const val COLUMN_TIME = "time"
    private const val COLUMN_RESPONSE = "response"
    private const val COLUMN_ERROR = "error"
    private const val COLUMN_QUERY_PARAMS = "query_params"
    private const val COLUMN_BODY_REQUEST = "body_request"
    private const val COLUMN_REQUEST_HEADERS = "request_headers"
    private const val COLUMN_RESPONSE_HEADERS = "response_headers"

    private const val TEXT_NOT_NULL = "TEXT NOT NULL"
    private const val TEXT = "TEXT"
    private const val INTEGER_NOT_NULL = "INTEGER NOT NULL"

    const val QUERY_TO_GET_ALL_REQUEST = "SELECT * FROM $REQUEST_TABLE ORDER BY $COLUMN_ID ASC"
    const val QUERY_TO_GET_ALL_REQUEST_SORTED_BY_TIME = "SELECT * FROM $REQUEST_TABLE ORDER BY $COLUMN_TIME ASC"
    val QUERY_TO_GET_ALL_GET_REQUEST = "SELECT * FROM $REQUEST_TABLE WHERE $COLUMN_REQUEST_TYPE = ${GET.name}"
    val QUERY_TO_GET_ALL_POST_REQUEST = "SELECT * FROM $REQUEST_TABLE WHERE $COLUMN_REQUEST_TYPE = ${POST.name}"
    val QUERY_TO_GET_ALL_SUCCESS_REQUEST = "SELECT * FROM $REQUEST_TABLE WHERE $COLUMN_RESPONSE IS NOT NULL AND $COLUMN_ERROR IS NULL"
    val QUERY_TO_GET_ALL_FAILED_REQUEST = "SELECT * FROM $REQUEST_TABLE WHERE $COLUMN_RESPONSE IS NULL AND $COLUMN_ERROR IS NOT NULL"

    fun createRequestTableQuery() = "CREATE TABLE $REQUEST_TABLE (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "$COLUMN_URL $TEXT_NOT_NULL, " +
                    "$COLUMN_REQUEST_TYPE $TEXT_NOT_NULL, " +
                    "$COLUMN_STATUS_CODE $INTEGER_NOT_NULL," +
                    "$COLUMN_TIME $INTEGER_NOT_NULL," +
                    "$COLUMN_RESPONSE $TEXT," +
                    "$COLUMN_ERROR $TEXT," +
                    "$COLUMN_QUERY_PARAMS $TEXT," +
                    "$COLUMN_BODY_REQUEST $TEXT," +
                    "$COLUMN_REQUEST_HEADERS $TEXT," +
                    "$COLUMN_RESPONSE_HEADERS $TEXT)"

    fun dropRequestTableIfExistQuery() = "DROP TABLE IF EXISTS $REQUEST_TABLE"

    fun addRequestToDB(httpResponse: HttpResponse) : ContentValues{
        val values = ContentValues()
        values.put(COLUMN_URL,httpResponse.url)
        values.put(COLUMN_REQUEST_TYPE,httpResponse.httpRequestType.name)
        values.put(COLUMN_STATUS_CODE,httpResponse.statusCode)
        values.put(COLUMN_TIME,httpResponse.elapsedTime)
        values.put(COLUMN_RESPONSE,httpResponse.response)
        values.put(COLUMN_ERROR,httpResponse.error)
        values.put(COLUMN_QUERY_PARAMS,httpResponse.queryParams)
        values.put(COLUMN_BODY_REQUEST,httpResponse.bodyRequest)
        values.put(COLUMN_REQUEST_HEADERS,httpResponse.requestHeaders)
        values.put(COLUMN_RESPONSE_HEADERS,httpResponse.responseHeaders)
        return values
    }

    @SuppressLint("Range")
    fun getRequestDataFromCursor(cursor:Cursor): MutableList<HttpResponse>{
        val requests = mutableListOf<HttpResponse>()
        if (cursor.moveToFirst()){
            do {
                val url = cursor.getString(cursor.getColumnIndex(COLUMN_URL))
                val type = cursor.getString(cursor.getColumnIndex(COLUMN_REQUEST_TYPE))
                val statusCode= cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS_CODE))
                val time = cursor.getInt(cursor.getColumnIndex(COLUMN_TIME))
                val response = cursor.getString(cursor.getColumnIndex(COLUMN_RESPONSE))
                val error = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR))
                val queryParams = cursor.getString(cursor.getColumnIndex(COLUMN_QUERY_PARAMS))
                val bodyRequest = cursor.getString(cursor.getColumnIndex(COLUMN_BODY_REQUEST))
                val requestHeader = cursor.getString(cursor.getColumnIndex(COLUMN_REQUEST_HEADERS))
                val responseHeader = cursor.getString(cursor.getColumnIndex(COLUMN_RESPONSE_HEADERS))
                val httpResponse = HttpResponse(
                    url = url,
                    httpRequestType = HttpRequestType.getType(type),
                    statusCode = statusCode,
                    elapsedTime = time.toLong(),
                    response = response,
                    error = error,
                    queryParams = queryParams,
                    bodyRequest = bodyRequest,
                    responseHeaders = responseHeader,
                    requestHeaders = requestHeader
                )
                requests.add(httpResponse)
            }while (cursor.moveToNext())
        }
        return requests
    }
}