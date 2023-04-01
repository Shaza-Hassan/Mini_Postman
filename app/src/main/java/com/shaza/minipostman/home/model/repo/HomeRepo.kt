package com.shaza.minipostman.home.model.repo

import com.shaza.minipostman.home.model.HttpRequestType
import com.shaza.minipostman.utils.CallAPI
import com.shaza.minipostman.utils.HTTPCallback

class HomeRepo {

    fun callAPI(url:String, httpRequestType: HttpRequestType, body:String?, headers:MutableMap<String,String>?, httpCallback: HTTPCallback){
        val callAPI = CallAPI(requestURL = url,httpRequestType,headers,body,httpCallback)
        callAPI.execute()
    }
}