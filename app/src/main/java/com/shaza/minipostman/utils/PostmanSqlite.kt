package com.shaza.minipostman.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.shaza.minipostman.shared.HttpResponse
import com.shaza.minipostman.shared.OrderClauses
import com.shaza.minipostman.shared.RequestTableData
import com.shaza.minipostman.shared.WhereClauses

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
    fun getAllRequests(whereClause: List<WhereClauses>, sortedBy: OrderClauses?): MutableList<HttpResponse>{
        val db = this.writableDatabase
        val query = RequestTableData.getRequests(whereClause, sortedBy)
        Log.v(this::class.java.simpleName,query)
        val cursor = db.rawQuery(query,null)
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