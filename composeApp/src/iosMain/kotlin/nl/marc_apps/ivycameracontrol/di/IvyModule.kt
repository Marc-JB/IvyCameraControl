package nl.marc_apps.ivycameracontrol.di

import com.ivyiot.ipcam_sdk.IvySdk
import com.ivyiot.ipcam_sdk.IvySdkImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val ivyModule = module {
    singleOf<IvySdk>(::IvySdkImpl)
}
