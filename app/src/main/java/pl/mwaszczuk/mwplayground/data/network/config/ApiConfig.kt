package pl.mwaszczuk.mwplayground.data.network.config

import pl.mwaszczuk.mwplayground.data.network.api.MainApi
import retrofit2.Retrofit
import retrofit2.create

// Here we provide all our API interfaces

internal fun provideMainApi(retrofit: Retrofit): MainApi = retrofit.create()