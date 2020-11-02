package pl.mwaszczuk.mwplayground.data.network.config

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("LongParameterList", "UnusedPrivateMember")
internal fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient()
        .newBuilder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
}

internal fun provideRetrofit(
    okHttpClient: OkHttpClient,
): Retrofit {
    val retrofitBuilder = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)

    return retrofitBuilder.build()
}

private const val JSON_MEDIA_TYPE = "application/json"
private const val HTTPS_PREFIX = "https://"
private const val API_URL = HTTPS_PREFIX + "api.openweathermap.org/"
