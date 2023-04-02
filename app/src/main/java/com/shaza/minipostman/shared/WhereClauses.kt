package com.shaza.minipostman.shared

import com.shaza.minipostman.shared.RequestTableData.COLUMN_ERROR
import com.shaza.minipostman.shared.RequestTableData.COLUMN_ID
import com.shaza.minipostman.shared.RequestTableData.COLUMN_REQUEST_TYPE
import com.shaza.minipostman.shared.RequestTableData.COLUMN_RESPONSE
import com.shaza.minipostman.shared.RequestTableData.COLUMN_TIME

enum class WhereClauses(val clauses: String) {
    GetAllRequest(""),
    GetAllGETRequest("$COLUMN_REQUEST_TYPE = '${HttpRequestType.GET.name}'"),
    GetAllPOSTRequest("$COLUMN_REQUEST_TYPE =  '${HttpRequestType.POST.name}'"),
    GetAllSuccessRequest("$COLUMN_RESPONSE IS NOT NULL AND $COLUMN_ERROR IS NULL"),
    GetAllFailedRequest("$COLUMN_ERROR IS NOT NULL AND $COLUMN_RESPONSE IS NULL")
}

enum class OrderClauses(val entity: String, val sortingCriteria: String) {
    OrderById(COLUMN_ID, "ASC"),
    OrderByTime(COLUMN_TIME, "ASC")
}