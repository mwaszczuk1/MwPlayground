package pl.mwaszczuk.mwplayground.data.repository.config

import org.koin.dsl.module
import pl.mwaszczuk.mwplayground.data.repository.main.MainRepository

val repositoryModule = module {
    factory { MainRepository(get()) }
}
