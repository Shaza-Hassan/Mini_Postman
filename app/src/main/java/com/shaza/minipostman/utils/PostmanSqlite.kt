package com.shaza.minipostman.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
import android.database.sqlite.SQLiteOpenHelper
import com.shaza.minipostman.shared.HttpResponse
import com.shaza.minipostman.shared.RequestTableData

class PostmanSqlite private constructor(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(RequestTableData.createRequestTableQuery())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(RequestTableData.dropRequestTableIfExistQuery())
        onCreate(db)
    }

    fun addRequestInDB(httpResponse: HttpResponse){
        val values = RequestTableData.addRequestToDB(httpResponse)
        val db = this.writableDatabase
        db.insertWithOnConflict(RequestTableData.REQUEST_TABLE,null,values,CONFLICT_REPLACE)
    }

    @SuppressLint("Recycle")
    fun getAllRequests(): MutableList<HttpResponse>{
        val db = this.writableDatabase
        val cursor = db.rawQuery(RequestTableData.QUERY_TO_GET_ALL_REQUEST,null)
        val requests = RequestTableData.getRequestDataFromCursor(cursor)
        return requests
    }

    @SuppressLint("Recycle")
    fun getAllRequestsSortedByTime(): MutableList<HttpResponse>{
        val db = this.writableDatabase
        val cursor = db.rawQuery(RequestTableData.QUERY_TO_GET_ALL_REQUEST_SORTED_BY_TIME,null)
        val requests = RequestTableData.getRequestDataFromCursor(cursor)
        return requests
    }

    @SuppressLint("Recycle")
    fun getAllGetRequests(): MutableList<HttpResponse>{
        val db = this.writableDatabase
        val cursor = db.rawQuery(RequestTableData.QUERY_TO_GET_ALL_GET_REQUEST,null)
        val requests = RequestTableData.getRequestDataFromCursor(cursor)
        return requests
    }

    @SuppressLint("Recycle")
    fun getAllPostRequest(): MutableList<HttpResponse>{
        val db = this.writableDatabase
        val cursor = db.rawQuery(RequestTableData.QUERY_TO_GET_ALL_POST_REQUEST,null)
        val requests = RequestTableData.getRequestDataFromCursor(cursor)
        return requests
    }

    @SuppressLint("Recycle")
    fun getAllSuccessRequest(): MutableList<HttpResponse>{
        val db = this.writableDatabase
        val cursor = db.rawQuery(RequestTableData.QUERY_TO_GET_ALL_SUCCESS_REQUEST,null)
        val requests = RequestTableData.getRequestDataFromCursor(cursor)
        return requests
    }

    @SuppressLint("Recycle")
    fun getAllFailedRequest(): MutableList<HttpResponse>{
        val db = this.writableDatabase
        val cursor = db.rawQuery(RequestTableData.QUERY_TO_GET_ALL_FAILED_REQUEST,null)
        val requests = RequestTableData.getRequestDataFromCursor(cursor)
        return requests
    }

    companion object{
        const val DB_NAME = "miniPostman.db"
        const val DB_VERSION = 1
        private var instance : PostmanSqlite? = null

        fun getInstance(context: Context): PostmanSqlite{
            if (instance == null) {
                instance = PostmanSqlite(context)
            }
            return instance!!
        }
    }
}