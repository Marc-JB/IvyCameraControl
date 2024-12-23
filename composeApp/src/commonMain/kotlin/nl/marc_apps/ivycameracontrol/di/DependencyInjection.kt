package nl.marc_apps.ivycameracontrol.di

import org.koin.core.KoinApplication
import org.koin.ksp.generated.*

fun KoinApplication.setupModules() {
    modules(defaultModule, IvyModule().module)
}
