package pl.mwaszczuk.mwplayground.data.network.api

import pl.mwaszczuk.mwplayground.data.network.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather")
    suspend fun getWeather(
            @Query("q") city: String,
            @Query("appid") appId: String,
    ): WeatherDto
}
