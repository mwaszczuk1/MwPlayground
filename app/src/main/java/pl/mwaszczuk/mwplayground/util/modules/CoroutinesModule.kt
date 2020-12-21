package pl.mwaszczuk.mwplayground.util.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.mwaszczuk.mwplayground.ui.main.WeatherUseCase
import pl.mwaszczuk.mwplayground.ui.main.coroutines.CoroutinesViewModel

val coroutinesModule = module {
    factory { WeatherUseCase(get()) }
    viewModel { CoroutinesViewModel(get()) }
}