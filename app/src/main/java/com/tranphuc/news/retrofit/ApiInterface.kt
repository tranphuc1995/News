package tranphuc.com.testkotlin.retrofit


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiInterface {
    @GET("upload/rss/{category}.rss")
    fun getRss(@Path("category") category: String): Call<String>
}