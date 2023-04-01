package com.shaza.minipostman.shared

enum class HttpRequestType {
    GET,
    POST;

    companion object{
        fun getType(type:String): HttpRequestType{
            return when(type){
                GET.name -> {
                   GET
                }
                POST.name -> {
                    POST
                }
                else -> {
                    GET
                }
            }
        }
    }
}