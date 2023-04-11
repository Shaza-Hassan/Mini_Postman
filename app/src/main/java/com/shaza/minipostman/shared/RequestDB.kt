package com.shaza.minipostman.shared

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.shaza.minipostman.utils.PostmanSqlite

/**
 * Created by Shaza Hassan on 11/Apr/2023.
 */
class RequestDB(context: Context) {

    private var dbHelper: PostmanSqlite? = null

    private var context: Context? = null

    private var database: SQLiteDatabase? = null

    init{
        this.context = context
        dbHelper = PostmanSqlite.getInstance(context)
        database = dbHelper?.writableDatabase
    }

    fun addRequestInDB(httpResponse: HttpResponse) {
        val values = RequestTableData.addRequestToDB(httpResponse)
        database?.insertWithOnConflict(RequestTableData.REQUEST_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getAllRequests(
        whereClause: List<WhereClauses>,
        sortedBy: OrderClauses?
    ): MutableList<HttpResponse> {
        val query = RequestTableData.getRequests(whereClause, sortedBy)
        Log.v(this::class.java.simpleName, query)
        val cursor = database?.rawQuery(query, null)
        val requests = cursor?.let { RequestTableData.getRequestDataFromCursor(it) }
        return requests ?: mutableListOf()
    }

}