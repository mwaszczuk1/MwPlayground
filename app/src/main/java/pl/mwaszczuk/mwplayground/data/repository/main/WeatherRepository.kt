package pl.mwaszczuk.mwplayground.data.repository.main

import pl.mwaszczuk.mwplayground.data.network.api.WeatherApi
import pl.mwaszczuk.mwplayground.data.network.config.appId

class WeatherRepository(
    private val api: WeatherApi
) {

    suspend fun getWeather(
        city: String
    ) = api.getWeather(city, appId)
}