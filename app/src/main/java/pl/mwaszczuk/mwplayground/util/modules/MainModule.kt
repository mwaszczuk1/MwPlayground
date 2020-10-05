package pl.mwaszczuk.mwplayground.util.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.mwaszczuk.mwplayground.ui.main.MainUseCase
import pl.mwaszczuk.mwplayground.ui.main.MainViewModel

val mainModule = module {
    factory { MainUseCase(get()) }
    viewModel { MainViewModel(get()) }
}
