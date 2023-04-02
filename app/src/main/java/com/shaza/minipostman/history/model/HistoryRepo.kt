package com.shaza.minipostman.history.model

import android.content.Context
import com.shaza.minipostman.shared.HttpResponse
import com.shaza.minipostman.shared.OrderClauses
import com.shaza.minipostman.shared.WhereClauses
import com.shaza.minipostman.utils.PostmanSqlite

class HistoryRepo {

    fun getAllRequests(context: Context, whereClause: List<WhereClauses>, sortedBy: OrderClauses?): MutableList<HttpResponse>{
        return PostmanSqlite.getInstance(context).getAllRequests(whereClause, sortedBy)
    }

}