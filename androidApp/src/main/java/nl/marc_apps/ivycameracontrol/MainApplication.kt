package nl.marc_apps.ivycameracontrol

import android.app.Application
import nl.marc_apps.ivycameracontrol.di.setupModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androix.startup.KoinStartup.onKoinStartup

class MainApplication : Application() {
    init {
        onKoinStartup {
            androidContext(this@MainApplication)
            androidLogger()
            setupModules()
        }
    }
}
