package com.shaza.minipostman.history.viewmodel

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shaza.minipostman.history.model.HistoryRepo
import com.shaza.minipostman.shared.HttpResponse
import com.shaza.minipostman.shared.OrderClauses
import com.shaza.minipostman.shared.WhereClauses
import java.util.concurrent.Executors

class HistoryViewModel : ViewModel() {
    val repo: HistoryRepo = HistoryRepo()

    val requests = MutableLiveData<MutableList<HttpResponse>>()

    val filter: MutableList<WhereClauses> = mutableListOf()
    var orderClauses: OrderClauses = OrderClauses.OrderById

    fun getAllRequests(context: Context) {
        filter.removeAll { it == WhereClauses.GetAllRequest }
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            val requests = repo.getAllRequests(context,filter,orderClauses)

            handler.post {
                this.requests.value = requests
            }
        }
    }

    fun updateRequestType(whereClauses: WhereClauses) {
        filter.removeAll {
            it == WhereClauses.GetAllPOSTRequest || it == WhereClauses.GetAllGETRequest
        }
        filter.add(whereClauses)
    }

    fun updateRequestStatus(whereClauses: WhereClauses) {
        filter.removeAll {
            it == WhereClauses.GetAllSuccessRequest || it == WhereClauses.GetAllFailedRequest
        }
        filter.add(whereClauses)
    }
}