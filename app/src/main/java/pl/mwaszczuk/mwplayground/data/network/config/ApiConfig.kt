package pl.mwaszczuk.mwplayground.data.network.config

import pl.mwaszczuk.mwplayground.data.network.api.MainApi
import pl.mwaszczuk.mwplayground.data.network.api.WeatherApi
import retrofit2.Retrofit
import retrofit2.create

// Here we provide all our API interfaces

internal fun provideMainApi(retrofit: Retrofit): MainApi = retrofit.create()
internal fun provideWeatherApi(retrofit: Retrofit): WeatherApi = retrofit.create()