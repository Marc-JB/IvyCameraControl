package nl.marc_apps.ivycameracontrol.di

import nl.marc_apps.ivycameracontrol.ui.CameraDetailViewModel
import nl.marc_apps.ivycameracontrol.ui.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelsModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::CameraDetailViewModel)
}
