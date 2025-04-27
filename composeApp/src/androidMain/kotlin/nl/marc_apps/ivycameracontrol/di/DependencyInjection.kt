package nl.marc_apps.ivycameracontrol.di

import org.koin.core.KoinApplication

fun KoinApplication.setupModules() {
    modules(viewModelsModule, ivyModule)
}
