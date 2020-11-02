package pl.mwaszczuk.mwplayground.data.repository.config

import org.koin.dsl.module
import pl.mwaszczuk.mwplayground.data.repository.main.MainRepository
import pl.mwaszczuk.mwplayground.data.repository.main.WeatherRepository

val repositoryModule = module {
    factory { MainRepository(get()) }
    factory { WeatherRepository(get()) }
}
