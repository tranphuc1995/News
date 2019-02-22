package tranphuc.com.testkotlin.retrofit

import com.tranphuc.news.nativelib.NativeLib

class ApiUtils {
    companion object {
        val BASE_URL: String = NativeLib.getDomain()
        fun getApiService(): ApiInterface? {
            return RetrofitClient.getClient(BASE_URL)?.create(ApiInterface::class.java)
        }
    }
}