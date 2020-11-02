package pl.mwaszczuk.mwplayground.data.network.config

import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    // OkHttp
    factory { provideOkHttpClient() }
    // Retrofit
    factory { provideRetrofit(get()) }

    // Apis
    single(named("1")) { provideMainApi(get()) }
    single(named("2")) { provideMainApi(get()) }

    single { provideWeatherApi(get()) }
}
