package pl.mwaszczuk.mwplayground.ui.main.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import pl.mwaszczuk.mwplayground.data.network.dto.WeatherDto
import pl.mwaszczuk.mwplayground.data.repository.main.WeatherRepository

class CoroutinesUseCase(
    private val repo: WeatherRepository
) {

    suspend fun getWeather(city: String) = repo.getWeather(city)

    // example of performing async operations in UseCase / Repository etc
    suspend fun getWeathersAsync(city: String): List<WeatherDto> {
        return withContext(Dispatchers.IO) {
            val weather1 = async { repo.getWeather("Białystok") }
            val weather2 = async { repo.getWeather("Warsaw") }
            val weather3 = async { repo.getWeather("Cracow") }

            listOf(
                weather1.await(),
                weather2.await(),
                weather3.await()
            )
        }
    }

}