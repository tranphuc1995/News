package com.tranphuc.news.global

import tranphuc.com.testkotlin.retrofit.ApiInterface

class VariableStatic {
    companion object {
        lateinit var mService: ApiInterface
    }
}