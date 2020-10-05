package pl.mwaszczuk.mwplayground.data.network.config

import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Suppress("LongParameterList", "UnusedPrivateMember")
internal fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient()
        .newBuilder()
        .build()
}

internal fun provideRetrofit(
    okHttpClient: OkHttpClient,
): Retrofit {
    val retrofitBuilder = Retrofit.Builder()
        .baseUrl(API_URL)
        .client(okHttpClient)

    return retrofitBuilder.build()
}

private const val JSON_MEDIA_TYPE = "application/json"
private const val HTTPS_PREFIX = "https://"
private const val API_URL = HTTPS_PREFIX + "api_base_url"
