package pl.mwaszczuk.mwplayground

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.mwaszczuk.mwplayground.data.network.config.networkModule
import pl.mwaszczuk.mwplayground.data.repository.config.repositoryModule
import pl.mwaszczuk.mwplayground.util.modules.coroutinesModule
import pl.mwaszczuk.mwplayground.util.modules.mainModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            modules(listOf(
                networkModule,
                repositoryModule,
                mainModule,
                coroutinesModule
            ))
//            koin.loadModules(listOf(
//                networkModule,
//                repositoryModule,
//                mainModule
//            ))
            koin.createRootScope()
        }
    }
}