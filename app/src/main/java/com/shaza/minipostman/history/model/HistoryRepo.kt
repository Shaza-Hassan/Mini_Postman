package com.shaza.minipostman.history.model

import android.content.Context
import com.shaza.minipostman.shared.HttpResponse
import com.shaza.minipostman.utils.PostmanSqlite

class HistoryRepo {

    fun getAllRequests(context: Context): MutableList<HttpResponse>{
        return PostmanSqlite.getInstance(context).getAllRequests()
    }

    fun getAllGetRequests(context: Context): MutableList<HttpResponse>{
        return PostmanSqlite.getInstance(context).getAllGetRequests()
    }

    fun getAllPostRequests(context: Context): MutableList<HttpResponse>{
        return PostmanSqlite.getInstance(context).getAllPostRequest()
    }

    fun getAllRequestsSortedByTime(context: Context): MutableList<HttpResponse>{
        return PostmanSqlite.getInstance(context).getAllRequests()
    }

    fun getAllSucceedRequests(context: Context): MutableList<HttpResponse>{
        return PostmanSqlite.getInstance(context).getAllSuccessRequest()
    }

    fun getAllFailedRequests(context: Context): MutableList<HttpResponse>{
        return PostmanSqlite.getInstance(context).getAllFailedRequest()
    }
}