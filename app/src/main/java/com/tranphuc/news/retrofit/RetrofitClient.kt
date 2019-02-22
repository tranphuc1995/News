package tranphuc.com.testkotlin.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import retrofit2.converter.scalars.ScalarsConverterFactory


class RetrofitClient {

    companion object {
        var retrofit: Retrofit? = null
        fun getClient(baseUrl: String): Retrofit? {
/*
            val gson = GsonBuilder()
                    .setLenient()
                    .create()*/

            if (retrofit == null) {
                retrofit = Retrofit.Builder().baseUrl(baseUrl)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return retrofit
        }
    }

}