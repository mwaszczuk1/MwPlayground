package pl.mwaszczuk.mwplayground.util.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.mwaszczuk.mwplayground.ui.main.coroutines.CoroutinesUseCase
import pl.mwaszczuk.mwplayground.ui.main.coroutines.CoroutinesViewModel

val coroutinesModule = module {
    factory { CoroutinesUseCase(get()) }
    viewModel { CoroutinesViewModel(get()) }
}