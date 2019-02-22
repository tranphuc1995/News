package com.tranphuc.news.global

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import io.realm.Realm
import io.realm.RealmConfiguration
import tranphuc.com.testkotlin.retrofit.ApiUtils

class MethodStatic {
    companion object {
        fun createGlobal() {
            VariableStatic.mService = ApiUtils.getApiService()!!
        }

        fun initRealm(context: Context): Realm? {
            var realm: Realm? = null
            Realm.init(context)
            var config: RealmConfiguration = RealmConfiguration.Builder()
                    .name("News")
                    .schemaVersion(1)
                    .deleteRealmIfMigrationNeeded()
                    .build();
            Realm.setDefaultConfiguration(config)
            realm = Realm.getDefaultInstance()
            return realm
        }

        fun setLanguage(context: Context, code: String) {
            var sharedPref: SharedPreferences = context.getSharedPreferences("language", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("code", code)
            editor.commit()
        }

        fun getLanguage(context: Context): String? {
            var sharedPref: SharedPreferences = context.getSharedPreferences("language", Context.MODE_PRIVATE)
            return sharedPref.getString("code", "vn")
        }

        fun isNetworkReachable(context: Context): Boolean {
            if (context != null) {
                var connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                var info: Array<NetworkInfo> = connectivityManager.getAllNetworkInfo()
                if (info != null) {
                    for (i in 0..(info.size - 1)) {
                        if (info.get(i).isConnected)
                            return true
                    }
                }
            }
            return false
        }
    }
}